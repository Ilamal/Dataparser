package uef.proj.dataparserUI;

import java.awt.Checkbox;
import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.xmlbeans.SystemProperties;

public class FXMLController implements Initializable {

    @FXML
    private VBox dragTargetProbe;
    @FXML
    private VBox dragTargetTrial;
    @FXML
    private Label successLabel;
    @FXML
    private Label successLabelTrial;
    
    //testataan tableViewin rakennusta
    @FXML
    private TableColumn<TableSetterGetter, String> name;
    
    @FXML
    private TableColumn<TableSetterGetter, Integer> id;
    @FXML
    private TableColumn<TableSetterGetter, CheckBox> select;
    @FXML
    private TableView<TableSetterGetter> tableView;
    ObservableList<TableSetterGetter> list = FXCollections.observableArrayList();
            
    
    //Headers to array to checkboxes testing 
    @FXML
    private GridPane gridPane;
   // private CheckBox[] headersCheckB;
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
        if (event.getGestureSource() != dragTargetProbe || event.getGestureSource() != dragTargetTrial
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
           // AnchorPane pane = FXMLLoader.load(getClass().getResource("Listat.fxml"));
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Listat.fxml"));
           Parent lista = (Parent)loader.load();
           
           Scene listaScene = new Scene(lista);
           primarystage.setScene(listaScene);
           primarystage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ei toimi");
        }
    }
    
    @FXML
    //Testing ways to print ArrayList and make CheckBoxes. THIS IS TEMP SOLUTION
   public void showList(ActionEvent e) {
       
     
       System.out.println("Testi näppäimelle!");
      
      
       ArrayList<String> testArray = new ArrayList<String>();
      testArray.add("hello there!");
      testArray.add("Hello again!");
      testArray.add("Moro");
      testArray.add("TERVE TERVE");
      String[] stringArray = testArray.toArray(new String[0]);   
       
       
       
       //tableView-testausta
       
       for (int i=0;i<stringArray.length;i++) {
           CheckBox ch = new CheckBox("" + i);
           list.add(new TableSetterGetter(i, stringArray[i], ch));
       }
       
       tableView.setItems(list);  
      
       id.setCellValueFactory(new PropertyValueFactory<TableSetterGetter, Integer>("id"));
       name.setCellValueFactory(new PropertyValueFactory<TableSetterGetter, String>("name"));
       select.setCellValueFactory(new PropertyValueFactory<TableSetterGetter, CheckBox>("checkBox"));
       
       //TODO: Sama otsikoille, otsikoinnin uudelleen nimeäminen
       
       
       
       /*
       LoadAndParse LD = new LoadAndParse(probeFile);
       ArrayList<String> headers = new ArrayList();
              
       headers = LD.getAllHeaders();       
     */
               
       
       //System.out.println(probeFile.exists());
      // System.out.println("hello"); 
      
     /*  
       final int numHeaders = 100000;
       final int numHeadersPerRow = 1;       
       
      
       
       
       
       //Nappia painamalla tulee esiin lista checkboxeja
      
       headersCheckB = new CheckBox[10000];
       for (int i=0;i<numHeaders; i++) {
           String temp = stringArray[i];
           CheckBox CB = new CheckBox(temp);
           headersCheckB[i] = CB;
           gridPane.add(headersCheckB[i], i % numHeadersPerRow, i/numHeadersPerRow);
       }
       */
       
   }

    @FXML
    public void Upload() {
        // UPLOAD CODE TODO
        // LoadAndParse LD = new LoadAndParse(loadedFile);
        // ArrayList headers = LD.getAllHeaders();
        // headers = showHeadersInScene(headers);
        // LD.readData();
        // LD.writeData();
     //   System.out.println(probeFile.exists());
        
    //    System.out.println(trialFile.exists());
        
        try {
          // AnchorPane pane = 
           //FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/scene.fxml"));
           //Parent lista = (Parent)loader.load();
           
          // Scene listaScene = new Scene(lista);
           primarystage.getScene().setRoot((Pane)FXMLLoader.load(getClass().getResource("/fxml/Listat.fxml")));
          // primarystage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ei toimi");
        }
        
        
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
        Alert alert = new Alert(AlertType.INFORMATION, "Drag and drop your statistics.xlsx file to the left and trials.xlsx to the right," +
                "then press upload to start parsing the files. Choose the data you want and give those namings of your liking." +
                "The application will give you a brand new xlsx file.");
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
