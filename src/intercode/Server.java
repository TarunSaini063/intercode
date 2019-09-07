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
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TARUN
 */
public class Server implements Runnable {

    Socket ss = null;
    DataInputStream dis = null;
    DataOutputStream dos = null;
    Editor obj = null;
    //static layoutController obj = null;
    Scanner scn = new Scanner(System.in);

    public Server(Socket ss, DataInputStream dis, DataOutputStream dos, Editor obj) {
        this.ss = ss;
        this.dis = dis;
        this.dos = dos;
        this.obj = obj;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        readMessage.start();
    }

    Thread readMessage = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    String msg = dis.readUTF();
                    System.out.println(msg);
                    obj.setMess(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        ss.close();
                        dis.close();
                        dos.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    });

}
