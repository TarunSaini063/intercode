/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package intercode;

import Controller.Editor;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author TARUN
 */
public class InterCode extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.resizableProperty().setValue(Boolean.FALSE);
        Parent root = FXMLLoader.load(getClass().getResource("Controller.Login.fxml"));
        Scene scene = new Scene(root);
        //scene.getStylesheets().add("C:\\Users\\TARUN\\Documents\\NetBeansProjects\\JavaFX1\\src\\javafx1\\loginui.css");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.print("craeting server");
        Editor.s = null;
        while (true) {
            try {
                Editor.s = new Socket("localhost", 3000);
                if (Editor.s != null) {
                    System.out.print("server connected");
                    break;
                }
            } catch (IOException e) {
                Thread.sleep(1000);
            }
        }
        Editor.dis = new DataInputStream(Editor.s.getInputStream());
        Editor.dos = new DataOutputStream(Editor.s.getOutputStream());
        //Server obj = new Server(layoutController.s, layoutController.dis, layoutController.dos);
        //Thread t = new Thread(obj);
        //t.start();
        launch(args);
    }
}
