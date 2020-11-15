/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package intercode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 *
 * @author TARUN
 */
public class Compile {

    public boolean compiling(String type,String input) throws IOException, InterruptedException {
        if (type.equals("C") || type.equals("CPP")) {
            return compileCORCPP(type,input);
        } else if (type.equals("java")) {

        }
        return false;
    }

    public boolean compileCORCPP(String CORCPP,String input) throws IOException, InterruptedException {
        File dir = new File("./src/Judge");
        String CPPprogram = dir.getCanonicalPath() + "/code.cpp";
        String Cprogram = dir.getCanonicalPath() + "/code.c";
        String exeName = dir.getCanonicalPath() + "/program";
        try {
            Process p = null;

            if (CORCPP.equals("C")) {
                p = Runtime.getRuntime().exec("gcc " + Cprogram + " -o " + exeName);
            } else {
                p = Runtime.getRuntime().exec("g++ " + CPPprogram + " -o " + exeName);
            }
            BufferedReader brRun = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String s;
            File InputFile = new File("./src/Judge/error.txt");
            FileWriter myWriter = new FileWriter(InputFile);
            int fl = 0;
            while ((s = brRun.readLine()) != null) {
                myWriter.write(s);
                fl++;
            }
            myWriter.close();
            if (fl > 1) {
                System.out.println("Compile unsuccessfully");
                return false;
            }
            runCORCPPFile(input);
            p.waitFor();
            System.out.println("exit: " + p.exitValue());
            p.destroy();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    void runCORCPPFile(String input) throws IOException, InterruptedException {
        File dir = new File("./src/Judge");
        String program = dir.getCanonicalPath() + "/program";
        Process process= Runtime.getRuntime().exec(program);
        // create write reader
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        // write input
        writer.write(input + "\n");
        writer.flush();
        // read output
        String line = "";
        File InputFile = new File("./src/Judge/output.txt");
        FileWriter myWriter = new FileWriter(InputFile);
        while ((line = reader.readLine()) != null) {
            line+=System.lineSeparator();
            System.out.println(line);
            myWriter.write(line);
        }
        // wait for process to finish
        int returnValue = process.waitFor();
        // close writer reader
        reader.close();
        writer.close();
        myWriter.close();
        System.out.println("Exit with value " + returnValue);
    }

    boolean compileJAVA() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
