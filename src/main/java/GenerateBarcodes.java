import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.print.*;
import java.io.*;


public class GenerateBarcodes {
    private static final String PATH = System.getProperty("user.dir") + "\\" + "barcode.pdf";

    public static void main(String[] args) {
        new GenerateBarcodes("Микитюк Максим Васильович", "1166");
    }

    public GenerateBarcodes(String name, String code) {
        createPDF(name, code);
        print(PATH);
    }

    private void print(String fileName) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Doc doc = new SimpleDoc(in, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();

        try {
            service.createPrintJob().print(doc, null);
        } catch (PrintException e) {
            e.printStackTrace();
        }
    }
    private void createPDF(String name, String code) {
        BaseFont bf = null;

        try {
            bf = BaseFont.createFont("f.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Document doc = new Document();
        PdfWriter docWriter = null;

        try {
            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(PATH));
            doc.setPageSize(PageSize.A4);
            doc.setMargins(10, 10, 10, 10);
            doc.open();

            /*ПИБ*/
            Phrase phrase = new Phrase(new Chunk(name, new Font(bf, 12)));
            PdfPCell cell1 = new PdfPCell(phrase);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setUseVariableBorders(true);
            cell1.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP);
            cell1.setBorderColor(BaseColor.BLACK);

            /*BarCode*/
            PdfPCell cell2 = new PdfPCell();
            cell2.setUseVariableBorders(true);
            cell2.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell2.setBorderColor(BaseColor.BLACK);
            cell2.addElement(getBarCode(code, docWriter));

            /*Table*/
            PdfPTable table = new PdfPTable(1);
            table.setTotalWidth(new float[]{180});
            table.setLockedWidth(true);
            table.addCell(cell1);
            table.addCell(cell2);
            doc.add(table);

        } catch (DocumentException dex) {
            dex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (doc != null) {
                doc.close();
            }
            if (docWriter != null) {
                docWriter.close();
            }
        }
    }

    private Image getBarCode(String code, PdfWriter docWriter){
        PdfContentByte cb = docWriter.getDirectContent();
        Barcode128 code128 = new Barcode128();
        code128.setCode(code.trim());
        code128.setCodeType(Barcode128.CODE128);
        Image code128Image = code128.createImageWithBarcode(cb, null, null);
        code128Image.scalePercent(100);
        code128Image.setWidthPercentage(70);
        code128Image.setAlignment(Element.ALIGN_CENTER);
        return code128Image;
    }


}