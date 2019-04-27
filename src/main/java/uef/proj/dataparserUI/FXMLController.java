package uef.proj.dataparserUI;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class FXMLController implements Initializable {

    //Intitialize first scene (Scene.fxml)
    @FXML
    private VBox dragTargetProbe;
    @FXML
    private VBox dragTargetTrial;
    @FXML
    private Label successLabel;
    @FXML
    private Label successLabelTrial;
    @FXML
    private Stage primarystage;

    //Initialize TableView 
    @FXML
    private TableColumn<TableSetterGetter, String> name;
    @FXML
    private TableColumn<TableSetterGetter, String> alias;
    @FXML
    private TableColumn<TableSetterGetter, CheckBox> normal;
    @FXML
    private TableColumn<TableSetterGetter, CheckBox> average;
    @FXML
    private TableView<TableSetterGetter> tableView;

    ObservableList<TableSetterGetter> list = FXCollections.observableArrayList();

    @FXML
    private Button btn_setDefault;
    @FXML
    private Button btn_setAverage;
    @FXML
    private Boolean buttonClickAverage;
    @FXML
    private Boolean buttonClickDefault;

    //Variables for user files 
    private File probeFile;
    private File trialFile;

    // Variable for using other class    
    private LoadAndParse LD;

    //Methods for action handling (button click, file drag)
    @FXML
    private void handleButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        probeFile = fileChooser.showOpenDialog(primarystage);
        successLabel.setText(probeFile.toString() + "\nready to upload");

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

    //Creates and fills TableView with data from input files. Makes header names editable and unsortable
    @FXML
    public void showList(ActionEvent e) {

        //Create new instance of LoadAndParse
        LD = new LoadAndParse(probeFile, trialFile);
        LD.readDateFromTrialList();
        ArrayList<String> headers;

        headers = LD.getAllHeaders();

        //Create TableView and fill it
        for (int i = 0; i < headers.size(); i++) {
            String nimi = headers.get(i);
            String alias = headers.get(i);
            CheckBox ch1 = new CheckBox();
            CheckBox ch2 = new CheckBox();

            list.add(new TableSetterGetter(nimi, alias, ch1, ch2));
        }

        //TableView headers editable
        tableView.setItems(list);
        tableView.setEditable(true);

        //Set values to TableView
        normal.setCellValueFactory(new PropertyValueFactory<TableSetterGetter, CheckBox>("checkBox"));
        name.setCellValueFactory(new PropertyValueFactory<TableSetterGetter, String>("name"));
        average.setCellValueFactory(new PropertyValueFactory<TableSetterGetter, CheckBox>("checkBox2"));
        //alias.setCellValueFactory(new PropertyValueFactory<TableSetterGetter, String>("alias"));

        alias.setCellFactory(TextFieldTableCell.forTableColumn());
        
        //TableView headers unsortable
        normal.setSortable(false);
        name.setSortable(false);
        alias.setSortable(false);
        average.setSortable(false);

    }

    //Edited header ("alias") shows instantly after edit
    @FXML
    public void onEditChanged(TableColumn.CellEditEvent<TableSetterGetter, String> CellEditEvent) {
        TableSetterGetter tsg = tableView.getSelectionModel().getSelectedItem();
        tsg.setAlias(CellEditEvent.getNewValue());
    }

    //Functionality for buttons "Default" and "Average" (Check/Uncheck all checkboxes)
    @FXML
    public void selectAll(ActionEvent event) {

        if (event.getSource() == btn_setDefault && buttonClickDefault == false) {
            for (TableSetterGetter x : tableView.getItems()) {
                System.out.println("");
                x.cb_default.setSelected(true);
                buttonClickDefault = true;
            }
        } else if (event.getSource() == btn_setDefault && buttonClickDefault == true) {
            for (TableSetterGetter x : tableView.getItems()) {
                x.cb_default.setSelected(false);
                buttonClickDefault = false;
            }
        }
        if (event.getSource() == btn_setAverage && buttonClickAverage == false) {
            for (TableSetterGetter x : tableView.getItems()) {
                x.cb_average.setSelected(true);
                buttonClickAverage = true;

            }
        } else if (event.getSource() == btn_setAverage && buttonClickAverage == true) {
            for (TableSetterGetter x : tableView.getItems()) {
                x.cb_average.setSelected(false);
                buttonClickAverage = false;
            }
        }
    }

    //Functionality for changing scene from first (Scene.fxml) to second (Listat.fxml)
    @FXML
    public void Upload() {
        try {
            if (probeFile.exists()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Listat.fxml"));
                primarystage.getScene().setRoot((Pane) loader.load());
                FXMLController controller = (FXMLController) loader.getController();
                controller.probeFile = probeFile;
                controller.trialFile = trialFile;
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ei toimi " + e.getMessage());
        }

        probeFile = null;
        trialFile = null;

    }

    @FXML
    public void saveTemplate() {

        ArrayList<Template> lis = new ArrayList();
        //Add the data from table
        for (TableSetterGetter x : tableView.getItems()) {
            Template template = new Template();
            template.heading = x.alias;
            template.alias = x.alias;
            lis.add(template);
        }
        File directory = new File("Templates");
        if (!directory.exists()) {
            directory.mkdir();
        }

        ObjectOutputStream objectOut = null;
        try {

            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DAT files (.dat)", ".dat");
            fileChooser.getExtensionFilters().add(extFilter);

            fileChooser.setInitialDirectory(new File("Templates"));
            objectOut = new ObjectOutputStream(new FileOutputStream(fileChooser.showSaveDialog(null).getAbsolutePath()));

            for (int i = 0; i < lis.size(); i++) {
                objectOut.writeObject((Object) lis.get(i));
            }
        } catch (IOException ex) {
            System.out.println("IOex");
        } catch (NullPointerException ex) {
            new LoadAndParse(probeFile, trialFile).alertError(ex, "Did you close without saving template?");
        } finally {
            try {
                objectOut.close();
            } catch (IOException | NullPointerException ex) {
                Logger.getLogger(LoadAndParse.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    //Building way to save data from TableView
    @FXML
    public void getValues() {
        ArrayList<HeaderInfo> li = new ArrayList();

        for (TableSetterGetter x : tableView.getItems()) {
            HeaderInfo hi = new HeaderInfo();
            hi.heading = x.alias;
            hi.alias = x.alias; // Ei käytössä
            hi.avg = x.cb_average.isSelected();
            hi.normal = x.cb_default.isSelected();

            //  if (hi.avg || hi.normal) {
            li.add(hi);
            // }            
        }

        HashMap<Integer, HashMap<String, Double>> hm = LD.readData(li);
        LD.addData(li, hm);
    }

    @FXML
    public void Exit() {
        System.exit(0);
    }

    //Content for "Help" button 
    @FXML
    public void Help() {
        Alert alert = new Alert(AlertType.INFORMATION, "Drag and drop your statistics.xlsx file to the left and trials.xlsx to the right,"
                + "then press upload to start parsing the files. Choose the data you want and give those namings of your liking."
                + "The application will give you a brand new xlsx file.");
        alert.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonClickAverage = false;
        buttonClickDefault = false;

    }

    void setStageAndSetupListeners(Stage stage) {
        this.primarystage = stage;
        // Set the percentage size of the drag zone
        dragTargetProbe.prefWidthProperty().bind(primarystage.widthProperty().multiply(0.3));
        dragTargetProbe.prefHeightProperty().bind(primarystage.heightProperty().multiply(0.3));
        dragTargetTrial.prefWidthProperty().bind(primarystage.widthProperty().multiply(0.3));
        dragTargetTrial.prefHeightProperty().bind(primarystage.heightProperty().multiply(0.3));
    }

    // Väliaikasesti kommentteihin asiakastapaamisen toiveiden mukaisesti.
    /*
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
     */
}
