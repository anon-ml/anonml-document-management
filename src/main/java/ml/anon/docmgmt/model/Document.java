package ml.anon.docmgmt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Representation of a document.
 *
 * @author mirco
 *
 */
@Entity
public class Document {

  private final static int MB_5 = 1024 * 1024 * 5;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(length = MB_5)
  private String text;

  @JsonIgnore
  @Column(length = MB_5)
  private byte[] file;

  public String getText() {
    return text;
  }

  public void setText(String plainText) {
    this.text = plainText;
  }

  public Long getId() {
    return id;
  }

  public byte[] getFile() {
    return file;
  }

  public void setFile(byte[] originalFile) {
    this.file = originalFile;
  }



}
