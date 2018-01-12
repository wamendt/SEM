package sem.gui.viewmodel.menufenster;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;
import sem.fachlogik.assistentsteuerung.impl.IAssistentSteuerungImpl;
import sem.fachlogik.grenzklassen.TagGrenz;
import sem.gui.viewmodel.hauptfenster.HauptfensterController;
import sem.gui.viewmodel.utils.ControllerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class AssistentController implements Initializable{
    private Parent parent ;

    @FXML
    private AnchorPane root;

    @FXML
    private ListView<TagGrenz> listViewTags;

    @FXML
    private FlowPane flowPaneWoerter;

    @FXML
    private TextField textFieldTagName;

    @FXML
    private Button btnTagNameAendern;

    @FXML
    private Button btnTrainieren;

    @FXML
    private TextField textFieldAlpha;

    @FXML
    private TextField textFieldBeta;

    @FXML
    private TextField textFieldAnzahlTags;

    @FXML
    private TextField textFieldAnzahlIterationen;

    @FXML
    private ProgressIndicator progressIndicator;

    private Thread assistentThread;

    @FXML
    private Label labelTagsWoerter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Task assistentTask = new Task() {

            @Override
            protected Object call() throws Exception {
                return null;
            }
        };
        assistentThread = new Thread(assistentTask);

        listViewTags.setCellFactory(new Callback<ListView<TagGrenz>, ListCell<TagGrenz>>() {
            @Override
            public ListCell call(ListView param) {
                return new TagListCell();
            }
        });

        listViewTags.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TagGrenz>() {
            @Override
            public void changed(ObservableValue<? extends TagGrenz> observable, TagGrenz oldValue, TagGrenz newValue) {
                flowPaneWoerter.getChildren().clear();
                for(String s : newValue.getWoerter()){
                    Label label = new Label(s);
                    MenuItem item = new MenuItem("Zur Stopliste hinzufügen");
                    item.setOnAction(event -> {
                        new IAssistentSteuerungImpl().wortZurStoplisteHinzufuegen(label.getText(), newValue.getTid());
                        flowPaneWoerter.getChildren().remove(label);
                    });
                    label.setContextMenu(new ContextMenu(item));
                    flowPaneWoerter.getChildren().add(label);
                }
                labelTagsWoerter.setVisible(true);
                textFieldTagName.setText(newValue.getName());
            }
        });
        listViewTags.setItems(FXCollections.observableArrayList(new IAssistentSteuerungImpl().zeigeAlleTagsAn()));
    }

    public void init(){
        root.prefWidthProperty().bind(((AnchorPane)parent).prefWidthProperty());
        root.prefHeightProperty().bind(((AnchorPane)parent).prefHeightProperty());

        root.prefWidthProperty().bind(((AnchorPane)parent).widthProperty());
        root.prefHeightProperty().bind(((AnchorPane)parent).heightProperty());
        labelTagsWoerter.setVisible(false);
    }

    @FXML
    public void btnTagNameAendernOnAction(ActionEvent event){
        TagGrenz tag = listViewTags.getSelectionModel().getSelectedItem();
        if(tag != null){
            tag.setName(textFieldTagName.getText());
            new IAssistentSteuerungImpl().updateTagGrenz(tag);
            HauptfensterController controller = ControllerFactory.getHauptfensterController();
            controller.initFlowPaneTags();
            controller.clickedTags.clear();
            listViewTags.refresh();
        }
    }

    @FXML
    public void btnTrainierenOnAction(ActionEvent event){
        try {
            int anzahlTags = Integer.parseInt(textFieldAnzahlTags.getText());
            double alphasum = Double.parseDouble(textFieldAlpha.getText());
            double beta = Double.parseDouble(textFieldBeta.getText());
            int numIterations = Integer.parseInt(textFieldAnzahlIterationen.getText());
            new IAssistentSteuerungImpl().trainiereSEM(anzahlTags, alphasum, beta, numIterations);
            ControllerFactory.getHauptfensterController().initFlowPaneTags();
            listViewTags.setItems(FXCollections.observableArrayList(new IAssistentSteuerungImpl().zeigeAlleTagsAn()));
        }catch(NumberFormatException e){

        }

    }

    public AnchorPane getRoot(){
        return root;
    }

    public void setParent(Parent parent){
        this.parent = parent;
    }
}
