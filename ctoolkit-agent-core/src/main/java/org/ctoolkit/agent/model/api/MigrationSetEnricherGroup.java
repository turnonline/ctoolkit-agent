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





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2019-10-05T08:46:04.321Z")
public class MigrationSetEnricherGroup  implements Serializable
{
  
  private List<MigrationSetEnricher> enrichers = new ArrayList<MigrationSetEnricher>();
  private String execution = "sequence";

  /**
   * Array of enrichers inside group
   **/
  public MigrationSetEnricherGroup enrichers(List<MigrationSetEnricher> enrichers) {
    this.enrichers = enrichers;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Array of enrichers inside group")
  @JsonProperty("enrichers")
  public List<MigrationSetEnricher> getEnrichers() {
    return enrichers;
  }
  public void setEnrichers(List<MigrationSetEnricher> enrichers) {
    this.enrichers = enrichers;
  }

  /**
   * How enrichers in group should be executed. Two possible values:  - sequence - enrichers will be executed step by step - parallel - enrichers will be executed simultaneously
   **/
  public MigrationSetEnricherGroup execution(String execution) {
    this.execution = execution;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "How enrichers in group should be executed. Two possible values:  - sequence - enrichers will be executed step by step - parallel - enrichers will be executed simultaneously")
  @JsonProperty("execution")
  public String getExecution() {
    return execution;
  }
  public void setExecution(String execution) {
    this.execution = execution;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSetEnricherGroup migrationSetEnricherGroup = (MigrationSetEnricherGroup) o;
    return Objects.equals(enrichers, migrationSetEnricherGroup.enrichers) &&
        Objects.equals(execution, migrationSetEnricherGroup.execution);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enrichers, execution);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetEnricherGroup {\n");
    
    sb.append("    enrichers: ").append(toIndentedString(enrichers)).append("\n");
    sb.append("    execution: ").append(toIndentedString(execution)).append("\n");
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

