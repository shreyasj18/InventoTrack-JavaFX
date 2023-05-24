package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    private Button button_login;
    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField tf_password;
    @FXML
    private Button button_signup;

    private Stage primaryStage;

    public void handleSignupButton(){
        button_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"sign-up.fxml","sign-up",null);
            }
        });
    }

    public void handleLogInButton(){
        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.logInUser(event,tf_username.getText(),tf_password.getText());
            }
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handleLogInButton();
        handleSignupButton();
    }

    public TextField getTf_username() {
        return tf_username;
    }
}



