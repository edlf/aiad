import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Eduardo Fernandes
 */

public class Main extends Application {
    private final static String appTitle = "TITLE";

    /**
     * Entry point
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = fxmlLoader.load();

        Platform.setImplicitExit(false);

        // primaryStage.setOnCloseRequest(event -> Platform.exit());

        primaryStage.setTitle(appTitle);
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
}
