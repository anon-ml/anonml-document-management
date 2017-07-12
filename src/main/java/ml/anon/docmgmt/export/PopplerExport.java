package ml.anon.docmgmt.export;

import lombok.extern.slf4j.Slf4j;
import ml.anon.exception.DocumentManagementException;
import ml.anon.model.docmgmt.Document;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by mirco on 25.06.17.
 */
@Slf4j
public class PopplerExport extends Export {

    private final static String COMPLEX_HTML = "-c";
    private final static String SINGLE_PAGE = "-s";
    private final static String PDF_LINKS = "-p";
    private final Anonymizer anonymizer = new Anonymizer();

    @Override
    protected File doExport(Document doc) throws DocumentManagementException {


        try {

            File tempIn = File.createTempFile(doc.getFileName(), ".temp");
            String outPath = callPdfToHTML(doc, tempIn);

            String result = outPath + "/" + doc.fileNameAs("pdf-html.html"); //TODO fix this
            String content = IOUtils.toString(new FileInputStream(result));
            String anon = anonymizer.anonymize(content, doc.getAnonymizations());
            IOUtils.write(anon, new FileOutputStream(result));

            return zip(outPath, doc.getFileName());


        } catch (IOException e) {
            e.printStackTrace();
            throw new DocumentManagementException(e);
        }

    }


    private String callPdfToHTML(Document doc, File tempIn) throws IOException {
        String outPath = FileUtils.getTempDirectoryPath() + RandomStringUtils.randomAlphanumeric(10);
        File dir = new File(outPath);
        dir.mkdirs();
        String out = outPath + "/" + doc.getFileName();
        FileUtils.writeByteArrayToFile(tempIn, doc.getFile());
        CommandLine cmd = new CommandLine("pdftohtml").addArgument(COMPLEX_HTML).addArgument(SINGLE_PAGE).addArgument(PDF_LINKS).addArgument(tempIn.getAbsolutePath()).addArgument(out);
        DefaultExecutor executor = new DefaultExecutor();
        ExecuteStreamHandler streamHandler = new PumpStreamHandler();
        executor.setStreamHandler(streamHandler);
        int res = executor.execute(cmd);
        log.info("PDFTOHTML stopped with code " + res);
        return outPath;
    }

    private File zip(String folder, String name) throws IOException {
        File zip = File.createTempFile(name, ".zip");
        ZipUtil.pack(new File(folder), zip);
        return zip;
    }
}
