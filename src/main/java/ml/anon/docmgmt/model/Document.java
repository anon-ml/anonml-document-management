package ml.anon.docmgmt.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Representation of a document.
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

  @JsonIgnore
  @Lob
  private byte[] originalFile;

  public String getPlainText() {
    return plainText;
  }

  public void setPlainText(String plainText) {
    this.plainText = plainText;
  }

  public Long getId() {
    return id;
  }

  public byte[] getOriginalFile() {
    return originalFile;
  }

  public void setOriginalFile(byte[] originalFile) {
    this.originalFile = originalFile;
  }



}
