package org.ctoolkit.agent.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-07-31T19:00:18.485Z")
public class ImportSet  implements Serializable {
  
  private String author = null;
  private String comment = null;
  private Boolean clean = false;
  private String namespace = null;
  private String kind = null;
  private String id = null;
  private String parentNamespace = null;
  private String parentKind = null;
  private String parentId = null;
  private String syncId = null;
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
   * Parent namespace (for elasticsearch it is ‘index’, for sql it is ‘schema’)
   **/
  public ImportSet parentNamespace(String parentNamespace) {
    this.parentNamespace = parentNamespace;
    return this;
  }

  
  @ApiModelProperty(value = "Parent namespace (for elasticsearch it is ‘index’, for sql it is ‘schema’)")
  @JsonProperty("parentNamespace")
  public String getParentNamespace() {
    return parentNamespace;
  }
  public void setParentNamespace(String parentNamespace) {
    this.parentNamespace = parentNamespace;
  }

  /**
   * Parent kind name (for elasticsearch it is ‘document’, for sql it is ‘table’)
   **/
  public ImportSet parentKind(String parentKind) {
    this.parentKind = parentKind;
    return this;
  }

  
  @ApiModelProperty(value = "Parent kind name (for elasticsearch it is ‘document’, for sql it is ‘table’)")
  @JsonProperty("parentKind")
  public String getParentKind() {
    return parentKind;
  }
  public void setParentKind(String parentKind) {
    this.parentKind = parentKind;
  }

  /**
   * Parent primary id. If you do not know in advance parent id (for instance in case of migration), you can specify lookup by witch parent property will be loaded as follows:  parentId=\"[lookup=parentPropertyName]1\"
   **/
  public ImportSet parentId(String parentId) {
    this.parentId = parentId;
    return this;
  }

  
  @ApiModelProperty(value = "Parent primary id. If you do not know in advance parent id (for instance in case of migration), you can specify lookup by witch parent property will be loaded as follows:  parentId=\"[lookup:parentPropertyName]1\"")
  @JsonProperty("parentId")
  public String getParentId() {
    return parentId;
  }
  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  /**
   * Value of sync id - used to identify record when updating existing record. By default sync property will be created with name '__syncId'. If you want to change name to something else write syncId as follows:  syncId=\"[mySyncId]1\"
   **/
  public ImportSet syncId(String syncId) {
    this.syncId = syncId;
    return this;
  }

  
  @ApiModelProperty(value = "Value of sync id - used to identify record when updating existing record. By default sync property will be created with name '__syncId'. If you want to change name to something else write syncId as follows:  syncId=\"[name:mySyncId]1\"")
  @JsonProperty("syncId")
  public String getSyncId() {
    return syncId;
  }
  public void setSyncId(String syncId) {
    this.syncId = syncId;
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
        Objects.equals(parentNamespace, importSet.parentNamespace) &&
        Objects.equals(parentKind, importSet.parentKind) &&
        Objects.equals(parentId, importSet.parentId) &&
        Objects.equals(syncId, importSet.syncId) &&
        Objects.equals(properties, importSet.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(author, comment, clean, namespace, kind, id, parentNamespace, parentKind, parentId, syncId, properties);
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
    sb.append("    parentNamespace: ").append(toIndentedString(parentNamespace)).append("\n");
    sb.append("    parentKind: ").append(toIndentedString(parentKind)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
    sb.append("    syncId: ").append(toIndentedString(syncId)).append("\n");
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

