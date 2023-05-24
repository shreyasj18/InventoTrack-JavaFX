package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class SellStocksController implements Initializable {
    @FXML
    private TextField tf_customerName,tf_customerCity,tf_productID,tf_sellingPrice,tf_productName;

    @FXML
    private DatePicker datePickerSaleDate;
    @FXML private MenuButton menuButtonQuantity;
    @FXML private Button buttonHome,buttonSell;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeQuantityMenu();
        setDatePickerFormat();

        tf_productID.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.isEmpty()) {
                    String productName = getProductName(newValue);
                    String sellingPrice = getSellingPrice(newValue);

                    tf_productName.setText(productName);
                    tf_sellingPrice.setText(sellingPrice);
                } else {
                    tf_productName.clear();
                    tf_sellingPrice.clear();
                }
            }
        });
    }

    public void handleSellItem() {
        String custName = tf_customerName.getText();
        String custCity = tf_customerCity.getText();
        String productId = tf_productID.getText();
        String sellingPrice = tf_sellingPrice.getText();
        String saleDate = datePickerSaleDate.getValue() != null ? datePickerSaleDate.getValue().toString() : "";

        if (custName.isEmpty() || custCity.isEmpty() || productId.isEmpty() || sellingPrice == null || sellingPrice.isEmpty() || saleDate.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter all fields");
            alert.showAndWait();
            return;
        }

        if (!isProductIdValid(productId)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Product!\nOR\nProduct out of stock!");
            alert.showAndWait();
            return;
        }

        int selectedQuantity = Integer.parseInt(menuButtonQuantity.getText());
        if (!decrementQuantity(productId, selectedQuantity)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Insufficient Stock");
            alert.setHeaderText(null);
            alert.setContentText("Insufficient stock available.");
            alert.showAndWait();
        }
        else {

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_practice", "root", "Shreyas@18");
             PreparedStatement statement = connection.prepareStatement("INSERT INTO customer (cust_name, cust_city, product_ID, quantity, sell_date, sold_by_userID,selling_price) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, custName);
            statement.setString(2, custCity);
            statement.setString(3, productId);
            statement.setString(4, menuButtonQuantity.getText());
            statement.setString(5, saleDate);
            statement.setInt(6, DBUtils.getRetrievedUserID());
            statement.setString(7,getSellingPrice(productId));

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int customerId = generatedKeys.getInt(1);
                    String productName = getProductName(productId);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sale Confirmation");
                    alert.setHeaderText(null);
                    alert.setContentText("Sale Successful!\n\nProduct: " + productName + "\nQuantity: " + menuButtonQuantity.getText() + "\nPrice: " + sellingPrice
                                        + "\nTotal Bill : INR "+ Double.parseDouble(getSellingPrice(productId))*selectedQuantity
                                        + "\nYour NET PROFIT is : INR "+ selectedQuantity*((Double.parseDouble(getSellingPrice(productId)))-(Double.parseDouble(getPurchaseValue(productId)))));
                    alert.showAndWait();


                    // Clear the input fields after successful sale
                    tf_customerName.clear();
                    tf_customerCity.clear();
                    tf_productID.clear();
                    tf_sellingPrice.clear();
                    menuButtonQuantity.setText("Select");
                    datePickerSaleDate.setValue(null);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            }
        }
    }

    private void initializeQuantityMenu() {
        for (int i = 1; i <= 10; i++) {
            MenuItem menuItem = new MenuItem(String.valueOf(i));
            menuItem.setOnAction(this::handleQuantityOption);
            menuButtonQuantity.getItems().add(menuItem);
        }
    }
    private void setDatePickerFormat() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        StringConverter<LocalDate> converter = new LocalDateStringConverter(dateFormatter, dateFormatter);
        datePickerSaleDate.setConverter(converter);
    }
    private void handleQuantityOption(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String quantity = menuItem.getText();
        menuButtonQuantity.setText(quantity);
    }
    private boolean isProductIdValid(String productId) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_practice", "root", "Shreyas@18");
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM inventory WHERE product_id = ?")) {

            statement.setString(1, productId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    @FXML
    private void handleHome(ActionEvent event) {
        DBUtils.changeScene(event, "logged-in.fxml", "Logged In", null);
    }

    private String getProductName(String productID){
        String retrivedProductName = null;
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT product_name FROM product WHERE product_id = ?")) {

            statement.setString(1, productID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                retrivedProductName = resultSet.getString("product_name");
            }
            return retrivedProductName;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retrivedProductName;
    }

    public String getSellingPrice(String productID){
        String retrivedSellingPrice = null;
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT selling_price FROM product WHERE product_id = ?")) {

            statement.setString(1, productID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                retrivedSellingPrice = resultSet.getString("selling_price");
            }
            return retrivedSellingPrice;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retrivedSellingPrice;
    }

    private String getPurchaseValue(String productID){
        String retrievedPurchaseValue = null;
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT purchase_price FROM product WHERE product_id = ?")) {

            statement.setString(1, productID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                retrievedPurchaseValue = resultSet.getString("purchase_price");
            }
            return retrievedPurchaseValue;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retrievedPurchaseValue;
    }
    private int getInventoryQuantity(String productID){
        int inventoryQuantity=0;
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT quantity FROM inventory WHERE product_id = ? AND user_id = ?")) {

            statement.setString(1, productID);
            statement.setInt(2, DBUtils.getRetrievedUserID());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                inventoryQuantity = resultSet.getInt("quantity");

            }
            return inventoryQuantity;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventoryQuantity;
    }

    private boolean decrementQuantity(String productID, int quantity) {
        boolean flag = false;
        int inventoryQuantity = getInventoryQuantity(productID);

        if (inventoryQuantity >= quantity) {
            try (Connection connection = DBUtils.getConnection();
                 PreparedStatement statement = connection.prepareStatement("UPDATE inventory SET quantity = GREATEST(quantity - ?, 0) WHERE product_id = ? AND user_id = ?")) {

                statement.setInt(1, quantity);
                statement.setString(2, productID);
                statement.setInt(3, DBUtils.getRetrievedUserID());

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Stock Updated");
                    alert.setHeaderText(null);
                    alert.setContentText("Stock quantity decremented successfully.");
                    alert.showAndWait();
                    flag = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            flag = false;
        }

        return flag;
    }

}
