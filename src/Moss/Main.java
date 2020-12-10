package Moss;

import java.io.File;
import java.util.Collection;
import java.net.URL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

    public ObservableList<PieChart.Data> main() throws Exception {
        // a list of students' source code files located in the prepared
        // directory.
        File tmp = new File("./src/Assignments");
        String Path = tmp.getCanonicalPath();
        System.out.println(Path);
        Collection<File> files = FileUtils.listFiles(new File(Path + "/solution"), new String[]{"c"}, true);

        // a list of base files that was given to the students for this
        // assignment.
        Collection<File> baseFiles = FileUtils.listFiles(new File(Path + "/base"), new String[]{"c"}, true);

        //get a new socket client to communicate with the Moss server
        //and set its parameters.
        SocketClient socketClient = new SocketClient();

        //set your Moss user ID
        socketClient.setUserID("743413275");
        //socketClient.setOpt...

        //set the programming language of all student source codes
        socketClient.setLanguage("c");

        //initialize connection and send parameters
        socketClient.run();

        // upload all base files
        for (File f : baseFiles) {
            socketClient.uploadBaseFile(f);
        }

        //upload all source files of students
        for (File f : files) {
            socketClient.uploadFile(f);
            String tmp1 = f.getCanonicalPath();
            System.out.println(tmp1);
        }

//        finished uploading, tell server to check files
        socketClient.sendQuery();
//
//        //get URL with Moss results and do something with it
        URL results = socketClient.getResultURL();
        System.out.println("Results available at " + results.toString());
//
        // Extracting results from the webpage
        Document doc = Jsoup.connect(results.toString()).get();
        System.out.println(doc.title());
        Elements table = doc.getElementsByTag("table");
        Elements rows = table.select("tr");
        ObservableList<PieChart.Data> res = FXCollections.observableArrayList();
        System.out.println("File 1\t\tFile 2\t\tLines Matched ");
        for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
            Element row = rows.get(i);
            Elements cols = row.select("td");
            int num = Integer.valueOf(cols.get(2).text());
            String file1 = cols.get(0).text(), file2 = cols.get(1).text();
            System.out.println(file1);

            file1 = file1.substring(0,file1.lastIndexOf(' ')-1);
            file1 = file1.substring(file1.lastIndexOf('/')+1);
            file2 = file2.substring(0,file2.lastIndexOf(' ')-1);
            file2 = file2.substring(file2.lastIndexOf('/')+1);
            System.out.println(file1);

            res.add(new PieChart.Data(file1 + "|" + file2, num));
        }
        return res;
    }
}
