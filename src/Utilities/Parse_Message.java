/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

/**
 *
 * @author tarun
 */
public class Parse_Message {

    static String text;
    static boolean status = false;
    private static Parse_Message instance = null;

    private Parse_Message() {
        System.out.println("Parsing Message");
    }

    public static Parse_Message getInstance() {
        if (instance == null) {
            instance = new Parse_Message();
        }
        status = false;
        text=null;
        return instance;
    }

    public void setMessage(String mess) {
        String[] lines = mess.split("\\r?\\n");
         System.out.println("Message received in parser\n"+mess);
        if (mess.equals("Message")) {
            status = true;
           
            for(int i=0;i<lines.length;i++){
                System.out.println(lines[i]);
            }
//            text = lines[1];
//            for (int i = 2; i < lines.length; i++) {
//                text = text + "\n" + lines[i];
//            }
        } else {
            text = mess;//.substring(7); //Editor
        }
    }

    public String getMessage() {
        return text;
    }

    public boolean isMessage() {
        return status;
    }
}
