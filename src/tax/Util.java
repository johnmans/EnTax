/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tax;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.JComponent;
import javax.swing.SwingWorker;

/**
 *
 * @author ninja
 */
public class Util {
    public static final String months[] = 
    {
        "Ιανουάριος",
        "Φεβρουάριος",
        "Μάρτιος",
        "Απρίλιος",
        "Μάιος",
        "Ιούνιος",
        "Ιούλιος",
        "Αύγουστος",
        "Σεπτέμβριος",
        "Οκτώβριος",
        "Νοέμβριος",
        "Δεκέμβριος" 
    };
    
    public static final boolean PDF = true;
    public static final boolean EXCEL = false;
    
    public static final Color darkGreen = Color.green.darker().darker();
    public static final Color darkOrange = Color.orange.darker().darker();
    
    public static String tablenameToDate(String name) {
        int table[] = new int[2];
        table = Util.tablenameToMonthAndYear(name);
        
        return Util.months[table[0] - 1] + " " + table[1];
    }
    
    public static int[] tablenameToMonthAndYear(String name) {
        int month = Integer.parseInt(name.substring(1, 3));
        int year = Integer.parseInt(name.substring(4, name.length()));
        
        int table[] = new int[2];
        table[0] = month;
        table[1] = year;
        
        return table;
    }
    
    public static String[][] arrayFromTablename(String dbTable) {
        String data[][] = null;
        int rows = 0;
        
        Connection c = null;
        Statement stmt = null;
        try {
          Class.forName("org.sqlite.JDBC");
          c = DriverManager.getConnection("jdbc:sqlite:etc/tax.sqlite");
          c.setAutoCommit(false);

          stmt = c.createStatement();
          ResultSet rs = stmt.executeQuery( "SELECT COUNT(*) FROM " + dbTable + ";" );
          rows = rs.getInt("COUNT(*)");
          data = new String[rows][4];
            
          rs = stmt.executeQuery( "SELECT * FROM " + dbTable + ";" );
          int i = 0;
          while ( rs.next() ) {
            String date = rs.getString("date");
            String price = String.valueOf(rs.getFloat("price"));
            String afm = rs.getString("afm");
            String name = rs.getString("name");
            
            data[i][0] = date;
            
            try {
                price.charAt(price.indexOf(".") + 2);
            } catch (Exception e) { price += "0"; };      
                    
            data[i][1] = price;
            data[i][2] = afm;
            data[i][3] = name;
            
            i++;
          }
          rs.close();
          stmt.close();
          c.close();
        } catch ( Exception e ) {
          e.printStackTrace();
          System.exit(0);
        }
        
        Arrays.sort(data, new Comparator<String[]>() {

            @Override
            public int compare(String[] t, String[] t1) {
                int date_1 = Integer.parseInt(t[0].substring(0, 2));
                int date_2 = Integer.parseInt(t1[0].substring(0, 2));
                if (date_1 > date_2)
                    return 1;
                else if ((date_1 == date_2))
                    return 0;
                else
                    return -1;
            }
        });
        
        return data;
    }
    
    public static String formatSum(float sum) {
        String sumS = String.valueOf(sum);
        try {
            sumS = sumS.substring(0, sumS.indexOf(".") + 3);
        } catch (Exception e) {
            sumS = sumS.substring(0, sumS.indexOf(".") + 2) + "0";
        }
        sumS += "€";
        
        return sumS;
    }
    
    public static void updateDB() {
        Object data[][] = new Object[][] {};
        int rows = 0;
        
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:etc/tax.sqlite");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            stmt.execute( "CREATE TABLE IF NOT EXISTS names(id INTEGER PRIMARY KEY, name CHAR(10), afm CHAR(10));" );
            c.commit();
            ResultSet rs = stmt.executeQuery( "SELECT COUNT(*) FROM names;" );
            rows = rs.getInt("COUNT(*)");
            if (rows > 0) {
                data = new Object[rs.getInt("COUNT(*)")][2];
            } else
                return;
            
            rs = stmt.executeQuery( "SELECT * FROM names;" );
            int i = 0;
            while ( rs.next() ) {
                String name = rs.getString("name");
                String afm = rs.getString("afm");
                data[i][0] = name;
                data[i][1] = afm;
                
                i++;
            }
            
            rs = stmt.executeQuery( "SELECT * FROM main.sqlite_master WHERE type='table';" );
            ArrayList<String> tableNames = new ArrayList<String>();
            while ( rs.next() ) {
               String name = rs.getString("name");
    //             System.out.println("#" + name + "#");
               if (name.startsWith("t"))
                   tableNames.add(name);
               else
                   continue;
            }

            for (int j=0; j<tableNames.size(); j++) {
                stmt.executeUpdate("UPDATE " + tableNames.get(j) + " SET name='';");
                c.commit();
                for (int f=0; f<data.length; f++) {
                    stmt.executeUpdate("UPDATE " + tableNames.get(j) + " SET name='" + data[f][0] + "' WHERE afm='" + data[f][1] + "';");
                    c.commit();
                }
            }
        
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            e.printStackTrace();
            System.exit(0);
        }
    }
    
    public static SwingWorker<Void, Void> linearGradient(final JComponent comp, final Color col, final int step, final int sleepTime) {
        final Color cCol = comp.getBackground();
        SwingWorker<Void, Void> colorThread = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                int cR = cCol.getRed();
                int cG = cCol.getGreen();
                int cB = cCol.getBlue();
                
                int R = col.getRed();
                int G = col.getGreen();
                int B = col.getBlue();
                
                int stepR = (R - cR) / step;
                int stepG = (G - cG) / step;
                int stepB = (B - cB) / step;
                
                for (int i=0; i<step; i++) {
                    cR += stepR;
                    cG += stepG;
                    cB += stepB;
                    
                    if (i < step/2)
                        Thread.sleep(sleepTime);
                    else
                        Thread.sleep(sleepTime/2);
                        
                    
                    comp.setBackground(new Color(cR, cG, cB));
                }
                
                return null;
            }
        };
        
        return colorThread;
    }
    
    public static void fadeInAndOut(JComponent comp, Color col) {
        Color cCol = comp.getBackground();
        SwingWorker<Void, Void> thread = Util.linearGradient(comp, col, 20, 20);
        thread.execute();
    }
}
