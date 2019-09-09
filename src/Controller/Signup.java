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
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

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
    void CreateAccount(ActionEvent event) throws ClassNotFoundException, SQLException {
        if(Password.getText().equals(Confpassword.getText())){
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/Users";
            Connection connection = DriverManager.getConnection(url, "root", "");
            String query1 = "INSERT INTO `user1` (`name`,`username`,`password`)" + " VALUES ('" + Name.getText() + "', '" + Email.getText() + "','" + Password.getText() + "')";
            PreparedStatement preStat = connection.prepareStatement(query1);
            preStat.executeUpdate();
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