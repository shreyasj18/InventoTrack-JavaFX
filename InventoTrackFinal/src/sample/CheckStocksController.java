package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CheckStocksController implements Initializable {

    @FXML
    private TableView<UserInventoryItem> tableView;
    @FXML
    private TableColumn<UserInventoryItem, String> categoryColumn;
    @FXML
    private TableColumn<UserInventoryItem, String> productIDColumn;
    @FXML
    private TableColumn<UserInventoryItem, String> productNameColumn;
    @FXML
    private TableColumn<UserInventoryItem, Double> purchaseValueColumn;
    @FXML
    private TableColumn<UserInventoryItem, Double> sellingPriceColumn;
    @FXML
    private TableColumn<UserInventoryItem, String> lastUpdatedColumn;
    @FXML
    private TableColumn<UserInventoryItem, Integer> inventoryColumn;

    @FXML
    private Button buttonHome;
    @FXML
    private Button btnInventoryValue,btnCheckSales;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        purchaseValueColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseValue"));
        sellingPriceColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        lastUpdatedColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
        inventoryColumn.setCellValueFactory(new PropertyValueFactory<>("inventory"));


        displayUserInventory();
    }

    private void displayUserInventory() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_practice", "root", "Shreyas@18");
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM userinventory WHERE userid = ?")) {

            statement.setInt(1, DBUtils.getRetrievedUserID());
            ResultSet resultSet = statement.executeQuery();

            tableView.getItems().clear();

            while (resultSet.next()) {
                String category = resultSet.getString("category_name");
                String productID = resultSet.getString("product_id");
                String productName = resultSet.getString("product_name");
                double purchaseValue = resultSet.getDouble("purchase_price");
                double sellingPrice = resultSet.getDouble("selling_price");
                String lastUpdated = resultSet.getString("date_added");
                int inventory = resultSet.getInt("quantity");

                UserInventoryItem item = new UserInventoryItem(category, productID, productName, purchaseValue, sellingPrice, lastUpdated, inventory);
                tableView.getItems().add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void handleHomeButton(ActionEvent event){
        DBUtils.changeScene(event,"logged-in.fxml","Logged-in!",null);
    }

    public void handleInventoryValue() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_practice", "root", "Shreyas@18");
             PreparedStatement statement = connection.prepareStatement("SELECT SUM(purchase_price * quantity) FROM userinventory WHERE userid = ?")) {

            statement.setInt(1, DBUtils.getRetrievedUserID());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            double inventoryValue = resultSet.getDouble(1);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inventory Value");
            alert.setHeaderText(null);
            alert.setContentText("Total Inventory Value: " + inventoryValue);
            alert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleCheckSales(ActionEvent event){
        DBUtils.changeScene(event,"checkSales.fxml","Your Sales",null);
    }
}
