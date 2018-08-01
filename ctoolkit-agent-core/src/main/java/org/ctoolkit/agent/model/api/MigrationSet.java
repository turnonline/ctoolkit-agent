package org.ctoolkit.agent.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-07-31T19:00:18.485Z")
public class MigrationSet   {
  
  private String author = null;
  private String comment = null;
  private String sourceNamespace = null;
  private String sourceParentNamespace = null;
  private String sourceKind = null;
  private String sourceSyncIdPropertyName = null;
  private String sourceParentForeignIdPropertyName = null;
  private String targetNamespace = null;
  private String targetKind = null;
  private String targetParentKind = null;
  private String targetParentId = null;
  private String targetSyncIdPropertyName = "__syncId";
  private String targetParentLookupPropertyName = "__syncId";
  private String query = null;
  private Boolean clean = false;
  private List<MigrationSetProperty> properties = new ArrayList<MigrationSetProperty>();

  /**
   * Migration set author
   **/
  public MigrationSet author(String author) {
    this.author = author;
    return this;
  }

  
  @ApiModelProperty(value = "Migration set author")
  @JsonProperty("author")
  public String getAuthor() {
    return author;
  }
  public void setAuthor(String author) {
    this.author = author;
  }

  /**
   * Optional comment for migration set
   **/
  public MigrationSet comment(String comment) {
    this.comment = comment;
    return this;
  }

  
  @ApiModelProperty(value = "Optional comment for migration set")
  @JsonProperty("comment")
  public String getComment() {
    return comment;
  }
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   * Source namespace (for elasticsearch it is 'index', for sql it is 'schema')
   **/
  public MigrationSet sourceNamespace(String sourceNamespace) {
    this.sourceNamespace = sourceNamespace;
    return this;
  }

  
  @ApiModelProperty(value = "Source namespace (for elasticsearch it is 'index', for sql it is 'schema')")
  @JsonProperty("sourceNamespace")
  public String getSourceNamespace() {
    return sourceNamespace;
  }
  public void setSourceNamespace(String sourceNamespace) {
    this.sourceNamespace = sourceNamespace;
  }

  /**
   * Source parent namespace (for elasticsearch it is ‘index’, for sql it is ‘schema’). If left empty 'sourceNamespace' will be used as parent namespace
   **/
  public MigrationSet sourceParentNamespace(String sourceParentNamespace) {
    this.sourceParentNamespace = sourceParentNamespace;
    return this;
  }

  
  @ApiModelProperty(value = "Source parent namespace (for elasticsearch it is ‘index’, for sql it is ‘schema’). If left empty 'sourceNamespace' will be used as parent namespace")
  @JsonProperty("sourceParentNamespace")
  public String getSourceParentNamespace() {
    return sourceParentNamespace;
  }
  public void setSourceParentNamespace(String sourceParentNamespace) {
    this.sourceParentNamespace = sourceParentNamespace;
  }

  /**
   * Source kind name (for elasticsearch it is 'document', for sql it is 'table')
   **/
  public MigrationSet sourceKind(String sourceKind) {
    this.sourceKind = sourceKind;
    return this;
  }

  
  @ApiModelProperty(value = "Source kind name (for elasticsearch it is 'document', for sql it is 'table')")
  @JsonProperty("sourceKind")
  public String getSourceKind() {
    return sourceKind;
  }
  public void setSourceKind(String sourceKind) {
    this.sourceKind = sourceKind;
  }

  /**
   * Name of sync id property in source kind - used to identify record when updating existing record (in most cases it is kind primary id)
   **/
  public MigrationSet sourceSyncIdPropertyName(String sourceSyncIdPropertyName) {
    this.sourceSyncIdPropertyName = sourceSyncIdPropertyName;
    return this;
  }

  
  @ApiModelProperty(value = "Name of sync id property in source kind - used to identify record when updating existing record (in most cases it is kind primary id)")
  @JsonProperty("sourceSyncIdPropertyName")
  public String getSourceSyncIdPropertyName() {
    return sourceSyncIdPropertyName;
  }
  public void setSourceSyncIdPropertyName(String sourceSyncIdPropertyName) {
    this.sourceSyncIdPropertyName = sourceSyncIdPropertyName;
  }

  /**
   * Name of foreign id property in source parent kind - used to identify parent id in target kind (in most cases it is foreign key to parent)
   **/
  public MigrationSet sourceParentForeignIdPropertyName(String sourceParentForeignIdPropertyName) {
    this.sourceParentForeignIdPropertyName = sourceParentForeignIdPropertyName;
    return this;
  }

  
  @ApiModelProperty(value = "Name of foreign id property in source parent kind - used to identify parent id in target kind (in most cases it is foreign key to parent)")
  @JsonProperty("sourceParentForeignIdPropertyName")
  public String getSourceParentForeignIdPropertyName() {
    return sourceParentForeignIdPropertyName;
  }
  public void setSourceParentForeignIdPropertyName(String sourceParentForeignIdPropertyName) {
    this.sourceParentForeignIdPropertyName = sourceParentForeignIdPropertyName;
  }

  /**
   * Target namespace (for elasticsearch it is 'index', for sql it is 'schema')
   **/
  public MigrationSet targetNamespace(String targetNamespace) {
    this.targetNamespace = targetNamespace;
    return this;
  }

  
  @ApiModelProperty(value = "Target namespace (for elasticsearch it is 'index', for sql it is 'schema')")
  @JsonProperty("targetNamespace")
  public String getTargetNamespace() {
    return targetNamespace;
  }
  public void setTargetNamespace(String targetNamespace) {
    this.targetNamespace = targetNamespace;
  }

  /**
   * Target kind name (for elasticsearch it is 'document', for sql it is 'table')
   **/
  public MigrationSet targetKind(String targetKind) {
    this.targetKind = targetKind;
    return this;
  }

  
  @ApiModelProperty(value = "Target kind name (for elasticsearch it is 'document', for sql it is 'table')")
  @JsonProperty("targetKind")
  public String getTargetKind() {
    return targetKind;
  }
  public void setTargetKind(String targetKind) {
    this.targetKind = targetKind;
  }

  /**
   * Target parent kind name
   **/
  public MigrationSet targetParentKind(String targetParentKind) {
    this.targetParentKind = targetParentKind;
    return this;
  }

  
  @ApiModelProperty(value = "Target parent kind name")
  @JsonProperty("targetParentKind")
  public String getTargetParentKind() {
    return targetParentKind;
  }
  public void setTargetParentKind(String targetParentKind) {
    this.targetParentKind = targetParentKind;
  }

  /**
   * Target parent id name
   **/
  public MigrationSet targetParentId(String targetParentId) {
    this.targetParentId = targetParentId;
    return this;
  }

  
  @ApiModelProperty(value = "Target parent id name")
  @JsonProperty("targetParentId")
  public String getTargetParentId() {
    return targetParentId;
  }
  public void setTargetParentId(String targetParentId) {
    this.targetParentId = targetParentId;
  }

  /**
   * Name of sync id property in target kind - used to identify record when updating existing record. You do not need to specify it if default value '__syncId' is sufficient for you
   **/
  public MigrationSet targetSyncIdPropertyName(String targetSyncIdPropertyName) {
    this.targetSyncIdPropertyName = targetSyncIdPropertyName;
    return this;
  }

  
  @ApiModelProperty(value = "Name of sync id property in target kind - used to identify record when updating existing record. You do not need to specify it if default value '__syncId' is sufficient for you")
  @JsonProperty("targetSyncIdPropertyName")
  public String getTargetSyncIdPropertyName() {
    return targetSyncIdPropertyName;
  }
  public void setTargetSyncIdPropertyName(String targetSyncIdPropertyName) {
    this.targetSyncIdPropertyName = targetSyncIdPropertyName;
  }

  /**
   * Name of property in target parent kind - used to lookup parent record and get its id in target data source to use as a parent id in child kind (in most cases it is parent kind sync id). You do not need to specify it if default value ‘__syncId’ is sufficient for you
   **/
  public MigrationSet targetParentLookupPropertyName(String targetParentLookupPropertyName) {
    this.targetParentLookupPropertyName = targetParentLookupPropertyName;
    return this;
  }

  
  @ApiModelProperty(value = "Name of property in target parent kind - used to lookup parent record and get its id in target data source to use as a parent id in child kind (in most cases it is parent kind sync id). You do not need to specify it if default value ‘__syncId’ is sufficient for you")
  @JsonProperty("targetParentLookupPropertyName")
  public String getTargetParentLookupPropertyName() {
    return targetParentLookupPropertyName;
  }
  public void setTargetParentLookupPropertyName(String targetParentLookupPropertyName) {
    this.targetParentLookupPropertyName = targetParentLookupPropertyName;
  }

  /**
   * Query which will narrow source kind records (partial changes). Keep 'null' if all records should be processed (initial import)
   **/
  public MigrationSet query(String query) {
    this.query = query;
    return this;
  }

  
  @ApiModelProperty(value = "Query which will narrow source kind records (partial changes). Keep 'null' if all records should be processed (initial import)")
  @JsonProperty("query")
  public String getQuery() {
    return query;
  }
  public void setQuery(String query) {
    this.query = query;
  }

  /**
   * Flag if all records for target kind should be removed prior to data migration
   **/
  public MigrationSet clean(Boolean clean) {
    this.clean = clean;
    return this;
  }

  
  @ApiModelProperty(value = "Flag if all records for target kind should be removed prior to data migration")
  @JsonProperty("clean")
  public Boolean getClean() {
    return clean;
  }
  public void setClean(Boolean clean) {
    this.clean = clean;
  }

  /**
   * Array of operations
   **/
  public MigrationSet properties(List<MigrationSetProperty> properties) {
    this.properties = properties;
    return this;
  }

  
  @ApiModelProperty(value = "Array of operations")
  @JsonProperty("properties")
  public List<MigrationSetProperty> getProperties() {
    return properties;
  }
  public void setProperties(List<MigrationSetProperty> properties) {
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
    MigrationSet migrationSet = (MigrationSet) o;
    return Objects.equals(author, migrationSet.author) &&
        Objects.equals(comment, migrationSet.comment) &&
        Objects.equals(sourceNamespace, migrationSet.sourceNamespace) &&
        Objects.equals(sourceParentNamespace, migrationSet.sourceParentNamespace) &&
        Objects.equals(sourceKind, migrationSet.sourceKind) &&
        Objects.equals(sourceSyncIdPropertyName, migrationSet.sourceSyncIdPropertyName) &&
        Objects.equals(sourceParentForeignIdPropertyName, migrationSet.sourceParentForeignIdPropertyName) &&
        Objects.equals(targetNamespace, migrationSet.targetNamespace) &&
        Objects.equals(targetKind, migrationSet.targetKind) &&
        Objects.equals(targetParentKind, migrationSet.targetParentKind) &&
        Objects.equals(targetParentId, migrationSet.targetParentId) &&
        Objects.equals(targetSyncIdPropertyName, migrationSet.targetSyncIdPropertyName) &&
        Objects.equals(targetParentLookupPropertyName, migrationSet.targetParentLookupPropertyName) &&
        Objects.equals(query, migrationSet.query) &&
        Objects.equals(clean, migrationSet.clean) &&
        Objects.equals(properties, migrationSet.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(author, comment, sourceNamespace, sourceParentNamespace, sourceKind, sourceSyncIdPropertyName, sourceParentForeignIdPropertyName, targetNamespace, targetKind, targetParentKind, targetParentId, targetSyncIdPropertyName, targetParentLookupPropertyName, query, clean, properties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSet {\n");
    
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    sourceNamespace: ").append(toIndentedString(sourceNamespace)).append("\n");
    sb.append("    sourceParentNamespace: ").append(toIndentedString(sourceParentNamespace)).append("\n");
    sb.append("    sourceKind: ").append(toIndentedString(sourceKind)).append("\n");
    sb.append("    sourceSyncIdPropertyName: ").append(toIndentedString(sourceSyncIdPropertyName)).append("\n");
    sb.append("    sourceParentForeignIdPropertyName: ").append(toIndentedString(sourceParentForeignIdPropertyName)).append("\n");
    sb.append("    targetNamespace: ").append(toIndentedString(targetNamespace)).append("\n");
    sb.append("    targetKind: ").append(toIndentedString(targetKind)).append("\n");
    sb.append("    targetParentKind: ").append(toIndentedString(targetParentKind)).append("\n");
    sb.append("    targetParentId: ").append(toIndentedString(targetParentId)).append("\n");
    sb.append("    targetSyncIdPropertyName: ").append(toIndentedString(targetSyncIdPropertyName)).append("\n");
    sb.append("    targetParentLookupPropertyName: ").append(toIndentedString(targetParentLookupPropertyName)).append("\n");
    sb.append("    query: ").append(toIndentedString(query)).append("\n");
    sb.append("    clean: ").append(toIndentedString(clean)).append("\n");
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

