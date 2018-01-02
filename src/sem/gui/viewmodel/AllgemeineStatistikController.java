package sem.gui.viewmodel;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;

public class AllgemeineStatistikController implements Initializable{
    @FXML
    private BarChart grafAllgemeineStatistik;

    @FXML
    private Parent root;



    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       // barChart.setData(FXCollections.observableArrayList(new String("hallo")));
        XYChart.Series series = new XYChart.Series();
        series.setName("Allgemeine Statistik");
        series.getData().add(new XYChart.Data<>("data",15));
        series.getData().add(new XYChart.Data<>("data1",25));
        series.getData().add(new XYChart.Data<>("data2",5));

        grafAllgemeineStatistik.getData().add(series);
    }

    public Parent getRoot (){
        return root;
    }
}
