/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

//import static Controller.Interviewee.dis;
import Controller.KeywordsAsync;
import Utilities.ScreenShoot;
import Utilities.Trie;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import intercode.Compile;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.IntFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static javafx.application.Application.setUserAgentStylesheet;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.InlineCssTextArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.EventStream;
import org.reactfx.EventStreams;
import static org.reactfx.EventStreams.nonNullValuesOf;
import org.reactfx.Subscription;
import org.reactfx.value.Val;
import org.reactfx.value.Var;

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
    private Stage stage;
    BoundsPopup AutoComplete;
    double caretXOffset = 0;
    double caretYOffset = 0;
    String currentWord = "";
    @FXML
    private Label C;

    @FXML
    private Label CPP;

    @FXML
    private Label JAVA;

    @FXML
    private Label PYTHON;

    @FXML
    private JFXComboBox<String> themes;

    @FXML
    private JFXTextArea input;

    @FXML
    private JFXTextArea output;

    @FXML
    private JFXButton compile;
    @FXML
    private TextArea Current_Message;
    @FXML
    private JFXTextArea question;
    @FXML
    private InlineCssTextArea Chat_window;
    @FXML
    private JFXButton endinter;

    @FXML
    private CodeArea editor;
    ObservableList<String> availableThemes = FXCollections.observableArrayList("Dark", "Light");
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

    private class BoundsPopup extends Popup {

        /**
         * Indicates whether popup should still be shown even when its item
         * (caret/selection) is outside viewport
         */
        private final Var<Boolean> showWhenItemOutsideViewport = Var.newSimpleVar(true);

        public final EventStream<Boolean> outsideViewportValues() {
            return showWhenItemOutsideViewport.values();
        }

        public final void invertViewportOption() {
            showWhenItemOutsideViewport.setValue(!showWhenItemOutsideViewport.getValue());
        }

        /**
         * Indicates whether popup has been hidden since its item
         * (caret/selection) is outside viewport and should be shown when that
         * item becomes visible again
         */
        private final Var<Boolean> hideTemporarily = Var.newSimpleVar(false);

        public final boolean isHiddenTemporarily() {
            return hideTemporarily.getValue();
        }

        public final void setHideTemporarily(boolean value) {
            hideTemporarily.setValue(value);
        }

        public final void invertVisibility() {
            if (isShowing()) {
                hide();
            } else {
                show(stage);
            }
        }

        private final VBox vbox;
        private final ListView<String> button;

        public final void setText(String text) {
            button.getItems().clear();
            button.getItems().add("First Item");
            button.getItems().add("Second Item");
            button.getItems().add("Third Item");
        }

        public final void setText(List<String> words) {
            button.getItems().clear();
            int ROW_HEIGHT = 24;
            for (String s : words) {
                button.getItems().add(s);
            }
            button.setPrefHeight(words.size() * ROW_HEIGHT + 2);
        }
        EventHandler<KeyEvent> Select_autoComplete = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    String selectedItem = button.getSelectionModel().getSelectedItem();
                    selectedItem = selectedItem.substring(currentWord.length());
                    editor.insertText(editor.getCaretPosition(), selectedItem);
                    AutoComplete.invertVisibility();
                    event.consume();
                }
            }
        };
        EventHandler<MouseEvent> Select_autoComplete_Click = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String selectedItem = button.getSelectionModel().getSelectedItem();
                editor.insertText(editor.getCaretPosition(), selectedItem);
                AutoComplete.invertVisibility();
                event.consume();
            }
        };

        BoundsPopup(String buttonText) {
            super();
            button = new ListView<String>();
            button.getItems().add("");
            button.addEventFilter(KeyEvent.KEY_PRESSED, Select_autoComplete);
            button.addEventFilter(MouseEvent.MOUSE_CLICKED, Select_autoComplete_Click);
            vbox = new VBox(button);
            vbox.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE, null, null)));
            vbox.setPadding(new Insets(5));
            getContent().add(vbox);
        }
    }

    class ArrowFactory implements IntFunction<Node> {

        private final ObservableValue<Integer> shownLine;

        ArrowFactory(ObservableValue<Integer> shownLine) {
            this.shownLine = shownLine;
        }

        @Override
        public Node apply(int lineNumber) {
            Polygon triangle = new Polygon(0.0, 0.0, 10.0, 5.0, 0.0, 10.0);
            triangle.setFill(Color.GREEN);
            ObservableValue<Boolean> visible = Val.map(
                    shownLine,
                    sl -> sl == lineNumber);

            triangle.visibleProperty().bind(Val.conditionOnShowing(visible, triangle));
            return triangle;
        }
    }

    Task task = new Task<Void>() {
        @Override
        public Void call() throws Exception {
            int i = 0;
            while (true) {
                String msg = dis.readUTF();
                String msg1 = msg;
                System.out.println("read message= " + msg);
                if (msg1.startsWith("Message")) {
                    int end = 7, start = 7;
                    int len = msg1.length();
                    for (; start < len; start++) {
                        if (msg1.charAt(start) == ' ' || msg1.charAt(start) == '\n') {
                            break;
                        }
                    }
                    String name = msg1.substring(start, end);
                    msg1 = msg1.substring(end);
                    String[] mess = msg1.split("\\r?\\n");
                    String background, background1;
//        Chat_window.setStyle("-fx-font-size: 25px;");
                    if (flip % 2 == 0) {
                        background = "-rtfx-background-color: green;";
                        background1 = "-rtfx-background-color: blue; ";
                        flip++;
                    } else {
                        background = "-rtfx-background-color: red;";
                        background1 = "-rtfx-background-color: blue; ";
                        flip++;
                    }
                    String message = name + "\n";
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Chat_window.appendText(message);
                            Chat_window.setStyle(line_number, 0, name.length(), background);
                        }
                    });

                    line_number++;
                    for (String lines : mess) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Chat_window.appendText(lines + "\n");
                                Chat_window.setStyle(line_number, 0, lines.length(), background1);
                            }
                        });

                        line_number++;
                    }
                    String background3 = "-fx-background-color: #6699cc;";
                     Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Chat_window.setStyle(background3);
                                Chat_window.appendText(System.lineSeparator());
                            }
                        });
                    line_number++;
                    continue;
                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            editor.replaceText(msg);
                        }
                    });
                }
                Thread.sleep(100);
            }
        }
    };
    int line_number = 0, flip = 0;

    @FXML
    void Send_Message(MouseEvent event) {
        String background, background1;
//        Chat_window.setStyle("-fx-font-size: 25px;");
        if (flip % 2 == 0) {
            background = "-rtfx-background-color: green;";
            background1 = "-rtfx-background-color: blue; ";
            flip++;
        } else {
            background = "-rtfx-background-color: red;";
            background1 = "-rtfx-background-color: blue; ";
            flip++;
        }
        String name = "Tarun ";
        String mess[] = Current_Message.getText().split("\\r?\\n");
        String message = name + "\n";
        Chat_window.appendText(message);
        Chat_window.setStyle(line_number, 0, name.length(), background);
        line_number++;
        name = "Message " + name;
        for (String lines : mess) {
            name = name + lines + "\n";
            Chat_window.appendText(lines + System.lineSeparator());
            Chat_window.setStyle(line_number, 0, lines.length(), background1);
            line_number++;
        }
        String background3 = "-fx-background-color: #6699cc;";
        Chat_window.setStyle(background3);
        line_number++;
        Chat_window.appendText(System.lineSeparator());
        Current_Message.setText("");
        try {
            dos.writeUTF(name);
            dos.flush();
            System.out.println("send message: " + name);
        } catch (IOException ex) {
//                Logger.getLogger(layoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void Message_received(String[] mess, String name) {
        String background, background1;
//        Chat_window.setStyle("-fx-font-size: 25px;");
        if (flip % 2 == 0) {
            background = "-rtfx-background-color: green;";
            background1 = "-rtfx-background-color: blue; ";
            flip++;
        } else {
            background = "-rtfx-background-color: red;";
            background1 = "-rtfx-background-color: blue; ";
            flip++;
        }
        String message = name + System.lineSeparator();
        Chat_window.appendText(message);
        Chat_window.setStyle(line_number, 0, name.length(), background);
        line_number++;
        for (String lines : mess) {
            Chat_window.appendText(lines + System.lineSeparator());
            Chat_window.setStyle(line_number, 0, lines.length(), background1);
            line_number++;
        }
        String background3 = "-fx-background-color: #6699cc;";
        Chat_window.setStyle(background3);
        line_number++;
        Chat_window.appendText(System.lineSeparator());
    }

    @FXML
    void changetheme(ActionEvent event) {
        System.out.println("scene stylesheets: " + themes.getStylesheets());
        Scene scene = themes.getScene();
        System.out.println(scene);
        scene.getStylesheets().clear();
        setUserAgentStylesheet(null);
        String currentTheme, path = "";
        currentTheme = themes.getValue().toString();
        System.out.println("Current theme= " + currentTheme);
        switch (currentTheme) {
            case "Dark":
                path = "/UI/themes/Dark.css";
                editor.setStyle("-fx-background-color: #999966; -fx-font-size: 20px; ");
                break;
            case "Light":
                editor.setStyle("-fx-background-color: white;-fx-font-size: 20px;");
                path = "/UI/themes/Light.css";

        }
        if (path != "") {
            scene.getStylesheets().add(KeywordsAsync.class.getResource(path).toExternalForm());
        } else {
            System.out.println("Current theme invalid");
        }
    }

    @FXML
    void endinterview(ActionEvent event) throws IOException {
        ss.close();
        dis.close();
        dos.close();
        Parent root = FXMLLoader.load(getClass().getResource("/UI/Login.fxml"));
        Scene scene = new Scene(root);
        Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stg.close();
        stg = new Stage();
        stg.setScene(scene);
        stg.resizableProperty().setValue(Boolean.FALSE);
        stg.show();

    }
    static int language = 0;

    @FXML
    void onclickC(MouseEvent event) {
        String c = "#include<stdio.h>" + System.lineSeparator() + "int main(void){" + System.lineSeparator() + "        //Code" + System.lineSeparator() + "        return 0;" + System.lineSeparator() + "}";
        editor.replaceText(c);
        language = 1;
        CPP.setTextFill(Color.web("#000000"));
        JAVA.setTextFill(Color.web("#000000"));
        C.setTextFill(Color.web("#ff0000"));
        if (editorStatus == 0) {
            System.out.print("start reading thread");
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            readMessage.start();
            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start();
            editorStatus = 1;
        }
    }

    @FXML
    void onclickCPP(MouseEvent event) {
        String c = "#include <iostream>" + System.lineSeparator() + "using namespace std;" + System.lineSeparator() + "int main(void){" + System.lineSeparator() + "        //Code" + System.lineSeparator() + "        return 0;" + System.lineSeparator() + "}";
        editor.replaceText(c);
        language = 2;
        CPP.setTextFill(Color.web("#ff0000"));
        C.setTextFill(Color.web("#000000"));
        JAVA.setTextFill(Color.web("#000000"));
        if (editorStatus == 0) {
            System.out.print("start reading thread");
//            readMessage.start();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start();
            editorStatus = 1;
        }

    }

    @FXML
    void onclickJAVA(MouseEvent event) {
        String c = "import java.io.*" + System.lineSeparator() + "class Gochi {" + System.lineSeparator() + "public static void main (String[] args) {" + System.lineSeparator() + "        //code;" + System.lineSeparator() + "        }" + System.lineSeparator() + "}";
        language = 3;
        editor.replaceText(c);
        JAVA.setTextFill(Color.web("#ff0000"));
        C.setTextFill(Color.web("#000000"));
        CPP.setTextFill(Color.web("#000000"));
        if (editorStatus == 0) {
            System.out.print("start reading thread");
//            readMessage.start();
            Thread th = new Thread(task);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            th.setDaemon(true);
            th.start();
            editorStatus = 1;
        }
    }

    @FXML
    void onclickPYTHON(MouseEvent event) {
        if (editorStatus == 0) {
//            readMessage.start();
            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start();
            editorStatus = 1;
        }

    }

    void setInput() throws IOException {
        String input1 = input.getText();
        File InputFile = new File("./src/Judge/input.txt");
        String name = InputFile.getCanonicalPath();
        String testcase = new String(input1 + " ");
        if (language == 1) {
            InputFile = new File("./src/Judge/code.c");
        } else if (language == 2) {
            InputFile = new File("./src/Judge/code.cpp");
        } else if (language == 3) {
            InputFile = new File("./src/Judge/code.java");
        }
        name = InputFile.getCanonicalPath();
        System.out.println(name);
        FileWriter myWriter = new FileWriter(InputFile);
        testcase = editor.getText();
        myWriter.write(testcase);
        myWriter.close();
    }

    @FXML
    void oncompile(ActionEvent event) throws IOException, InterruptedException {
        boolean success = false;
        setInput();
        if (language == 1) {
            success = compiler.compiling("C", input.getText());
        } else if (language == 2) {
            success = compiler.compiling("CPP", input.getText());
        } else if (language == 3) {
            success = compiler.compiling("JAVA", input.getText());
        }
        Thread.sleep(500);
        if (success) {

            String output1 = "";
            try (BufferedReader br = new BufferedReader(new FileReader("./src/Judge/output.txt"))) {
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
        } else {
            String error = "";
            try (BufferedReader br = new BufferedReader(new FileReader("./src/Judge/error.txt"))) {
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
            output.setText(error);
        }
    }

    //Creating EventHandler Object   
    EventHandler<KeyEvent> Tab_Filter = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.TAB) {
                String s = "        ";//Tab size=8
                editor.insertText(editor.getCaretPosition(), s);
                event.consume();
            }
        }
    };
    Pattern whiteSpace = Pattern.compile("^\\s+");
    //Creating EventHandler Object   
    EventHandler<KeyEvent> Indentation_filter = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER) {
                Matcher m = whiteSpace.matcher(editor.getParagraph(editor.getCurrentParagraph()).getSegments().get(0));
                if (m.find()) {
                    Platform.runLater(() -> editor.insertText(editor.getCaretPosition(), System.lineSeparator() + m.group()));
                }
                event.consume();
            }
        }
    };
    EventHandler<KeyEvent> AutoComplete_filter = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.isControlDown() && event.getCode() == KeyCode.SPACE && event.getCharacter() != " ") {
                int nm = editor.caretColumnProperty().getValue();
                System.out.println("AutoComplete\n" + "line number= " + nm + " " + editor.getCaretColumn());
                Trie trie = new Trie(editor.getText());
                List<String> words = new ArrayList<String>();
                words = trie.getWordsForPrefix(currentWord);
                for (String word : words) {
                    System.out.println(word);
                }
                AutoComplete.setText(words);
                AutoComplete.show(stage);
                event.consume();
            }
        }
    };

    @FXML
    void onwriting(KeyEvent event) {
        try {
            dos.writeUTF(editor.getText());
            dos.flush();
            System.out.println("send message: " + editor.getText());
        } catch (IOException ex) {
//                Logger.getLogger(layoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (AutoComplete.isShowing()) {
            AutoComplete.invertVisibility();
        }
//        System.out.println(currentWord);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IntFunction<Node> numberFactory = LineNumberFactory.get(editor);
        IntFunction<Node> arrowFactory = new ArrowFactory(editor.currentParagraphProperty());
        IntFunction<Node> graphicFactory = line -> {
            HBox hbox = new HBox(
                    numberFactory.apply(line),
                    arrowFactory.apply(line));
            hbox.setAlignment(Pos.CENTER_LEFT);
            return hbox;
        };
        editor.setParagraphGraphicFactory(graphicFactory);
        Subscription cleanupWhenNoLongerNeedIt = editor.multiPlainChanges().successionEnds(Duration.ofMillis(500)).subscribe(ignore -> editor.setStyleSpans(0, computeHighlighting(editor.getText())));
        themes.setItems(availableThemes);
        editor.setStyle("-fx-font-size: 20px;");
        editor.setWrapText(true);
        editor.addEventFilter(KeyEvent.KEY_PRESSED, Tab_Filter);
        editor.addEventFilter(KeyEvent.KEY_PRESSED, Indentation_filter);
        editor.addEventFilter(KeyEvent.KEY_PRESSED, AutoComplete_filter);
        AutoComplete = new BoundsPopup("I am the caret popup button!");
        EventStream<Optional<Bounds>> caretBounds = nonNullValuesOf(editor.caretBoundsProperty());
        Subscription caretPopupSub = EventStreams.combine(caretBounds, AutoComplete.outsideViewportValues())
                .subscribe(tuple3 -> {
                    Optional<Bounds> opt = tuple3._1;
                    boolean showPopupWhenCaretOutside = tuple3._2;

                    if (opt.isPresent()) {
                        Bounds b = opt.get();
                        AutoComplete.setX(b.getMaxX() + caretXOffset);
                        AutoComplete.setY(b.getMaxY() + caretYOffset);

                        if (AutoComplete.isHiddenTemporarily()) {
                            AutoComplete.show(stage);
                            AutoComplete.setHideTemporarily(false);
                        }

                    } else {
                        if (!showPopupWhenCaretOutside) {
                            AutoComplete.hide();
                            AutoComplete.setHideTemporarily(true);
                        }
                    }
                });
        editor.caretPositionProperty().addListener((obs, oldPosition, newPosition) -> {
            String text = editor.getText().substring(0, newPosition.intValue());
            int index;
            for (index = text.length() - 1; index >= 0 && !Character.isWhitespace(text.charAt(index)); index--);
            String prefix = text.substring(index + 1, text.length());
            currentWord = prefix;
        });

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
//            System.out.println(styleClass);
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}
