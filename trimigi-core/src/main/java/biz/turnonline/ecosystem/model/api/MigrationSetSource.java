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

package biz.turnonline.ecosystem.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2019-10-12T18:18:04.105Z")
public class MigrationSetSource implements Serializable
{
  
  private String namespace = null;
  private String kind = null;
  private Date changeDate = null;
  private String idSelector = null;
  private Long limit = 0l;
  private Long offset = 0l;
  private List<QueryFilter> filters = new ArrayList<QueryFilter>();

  /**
   * Source namespace (for elasticsearch it is ‘index’, for sql it is ‘schema’)
   **/
  public MigrationSetSource namespace(String namespace) {
    this.namespace = namespace;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Source namespace (for elasticsearch it is ‘index’, for sql it is ‘schema’)")
  @JsonProperty("namespace")
  public String getNamespace() {
    return namespace;
  }
  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  /**
   * Source kind name (for elasticsearch it is ‘document’, for sql it is ‘table’)
   **/
  public MigrationSetSource kind(String kind) {
    this.kind = kind;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Source kind name (for elasticsearch it is ‘document’, for sql it is ‘table’)")
  @JsonProperty("kind")
  public String getKind() {
    return kind;
  }
  public void setKind(String kind) {
    this.kind = kind;
  }

  /**
   * Date of change in source system. Used with conjunction with MigrationSetTarget.syncDateProperty and MigrationSetTarget.queryIdentifier
   **/
  public MigrationSetSource changeDate(Date changeDate) {
    this.changeDate = changeDate;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Date of change in source system. Used with conjunction with MigrationSetTarget.syncDateProperty and MigrationSetTarget.queryIdentifier")
  @JsonProperty("changeDate")
  public Date getChangeDate() {
    return changeDate;
  }
  public void setChangeDate(Date changeDate) {
    this.changeDate = changeDate;
  }

  /**
   * Selector used to create id in target agent. Result of this pattern can be encoded into base64 string and it will be used as a primary id of target kind.  If omitted id will be generated
   **/
  public MigrationSetSource idSelector(String idSelector) {
    this.idSelector = idSelector;
    return this;
  }

  
  @ApiModelProperty(value = "Selector used to create id in target agent. Result of this pattern can be encoded into base64 string and it will be used as a primary id of target kind.  If omitted id will be generated")
  @JsonProperty("idSelector")
  public String getIdSelector() {
    return idSelector;
  }
  public void setIdSelector(String idSelector) {
    this.idSelector = idSelector;
  }

  /**
   * Query limit. Default is 0 which means no limit.
   **/
  public MigrationSetSource limit(Long limit) {
    this.limit = limit;
    return this;
  }

  
  @ApiModelProperty(value = "Query limit. Default is 0 which means no limit.")
  @JsonProperty("limit")
  public Long getLimit() {
    return limit;
  }
  public void setLimit(Long limit) {
    this.limit = limit;
  }

  /**
   * Query offset. Default is 0 which means no offset.
   **/
  public MigrationSetSource offset(Long offset) {
    this.offset = offset;
    return this;
  }

  
  @ApiModelProperty(value = "Query offset. Default is 0 which means no offset.")
  @JsonProperty("offset")
  public Long getOffset() {
    return offset;
  }
  public void setOffset(Long offset) {
    this.offset = offset;
  }

  /**
   * List of query filters
   **/
  public MigrationSetSource filter(List<QueryFilter> filter) {
    this.filters = filter;
    return this;
  }

  
  @ApiModelProperty(value = "List of query filters")
  @JsonProperty("filter")
  public List<QueryFilter> getFilters() {
    return filters;
  }
  public void setFilters( List<QueryFilter> filters ) {
    this.filters = filters;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSetSource migrationSetSource = (MigrationSetSource) o;
    return Objects.equals(namespace, migrationSetSource.namespace) &&
        Objects.equals(kind, migrationSetSource.kind) &&
        Objects.equals(changeDate, migrationSetSource.changeDate) &&
        Objects.equals(idSelector, migrationSetSource.idSelector) &&
        Objects.equals(limit, migrationSetSource.limit) &&
        Objects.equals(offset, migrationSetSource.offset) &&
        Objects.equals( filters, migrationSetSource.filters );
  }

  @Override
  public int hashCode() {
    return Objects.hash(namespace, kind, changeDate, idSelector, limit, offset, filters );
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetSource {\n");
    
    sb.append("    namespace: ").append(toIndentedString(namespace)).append("\n");
    sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
    sb.append("    changeDate: ").append(toIndentedString(changeDate)).append("\n");
    sb.append("    idSelector: ").append(toIndentedString(idSelector)).append("\n");
    sb.append("    limit: ").append(toIndentedString(limit)).append("\n");
    sb.append("    offset: ").append(toIndentedString(offset)).append("\n");
    sb.append("    filter: ").append(toIndentedString( filters )).append("\n");
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

