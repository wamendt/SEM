package sem.systemfunktionen.assistent.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AssistentTest {
    static Assistent cut = Assistent.getInstance();


    @BeforeAll
    static void init() throws IOException, ClassNotFoundException {
        //cut.makeModel(20, 0.01, 0.01);
        cut.loadModel();
    }

    @Test
    void infer() throws IOException {
        cut.infer(getData("resources/topicinfertest.txt"));
    }
    @Test
    void trainModel() throws IOException {
        List<Data> data = getData("resources/topictraintest.txt");
        cut.trainModel(data);
        cut.saveModel();

        int i = 1;
        for(Data d : data){
            System.out.println("************************************");
            System.out.println(i + ":");
            System.out.println("topicdistribution: " + Arrays.toString(d.topicDistribion));
        }
    }


    List<Data> getData(String path) throws IOException {
        List<Data> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        for(String line = br.readLine(); line != null; line = br.readLine()){
            Pattern p = Pattern.compile("(\\S*)[\\s]*(\\S)[\\s]*(.*)");
            Matcher m = p.matcher(line);
            m.find();
            Data d = new Data();
            d.content = m.group(3);
            data.add(d);
        }
        br.close();
        return data;
    }
}