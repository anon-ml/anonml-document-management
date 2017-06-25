package ml.anon.docmgmt.export;

import ml.anon.exception.DocumentManagementException;
import ml.anon.model.docmgmt.Document;
import ml.anon.model.docmgmt.FileType;

import java.io.*;

/**
 * Created by mirco on 20.05.17.
 */
public abstract class Export {

    protected final Anonymizer anonymizer = new Anonymizer();


    public static File export(Document doc) {
        if (doc.getOriginalFileType() == FileType.PDF) {
            return new PopplerExport().doExport(doc);
        } else {
            throw new DocumentManagementException("filetype not supported yet", null);
        }
    }

    protected abstract File doExport(Document doc) throws DocumentManagementException;

    private static class PDFExport extends Export {

        private final HtmlConvert converter = new HtmlConvert();


        @Override
        protected File doExport(Document doc) {
            try {
                File outfile = File.createTempFile(doc.getFileName(), ".html");
                Writer output = new PrintWriter(outfile, "utf-8");
                String text = converter.toHtml(new ByteArrayInputStream(doc.getFile()), FileType.PDF);
                output.write(anonymizer.anonymize(text, doc.getAnonymizations()));
                output.close();
                return outfile;
            } catch (IOException e) {
                throw new DocumentManagementException(e);
            }

        }
    }


}
