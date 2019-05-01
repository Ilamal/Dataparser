package uef.proj.dataparserUI;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author UEF Projektityö 2019 - Tony Heikkilä, Ilari Malinen, Mikko Nygård,
 * Toni Takkinen
 * @version 1.0
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("DataParser");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StartScreen.fxml"));
        Parent root = (Parent) loader.load();
        FXMLController controller = (FXMLController) loader.getController();
        
        controller.setStageAndSetupListeners(stage);        

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
