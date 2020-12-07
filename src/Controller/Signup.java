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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Signup implements Initializable {

    @FXML
    private JFXTextField Name;
    ToggleGroup group;
    @FXML
    private JFXTextField Email;

    @FXML
    private JFXPasswordField Password;
    @FXML
    private HBox post;
    @FXML
    private JFXPasswordField Confpassword;
    @FXML
    private JFXButton AddUser;
    private String postName="Interviewee";
    RadioButton Intervieweer,Interviewee;
    @FXML
    void CreateAccount(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        if (Password.getText().equals(Confpassword.getText())) {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/InterCode";
            Connection connection = DriverManager.getConnection(url, "root", "root");
            String query1 = "INSERT INTO `Users` (`email`,`name`,`password`,`post`)" + " VALUES ('" + Email.getText() + "', '" + Name.getText() + "','" + Password.getText() + "','" + postName + "')";
            PreparedStatement preStat = connection.prepareStatement(query1);
            preStat.executeUpdate();
            query1 = "INSERT INTO `Assignments` (`user`,`post`)" + " VALUES ('" + Email.getText() + "','" + postName + "')";
            preStat = connection.prepareStatement(query1);
            preStat.executeUpdate();
            System.out.println("new user created");
            Parent root = FXMLLoader.load(getClass().getResource("/UI/Login.fxml"));
            Scene scene = new Scene(root);
            Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stg.hide();
            stg.setScene(scene);
            stg.show();
            connection.close();
        } else {
            Confpassword.setStyle("-fx-text-box-border: red");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Name.setStyle("-fx-text-inner-color: white");
        Password.setStyle("-fx-text-inner-color: white");
        Email.setStyle("-fx-text-inner-color: white");
        Confpassword.setStyle("-fx-text-inner-color: white");
        group = new ToggleGroup();
 
       group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
           @Override
           public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
               if (group.getSelectedToggle() != null) {
                   RadioButton button = (RadioButton) group.getSelectedToggle();
                   System.out.println("Button: " + button.getText());
                   postName=button.getText();
               }
           }
       });
       Interviewee = new RadioButton("Interviewee");
       Interviewee.setToggleGroup(group);
       Interviewee.setStyle("-fx-text-fill: white;");
       Intervieweer = new RadioButton("Intervieweer");
       Intervieweer.setToggleGroup(group);
       Interviewee.setSelected(true);
       Intervieweer.setStyle("-fx-text-fill: white;");
       post.setPadding(new Insets(10));
       post.setSpacing(5);
       post.getChildren().addAll(Interviewee, Intervieweer);
    }

}
