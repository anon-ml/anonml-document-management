package ml.anon.documentmanagement.model;

/**
 * Created by mirco on 01.06.17.
 */
public enum FileType {
    TXT("txt"), PDF("pdf");

    public final String extension;

    FileType(String extension) {
        this.extension = extension;
    }
}
