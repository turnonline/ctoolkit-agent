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
import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-08-29T18:41:11.119Z")
public class MigrationSetRule
        implements Serializable {
  
  private String property = null;
  private String operation = null;
  private String value = null;
  private MigrationSetRuleGroup ruleGroups = null;

  /**
   * Property name
   **/
  public MigrationSetRule property( String property) {
    this.property = property;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Property name")
  @JsonProperty("property")
  public String getProperty() {
    return property;
  }
  public void setProperty(String property) {
    this.property = property;
  }

  /**
   * Operation expression
   **/
  public MigrationSetRule operation( String operation) {
    this.operation = operation;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Operation expression")
  @JsonProperty("operation")
  public String getOperation() {
    return operation;
  }
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * Property value
   **/
  public MigrationSetRule value( String value) {
    this.value = value;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Property value")
  @JsonProperty("value")
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }

  /**
   **/
  public MigrationSetRule ruleSet( MigrationSetRuleGroup ruleSet) {
    this.ruleGroups = ruleSet;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("ruleSet")
  public MigrationSetRuleGroup getRuleGroups() {
    return ruleGroups;
  }
  public void setRuleGroups( MigrationSetRuleGroup ruleGroups ) {
    this.ruleGroups = ruleGroups;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSetRule migrationSetRule = ( MigrationSetRule ) o;
    return Objects.equals(property, migrationSetRule.property) &&
        Objects.equals(operation, migrationSetRule.operation) &&
        Objects.equals(value, migrationSetRule.value) &&
        Objects.equals( ruleGroups, migrationSetRule.ruleGroups );
  }

  @Override
  public int hashCode() {
    return Objects.hash(property, operation, value, ruleGroups );
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetPropertyRule {\n");
    
    sb.append("    property: ").append(toIndentedString(property)).append("\n");
    sb.append("    operation: ").append(toIndentedString(operation)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    ruleSet: ").append(toIndentedString( ruleGroups )).append("\n");
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

