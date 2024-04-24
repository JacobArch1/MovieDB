import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Driver extends Application{
    @Override
    public void start(Stage primaryStage) throws IOException{
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        primaryStage.setTitle("MovieDB Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        /*Test */
    }

    public static void main(String[] args) {
        launch(args);
    }
}