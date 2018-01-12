package sem.fachlogik.assistentsteuerung.core;

import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.ArrayIterator;
import cc.mallet.topics.MarginalProbEstimator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicModelDiagnostics;
import cc.mallet.types.InstanceList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Assistent2 {

    private static final String MODEL_PATH = "src/resources/ptm.model";
    private static final String PIPE_PATH = "src/resources/ptm.pipe";
    private static final String STOPLIST_EN = "src/resources/stoplist_en.txt";
    private static final String STOPLIST_DE = "src/resources/stoplist_de.txt";
    private static final String STOPLIST_SONST = "src/resources/stoplist_sonstige.txt";
    private static Assistent2 instance;

    private ParallelTopicModel model;
    private SerialPipes pipe;
    private InstanceList instances;
    private TopicModelDiagnostics diagnostics;

    private Assistent2(){}

    public static Assistent2 getInstance() {
        if(instance == null)
            instance = new Assistent2();
        return instance;
    }

    /*Methoden zur Konfiguration*/
    public int getNumTopics(){
        return model.getNumTopics();
    }
    public void setNumTopics(int topics){
        model.setNumTopics(topics);
    }
    public int getNumIterations(){
        return model.numIterations;
    }
    public void setNumIterations(int iterations){
        model.setNumIterations(iterations);
    }
    public void setNumThreads(int threads){
        model.setNumThreads(threads);
    }

    /*Methoden zur Analyse von Daten*/
    public Object[][] getTopWords(int numWords){
        return model.getTopWords(numWords);
    }

    public double[] getTopicDistribution(int instanceID){
        return model.getTopicProbabilities(instanceID);
    }


    public void train(Map<String, List<String>> data){
        if (model == null){
            throw new IllegalStateException("Model nicht initialisiert");
        }

        instances = new InstanceList(pipe);
        for(String s: data.keySet()){
            instances.addThruPipe(new ArrayIterator(data.get(s), s));
        }

        model.addInstances(instances);
        try{
            model.estimate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        diagnostics = new TopicModelDiagnostics(model, 100);
        diagnostics.toString();
    }

    public void evaluate(Map<String, List<String>> data){
        instances = new InstanceList(pipe);
        for(String s: data.keySet()){
            instances.addThruPipe(new ArrayIterator(data.get(s),s));
        }

        MarginalProbEstimator estimator = model.getProbEstimator();
        estimator.setPrintWords(true);
        double loglike = estimator.evaluateLeftToRight(instances,10,true,System.out);
        System.out.println(loglike + " ***");
    }

    /*Methoden zum Serializieren*/
    public void saveModel(){
        try {
            model.write(new File(MODEL_PATH));
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(PIPE_PATH)));
            oos.writeObject(pipe);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadModel(){
        try {
            model = ParallelTopicModel.read(new File(MODEL_PATH));
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(PIPE_PATH)));
            pipe = (SerialPipes) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeModel(int numTopics, double alphasum, double beta){
        model = new ParallelTopicModel(numTopics, alphasum, beta);

        List<Pipe> pipes = new ArrayList<>();
        pipes.add(new Target2Label());
        pipes.add(new CharSequenceRemoveUUEncodedBlocks());
        pipes.add(new CharSequenceRemoveHTML());
        pipes.add(new CharSequence2TokenSequence());
        pipes.add(new TokenSequenceRemoveStopwords()
                .addStopWords(new File(STOPLIST_DE))
                .addStopWords(new File(STOPLIST_EN))
                .addStopWords(new File(STOPLIST_SONST)));
        pipes.add(new TokenSequenceLowercase());
        pipes.add(new TokenSequence2FeatureSequence());
        pipe = new SerialPipes(pipes);
    }

}
