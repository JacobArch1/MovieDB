import java.io.IOException;
import java.sql.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Login {
    String mySQLPassword = "pass";
    static User user = new User();
    SceneController scene = new SceneController();

    @FXML
    private Button btnLogin, btnRegister;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private TextField tfUsername;

    @FXML
    private Label lblResults, lblRegistered;

    Connection con;
    public void initialize() throws SQLException, IOException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb", "root", mySQLPassword);
    }

    @FXML
    void btnLoginClicked(ActionEvent event) throws SQLException, IOException {
        String Username = tfUsername.getText();
        String Password = pfPassword.getText();

        if(!checkCredentials(Username, Password)){
            lblResults.setVisible(true);
            lblResults.setText("Invalid Credentials");
        }
        else{
            scene.SwitchScenes(event, "MainSceneUpdated.fxml");
        }
    }

    @FXML
    void btnRegisterClicked(ActionEvent event) throws SQLException, IOException {
        String Username = tfUsername.getText();
        String Password = pfPassword.getText();

        if(!checkRegisterValid(Username, Password)){
            lblResults.setVisible(true);
            lblResults.setText("Error, Username Taken");
        }
        else{
            lblRegistered.setVisible(true);
        }
    }

    public boolean checkRegisterValid(String Username, String Password) throws SQLException{
        if (Username == null || Password == null || Username.trim().isEmpty() || Password.trim().isEmpty()) {
            return false;
        }

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT username FROM login_credentials WHERE BINARY username = \"" + Username + "\"");

        if(!rs.next()){
            stmt.execute("INSERT INTO login_credentials(username, password) VALUES(\"" + Username + "\",\"" + Password + "\")");
            return true;
        }
        
        return false;
    }

    public boolean checkCredentials(String Username, String Password)  throws SQLException {
        if (Username == null || Password == null || Username.trim().isEmpty() || Password.trim().isEmpty()) {
            return false;
        }

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id FROM login_credentials WHERE BINARY username = \"" + Username +
            "\" AND BINARY password = \"" + Password + "\"");

        if(rs.next()){
            user.setUsername(Username);
            user.setId(rs.getLong("id"));
            return true;
        }

        return false;
    }

    @FXML
    void pfPasswordClicked(ActionEvent event) {
        lblResults.setVisible(false);
        lblRegistered.setVisible(false);
    }

    @FXML
    void tfUsernameClicked(ActionEvent event) {
        lblResults.setVisible(false);
        lblRegistered.setVisible(false);
    }
}