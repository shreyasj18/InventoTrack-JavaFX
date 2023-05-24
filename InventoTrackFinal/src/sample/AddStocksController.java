package sample;

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

public class AddStocksController implements Initializable {
    @FXML
    public Label LabelEnterPID;
    @FXML
    public Label LabelDateAdded;
    @FXML
    public Label LabelQuantity;
    @FXML
    private TextField tfProductID,tf_productName;
    @FXML
    private DatePicker datePicker;
    @FXML
    private MenuButton menuButtonQuantity;
    @FXML
    private Button btnAddItem;
    @FXML
    private Button btnDone;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeQuantityMenu();
        setDatePickerFormat();

        tfProductID.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                String productName = getProductName(newValue);
                tf_productName.setText(productName != null ? productName : "");
            } else {
                tf_productName.clear();
            }
        });
    }


    private void initializeQuantityMenu() {
        for (int i = 1; i <= 10; i++) {
            MenuItem menuItem = new MenuItem(String.valueOf(i));
            menuItem.setOnAction(this::handleQuantityOption);
            menuButtonQuantity.getItems().add(menuItem);
        }
    }

    private void handleQuantityOption(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String quantity = menuItem.getText();
        menuButtonQuantity.setText(quantity);
    }
    @FXML
    private void handleAddItemAction() {
        String productId = tfProductID.getText();
        String quantityText = menuButtonQuantity.getText();
        int quantity = 0;
        String dateAdded = datePicker.getValue() != null ? datePicker.getValue().toString() : "";

        if (productId.isEmpty() || quantityText.equals("Select") || dateAdded.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter all fields");
            alert.showAndWait();
        } else {
            try {
                quantity = Integer.parseInt(quantityText);

                if (isProductIdValid(productId)) {
                    incrementQuantity(productId, quantity);
                } else {
                    // Show an alert message for invalid productID
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid Product ID");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Invalid quantity");
                alert.showAndWait();
            }
        }

        tfProductID.clear();
        menuButtonQuantity.setText("Select");
        datePicker.setValue(null);
    }

    private String getProductName(String productId) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_practice", "root", "Shreyas@18");
             PreparedStatement statement = connection.prepareStatement("SELECT product_name FROM product WHERE product_id = ?")) {

            statement.setString(1, productId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("product_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setDatePickerFormat() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        StringConverter<LocalDate> converter = new LocalDateStringConverter(dateFormatter, dateFormatter);
        datePicker.setConverter(converter);
    }

    private boolean isProductIdValid(String productId) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_practice", "root", "Shreyas@18");
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM product WHERE product_id = ?")) {

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

    public void handleDone(ActionEvent event){
        DBUtils.changeScene(event,"logged-in.fxml","Invento-Track",null);
    }

    private void incrementQuantity(String productId, int quantity) {
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement("SELECT COUNT(*) FROM inventory WHERE product_id = ? AND user_id = ?");
             PreparedStatement updateStatement = connection.prepareStatement("UPDATE inventory SET quantity = quantity + ?, date_added = ? WHERE product_id = ? AND user_id = ?");
             PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO inventory (user_id, product_id, quantity, date_added) VALUES (?, ?, ?, ?)")) {

            selectStatement.setString(1, productId);
            selectStatement.setInt(2, DBUtils.getRetrievedUserID());
            ResultSet resultSet = selectStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                updateStatement.setInt(1, quantity);
                updateStatement.setString(2, getCurrentDate());
                updateStatement.setString(3, productId);
                updateStatement.setInt(4, DBUtils.getRetrievedUserID());
                int rowsAffected = updateStatement.executeUpdate();
                if (rowsAffected > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Stock Updated");
                    alert.setHeaderText(null);
                    alert.setContentText("Stock quantity incremented successfully.");
                    alert.showAndWait();
                }
            } else {
                insertStatement.setInt(1, DBUtils.getRetrievedUserID());
                insertStatement.setString(2, productId);
                insertStatement.setInt(3, quantity);
                insertStatement.setString(4, getCurrentDate());
                insertStatement.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Stock Added");
                alert.setHeaderText(null);
                alert.setContentText("Stock added successfully.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentDate() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            return selectedDate.format(formatter);
        } else {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            return currentDate.format(formatter);
        }
    }



}

