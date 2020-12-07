/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author tarun
 */
public class Assignment {

    private Parent parent;
    private Scene scene;
    private Stage stage;
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
                    System.out.println(Assignmentid+" user= "+user);
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
        if(connection == null){
            System.out.print("not connected ");
            return;
        }
        Statement st = connection.createStatement();
        String query = "SELECT count(*) as total FROM Assignments where user = "+"'" +user+ "'";
        ResultSet rs = st.executeQuery(query);
        int num = 0;
        while (rs.next()) {
            num = (rs.getInt(1));
        }
//        System.out.println("user= "+user);
//        System.out.println(num);
        Change(num);
        connection.close();
    }

    public void Change(int x) {
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
        stage.setTitle("Assignments");
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.show();
        user = usr;
        System.out.println("User in inilz "+user);
        AssignmentCount = 0;
    }

}
