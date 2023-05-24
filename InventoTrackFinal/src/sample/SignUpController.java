package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML private Button button_signup;
    @FXML  Button button_log_in;
    @FXML  TextField tf_userName;
    @FXML
    PasswordField tf_passWord;
    @FXML PasswordField tf_confPass;

    public void handleButton(){
        button_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!tf_userName.getText().trim().isEmpty() && !tf_passWord.getText().trim().isEmpty() && (Objects.equals(tf_passWord.getText(), tf_confPass.getText()))) {
                    DBUtils.signUpUser(event, tf_userName.getText(), tf_passWord.getText());
                } else {
                    System.out.println("Please fill in all information.");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Passwords Don't Match");
                    alert.show();
                }
            }
        });
    }

    public void handleLoginBtn(ActionEvent event){
        DBUtils.changeScene(event,"sample.fxml","Invento-Track",null);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            handleButton();
        }
}
