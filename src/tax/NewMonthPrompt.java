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
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author ninja
 */
public class NewMonthPrompt extends javax.swing.JFrame {

    private MainForm f;
    /**
     * Creates new form newm
     */
    public NewMonthPrompt(MainForm f) {
        initComponents();
        initCombos();
        this.setResizable(false);
        this.f = f;
        
        try {
            this.setIconImage((BufferedImage) ImageIO.read(Tax.class.getResource("icons/newMonth.png")));
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.toFront();
    }
    
    private void initCombos() {        
        yearBox.removeAllItems();
        for (int i=2013; i<=Calendar.getInstance().get(Calendar.YEAR); i++) {
            yearBox.addItem(i);
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

        monthBox = new javax.swing.JComboBox();
        yearBox = new javax.swing.JComboBox();
        okBut = new javax.swing.JButton();
        cancelBut = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Δημιουργία");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        monthBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(monthBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 120, -1));

        yearBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        yearBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                yearBoxItemStateChanged(evt);
            }
        });
        getContentPane().add(yearBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 70, -1));

        okBut.setText("OK");
        okBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButActionPerformed(evt);
            }
        });
        getContentPane().add(okBut, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 40, -1));

        cancelBut.setText("Άκυρο");
        cancelBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButActionPerformed(evt);
            }
        });
        getContentPane().add(cancelBut, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 60, -1));

        setSize(new java.awt.Dimension(231, 123));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButActionPerformed

    private void okButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButActionPerformed
        this.dispose();
        int month = 0;
        for (int i=0; i<12; i++) {
            if (monthBox.getSelectedItem().equals(Util.months[i])) {
                month = i + 1;
                break;
            }
        }
        
        String monS;
        if (month < 10)
            monS = "0" + month;
        else
            monS = month + "";
        
        String db_name = "t" + monS + "_" + (Integer) yearBox.getSelectedItem(); 
        
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:etc/tax.sqlite");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            stmt.execute( "CREATE TABLE IF NOT EXISTS " + db_name + "(id INTEGER PRIMARY KEY, date CHAR(10), price REAL, afm CHAR(10), name CHAR(10));" );
            System.out.println(db_name + " created if not existed");
            c.commit();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        
        f.loadTable(db_name);
    }//GEN-LAST:event_okButActionPerformed

    private void yearBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_yearBoxItemStateChanged
        monthBox.removeAllItems();
        for (int i=0; i<Util.months.length; i++)
            monthBox.addItem(Util.months[i]);
        
        String yearS = evt.getItem().toString();
        
        System.out.println(yearS);
        
        Connection c = null;
        Statement stmt = null;
        try {
          Class.forName("org.sqlite.JDBC");
          c = DriverManager.getConnection("jdbc:sqlite:etc/tax.sqlite");
          c.setAutoCommit(false);

          stmt = c.createStatement();
          ResultSet rs = stmt.executeQuery( "SELECT * FROM main.sqlite_master WHERE type='table';" );
          while ( rs.next() ) {
             String name = rs.getString("name");
             if (!name.startsWith("t"))
                 continue;
             
             int monthYear[] = new int[2];
             monthYear = Util.tablenameToMonthAndYear(name);
             int month = monthYear[0];
             int year = monthYear[1];
             
             if (!String.valueOf(year).equals(yearS))
                 continue;
             else {
                 monthBox.removeItem(Util.months[month - 1]);
             }
          }
          rs.close();
          stmt.close();
          c.close();
        } catch ( Exception e ) {
          e.printStackTrace();
          System.exit(0);
        }
        
        if (monthBox.getItemCount() == 0) {
            yearBox.removeItem(evt.getItem());
        }
    }//GEN-LAST:event_yearBoxItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBut;
    private javax.swing.JComboBox monthBox;
    private javax.swing.JButton okBut;
    private javax.swing.JComboBox yearBox;
    // End of variables declaration//GEN-END:variables
}