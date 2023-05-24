package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class DBUtils {

    private static int retrievedUserID;

    public static int getRetrievedUserID() {
        return retrievedUserID;
    }

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username){
        Parent root = null;

        if (username != null){
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                LoggedInController loggedInController = loader.getController();
                //loggedInController.setUserInformation(username);
                //loggedInController.setUsername(username);

            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        else {
            try {
                root = FXMLLoader.load(Objects.requireNonNull(DBUtils.class.getResource(fxmlFile)));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root,800,600));
        stage.show();
    }
    public static void signUpUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        PreparedStatement psRetrieveUserID = null;
        ResultSet resultSet = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_practice", "root", "Shreyas@18");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM user WHERE username = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            if (resultSet.isBeforeFirst()) {
                System.out.println("User already exists");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You can't use this username.");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO user (username, password) VALUES (?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.executeUpdate();

                psRetrieveUserID = connection.prepareStatement("SELECT userid FROM user WHERE username = ?");
                psRetrieveUserID.setString(1, username);
                rs = psRetrieveUserID.executeQuery();
                if (rs.next()) {
                    retrievedUserID = rs.getInt("userid");
                }

                changeScene(event, "logged-in.fxml", "Welcome", username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psRetrieveUserID != null) {
                try {
                    psRetrieveUserID.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void logInUser(ActionEvent event,String username,String password){
        Connection connection = null;
        PreparedStatement preparedStatement=null;
        PreparedStatement ps =null;
        ResultSet resultSet = null;
        ResultSet rs = null;
        try {
             connection
                    = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_practice","root","Shreyas@18");
           preparedStatement  = connection.prepareStatement("SELECT password FROM user WHERE username = ? ");
            preparedStatement.setString(1,username);
             resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()){
                System.out.println("User not found on database!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided Credentials are incorrect");
                alert.show();
            }
            else {
                while (resultSet.next()){
                    String retrievedPassword = resultSet.getString("password");

                    if (retrievedPassword.equals(password)){
                        changeScene(event,"logged-in.fxml","Welcome",username);

                        connection
                                = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_practice","root","Shreyas@18");
                        ps  = connection.prepareStatement("SELECT userid FROM user WHERE username = ? ");
                        ps.setString(1,username);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            retrievedUserID = rs.getInt("userid");
                        }

                    }
                    else {
                        System.out.println("Passwords did not match");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect");
                        alert.show();
                    }
                }

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null){
                try {
                    preparedStatement.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if (connection!= null){
                try {
                    connection.close();

                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            }
    }
    public static Connection getConnection() {
        Connection connection = null;
        try {
            String url = "jdbc:mysql://localhost:3306/jdbc_practice";
            String username = "root";
            String password = "Shreyas@18";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

}
