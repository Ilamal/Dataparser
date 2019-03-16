package uef.proj.dataparserUI;

import java.io.File;
import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class FXMLController implements Initializable {

    @FXML
    private VBox dragTargetProbe;
    @FXML
    private VBox dragTargetTrial;
    @FXML
    private Label successLabel;
    @FXML
    private Label successLabelTrial;

    //Testataan tableViewin rakennusta
    @FXML
    private TableColumn<TableSetterGetter, String> name;
    @FXML
    private TableColumn<TableSetterGetter, CheckBox> normal;
    @FXML
    private TableColumn<TableSetterGetter, CheckBox> average;
    @FXML
    private TableColumn<TableSetterGetter, TextField> start;
    @FXML
    private TableColumn<TableSetterGetter, TextField> end;
    
    @FXML
    private TableView<TableSetterGetter> tableView;
    
ObservableList<TableSetterGetter> list = FXCollections.observableArrayList();

    @FXML
    private ProgressBar progressBar; // Is this needed? Need to impement some info from loadAndParse of progress or just have it say when one function is done etc.
    private File probeFile;
    private File trialFile;
    private Stage primarystage;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(primarystage);
        successLabel.setText(file.toString() + "\nready to upload");
    }

    @FXML
    private void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != dragTargetProbe //|| event.getGestureSource() != dragTargetTrial
                && event.getDragboard().hasFiles()) {
            /* allow for both copying and moving, whatever user chooses */
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    @FXML
    public void handleDragDroppedProbe(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            successLabel.setText(db.getFiles().toString() + "\nready to upload");
            probeFile = db.getFiles().get(0);
            success = true;
        }
        /* let the source know whether the string was successfully 
                 * transferred and used */
        event.setDropCompleted(success);
        System.out.println("drop : " + success);
        event.consume();
    }

    @FXML
    public void handleDragDroppedTrial(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            successLabelTrial.setText(db.getFiles().toString() + "\nready to upload");
            trialFile = db.getFiles().get(0);
            success = true;
        }
        /* let the source know whether the string was successfully 
                 * transferred and used */
        event.setDropCompleted(success);
        System.out.println("drop : " + success);
        event.consume();
    }

    @FXML
    public void onButtonClick(ActionEvent event) {
        try {
            // AnchorPane pane = FXMLLoader.load(getClass().getResource("Listat.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Listat.fxml"));
            Parent lista = (Parent) loader.load();

            Scene listaScene = new Scene(lista);
            primarystage.setScene(listaScene);
            primarystage.show();
        } catch (IOException e) {
            System.out.println("Ei toimi " + e.getMessage());
        }
    }

    @FXML
    public void showList(ActionEvent e) {
        //TODO: Otsikoille mahdollisuus uudelleen nimeämiseen. Otsikoiden poisto.  

        LoadAndParse LD = new LoadAndParse(probeFile);
        ArrayList<String> headers;

        headers = LD.getAllHeaders();

        //TableView-testausta
        for (int i = 0; i < headers.size(); i++) {
            CheckBox ch1 = new CheckBox();
            CheckBox ch2 = new CheckBox();
            TextField tf1 = getNumberField();
            tf1.setText("1");            
            TextField tf2 = getNumberField();
            tf2.setText("5");
            list.add(new TableSetterGetter(headers.get(i), tf1, tf2, ch1, ch2));
        }

        tableView.setItems(list);

        normal.setCellValueFactory(new PropertyValueFactory<TableSetterGetter, CheckBox>("checkBox"));
        name.setCellValueFactory(new PropertyValueFactory<TableSetterGetter, String>("name"));
        average.setCellValueFactory(new PropertyValueFactory<TableSetterGetter, CheckBox>("checkBox2"));
        start.setCellValueFactory(new PropertyValueFactory<TableSetterGetter, TextField>("startDay"));
        end.setCellValueFactory(new PropertyValueFactory<TableSetterGetter, TextField>("endDay"));
        
        
        
        
    }

    @FXML
    public void Upload() {

        try {
            if (probeFile.exists()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Listat.fxml"));
                primarystage.getScene().setRoot((Pane) loader.load());
                FXMLController controller = (FXMLController) loader.getController();
                controller.probeFile = probeFile;
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ei toimi " + e.getMessage());
        }

        probeFile = null;
        trialFile = null;

    }
    //Building way to save data from TableView
    @FXML 
    public void saveData() {
        
    }    
    
    @FXML
    public void getValues() {  
        ArrayList<HeaderInfo> li = new ArrayList();  
        
        for (TableSetterGetter x:tableView.getItems()){
            HeaderInfo  hi = new HeaderInfo();
            hi.heading = x.name;
            hi.alias = x.name;
            hi.avg = x.cb_average.isSelected();
            hi.normal = x.cb_default.isSelected();
            hi.startDay = Integer.parseInt(x.startDay.getText());
            hi.endDay = Integer.parseInt(x.endDay.getText());
            
            
            li.add(hi);
        }
        
       System.out.println(li.get(0).normal);
       
            
       
    }
        
   
    
    public void ProgressCounter() {
        // TODO
        // progressBar.setProgress(ProgressDataFromServer);
        // passwordLabel.setText("Password found : " + password);
    }

    @FXML
    public void Exit() {
        System.exit(0);
    }

    @FXML
    public void Help() {
        Alert alert = new Alert(AlertType.INFORMATION, "Drag and drop your statistics.xlsx file to the left and trials.xlsx to the right,"
                + "then press upload to start parsing the files. Choose the data you want and give those namings of your liking."
                + "The application will give you a brand new xlsx file.");
        alert.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    void setStageAndSetupListeners(Stage stage) {
        this.primarystage = stage;
        // Set the percentage size of the drag zone
        dragTargetProbe.prefWidthProperty().bind(primarystage.widthProperty().multiply(0.3));
        dragTargetProbe.prefHeightProperty().bind(primarystage.heightProperty().multiply(0.3));
        dragTargetTrial.prefWidthProperty().bind(primarystage.widthProperty().multiply(0.3));
        dragTargetTrial.prefHeightProperty().bind(primarystage.heightProperty().multiply(0.3));
    }

    private TextField getNumberField() {
        final TextField textField = new TextField();
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        return textField;
    }
}
