/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import static Controller.Interviewee.dis;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import com.sun.javafx.scene.text.HitInfo;
import intercode.Compile;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.URL;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

/**
 *
 * @author TARUN
 */
public class Interviewer implements Initializable {

    static Socket ss;
    static DataInputStream dis = null;
    static DataOutputStream dos = null;
    Compile compiler = new Compile();
    static int editorStatus = 0;
    @FXML
    private Label C;

    @FXML
    private Label CPP;

    @FXML
    private Label JAVA;

    @FXML
    private Label PYTHON;

    @FXML
    private JFXComboBox<?> frontsize;

    @FXML
    private JFXComboBox<?> fronttype;

    @FXML
    private JFXTextArea input;

    @FXML
    private JFXTextArea output;

    @FXML
    private JFXButton compile;

    @FXML
    private JFXTextArea question;

    @FXML
    private JFXButton endinter;

    @FXML
    private CodeArea editor;
    private static final String[] KEYWORDSJAVA = new String[]{"abstract", "assert", "boolean", "break",
        "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else",
        "enum", "extends", "final", "finally", "float", "for", "goto", "if", "implements", "import",
        "instanceof", "int", "interface", "long", "native", "new", "package", "private", "protected", "public",
        "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws",
        "transient", "try", "void", "volatile", "while"
    };

    private static final String[] KEYWORDSCPP = {"auto", "const", "double", "float", "int", "short",
        "struct", "unsigned", "break", "continue", "else", "for", "long", "signed",
        "switch", "void", "case", "default", "enum", "goto", "register", "sizeof",
        "typedef", "volatile", "char", "do", "extern", "if", "return", "static",
        "union", "while", "asm", "dynamic_cast", "namespace", "reinterpret_cast", "try",
        "bool", "explicit", "new", "static_cast", "typeid", "catch", "false", "operator",
        "template", "typename", "class", "friend", "private", "this", "using", "const_cast",
        "inline", "public", "throw", "virtual", "delete", "mutable", "protected", "true", "wchar_t"};

    private static final String[] KEYWORDSC = {"auto", "break", "case", "char", "const", "continue", "default",
        "do", "double", "else", "enum", "extern", "float", "for", "goto", "if", "int", "long", "register",
        "return", "short", "signed", "sizeof", "static", "struct", "switch", "typedef", "union",
        "unsigned", "void", "volatile", "while"
    };

    private static final String[] KEYWORDSPYTHON = {"False", "class", "finally", "is", "return", "None", "continue",
        "for", "lambda", "try", "True", "def", "from", "nonlocal", "while", "and", "del", "global", "not", "with", "as",
        "elif", "if", "or", "yield", "assert", "else", "import", "pass", "break", "except", "in", "raise"
    };

    private static final String KEYWORD_PATTERN_JAVA = "\\b(" + String.join("|", KEYWORDSJAVA) + ")\\b";
    private static final String KEYWORD_PATTERN_CPP = "\\b(" + String.join("|", KEYWORDSCPP) + ")\\b";
    private static final String KEYWORD_PATTERN_C = "\\b(" + String.join("|", KEYWORDSC) + ")\\b";
    private static final String KEYWORD_PATTERN_PYTHON = "\\b(" + String.join("|", KEYWORDSPYTHON) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

    private static final Pattern PATTERN_JAVA = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN_JAVA + ")"
            + "|(?<PAREN>" + PAREN_PATTERN + ")"
            + "|(?<BRACE>" + BRACE_PATTERN + ")"
            + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
            + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
            + "|(?<STRING>" + STRING_PATTERN + ")"
            + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );
    private static final Pattern PATTERN_CPP = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN_CPP + ")"
            + "|(?<PAREN>" + PAREN_PATTERN + ")"
            + "|(?<BRACE>" + BRACE_PATTERN + ")"
            + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
            + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
            + "|(?<STRING>" + STRING_PATTERN + ")"
            + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );
    private static final Pattern PATTERN_C = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN_C + ")"
            + "|(?<PAREN>" + PAREN_PATTERN + ")"
            + "|(?<BRACE>" + BRACE_PATTERN + ")"
            + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
            + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
            + "|(?<STRING>" + STRING_PATTERN + ")"
            + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );
    private static final Pattern PATTERN_PYTHON = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN_PYTHON + ")"
            + "|(?<PAREN>" + PAREN_PATTERN + ")"
            + "|(?<BRACE>" + BRACE_PATTERN + ")"
            + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
            + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
            + "|(?<STRING>" + STRING_PATTERN + ")"
            + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    public void setMess(String msg) {
        editor.appendText(msg);
        final Pattern whiteSpace = Pattern.compile("^\\s+");
        int caretPosition = editor.getCaretPosition();
        int currentParagraph = editor.getCurrentParagraph();
        Matcher m0 = whiteSpace.matcher(editor.getParagraph(currentParagraph - 1).getSegments().get(0));
        if (m0.find()) {
            Platform.runLater(() -> editor.insertText(caretPosition, m0.group()));
        }
    }

    Thread readMessage = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    String msg = dis.readUTF();
                    System.out.println("read message= " + msg);
                    setMess(msg);
                } catch (IOException e) {
                    try {
                        ss.close();
                        dis.close();
                        dos.close();
                    } catch (IOException ex) {
                        //Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    });

    @FXML
    void changesize(ActionEvent event) {

    }

    @FXML
    void changetype(ActionEvent event) {

    }

    @FXML
    void endinterview(ActionEvent event) throws IOException {
        ss.close();
        dis.close();
        dos.close();

    }
    static int language = 0;

    @FXML
    void onclickC(MouseEvent event) {
        String c = "#include<stdio.h>" + System.lineSeparator() + "int main(void){" + System.lineSeparator() + "    //Code" + System.lineSeparator() + "    Return 0;" + System.lineSeparator() + "}";
        editor.replaceText(0, 0, c);
        language = 1;
        CPP.setTextFill(Color.web("#000000"));
        JAVA.setTextFill(Color.web("#000000"));
        C.setTextFill(Color.web("#ff0000"));
        if(editorStatus==0)
        {
            readMessage.start();
            editorStatus=1;
        }
    }

    @FXML
    void onclickCPP(MouseEvent event) {
        String c = "#include <iostream>" + System.lineSeparator() + "using namespace std;" + System.lineSeparator() + "int main(void){" + System.lineSeparator() + "    //Code" + System.lineSeparator() + "    Return 0;" + System.lineSeparator() + "}";
        editor.replaceText(0, 0, c);
        language = 2;
        CPP.setTextFill(Color.web("#ff0000"));
        C.setTextFill(Color.web("#000000"));
        JAVA.setTextFill(Color.web("#000000"));
        if(editorStatus==0)
        {
            readMessage.start();
            editorStatus=1;
        }

    }

    @FXML
    void onclickJAVA(MouseEvent event) {
        String c = "import java.io.*" + System.lineSeparator() + "class Gochi {" + System.lineSeparator() + "    public static void main (String[] args) {" + System.lineSeparator() + "        //code;" + System.lineSeparator() + "	}" + System.lineSeparator() + "}";
        language = 3;
        editor.replaceText(0, 0, c);
        JAVA.setTextFill(Color.web("#ff0000"));
        C.setTextFill(Color.web("#000000"));
        CPP.setTextFill(Color.web("#000000"));
        if(editorStatus==0)
        {
//            readMessage.start();
            editorStatus=1;
        }
    }

    @FXML
    void onclickPYTHON(MouseEvent event) {
        if(editorStatus==0)
        {
            readMessage.start();
            editorStatus=1;
        }

    }

    void setInput() throws IOException {
        String input1 = input.getText();
        String input = new String(input1 + " ");
        if (input1 == null) {
            return;
        }
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("C:\\Users\\TARUN\\Documents\\NetBeansProjects\\InterCode\\src\\Judge\\input.txt"), "utf-8"))) {
            System.out.println(input);
            writer.write(input);
        }
    }

    @FXML
    void oncompile(ActionEvent event) throws IOException, InterruptedException {
        boolean success = false;
        setInput();
        if (language == 1) {
            success = compiler.compiling("C");
        } else if (language == 2) {
            success = compiler.compiling("CPP");
        } else if (language == 3) {
            success = compiler.compiling("JAVA");
        }
        Thread.sleep(500);
        if (success) {
            String error = "";
            String output1 = "";
            //reading output
            try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\TARUN\\Documents\\NetBeansProjects\\InterCode\\src\\Judge\\output.txt"))) {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                output1 = sb.toString();
                output.setText(output1);
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
                    output.setText(error);
                }
            } else {
            }
        } else {
            output.setText("Compilation Error");
        }
    }

    @FXML
    void onwriting(KeyEvent event) {
        final Pattern whiteSpace = Pattern.compile("^\\s+");
        int caretPosition = editor.getCaretPosition();
        int currentParagraph = editor.getCurrentParagraph();
        Matcher m0 = whiteSpace.matcher(editor.getParagraph(currentParagraph - 1).getSegments().get(0));
        if (m0.find()) {
            Platform.runLater(() -> editor.insertText(caretPosition, m0.group()));
        }
//        try {
//            dos.writeUTF(editor.getText());
//            dos.flush();
//            System.out.println("send message: ");
//        } catch (IOException ex) {
////                Logger.getLogger(layoutController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editor.setParagraphGraphicFactory(LineNumberFactory.get(editor));
        Subscription cleanupWhenNoLongerNeedIt = editor.multiPlainChanges().successionEnds(Duration.ofMillis(500)).subscribe(ignore -> editor.setStyleSpans(0, computeHighlighting(editor.getText())));
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN_C.matcher(text);;
        if (language == 2) {
            matcher = PATTERN_CPP.matcher(text);
        } else if (language == 3) {
            matcher = PATTERN_JAVA.matcher(text);
        } else if (language == 4) {
            matcher = PATTERN_PYTHON.matcher(text);
        }
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass
                    = matcher.group("KEYWORD") != null ? "keyword"
                    : matcher.group("PAREN") != null ? "paren"
                    : matcher.group("BRACE") != null ? "brace"
                    : matcher.group("BRACKET") != null ? "bracket"
                    : matcher.group("SEMICOLON") != null ? "semicolon"
                    : matcher.group("STRING") != null ? "string"
                    : matcher.group("COMMENT") != null ? "comment"
                    : null;
            /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}
