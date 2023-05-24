package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CheckSales implements Initializable {
    @FXML
    private TableView<Customer> tableView;
    @FXML
    private TableColumn<Customer, Integer> customerIDColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TableColumn<Customer, String> cityColumn;
    @FXML
    private TableColumn<Customer, Integer> productIDColumn;
    @FXML
    private TableColumn<Customer, Integer> quantityColumn;
    @FXML
    private TableColumn<Customer, Date> soldOnColumn;
    @FXML
    private TableColumn<Customer, Double> amountColumn;
    @FXML private Button backButton,homeButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("productID"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        soldOnColumn.setCellValueFactory(new PropertyValueFactory<>("soldOn"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        populateTableView();
    }

    private void populateTableView() {
        int loggedInUserID = DBUtils.getRetrievedUserID();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_practice", "root", "Shreyas@18");
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM customer WHERE sold_by_userID = ?")) {
            statement.setInt(1, loggedInUserID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int customerID = resultSet.getInt("customerID");
                String customerName = resultSet.getString("cust_name");
                String city = resultSet.getString("cust_city");
                int productID = resultSet.getInt("product_ID");
                int quantity = resultSet.getInt("quantity");
                Date soldOn = resultSet.getDate("sell_date");
                double amount = resultSet.getDouble("amount");

                Customer customer = new Customer(customerID, customerName, city, productID, quantity, soldOn,amount);
                tableView.getItems().add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   public void handleHome(ActionEvent event){
        DBUtils.changeScene(event,"logged-in.fxml","Logged-in",null);
   }
    public void handleBackButton(ActionEvent event){
        DBUtils.changeScene(event,"checkStocks.fxml","Logged-in",null);
    }
}
