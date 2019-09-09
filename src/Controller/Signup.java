/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

/**
 *
 * @author TARUN
 */
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Signup implements Initializable {

    @FXML
    private JFXTextField Name;

    @FXML
    private JFXTextField Email;

    @FXML
    private JFXPasswordField Password;

    @FXML
    private JFXPasswordField Confpassword;
    @FXML
    private JFXButton AddUser;

    @FXML
    void CreateAccount(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        if(Password.getText().equals(Confpassword.getText())){
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/users";
            Connection connection = DriverManager.getConnection(url, "root", "");
            String query1 = "INSERT INTO `users` (`email`,`name`,`password`)" + " VALUES ('" + Email.getText() + "', '" + Name.getText() + "','" + Password.getText() + "')";
            PreparedStatement preStat = connection.prepareStatement(query1);
            preStat.executeUpdate();
            System.out.println("new user created");
            Parent root = FXMLLoader.load(getClass().getResource("/UI/Login.fxml"));
            Scene scene = new Scene(root);
            Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stg.hide();
            stg.setScene(scene);
            stg.show();
        }
        else
        {
            Confpassword.setStyle("-fx-text-box-border: red");
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Name.setStyle("-fx-text-inner-color: white");
        Password.setStyle("-fx-text-inner-color: white");
        Email.setStyle("-fx-text-inner-color: white");
        Confpassword.setStyle("-fx-text-inner-color: white");
    }

}