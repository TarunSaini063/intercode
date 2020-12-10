/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Moss.Main;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author tarun
 */
public class Assignment implements Initializable {

    private Parent parent;
    private Scene scene;
    private Stage stage;
    @FXML
    private Button upload;
    @FXML
    private VBox stack;
    List<Button> buttonlist = new ArrayList<>();
    private int AssignmentCount = 0;
    private static String user;
    public int count;

    private void setId(Button btn) {
        btn.setId(String.valueOf(AssignmentCount));
        btn.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(btn.getId());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                int Assignmentid;
                Assignmentid = Integer.parseInt(btn.getId());
                UpdateAssignment updateAssignemnt = new UpdateAssignment();
                try {
                    System.out.println(Assignmentid + " user= " + user);
                    updateAssignemnt.initialize(Assignmentid, user);
                } catch (IOException ex) {
                    Logger.getLogger(Assignment.class.getName()).log(Level.SEVERE, null, ex);
                }
                stage.close();
            }
        });

        AssignmentCount++;
    }
    @FXML
    private Button fetch;

    @FXML
    void fetchassignment(ActionEvent e) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/InterCode";
        Connection connection = DriverManager.getConnection(url, "root", "root");
        if (connection == null) {
            System.out.print("not connected ");
            return;
        }
        Statement st = connection.createStatement();
        String query = "SELECT count(*) as total FROM Assignments where user = " + "'" + user + "'";
        ResultSet rs = st.executeQuery(query);
        int num = 0;
        while (rs.next()) {
            num = (rs.getInt(1));
        }
        Change(num);
        String sql = "SELECT * FROM Assignments where user = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        stage = (Stage) upload.getScene().getWindow();
        String windowName = stage.getTitle();
        String[] words = windowName.split("\\W+");
        String user = words[1];
        statement.setString(1, user);
        String num1 = null;
        rs = statement.executeQuery();
        while (rs.next()) {
            num1 = (rs.getString("post"));
        }
        System.out.println("Post =" + num1 + " user= " + user);
        if (num1 != null && num1.equals("Intervieweer")) {
            upload.setVisible(true);
        }
        connection.close();
    }
    File file = null;

    void SelectFile() {
        stage = (Stage) upload.getScene().getWindow();
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt");
        chooser.getExtensionFilters().add(extentionFilter);
        chooser.setTitle("Open File");
        file = chooser.showOpenDialog(stage);
        String file_name;
        if (file != null) {
            file_name = file.getName();
            upload.setText(file_name);
        } else {
            upload.setText("Reupload");
            file = null;
        }
    }

    @FXML
    void plagiarism(ActionEvent event) throws Exception {
        Main obj = new Main();
        ObservableList<PieChart.Data> pieChartData = obj.main();
        System.out.println(pieChartData.size());
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/UI/piechart.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.hide();
            stage = new Stage();
            stage.setTitle("plagiarism");
            stage.setScene(new Scene(root1));
            Piechart piechart = (Piechart) fxmlLoader.getController();
            piechart.setIndex(pieChartData);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void upload(ActionEvent event) throws SQLException, IOException {
        try {
            SelectFile();
            if (file == null) {
                return;
            }
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UpdateAssignment.class.getName()).log(Level.SEVERE, null, ex);
            }
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/InterCode";
            Connection connection = DriverManager.getConnection(url, "root", "root");
            String sql = "INSERT INTO Assignments (question ,user ,assignmentId) values(?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setBlob(1, inputStream);
            String asgm = "0";
            asgm = Integer.toString(AssignmentCount + 1);
            AssignmentCount++;
            statement.setString(2, user);
            statement.setString(3, asgm);
            statement.executeUpdate();
            connection.close();
            inputStream.close();
            Alert sameclient = new Alert(Alert.AlertType.INFORMATION);  //Alert is shown
            sameclient.setTitle("Updates");
            sameclient.setContentText("Successfully update ");
            sameclient.setHeaderText("new Assignment added ");
            sameclient.show();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Assignment.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void Change(int x) {
        buttonlist.clear();
        for (int i = 0; i < x; i++) {
            Button tmp = new Button("Assignment " + i);
            setId(tmp);
            buttonlist.add(tmp);
        }
        stack.getChildren().clear(); //remove all Buttons that are currently in the container
        stack.setSpacing(20);
        stack.getChildren().addAll(buttonlist); //then add all your Buttons that you just created
    }

    public void initialize(String usr) throws IOException {
        parent = FXMLLoader.load(getClass().getResource("/UI/Assignment.fxml"));
        scene = new Scene(parent);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Assignments (" + usr + ")");
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.show();
        user = usr;
        System.out.println("User in inilz " + user);
        AssignmentCount = 0;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        upload.setVisible(false);
    }

}
