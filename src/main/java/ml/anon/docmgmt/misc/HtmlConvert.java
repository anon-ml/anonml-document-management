package ml.anon.docmgmt.misc;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import ml.anon.model.docmgmt.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;

import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by mirco on 20.05.17.
 */

@Log
public class HtmlConvert {


    @SneakyThrows
    public String toHtml(InputStream document, Document.FileType type) {

        if (type == Document.FileType.PDF) {

            PDDocument pdDocument = PDDocument.load(document);

            PDFDomTree parser = new PDFDomTree();

            Writer output = new StringWriter();
            parser.writeText(pdDocument, output);
            String html = output.toString();
            output.close();
            return html;
        } else {
            log.warning("only pdf supported");
            return "";
        }
    }


}
