import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.scene.Node;

public class SceneController {
    private Stage stg;
    private Scene scn;

    public void SwitchScenes(ActionEvent event, String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));

        if (event.getSource() instanceof MenuItem) {
            MenuItem menuItem = (MenuItem) event.getSource();
            Scene scene = menuItem.getParentPopup().getOwnerWindow().getScene();
            stg = (Stage) scene.getWindow();
        } else {
            stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
        }

        if ("Login.fxml".equals(fxml)) {
            stg.setTitle("MovieDB Login");
        } else {
            stg.setTitle("Movie Database");
        }

        scn = new Scene(root);
        stg.setResizable(false);
        stg.setScene(scn);
        stg.show();
    }
}