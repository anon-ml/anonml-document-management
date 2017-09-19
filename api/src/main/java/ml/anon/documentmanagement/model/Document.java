package ml.anon.documentmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ml.anon.anonymization.model.Anonymization;
import ml.anon.model.BaseEntity;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
@org.springframework.data.mongodb.core.mapping.Document(collection = "Documents")
public class Document extends BaseEntity {

    @JsonIgnore
    private final static int MB_5 = 1024 * 1024 * 5;

    @Version
    private int version;

    private String fileName;

    private List<String> text;

    private String displayableText;

    private String fullText;


    private DocumentState state;

    @NonNull
    private FileType originalFileType;

    private String nerResult;

    @JsonIgnore
    private byte[] file;


    private List<String> chunks;

    private List<Anonymization> anonymizations;

    private LocalDateTime lockedAt;

    public String fileNameAs(String extension) {
        return FilenameUtils.removeExtension(fileName) + "." + extension;
    }


}
