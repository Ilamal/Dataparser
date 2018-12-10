package cc.paas.userinterface;

import com.sun.glass.ui.Window;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FXMLController implements Initializable {

    @FXML
    private VBox dragTarget;
    @FXML
    private Label successLabel;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label passwordLabel;
    private File loadedFile;
    private Stage primarystage;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PCAP files (*.pcap)", "*.pcap");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(primarystage);
        successLabel.setText(file.toString() + "\nready to upload");
    }

    @FXML
    private void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != dragTarget
                && event.getDragboard().hasFiles()) {
            /* allow for both copying and moving, whatever user chooses */
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    @FXML
    public void handleDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            successLabel.setText(db.getFiles().toString() + "\nready to upload");
            loadedFile = db.getFiles().get(0);
            success = true;
        }
        /* let the source know whether the string was successfully 
                 * transferred and used */
        event.setDropCompleted(success);
        System.out.println("drop : " + success);
        event.consume();
    }

    @FXML
    public void Upload() {
        // UPLOAD CODE TODO
        // uploadedFile -> Send to server
        progressBar.setProgress(progressBar.getProgress()+0.1);
        loadedFile = null;
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
        dragTarget.prefWidthProperty().bind(primarystage.widthProperty().multiply(0.3));
        dragTarget.prefHeightProperty().bind(primarystage.heightProperty().multiply(0.3));
    }
}
