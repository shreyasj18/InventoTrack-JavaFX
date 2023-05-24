package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {
    @FXML
    private Button button_logout;

    @FXML
    Label label_welcome;

    @FXML private Button button_addStocks;
    @FXML private Button button_checkStocks;
    @FXML private Button button_sellStocks;

    public void handleLogOutButton(){
        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"sample.fxml","Log in",null);
            }
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
      handleLogOutButton();
      handleAddStocks();
      handleCheckStocks();
      handleSellStocks();

    }

    public void setUserInformation(String username){
        label_welcome.setText("Welcome "+username+"!");
    }

    @FXML
    private void handleAddStocks() {
        button_addStocks.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //DBUtils.changeScene(event, "sellStocks.fxml", "Sell Stocks",null);
                Parent root = null;
                try {
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("addStocks.fxml")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle("add stocks");
                stage.setScene(new Scene(root,800,600));
                stage.show();
            }
        });
    }

    @FXML
    private void handleCheckStocks() {

        button_checkStocks.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //DBUtils.changeScene(event, "sellStocks.fxml", "Sell Stocks",null);
                Parent root = null;
                try {
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("checkStocks.fxml")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle("check stocks");
                stage.setScene(new Scene(root,800,600));
                stage.show();
            }
        });
    }

    @FXML
    private void handleSellStocks() {
        // Load the sellStocks.fxml page

        button_sellStocks.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //DBUtils.changeScene(event, "sellStocks.fxml", "Sell Stocks",null);
                Parent root = null;
                try {
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("sellStocks.fxml")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle("sell stocks");
                stage.setScene(new Scene(root,800,600));
                stage.show();
            }
        });
    }

}
