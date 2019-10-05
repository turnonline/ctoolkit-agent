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
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2019-10-05T08:46:04.321Z")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MigrationSetDatabaseSelectEnricher.class, name = "database"),
        @JsonSubTypes.Type(value = MigrationSetRestEnricher.class, name = "rest"),
        @JsonSubTypes.Type(value = MigrationSetGroovyEnricher.class, name = "groovy")
})
public class MigrationSetEnricher implements Serializable
{
  
  private String name = null;
  private List<MigrationSetEnricherGroup> enrichers = new ArrayList<MigrationSetEnricherGroup>();

  /**
   * Name of enricher. Will be used as prefix when adding enricher values into context
   **/
  public MigrationSetEnricher name(String name) {
    this.name = name;
    return this;
  }

  
  @ApiModelProperty(value = "Name of enricher. Will be used as prefix when adding enricher values into context")
  @JsonProperty("name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Array of enrichers groups
   **/
  public MigrationSetEnricher enrichers(List<MigrationSetEnricherGroup> enrichers) {
    this.enrichers = enrichers;
    return this;
  }

  
  @ApiModelProperty(value = "Array of enrichers groups")
  @JsonProperty("enrichers")
  public List<MigrationSetEnricherGroup> getEnrichers() {
    return enrichers;
  }
  public void setEnrichers(List<MigrationSetEnricherGroup> enrichers) {
    this.enrichers = enrichers;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSetEnricher migrationSetEnricher = (MigrationSetEnricher) o;
    return Objects.equals(name, migrationSetEnricher.name) &&
        Objects.equals(enrichers, migrationSetEnricher.enrichers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, enrichers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetEnricher {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    enrichers: ").append(toIndentedString(enrichers)).append("\n");
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

