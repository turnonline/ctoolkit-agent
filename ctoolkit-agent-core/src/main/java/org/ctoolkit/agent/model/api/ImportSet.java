package org.ctoolkit.agent.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-08-29T18:41:11.119Z")
public class ImportSet implements Serializable {
  
  private String author = null;
  private String comment = null;
  private Boolean clean = false;
  private String namespace = null;
  private String kind = null;
  private String id = null;
  private List<ImportSetProperty> properties = new ArrayList<ImportSetProperty>();

  /**
   * Import set author
   **/
  public ImportSet author(String author) {
    this.author = author;
    return this;
  }

  
  @ApiModelProperty(value = "Import set author")
  @JsonProperty("author")
  public String getAuthor() {
    return author;
  }
  public void setAuthor(String author) {
    this.author = author;
  }

  /**
   * Optional comment for import set
   **/
  public ImportSet comment(String comment) {
    this.comment = comment;
    return this;
  }

  
  @ApiModelProperty(value = "Optional comment for import set")
  @JsonProperty("comment")
  public String getComment() {
    return comment;
  }
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   * Flag if all records for kind should be removed prior to data import
   **/
  public ImportSet clean(Boolean clean) {
    this.clean = clean;
    return this;
  }

  
  @ApiModelProperty(value = "Flag if all records for kind should be removed prior to data import")
  @JsonProperty("clean")
  public Boolean getClean() {
    return clean;
  }
  public void setClean(Boolean clean) {
    this.clean = clean;
  }

  /**
   * Namespace (for elasticsearch it is ‘index’, for sql it is ‘schema’)
   **/
  public ImportSet namespace(String namespace) {
    this.namespace = namespace;
    return this;
  }

  
  @ApiModelProperty(value = "Namespace (for elasticsearch it is ‘index’, for sql it is ‘schema’)")
  @JsonProperty("namespace")
  public String getNamespace() {
    return namespace;
  }
  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  /**
   * Kind name (for elasticsearch it is ‘document’, for sql it is ‘table’)
   **/
  public ImportSet kind(String kind) {
    this.kind = kind;
    return this;
  }

  
  @ApiModelProperty(value = "Kind name (for elasticsearch it is ‘document’, for sql it is ‘table’)")
  @JsonProperty("kind")
  public String getKind() {
    return kind;
  }
  public void setKind(String kind) {
    this.kind = kind;
  }

  /**
   * Primary id
   **/
  public ImportSet id(String id) {
    this.id = id;
    return this;
  }

  
  @ApiModelProperty(value = "Primary id")
  @JsonProperty("id")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }


  /**
   * Array of import set properties
   **/
  public ImportSet properties(List<ImportSetProperty> properties) {
    this.properties = properties;
    return this;
  }

  
  @ApiModelProperty(value = "Array of import set properties")
  @JsonProperty("properties")
  public List<ImportSetProperty> getProperties() {
    return properties;
  }
  public void setProperties(List<ImportSetProperty> properties) {
    this.properties = properties;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImportSet importSet = (ImportSet) o;
    return Objects.equals(author, importSet.author) &&
        Objects.equals(comment, importSet.comment) &&
        Objects.equals(clean, importSet.clean) &&
        Objects.equals(namespace, importSet.namespace) &&
        Objects.equals(kind, importSet.kind) &&
        Objects.equals(id, importSet.id) &&
        Objects.equals(properties, importSet.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(author, comment, clean, namespace, kind, id, properties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImportSet {\n");
    
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    clean: ").append(toIndentedString(clean)).append("\n");
    sb.append("    namespace: ").append(toIndentedString(namespace)).append("\n");
    sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

