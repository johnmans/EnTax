/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tax;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel;

/**
 *
 * @author ninja
 */
public class Tax {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            try {
              UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
            } catch (Exception e) {
              System.out.println("Substance Graphite failed to initialize");
            }
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        MainForm f = new MainForm();
                        f.setVisible(true);
                        f.setResizable(false);
                    }
            });
          }
        });
        
    }
    
    
}
