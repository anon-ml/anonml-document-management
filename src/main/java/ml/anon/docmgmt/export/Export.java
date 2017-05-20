package ml.anon.docmgmt.export;

import ml.anon.exception.DocumentManagementException;
import ml.anon.model.docmgmt.Document;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Created by mirco on 20.05.17.
 */
public abstract class Export {


    public static File export(Document doc) {
        if (doc.getOriginalFileType() == Document.FileType.PDF) {
            return new PDFExport().doExport(doc);
        } else {
            throw new DocumentManagementException("filetype not supported yet", null);
        }
    }

    protected abstract File doExport(Document doc) throws DocumentManagementException;

    private static class PDFExport extends Export {

        @Override
        protected File doExport(Document doc) {
            try {
                File outfile = File.createTempFile(doc.getFileName(), ".html");
                Writer output = new PrintWriter(outfile, "utf-8");
                output.write(doc.getHtmlView());
                output.close();
                return outfile;
            } catch (IOException e) {
                throw new DocumentManagementException(e);
            }

        }
    }


}
