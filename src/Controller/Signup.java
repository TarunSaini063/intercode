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
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Name.setStyle("-fx-text-inner-color: white");
        Password.setStyle("-fx-text-inner-color: white");
        Email.setStyle("-fx-text-inner-color: white");
        Confpassword.setStyle("-fx-text-inner-color: white");
    }

}