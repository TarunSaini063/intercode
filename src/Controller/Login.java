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
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
    private JFXCheckBox Remember;

    @FXML
    private JFXButton Login;

    @FXML
    private Label Forgot;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    void OnSighUp(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("UI.Signup.fxml"));
        Scene scene = new Scene(root);
        Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stg.hide();
        stg.setScene(scene);
        stg.show();
    }

}
