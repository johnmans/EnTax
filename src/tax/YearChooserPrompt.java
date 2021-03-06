/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tax;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author ninja
 */
public class YearChooserPrompt extends javax.swing.JFrame {
    
    private MainForm f;
    private boolean mode;
    
    /**
     * Creates new form YearChooserPrompt
     */
    public YearChooserPrompt(MainForm f, boolean mode) {
        initComponents();
        initCombo();
        this.f = f;
        this.mode = mode;
        
        if (mode) {
            try {
                this.setIconImage((BufferedImage) ImageIO.read(Tax.class.getResource("icons/print.png")));
            } catch (IOException ex) {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                this.setIconImage((BufferedImage) ImageIO.read(Tax.class.getResource("icons/excel.png")));
            } catch (IOException ex) {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        this.toFront();
    }
    
    private void initCombo() {
        yearBox.removeAllItems();
        ArrayList<Integer> years = new ArrayList<Integer>();
        
        Connection c = null;
        Statement stmt = null;
        try {
          Class.forName("org.sqlite.JDBC");
          c = DriverManager.getConnection("jdbc:sqlite:etc/tax.sqlite");
          c.setAutoCommit(false);

          stmt = c.createStatement();
          ResultSet rs = stmt.executeQuery( "SELECT * FROM main.sqlite_master WHERE type='table' ORDER BY name ASC;" );

          while ( rs.next() ) {
             String name = rs.getString("name");
//             System.out.println("#" + name + "#");
             if (name.startsWith("t")) {
                 int year = Util.tablenameToMonthAndYear(name)[1];
                 
                 if (!years.contains(year))
                    years.add(year);
             }
             else
                 continue;
          }
          rs.close();
          stmt.close();
          c.close();
        } catch ( Exception e ) {
          e.printStackTrace();
          System.exit(0);
        }
        
        for (int i=0; i<years.size(); i++) {
             yearBox.addItem(years.get(i));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        yearBox = new javax.swing.JComboBox();
        cancelBut = new javax.swing.JButton();
        okBut = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Έτος");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        yearBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(yearBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, -1));

        cancelBut.setText("Άκυρο");
        cancelBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButActionPerformed(evt);
            }
        });
        getContentPane().add(cancelBut, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 60, -1));

        okBut.setText("ΟΚ");
        okBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButActionPerformed(evt);
            }
        });
        getContentPane().add(okBut, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 40, -1));

        setSize(new java.awt.Dimension(139, 119));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void okButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButActionPerformed
        this.dispose();
        if (mode)
            f.printYear(Integer.parseInt(yearBox.getSelectedItem().toString()));
        else
            f.exportExcelYear(Integer.parseInt(yearBox.getSelectedItem().toString()));
    }//GEN-LAST:event_okButActionPerformed

    private void cancelButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBut;
    private javax.swing.JButton okBut;
    private javax.swing.JComboBox yearBox;
    // End of variables declaration//GEN-END:variables
}
