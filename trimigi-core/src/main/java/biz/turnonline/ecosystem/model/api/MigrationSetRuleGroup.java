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
import java.util.List;
import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-08-29T18:41:11.119Z")
public class MigrationSetRuleGroup
        implements Serializable {
  
  private String operation = "and";
  private List<MigrationSetRule> rules = new ArrayList<MigrationSetRule>();

  /**
   * Rule logical operation
   **/
  public MigrationSetRuleGroup operation( String operation) {
    this.operation = operation;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Rule logical operation")
  @JsonProperty("operation")
  public String getOperation() {
    return operation;
  }
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * Array of rules
   **/
  public MigrationSetRuleGroup rules( List<MigrationSetRule> rules) {
    this.rules = rules;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Array of rules")
  @JsonProperty("rules")
  public List<MigrationSetRule> getRules() {
    return rules;
  }
  public void setRules(List<MigrationSetRule> rules) {
    this.rules = rules;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSetRuleGroup migrationSetRuleGroup = ( MigrationSetRuleGroup ) o;
    return Objects.equals(operation, migrationSetRuleGroup.operation) &&
        Objects.equals(rules, migrationSetRuleGroup.rules);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operation, rules);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetPropertyRuleSet {\n");
    
    sb.append("    operation: ").append(toIndentedString(operation)).append("\n");
    sb.append("    rules: ").append(toIndentedString(rules)).append("\n");
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

