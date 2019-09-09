/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 *
 * @author TARUN
 */
public class Profile implements Initializable {

    static String current_user = null;
    @FXML
    private JFXButton givInterview;

    @FXML
    private Label watiforinterviewer;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField Name;

    @FXML
    private JFXTextField Address1;

    @FXML
    private JFXTextField Address2;

    @FXML
    private JFXTextField Github;

    @FXML
    private JFXTextField Linkedin;

    @FXML
    private JFXTextField Email;

    @FXML
    private JFXTextField Mobile;

    @FXML
    private ImageView Profilepic;

    @FXML
    private JFXButton Changeprof;

    @FXML
    private JFXTextField Skill;

    @FXML
    private JFXTextField Qualification;

    @FXML
    private JFXButton Save;
    @FXML
    private JFXButton takeInterview;

    @FXML
    private Label watiforinterviewee;

    @FXML
    void Saveprofile(ActionEvent event) {
        System.out.println("Do not save changes");
    }

    @FXML
    void takeInterview(ActionEvent event) throws InterruptedException, IOException {
        watiforinterviewee.setText("Wating for Interviewee");
        Interviewer.ss = null;
        while (true) {
            try {
                Interviewer.ss = new Socket("localhost", 3000);
                if (Interviewer.ss != null) {
                    System.out.println("server connected");
                    Parent root = FXMLLoader.load(getClass().getResource("/UI/Interviewer.fxml"));
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(KeywordsAsync.class.getResource("/UI/keywords.css").toExternalForm());
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.hide();
                    stage.setScene(scene);
                    stage.setTitle("InterCode");
                    stage.show();
                    break;
                }
            } catch (IOException e) {
                Thread.sleep(1000);
            }
        }
        Interviewer.dis = new DataInputStream(Interviewer.ss.getInputStream());
        Interviewer.dos = new DataOutputStream(Interviewer.ss.getOutputStream());
    }

    @FXML
    void giveInterview(ActionEvent event) throws IOException {
        Interviewee.ss = new ServerSocket(3000);
        watiforinterviewer.setText("Wating for Intervieweer");
        Interviewee.s = Interviewee.ss.accept();
        Interviewee.dis = new DataInputStream(Interviewee.s.getInputStream());
        Interviewee.dos = new DataOutputStream(Interviewee.s.getOutputStream());
        Parent root = FXMLLoader.load(getClass().getResource("/UI/Interviewer.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(KeywordsAsync.class.getResource("/UI/keywords.css").toExternalForm());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide();
        stage.setScene(scene);
        stage.setTitle("InterCode");
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Name.setStyle("-fx-text-inner-color: white");
        Skill.setStyle("-fx-text-inner-color: white");
        Qualification.setStyle("-fx-text-inner-color: white");
        Mobile.setStyle("-fx-text-inner-color: white");
        Github.setStyle("-fx-text-inner-color: white");
        Linkedin.setStyle("-fx-text-inner-color: white");
        Email.setStyle("-fx-text-inner-color: white");
        Address1.setStyle("-fx-text-inner-color: white");
        Address2.setStyle("-fx-text-inner-color: white");
        watiforinterviewee.setStyle("-fx-text-inner-color: white");
        Name.setDisable(true);
        Email.setDisable(true);
        System.out.println("Finding Current user");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
        }
        String url = "jdbc:mysql://localhost:3306/users";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, "root", "");
        } catch (SQLException ex) {
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
        }
        String query3 = "SELECT * FROM users";
        PreparedStatement preStat = null;
        try {
            preStat = connection.prepareStatement(query3);
        } catch (SQLException ex) {
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
        }
        int status = 0;
        ResultSet result = null;
        try {
            result = preStat.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            while (result.next()) {
                String email = result.getString("email");
                if (current_user.equals(email)) {
                    Name.setText(result.getString("name"));
                    Github.setText(result.getString("github"));
                    Linkedin.setText(result.getString("linkedin"));
                    Address1.setText(result.getString("address1"));
                    Address2.setText(result.getString("address2"));
                    Skill.setText(result.getString("skill"));
                    Qualification.setText(result.getString("qualification"));
                    Mobile.setText(result.getString("mobile"));
                    Email.setText(email);
                    System.out.println("Current user caught");
                    break;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
