package uef.proj.dataparserUI;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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

/**
 * @author UEF Projektityö 2019 - Tony Heikkilä, Ilari Malinen, Mikko Nygård,
 * Toni Takkinen
 * @version 1.0
 */
public class FXMLController implements Initializable {

    //Intitialize first scene (StartScene.fxml)  
    @FXML
    private VBox dragTargetProbe;

    @FXML
    private VBox dragTargetTrial;

    @FXML
    private Label successLabel;

    @FXML
    private Label successLabelTrial;

    @FXML
    private Label successLabelTemplate;

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

    private ArrayList<HeaderInfo> read;

    // Variable for using other class (LoadAndParse.java)   
    private LoadAndParse LD;

    /**
     * Handles user choosing file with "Choose File"-button (probeFile).
     *
     * @param event User clicking "Choose File"
     */
    @FXML
    private void handleChooseFileProbe(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File("."));
        probeFile = fileChooser.showOpenDialog(primarystage);
        if (probeFile != null) {
            successLabel.setText(probeFile.toString() + "\nReady to upload");
            successLabel.setWrapText(true);
        }
    }

    /**
     * Handles user choosing file with "Choose File"-button (trialFile).
     *
     * @param event User clicking "Choose File"
     */
    @FXML
    private void handleChooseFileTrial(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File("."));
        trialFile = fileChooser.showOpenDialog(primarystage);
        if (trialFile != null) {
            successLabelTrial.setText(trialFile.toString() + "\nReady to upload");
            successLabelTrial.setWrapText(true);
        }
    }

    /**
     * Handles user dragging file over the area in first scene.
     *
     * @param event User dragging file over the box
     */
    @FXML
    private void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != dragTargetProbe //|| event.getGestureSource() != dragTargetTrial
                && event.getDragboard().hasFiles()) {
            /* allow for both copying and moving, whatever user chooses */
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    /**
     * Handles user dropping dragged probeFile over the area in first scene.
     *
     * @param event User drag and drops probeFile
     */
    @FXML
    public void handleDragDroppedProbe(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            successLabel.setText(db.getFiles().toString() + "\n\nReady to upload");
            successLabel.setWrapText(true);
            successLabel.prefHeight(300);
            probeFile = db.getFiles().get(0);
            success = true;
        }
        /* let the source know whether the string was successfully 
                 * transferred and used */
        event.setDropCompleted(success);
        System.out.println("drop : " + success);
        event.consume();
    }

    /**
     * Handles user dropping dragged trialFile over the area in first scene.
     *
     * @param event User drag and drops trialFile
     */
    @FXML
    public void handleDragDroppedTrial(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            successLabelTrial.setText(db.getFiles().toString() + "\n\nReady to upload");
            successLabelTrial.setWrapText(true);
            successLabelTrial.prefHeight(300);
            trialFile = db.getFiles().get(0);
            success = true;
        }
        /* let the source know whether the string was successfully 
                 * transferred and used */
        event.setDropCompleted(success);
        event.consume();
    }

    /**
     * Functionality for "Clear"-button.
     */
    @FXML
    public void clearDraggedFiles() {
        probeFile = null;
        trialFile = null;
        read = null;

        successLabelTemplate.setText("");

        successLabel.setText("");
        successLabelTrial.setText("");
    }

    /**
     * Creates and fills TableView with data from input files and makes header
     * names editable and unsortable.
     */
    @FXML
    public void showList() {

        //Create new instance of LoadAndParse
        LD = new LoadAndParse(probeFile, trialFile);

        ArrayList<String> headers;
        headers = LD.getAllHeaders();

        /**
         * Create TableView and fill it with data. If template is selected uses
         * it to fill headers/checkboxes
         */
        for (int i = 0; i < headers.size(); i++) {
            String nimi = headers.get(i);
            String alias;

            CheckBox ch1;
            CheckBox ch2;
            if (read != null) {
                if (!nimi.equals(read.get(i).heading)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Template file did not match the statistics file!\nPlease check pairability. (Clear button resets template)");
                    alert.show();
                    returnScene();
                    return;
                }
                alias = read.get(i).alias;
                ch1 = new CheckBox();
                ch1.setSelected(read.get(i).normal);
                ch2 = new CheckBox();
                ch2.setSelected(read.get(i).avg);

            } else {
                alias = "";
                ch1 = new CheckBox();
                ch2 = new CheckBox();
            }
            list.add(new TableSetterGetter(nimi, alias, ch1, ch2));
        }

        //TableView headers editable and unsortable
        tableView.setItems(list);
        tableView.setEditable(true);

        normal.setSortable(false);
        name.setSortable(false);
        alias.setSortable(false);
        average.setSortable(false);

        //Set values to TableView
        normal.setCellValueFactory(new PropertyValueFactory<TableSetterGetter, CheckBox>("checkBox"));
        name.setCellValueFactory(new PropertyValueFactory<TableSetterGetter, String>("name"));
        average.setCellValueFactory(new PropertyValueFactory<TableSetterGetter, CheckBox>("checkBox2"));
        alias.setCellValueFactory(new PropertyValueFactory<TableSetterGetter, String>("alias"));

        alias.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    /**
     * User edited header ("alias") shows instantly on TableView after edit.
     *
     * @param CellEditEvent Makes user changes to TableView cell permanent
     */
    @FXML
    public void onEditChanged(TableColumn.CellEditEvent<TableSetterGetter, String> CellEditEvent) {
        TableSetterGetter tsg = tableView.getSelectionModel().getSelectedItem();
        tsg.setAlias(CellEditEvent.getNewValue());
    }

    /**
     * Functionality for buttons "Default" and "Average" (Check/Uncheck all
     * checkboxes).
     *
     * @param event Event for selecting all default and/or average checkboxes at
     * once
     */
    @FXML
    public void selectAll(ActionEvent event) {

        if (event.getSource() == btn_setDefault && buttonClickDefault == false) {
            for (TableSetterGetter x : tableView.getItems()) {
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

    /**
     * Functionality for changing scene from first (StartScreen.fxml) to second
     * (TableScreen.fxml)
     */
    @FXML
    public void Upload() {
        try {
            if (probeFile != null && probeFile.exists() && trialFile != null && trialFile.exists()) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TableScreen.fxml"));
                primarystage.getScene().setRoot((Pane) loader.load());
                FXMLController controller = (FXMLController) loader.getController();

                controller.probeFile = probeFile;
                controller.trialFile = trialFile;
                
                controller.successLabelTemplate = successLabelTemplate;
                controller.primarystage = primarystage;
                controller.read = read;

                controller.showList();

            } else {
                Help();
            }

        } catch (IOException e) {
            new LoadAndParse(probeFile, trialFile).alertError(e, "Something went wrong with uploading the files...");
        }

        probeFile = null;
        trialFile = null;

    }

    /**
     * Functionality for "Return"-button.
     *
     */
    @FXML
    public void returnScene() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StartScreen.fxml"));
            primarystage.getScene().setRoot((Parent) loader.load());
            FXMLController controller = (FXMLController) loader.getController();
            controller.setStageAndSetupListeners(primarystage);
            controller.probeFile = probeFile;
            controller.trialFile = trialFile;
            controller.primarystage = primarystage;
            controller.read = read;
            controller.successLabelTemplate.setText(successLabelTemplate.getText());
            controller.successLabel.setText(probeFile.toString() + "\n\nReady to upload");
            controller.successLabel.setWrapText(true);
            controller.successLabelTrial.setText(trialFile.toString() + "\n\nReady to upload");
            controller.successLabelTrial.setWrapText(true);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    /**
     * Opens files, makes directory if it does not exist and writes template
     * object to that directory.
     *
     */
    @FXML
    public void saveTemplate() {

        ArrayList<HeaderInfo> lis = new ArrayList();
        //Add the data from table
        for (TableSetterGetter x : tableView.getItems()) {
            HeaderInfo template = new HeaderInfo();
            template.heading = x.name;
            template.alias = x.alias;
            template.avg = x.cb_average.isSelected();
            template.normal = x.cb_default.isSelected();
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
            new LoadAndParse(probeFile, trialFile).alertError(ex, "Something went wrong with saving the template...");
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

    /**
     * Reads user generated .DAT file to ArrayList.
     *
     *
     * @throws IOException If there is failure with I/O operation
     */
    public void openTemplate() throws IOException {

        ObjectInputStream objectIn = null;
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DAT files (*.dat)", "*.dat");
        fileChooser.getExtensionFilters().add(extFilter);

        if (new File("Templates").exists()) {
            fileChooser.setInitialDirectory(new File("Templates"));
        } else {
            fileChooser.setInitialDirectory(new File("."));
        }
        String path = fileChooser.showOpenDialog(null).getAbsolutePath();
        objectIn = new ObjectInputStream(new FileInputStream(path));
        String tempLabel = path.substring(path.lastIndexOf("\\") + 1);

        read = new ArrayList();
        Object o = null;
        successLabelTemplate.setText("Chosen template: " + tempLabel);
        try {
            while ((o = objectIn.readObject()) != null) {
                read.add((HeaderInfo) o);
            }
        } catch (ClassNotFoundException ex) {
            new LoadAndParse(probeFile, trialFile).alertError(ex, "The template file was incorrect probably...");
        } catch (NullPointerException | EOFException ex) {

        }
    }

    /**
     * Reads data from tableView to ArrayList and adds that data to LoadAndParse
     * variable.
     */
    @FXML
    public void getValues() {
        ArrayList<HeaderInfo> li = new ArrayList();

        for (TableSetterGetter x : tableView.getItems()) {
            HeaderInfo hi = new HeaderInfo();
            hi.heading = x.getName();
            hi.alias = x.alias; // Ei käytössä
            hi.avg = x.cb_average.isSelected();
            hi.normal = x.cb_default.isSelected();

            li.add(hi);

        }

        HashMap<Integer, HashMap<String, Double>> hm = LD.readData(li);
        LD.addData(li, hm);
    }

    /**
     * Calls System.exit(0) and stops the program.
     */
    @FXML
    public void Exit() {
        System.exit(0);
    }

    /**
     * Opens help window for user.
     */
    @FXML
    public void Help() {
        Alert alert = new Alert(AlertType.INFORMATION, "Drag and drop your .xlsx file containing the statistics data "
                + "to the left and .xlsx containing the trial list to the right,"
                + " then press Upload to start parsing the files.  "
                + "\n\nTo return to the first screen, press Return. To undo selection of given files, press Clear."
                + "\n\nHeadings are parsed from the given files. "
                + " Doubleclicking Alias box lets you edit the heading names to the generated file. Aliases are headings that show on the generated file."
                + " Remember to press Enter after you have written a new alias (saves the alias to the box)."
                + "\n\nChecking the Default checkbox generates data for each unique day+trial."
                + " Checking the Average checkbox gives day-specific averages for each animal."
                + " Clicking All defaults and All Averages help you (un)check all checkboxes in a column."
                + "\n\nSave Template lets you save a new template to your selected folder. Templates are found in program folder by default."
                + "\nYou can open your existing templates by pressing the Open Template button in starting screen. "
                + "\n\nPressing Generate button will generate a brand new statistics .xlsx file.");
        alert.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonClickAverage = false;
        buttonClickDefault = false;
    }

    /**
     *
     * @param stage Variable for user interface "stage" or "window".
     */
    void setStageAndSetupListeners(Stage stage) {
        this.primarystage = stage;
        // Set the percentage size of the drag zone
        dragTargetProbe.prefWidthProperty().bind(primarystage.widthProperty().multiply(0.3));
        dragTargetProbe.prefHeightProperty().bind(primarystage.heightProperty().multiply(0.3));
        dragTargetTrial.prefWidthProperty().bind(primarystage.widthProperty().multiply(0.3));
        dragTargetTrial.prefHeightProperty().bind(primarystage.heightProperty().multiply(0.3));
    }
}
