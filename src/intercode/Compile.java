/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package intercode;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author TARUN
 */
public class Compile {

    public boolean compiling(String type) {
        if (type.equals("C") || type.equals("CPP")) {
            return compileCORCPP(type);
        } else if (type.equals("java")) {

        }
        return false;
    }

    public boolean compileCORCPP(String CORCPP) {
        File dir = new File("C:/Users/TARUN/Documents/NetBeansProjects/InterCode/src/Judge");
        String filename1 = "C:/Users/TARUN/Documents/NetBeansProjects/InterCode/src/Judge/Code.c";
        String filename2 = "C:/Users/TARUN/Documents/NetBeansProjects/InterCode/src/Judge/Code.cpp";
        try {
            String exeName = "cFile";
            Process p;
            if (CORCPP.equals("C")) {
                p = Runtime.getRuntime().exec("cmd /C gcc " + filename1 + " -o " + exeName, null, dir);
            } else {
                p = Runtime.getRuntime().exec("cmd /C g++ " + filename2 + " -o " + exeName, null, dir);
            }
            BufferedReader brRun = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String errorRun = brRun.readLine();
            if (errorRun != null) {
                System.out.println("Error Run = " + errorRun);
                return false;
            }
            System.out.println("Compile Successfully");
            runCORCPPFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    void runCORCPPFile() throws IOException {
        Runtime r = Runtime.getRuntime();
        r.exec("cmd /C cd C:/Users/TARUN/Documents/NetBeansProjects/InterCode/src/Judge && cFile.exe <input.txt> output.txt");
    }

    boolean compileJAVA() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
