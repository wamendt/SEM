package sem.systemfunktionen.assistent.core;

import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.ArrayIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.*;
import sem.datenhaltung.semmodel.entities.Tag;

import java.io.*;
import java.util.*;

public class Assistent {

    private static Assistent instance;

    private ParallelTopicModel model;
    private SerialPipes pipe;
    private ArrayList<Tag> tags;

    /*default Modelkonfiguration*/
    private final int NUM_TOPICS = 20;
    private final double ALPHA_SUM = 1.0;
    private final double BETA = 0.01;
    private final int NUM_THREADS = 4;
    private final String MODEL_PATH = "resources/ptm.model";
    private final String STOPLIST_EN = "resources/stoplist_en.txt";
    private final String STOPLIST_DE = "resources/stoplist_de.txt";
    private final int NUM_ITERATIONS = 50;

    private Assistent(){
        makePipe();
    }

    public static Assistent getInstance(){
        if(instance == null) {
            instance = new Assistent();
        }
        return instance;
    }

    private InstanceList makeInstanceList(List<Data> data){
        InstanceList instances = new InstanceList(pipe);
        System.out.println(data.size());
        ArrayList<String> content = new ArrayList<>();
        for(Data d : data){
            content.add(d.content);
        }
        instances.addThruPipe(new ArrayIterator(content, "content"));
        return instances;
    }


    private void makePipe(){
        List<Pipe> pipeList = new ArrayList<>();
        pipeList.add(new Target2Label());
        pipeList.add(new CharSequence2TokenSequence());
        pipeList.add(new TokenSequenceRemoveStopwords()
                .addStopWords(new File(STOPLIST_DE))
                .addStopWords(new File(STOPLIST_EN)));
        pipeList.add(new TokenSequenceLowercase());
        pipeList.add(new TokenSequence2FeatureSequence());
        pipe = new SerialPipes(pipeList);
    }

    private void filldata(List<Data> data, InstanceList instances){
        Alphabet dataAlphabet = instances.getDataAlphabet();

        for(int i = 0; i < instances.size(); i++){
            FeatureSequence tokens = (FeatureSequence) model.getData().get(i).instance.getData();
            LabelSequence topics = model.getData().get(i).topicSequence;
            Map<Integer, ArrayList<String>> topicwordmap = data.get(i).topicwordmap;

            for(int position = 0; position < tokens.getLength(); position++){
                topicwordmap.put(topics.getIndexAtPosition(position), new ArrayList<>());
            }
            for(int position = 0; position < tokens.getLength(); position++){
                topicwordmap.get(topics.getIndexAtPosition(position))
                        .add((String) dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)));
            }

            data.get(i).topicDistribion = model.getTopicProbabilities(i);

        }
    }


    public void trainModel(List<Data> data){
        if(model == null)
            makeModel(NUM_TOPICS, ALPHA_SUM, BETA);


        InstanceList instances = makeInstanceList(data);

        model.addInstances(instances);
        model.setNumThreads(NUM_THREADS);
        model.setNumIterations(NUM_ITERATIONS);

        try{
            model.estimate();
            filldata(data, instances);
//            // Get an array of sorted sets of word ID/count pairs
//            ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
//
//            // Show top 5 words in topics with proportions for the first document
//            for (int topic = 0; topic < NUM_TOPICS; topic++) {
//                Iterator<IDSorter> iteratorID = topicSortedWords.get(topic).iterator();
//
//                out = new Formatter(new StringBuilder(), Locale.US);
//                out.format("%d\t%.3f\t", topic, topicDistribution[topic]);
//                int rank = 0;
//                while (iteratorID.hasNext() && rank < 5) {
//                    IDSorter idCountPair = iteratorID.next();
//                    out.format("%s (%.0f) ", dataAlphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight());
//                    rank++;
//                }
//                System.out.println(out);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void infer(List<Data> data){
        makePipe();

        InstanceList instances = makeInstanceList(data);

        TopicInferencer inferencer = model.getInferencer();

        IDSorter[] sortedTopics = new IDSorter[model.getNumTopics()];
        for(int topic = 0; topic < model.getNumTopics(); topic ++){
            /*Initialize the sorters with dummy values*/
            sortedTopics[topic] = new IDSorter(topic, topic);
        }

        for(Instance instance : instances){
            double[] topicDistribution = inferencer.getSampledDistribution(instance, 50, 5, 5);
            if(instance.getSource() != null){
                System.out.println(instance.getSource());
            }else{
                System.out.println("null-Source");
            }

            for(int topic = 0; topic < model.getNumTopics(); topic ++){
                sortedTopics[topic].set(topic, topicDistribution[topic]);
            }
            Arrays.sort(sortedTopics);

            for(int i = 0; i < model.getNumTopics(); i++){
                System.out.println(sortedTopics[i].getID() + " " + sortedTopics[i].getWeight());
            }
        }


    }

    public void makeModel(int numTopics, double alphasum, double beta){
        model = new ParallelTopicModel(numTopics, alphasum, beta);
    }

    public void loadModel() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(MODEL_PATH)));
        model = (ParallelTopicModel) ois.readObject();
    }

    public void saveModel() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(MODEL_PATH)));
        oos.writeObject(model);
    }
}
