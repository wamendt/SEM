package sem.systemfunktionen.assistent.core;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Data {

    public String content;
    public Map<Integer, ArrayList<String>> topicwordmap;
    public double[] topicDistribion;


    public Data(){
        topicwordmap = new TreeMap<>();

    }
}
