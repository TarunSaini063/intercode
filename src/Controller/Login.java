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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author TARUN
 */
public class Login implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXButton Signup;
    @FXML
    private JFXTextField UserName;

    @FXML
    private JFXPasswordField Password;
    @FXML
    private JFXCheckBox Remember;

    @FXML
    private JFXButton Login;

    @FXML
    private Label Forgot;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        UserName.setStyle("-fx-text-inner-color: white");
        Password.setStyle("-fx-text-inner-color: white");
    }

    @FXML
    void Login(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
        System.out.println("Try to connect database again");
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/InterCode";
        Connection connection = DriverManager.getConnection(url, "root", "root");
        String query3 = "SELECT * FROM Users";
        PreparedStatement preStat = connection.prepareStatement(query3);
        int status = 0;
        ResultSet result = preStat.executeQuery();
        while (result.next()) {
            String email = result.getString("email");
            String pass = result.getString("password");
            System.out.println("email= "+email);
            System.out.println(" pass= "+pass);
            if ((email.equals(UserName.getText())) && (pass.equals(Password.getText()))) {
                status = 1;
                System.out.println("Login successfull");
                break;
            }
        }
        if (status == 1) {
            Profile.current_user=UserName.getText();
            Parent root = FXMLLoader.load(getClass().getResource("/UI/Profile.fxml"));
            Scene scene = new Scene(root);
            Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stg.resizableProperty().setValue(Boolean.FALSE);
            stg.hide();
            stg.setScene(scene);
            stg.show();
        }
        else
        {
            System.out.println("new User");
        }
    }

    @FXML
    void OnSighUp(MouseEvent event) throws IOException {
        System.out.println("start Signup");
        Parent root = FXMLLoader.load(getClass().getResource("/UI/Signup.fxml"));
        Scene scene = new Scene(root);
        Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stg.resizableProperty().setValue(Boolean.FALSE);
        stg.hide();
        stg.setScene(scene);
        stg.show();
    }

}
