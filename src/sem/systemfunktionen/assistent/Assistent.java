package sem.systemfunktionen.assistent;

import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.ArrayIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.InstanceList;
import sem.datenhaltung.semmodel.entities.AssistentKonfig;
import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Tag;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Assistent {
    /*Singelton*/
    private static Assistent instance = new Assistent();

    /*Die Anzahl der verwendeten Threads zur klassifizierung der Emails*/
    private static final int NUM_THREADS = 4;

    /*Die Konfiguration fuer den LDA Algorithmus*/
    private AssistentKonfig konfig;

    private ArrayList<Tag> tags;

    private Assistent(){}


    public static Assistent getInstance(){
        return instance;
    }


    public void train(){

    }

    public void setAssiKonfig(AssistentKonfig konfig){
        this.konfig = konfig;
    }


    public void train(List<EMail> emails){

        ArrayList<String> contents = new ArrayList<>();
        System.out.println(contents.size());

        int i = 0;
        for(EMail e : emails){
            contents.add(e.getInhalt());
            System.out.println(e.getInhalt() + i +  "\n******************************************"
                    + "\n********************************************\n\n");
            i++;
        }
        ArrayIterator iterator = new ArrayIterator(contents.toArray(), "content");

        SerialPipes pipe = new SerialPipes(createPipeList());

        InstanceList instances = new InstanceList(pipe);
        instances.addThruPipe(iterator);



        ParallelTopicModel model = new ParallelTopicModel(konfig.getAnzahlTags(),
                konfig.getAlphaWert(), konfig.getBetaWert());

        model.addInstances(instances);
        model.setNumThreads(NUM_THREADS);
        model.setNumIterations(konfig.getAnzahlDurchlauf());

        try {
            model.estimate();
            model.printTopicWordWeights(new File("out"));
            model.printDocumentTopics(new File("documenttopics"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private List<Pipe> createPipeList(){
        List<Pipe> pipeList = new ArrayList<>();
        pipeList.add(new Target2Label());
        pipeList.add(new CharSequence2TokenSequence());
        pipeList.add(new TokenSequenceRemoveStopwords().addStopWords(new File(konfig.getStopwordPfad())));
        pipeList.add(new TokenSequenceLowercase());
        pipeList.add(new TokenSequence2FeatureSequence());
        return pipeList;
    }

    private void saveModel(ParallelTopicModel model, String path) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(path)));
        oos.writeObject(model);
        oos.close();
    }

    private void savePipeline(SerialPipes pipeline, String path) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(path)));
        oos.writeObject(pipeline);
        oos.close();
    }

    private ParallelTopicModel loadModel(String path) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(path)));
        ParallelTopicModel model = (ParallelTopicModel) ois.readObject();
        ois.close();
        return model;
    }

    private SerialPipes loadPipeline(String path) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(path)));
        SerialPipes pipeline = (SerialPipes)ois.readObject();
        ois.close();
        return pipeline;
    }
}
