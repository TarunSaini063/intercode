/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author tarun
 */
public class UpdateAssignment {

    private Parent parent;
    private Scene scene;
    private Stage stage;
    @FXML
    private Button SelectFile;

    @FXML
    private TextField FileName;

    @FXML
    private Button SendFile;

    @FXML
    private Button Download;
    File file;

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "#";
        }
        return name.substring(lastIndexOf);
    }

    @FXML
    void SelectFile(ActionEvent event) {
        stage = (Stage) SelectFile.getScene().getWindow();
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt");
        chooser.getExtensionFilters().add(extentionFilter);
        chooser.setTitle("Open File");
        file = chooser.showOpenDialog(stage);
        String file_name;
        if (file != null) {
            System.out.println(getFileExtension(file));
            if (getFileExtension(file).equals(".txt")) {
                file_name = file.getName();
                FileName.setText(file_name);
                SendFile.setDisable(false);

            } else {
                FileName.setText("Select txt file only..");
                file = null;
            }
        } else {
            FileName.setText("Error in Selecting File");
            file = null;
        }
    }

    @FXML
    void getassignment(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/InterCode";
        Connection connection = DriverManager.getConnection(url, "root", "root");
        String sql = "Select  * from Assignments where user = ? and assignmentId = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        stage = (Stage) SelectFile.getScene().getWindow();
        String windowName = stage.getTitle();
        String[] words = windowName.split("\\W+");
        String user, id;
        id = words[1];
        user = words[3];
        System.out.println(user + " " + id);
        statement.setString(1, user);
        statement.setString(2, id);
        ResultSet rs = statement.executeQuery();
        Blob blob = null;
        while (rs.next()) {
            blob = rs.getBlob("assignment");
        }
        if (blob == null) {
            System.out.println("Blob null\n line " + 90 + " updAssign");
            return;
        }
        byte byteArray[] = blob.getBytes(1, (int) blob.length());
        String Path = "./src/Assignments/" + user;
        File dir = new File(Path);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }
        Path += "/" + id + ".txt";
        if (new File(Path).exists()) {
            new File(Path).delete();
        }
        System.out.println(Path);
        FileOutputStream outPutStream = new FileOutputStream(Path);
        outPutStream.write(byteArray);
        outPutStream.close();
        connection.close();
        Alert sameclient = new Alert(Alert.AlertType.INFORMATION);  //Alert is shown 
        sameclient.setTitle("Updates");
        sameclient.setContentText("task Successful ");
        sameclient.setHeaderText(" Successfully download assignment " + id);
        sameclient.show();

    }

    @FXML
    void setassignment(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        if (file == null) {
            return;
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UpdateAssignment.class.getName()).log(Level.SEVERE, null, ex);
        }
        stage = (Stage) SelectFile.getScene().getWindow();
        String windowName = stage.getTitle();
        String[] words = windowName.split("\\W+");
        if (words.length < 4) {
            System.out.println("window name incorrect");
            return;
        }
        String user, id;
        id = words[1];
        user = words[3];
        System.out.println(user + " " + id);
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/InterCode";
        Connection connection = DriverManager.getConnection(url, "root", "root");
        String sql = "UPDATE Assignments SET assignment = ? where user = ? and assignmentId = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setBlob(1, inputStream);
        statement.setString(2, user);
        statement.setString(3, id);
        int updates = 0;
        updates = statement.executeUpdate();
        connection.close();
        inputStream.close();
        System.out.println(sql + " " + user + " " + id);
        Alert sameclient = new Alert(Alert.AlertType.INFORMATION);  //Alert is shown 
        sameclient.setTitle("Updates");
        sameclient.setContentText("Successfully update ");
        sameclient.setHeaderText("total Updates = " + updates);
        sameclient.show();

    }

    public void initialize(int id, String user) throws IOException {
        parent = FXMLLoader.load(getClass().getResource("/UI/UpdateAssignment.fxml"));
        scene = new Scene(parent);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Assignment " + id + " User= " + user);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.show();
        file = null;
    }
}
