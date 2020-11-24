package Controller;

import Controller.KeywordsAsync;
import Utilities.Trie;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXToggleButton;
import intercode.Compile;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.IntFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static javafx.application.Application.setUserAgentStylesheet;
import javafx.application.Platform;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
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
public class Interviewee implements Initializable {

    String current_user = Profile.current_user;
    static ServerSocket ss = null;
    static Socket s;
    static DataInputStream dis = null;
    static DataOutputStream dos = null;
    Compile compiler = new Compile();
    private Stage stage;
    BoundsPopup AutoComplete;
    double caretXOffset = 0;
    double caretYOffset = 0;
    String currentWord = "";
    @FXML
    private Label C;
    @FXML
    private VBox content;
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
    private TextArea Current_Message;
    @FXML
    private JFXTextArea output;

    @FXML
    private JFXButton compile;

    @FXML
    private JFXTextArea question;

    @FXML
    private JFXButton endinter;

    @FXML
    private JFXToggleButton changeTheme;
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
    Task task = new Task<Void>() {
        @Override
        public Void call() throws Exception {
            int i = 0;
            while (true) {
                String msg = dis.readUTF();
                String msg1 = msg.substring(6);
                System.out.println("read message= " + msg);
                if (msg.startsWith("Message")) {
                    System.out.println("chat Message ");
                    msg = msg.substring(7);
                    setMessage(msg);
                    continue;
                }
                System.out.println("Editor Message ");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("read message= " + msg1);
                            editor.replaceText(msg1);
                    }
                });
                Thread.sleep(100);
            }
        }
    };

    void setMessage(String name) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Message received " + name);
                final Random rng = new Random();
                AnchorPane anchorPane = new AnchorPane();
                String style = String.format("-fx-background: rgb(%d, %d, %d);"
                        + "-fx-background-color: -fx-background;",
                        rng.nextInt(256),
                        rng.nextInt(256),
                        rng.nextInt(256));
                anchorPane.setStyle(style);
                Label label = new Label(name);
                label.setWrapText(true);
                label.setMaxWidth(300);
                FileInputStream input = null;
                try {
                    input = new FileInputStream("/home/tarun/Desktop/JavaDev/intercode/src/Image/user1.png");
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Interviewer.class.getName()).log(Level.SEVERE, null, ex);
                }
                Image image = new Image(input);
                ImageView button = new ImageView(image);
                button.setFitHeight(70);
                button.setFitWidth(70);
                AnchorPane.setLeftAnchor(button, 5.0);
                AnchorPane.setTopAnchor(button, 5.0);
                AnchorPane.setRightAnchor(label, 5.0);
                AnchorPane.setTopAnchor(label, 5.0);
                AnchorPane.setBottomAnchor(label, 5.0);
                anchorPane.getChildren().addAll(label, button);
                content.getChildren().add(anchorPane);
            }
        });

    }

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
                    if (currentWord.length() <= selectedItem.length()) {
                        selectedItem = selectedItem.substring(currentWord.length());
                        editor.insertText(editor.getCaretPosition(), selectedItem);
                    }
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
    int line_number = 0, flip = 0;

    @FXML
    void Send_Message(MouseEvent event) {
        final Random rng = new Random();
        String name = Current_Message.getText();
        Current_Message.setText("");
        AnchorPane anchorPane = new AnchorPane();
        String style = String.format("-fx-background: rgb(%d, %d, %d);"
                + "-fx-background-color: -fx-background;",
                rng.nextInt(256),
                rng.nextInt(256),
                rng.nextInt(256));
        anchorPane.setStyle(style);
        Label label = new Label(name);
        label.setWrapText(true);
        label.setMaxWidth(300);
        FileInputStream input = null;
        try {
            input = new FileInputStream("/home/tarun/Desktop/JavaDev/intercode/src/Image/user1.png");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Interviewer.class.getName()).log(Level.SEVERE, null, ex);
        }
        Image image = new Image(input);
        ImageView button = new ImageView(image);
        button.setFitHeight(70);
        button.setFitWidth(70);
        AnchorPane.setLeftAnchor(button, 5.0);
        AnchorPane.setTopAnchor(button, 5.0);
        AnchorPane.setRightAnchor(label, 5.0);
        AnchorPane.setTopAnchor(label, 5.0);
        AnchorPane.setBottomAnchor(label, 5.0);
        anchorPane.getChildren().addAll(label, button);
        content.getChildren().add(anchorPane);
        name = "Message\n" + current_user + "\n" + name;
        try {
            dos.writeUTF(name);
            dos.flush();
            System.out.println("send message: " + name);
        } catch (IOException ex) {
//                Logger.getLogger(layoutController.class.getName()).log(Level.SEVERE, null, ex);
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
//        System.out.println(OTHERCPP);
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
            System.out.println("starting reading thread");
//            readMessage.start();
            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            editorStatus = 1;
        }
    }
    private static int editorStatus = 0;

    @FXML
    void onclickCPP(MouseEvent event) {
        String c = "#include <iostream>" + System.lineSeparator() + "using namespace std;" + System.lineSeparator() + "int main(void){" + System.lineSeparator() + "        //Code" + System.lineSeparator() + "        return 0;" + System.lineSeparator() + "}";
        editor.replaceText(c);
        language = 2;
        CPP.setTextFill(Color.web("#ff0000"));
        C.setTextFill(Color.web("#000000"));
        JAVA.setTextFill(Color.web("#000000"));
        if (editorStatus == 0) {
            System.out.println("starting reading thread");
//            readMessage.start();
            Thread th = new Thread(task);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
            System.out.println("starting reading thread");
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
            String mess = "Editor\n" + editor.getText();
            dos.writeUTF(mess);
            dos.flush();
            System.out.println("send message: " + editor.getText());
        } catch (IOException ex) {
//                Logger.getLogger(layoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (AutoComplete.isShowing()) {
            AutoComplete.invertVisibility();
        }
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
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}
