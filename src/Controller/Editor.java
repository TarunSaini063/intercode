/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import com.jfoenix.controls.JFXTextArea;
import intercode.Compile;
import intercode.Server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 *
 * @author TARUN
 */
public class Editor {

    public static Socket s;
    public static DataInputStream dis = null;
    public static DataOutputStream dos = null;
    Compile compile = new Compile();
    static int i = 0;
    @FXML
    private TextArea editor;
    @FXML
    private Label text;

    @FXML
    private Label C;

    @FXML
    private Label CPP;

    @FXML
    private Label JAVA;
    @FXML
    private JFXTextArea Input;

    @FXML
    private JFXTextArea Output;

    String getChar(KeyEvent e) {
        String s = null;
        if (null != e.getCode()) switch (e.getCode()) {
            case A:
                s = "A";
                break;
            case B:
                s = "B";
                break;
            case C:
                s = "C";
                break;
            case D:
                s = "D";
                break;
            case E:
                s = "E";
                break;
            case F:
                s = "F";
                break;
            case G:
                s = "G";
                break;
            case H:
                s = "H";
                break;
            case I:
                s = "I";
                break;
            case J:
                s = "J";
                break;
            case K:
                s = "K";
                break;
            case L:
                s = "L";
                break;
            case M:
                s = "M";
                break;
            case N:
                s = "N";
                break;
            case O:
                s = "O";
                break;
            case P:
                s = "P";
                break;
            case Q:
                s = "Q";
                break;
            case R:
                s = "R";
                break;
            case S:
                s = "S";
                break;
            case T:
                s = "T";
                break;
            case U:
                s = "U";
                break;
            case V:
                s = "V";
                break;
            case W:
                s = "W";
                break;
            case X:
                s = "X";
                break;
            case Y:
                s = "Y";
                break;
            case Z:
                s = "Z";
                break;
            default:
                break;
        }
        return s;
    }
    int language = 1;

    @FXML
    void Activate(MouseEvent event) {
        if (i == 0) {
            Server obj = new Server(s, dis, dos, this);
            Thread t = new Thread(obj);
            t.start();
            i = 1;
        }
    }

    @FXML
    public void keyPressed(KeyEvent e) throws IOException {
        System.out.println("Key pressed");

        String s1 = null;
        s1 = getChar(e);
        if (s1 != null) {
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            try {
                System.out.println("sending A : client");
                dos.writeUTF(s1);
                dos.flush();
                System.out.println("send data");
            } catch (IOException ex) {
                //Logger.getLogger(layoutController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setMess(String s) {
        System.out.println("FXMLDocumentController.#handleButtonAction");
        editor.appendText(s);
    }

    @FXML
    void ClickC(MouseEvent event) {
        String c = "#include<stdio.h>" + System.lineSeparator() + "int main(void){" + System.lineSeparator() + "    //Code" + System.lineSeparator() + "    Return 0;" + System.lineSeparator() + "}";
        editor.setText(c);
        language = 1;
        CPP.setTextFill(Color.web("#000000"));
        JAVA.setTextFill(Color.web("#000000"));
        C.setTextFill(Color.web("#ff0000"));

    }

    @FXML
    void ClickCpp(MouseEvent event) {
        String c = "#include <iostream>" + System.lineSeparator() + "using namespace std;" + System.lineSeparator() + "int main(void){" + System.lineSeparator() + "    //Code" + System.lineSeparator() + "    Return 0;" + System.lineSeparator() + "}";
        editor.setText(c);
        language = 2;
        CPP.setTextFill(Color.web("#ff0000"));
        C.setTextFill(Color.web("#000000"));
        JAVA.setTextFill(Color.web("#000000"));
    }

    @FXML
    void Clickjava(MouseEvent event) {
        String c = "import java.io.*" + System.lineSeparator() + "class Gochi {" + System.lineSeparator() + "    public static void main (String[] args) {" + System.lineSeparator() + "        //code;" + System.lineSeparator() + "	}" + System.lineSeparator() + "}";
        language = 3;
        editor.setText(c);
        JAVA.setTextFill(Color.web("#ff0000"));
        C.setTextFill(Color.web("#000000"));
        CPP.setTextFill(Color.web("#000000"));
    }

    void setInput() throws IOException {
        String input1 = Input.getText();
        String input = new String(input1 + " ");
        if (input == null) {
            return;
        }
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("C:\\Users\\TARUN\\Documents\\NetBeansProjects\\InterCode\\src\\Judge\\input.txt"), "utf-8"))) {
            System.out.println(input);
            writer.write(input);
        }
    }

    @FXML
    void CompileCode(MouseEvent event) throws FileNotFoundException, IOException, InterruptedException {
        boolean success = false;
        setInput();
        if (language == 1) {
            success = compile.compiling("C");
        } else if (language == 2) {
            success = compile.compiling("CPP");
        } else if (language == 3) {
            success = compile.compiling("JAVA");
        }
        Thread.sleep(500);
        if (success) {
            String error = "";
            String output = "";
            //reading output
            try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\TARUN\\Documents\\NetBeansProjects\\InterCode\\src\\Judge\\ouput.txt"))) {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                output = sb.toString();
                Output.setText(output);
            }
            if (output.equals("")) {
                try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\TARUN\\Documents\\NetBeansProjects\\InterCode\\src\\Judge\\error.txt"))) {
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();
                    while (line != null) {
                        sb.append(line);
                        sb.append(System.lineSeparator());
                        line = br.readLine();
                    }
                    error = sb.toString();
                    Output.setText(error);
                }
            } else {
            }
        } else {
            Output.setText("Compilation Error");
        }
    }

    @FXML
    public void initialize() {
        String c = "#include<stdio.h>" + System.lineSeparator() + "int main(void){" + System.lineSeparator() + "    //Code" + System.lineSeparator() + "    Return 0;" + System.lineSeparator() + "}";
        editor.setText(c);
        C.setTextFill(Color.web("#ff0000"));

    }

}
