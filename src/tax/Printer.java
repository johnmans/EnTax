/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText. KMA
 */

package tax;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;

public class Printer implements PdfPTableEvent {

    public static void createPdf(String filename, String dbTable) throws SQLException, DocumentException, IOException {
        // step 1
        Document document = new Document(PageSize.A4);
//        System.out.println(Tax.class.getResource("fonts/arial.ttf").getPath());
        BaseFont bf = BaseFont.createFont("etc/Arial.ttf", "Cp1253", BaseFont.EMBEDDED);
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        PdfPTableEvent event = new Printer();
        PdfPTable table = getTable(dbTable, bf);
        table.setTableEvent(event);
        document.add(table);
        document.newPage();
        // step 5
        document.close();
    }
    
    
    public static PdfPTable getTable(String dbTable, BaseFont bf)
        throws SQLException, DocumentException, IOException {
        PdfPTable table = new PdfPTable(new float[] { 1, 2, 2, 3, 2 });
        table.setWidthPercentage(100f);
        table.getDefaultCell().setPadding(10);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        table.getDefaultCell().setColspan(5);
        table.getDefaultCell().setBackgroundColor(BaseColor.ORANGE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(new Phrase(Util.tablenameToDate(dbTable), new Font(bf, 16, Font.NORMAL, BaseColor.DARK_GRAY)));
        table.getDefaultCell().setPadding(3);
        table.getDefaultCell().setColspan(1);
        table.getDefaultCell().setBackgroundColor(BaseColor.GRAY);
        
        Font titleFont = new Font(bf, 13, Font.NORMAL, BaseColor.WHITE);
        table.addCell(new Phrase("Α/Α", titleFont));
        table.addCell(new Phrase("Ημερομηνία", titleFont));
        table.addCell(new Phrase("Τιμή", titleFont));
        table.addCell(new Phrase("ΑΦΜ", titleFont));
        table.addCell(new Phrase("Όνομα", titleFont));
        table.getDefaultCell().setBackgroundColor(null);
        
        String data[][] = null;
        data = Util.arrayFromTablename(dbTable);
        int rows = data.length;
        float sum = 0;
        
        for (int i=0; i<rows; i++) {
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(i + "");
            table.addCell(data[i][0]);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(data[i][1] + "€");
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(data[i][2]);
            table.addCell(data[i][3]);
            
            sum += Float.parseFloat(data[i][1]);
        }
        
        if (rows > 0) {
            PdfPCell cell = new PdfPCell();
            
            cell.setPadding(7);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setBorderColor(BaseColor.WHITE);
            cell.disableBorderSide(Rectangle.TOP | Rectangle.RIGHT);
            cell.setPhrase(new Phrase(""));

            table.addCell(cell);
            cell.setBorderColor(BaseColor.BLACK);
            cell.setBackgroundColor(BaseColor.GRAY);
            cell.setPhrase(new Phrase("Σύνολο", titleFont));
            cell.enableBorderSide(Rectangle.RIGHT);
            table.addCell(cell);

            String sumS = Util.formatSum(sum);

            cell.setBackgroundColor(BaseColor.ORANGE);
            cell.setPhrase(new Phrase(sumS));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell.setBorderColor(BaseColor.WHITE);
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.disableBorderSide(Rectangle.LEFT);
            cell.setPhrase(new Phrase(""));
            table.addCell(cell);
            table.addCell(cell);
        }

        return table;
    }
    
    
    public void tableLayout(PdfPTable table, float[][] widths, float[] heights,
        int headerRows, int rowStart, PdfContentByte[] canvases) {
        int columns;
        Rectangle rect;
        int footer = widths.length - table.getFooterRows();
        int header = table.getHeaderRows() - table.getFooterRows() + 1;
        for (int row = header; row < footer; row += 2) {
            columns = widths[row].length - 1;
            rect = new Rectangle(widths[row][0], heights[row],
                        widths[row][columns], heights[row + 1]);
            rect.setBackgroundColor(BaseColor.LIGHT_GRAY);
            rect.setBorder(Rectangle.NO_BORDER);
            canvases[PdfPTable.BASECANVAS].rectangle(rect);
        }
    }

}
