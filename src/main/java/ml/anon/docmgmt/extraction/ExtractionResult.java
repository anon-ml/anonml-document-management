package ml.anon.docmgmt.extraction;

import lombok.Builder;
import lombok.Data;
import ml.anon.model.docmgmt.FileType;

import java.util.List;

/**
 * Created by mirco on 01.06.17.
 */
@Data
@Builder
public class ExtractionResult {
    private final List<String> paginated;
    private final String fullText;
    private FileType type;

}
