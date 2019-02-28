package cc.paas.userinterface;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FXMLController implements Initializable {

    @FXML
    private VBox dragTargetProbe;
    @FXML
    private VBox dragTargetTrial;
    @FXML
    private Label successLabel;
    @FXML
    private Label successLabelTrial;
    @FXML
    private ProgressBar progressBar;
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
        if (event.getGestureSource() != dragTargetProbe
                && event.getDragboard().hasFiles()) {
            /* allow for both copying and moving, whatever user chooses */
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    // Need 2 for probe file and triallist file!!
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
            AnchorPane pane = FXMLLoader.load(getClass().getResource("Listat.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ei toimi");
        }
    }

    @FXML
    public void Upload() {
        // UPLOAD CODE TODO
        // uploadedFile -> Send to server
        //LoadAndParse LD = new LoadAndParse(loadedFile);
        // ArrayList headers = LD.getAllHeaders();
        // headers = showHeadersInScene(headers);
        // LD.readData();
        // LD.writeData();

        // Get progress somehow from LoadAndParse ??     
        // progressBar.setProgress(progressBar.getProgress()+0.1);
        probeFile = null;
        // trialFile = null;
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
        Alert alert = new Alert(AlertType.INFORMATION, "Upload a PCAP type dictionary file to crack password");
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
}
