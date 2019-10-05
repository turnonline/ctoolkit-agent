/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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
  private String changeDate = null;
  private String syncDateProperty = "syncDate";
  private String idSelector = null;
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
   * Date of change in source system. Used with conjunction with syncDateProperty and queryIdentifier
   **/
  public ImportSet changeDate(String changeDate) {
    this.changeDate = changeDate;
    return this;
  }

  
  @ApiModelProperty(value = "Date of change in source system. Used with conjunction with syncDateProperty and queryIdentifier")
  @JsonProperty("changeDate")
  public String getChangeDate() {
    return changeDate;
  }
  public void setChangeDate(String changeDate) {
    this.changeDate = changeDate;
  }

  /**
   * Name of sync date property which will be used to determine if import is required or not.  Must be type of 'date'
   **/
  public ImportSet syncDateProperty(String syncDateProperty) {
    this.syncDateProperty = syncDateProperty;
    return this;
  }

  
  @ApiModelProperty(value = "Name of sync date property which will be used to determine if import is required or not.  Must be type of 'date'")
  @JsonProperty("syncDateProperty")
  public String getSyncDateProperty() {
    return syncDateProperty;
  }
  public void setSyncDateProperty(String syncDateProperty) {
    this.syncDateProperty = syncDateProperty;
  }

  /**
   * Selector which will be used to get identifier. Can be simple 'id' or embedded value, for instance 'identifications.type=SALESFORCE and identifications.value=${ids.id}'  It is used in conjunction with syncDateProperty with following logic:  - if record does not exists, syncDate will be ignored and new record will be created - if record exists and syncDate is null or is before MigrationSetSource.changeDate, record will be updated - if record exists and syncDate is after MigrationSetSource.changeDate import will be skipped
   **/
  public ImportSet idSelector(String idSelector) {
    this.idSelector = idSelector;
    return this;
  }

  
  @ApiModelProperty(value = "Selector which will be used to get identifier. Can be simple 'id' or embedded value, for instance 'identifications.type=SALESFORCE and identifications.value=${ids.id}'  It is used in conjunction with syncDateProperty with following logic:  - if record does not exists, syncDate will be ignored and new record will be created - if record exists and syncDate is null or is before MigrationSetSource.changeDate, record will be updated - if record exists and syncDate is after MigrationSetSource.changeDate import will be skipped")
  @JsonProperty("idSelector")
  public String getIdSelector() {
    return idSelector;
  }
  public void setIdSelector(String idSelector) {
    this.idSelector = idSelector;
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
        Objects.equals(changeDate, importSet.changeDate) &&
        Objects.equals(syncDateProperty, importSet.syncDateProperty) &&
        Objects.equals(idSelector, importSet.idSelector) &&
        Objects.equals(properties, importSet.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(author, comment, clean, namespace, kind, id, changeDate, syncDateProperty, idSelector, properties);
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
    sb.append("    changeDate: ").append(toIndentedString(changeDate)).append("\n");
    sb.append("    syncDateProperty: ").append(toIndentedString(syncDateProperty)).append("\n");
    sb.append("    idSelector: ").append(toIndentedString(idSelector)).append("\n");
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

