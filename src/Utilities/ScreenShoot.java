/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author tarun
 */
public class ScreenShoot {

    public ScreenShoot() {
       
    }
    public void capture(){
         Robot r = null;
        try {
            r = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(ScreenShoot.class.getName()).log(Level.SEVERE, null, ex);
        }
        File dir = new File("./src/Image");
        String path = null;
        try {
            path = dir.getCanonicalPath()+"/ScreenShoot.jpg";
        } catch (IOException ex) {
            Logger.getLogger(ScreenShoot.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Used to get ScreenSize and capture image 
        Rectangle capture = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage Image = r.createScreenCapture(capture);
        try {
            ImageIO.write(Image, "jpg", new File(path));
        } catch (IOException ex) {
            Logger.getLogger(ScreenShoot.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Screenshot saved");
    }
}
