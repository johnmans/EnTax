/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tax;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author ninja
 */
public class Completed extends javax.swing.JFrame {

    /**
     * Creates new form CompletedPrompt
     */
    public Completed(String labText) {
        initComponents();
        this.setResizable(false);
        this.compLab.setText(labText);
        
        try {
            this.setIconImage((BufferedImage) ImageIO.read(Tax.class.getResource("icons/complete.png")));
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.toFront();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        compLab = new javax.swing.JLabel();
        okBut = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Επιτυχία");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        compLab.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(compLab, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 10, 160, 40));

        okBut.setText("ΟΚ");
        okBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButActionPerformed(evt);
            }
        });
        getContentPane().add(okBut, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 40, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tax/icons/complete48.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 50, 50));

        setSize(new java.awt.Dimension(252, 132));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void okButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButActionPerformed
        this.dispose();
    }//GEN-LAST:event_okButActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel compLab;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton okBut;
    // End of variables declaration//GEN-END:variables
}