package ml.anon.docmgmt.model;

import java.io.File;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Representation of a document
 *
 * @author mirco
 *
 */
@Entity
public class Document {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String plainText;
  private File originalFile;

  public String getPlainText() {
    return plainText;
  }

  public void setPlainText(String plainText) {
    this.plainText = plainText;
  }

  public File getOriginalFile() {
    return originalFile;
  }

  public void setOriginalFile(File originalFile) {
    this.originalFile = originalFile;
  }



}
