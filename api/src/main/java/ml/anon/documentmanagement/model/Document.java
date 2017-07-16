package ml.anon.documentmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ml.anon.model.anonymization.Anonymization;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Representation of a document.
 *
 * @author mirco
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@org.springframework.data.mongodb.core.mapping.Document(collection = "documents")
public class Document {

    private final static int MB_5 = 1024 * 1024 * 5;

    @Id
    private String id;

    private String fileName;

    private List<String> text;

    private String displayableText;

    private String fullText;

    @NonNull
    private FileType originalFileType;


    @JsonIgnore
    private byte[] file;

    private List<String> chunks;

    private List<Anonymization> anonymizations;

    public String fileNameAs(String extension) {
        return FilenameUtils.removeExtension(fileName) + "." + extension;
    }


}
