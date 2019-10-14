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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-08-30T05:34:23.887Z")
public class MigrationSetProperty implements Serializable {

  private String sourceProperty = null;
  private String targetType = null;
  private String targetProperty = null;
  @JsonDeserialize(using = MigrationSetPropertyValueJsonDeserializer.class)
  private Object targetValue = null;
  private List<MigrationSetPropertyTransformer> transformers;

  /**
   * Source property name
   **/
  public MigrationSetProperty sourceProperty(String sourceProperty) {
    this.sourceProperty = sourceProperty;
    return this;
  }


  @ApiModelProperty(required = true, value = "Source property name")
  @JsonProperty("sourceProperty")
  public String getSourceProperty() {
    return sourceProperty;
  }
  public void setSourceProperty(String sourceProperty) {
    this.sourceProperty = sourceProperty;
  }

  /**
   * Property type - specified for every agent (for instance string, int, date). Also there are these reserved names:  - list - if you want to create list of values (simple or complex) - object - if you want to create embedded object
   **/
  public MigrationSetProperty targetType(String targetType) {
    this.targetType = targetType;
    return this;
  }


  @ApiModelProperty(required = true, value = "Property type - specified for every agent (for instance string, int, date). Also there are these reserved names:  - list - if you want to create list of values (simple or complex) - object - if you want to create embedded object")
  @JsonProperty("targetType")
  public String getTargetType() {
    return targetType;
  }
  public void setTargetType(String targetType) {
    this.targetType = targetType;
  }

  /**
   * Target property name
   **/
  public MigrationSetProperty targetProperty(String targetProperty) {
    this.targetProperty = targetProperty;
    return this;
  }


  @ApiModelProperty(required = true, value = "Target property name")
  @JsonProperty("targetProperty")
  public String getTargetProperty() {
    return targetProperty;
  }
  public void setTargetProperty(String targetProperty) {
    this.targetProperty = targetProperty;
  }

  /**
   * Property value. It can be simple string or list of MigrationSetProperty which can create complex structure
   **/
  public MigrationSetProperty targetValue(Object targetValue) {
    this.targetValue = targetValue;
    return this;
  }


  @ApiModelProperty(required = true, value = "Property value. It can be simple string or list of MigrationSetProperty which can create complex structure")
  @JsonProperty("targetValue")
  public Object getTargetValue() {
    return targetValue;
  }
  public void setTargetValue(Object targetValue) {
    this.targetValue = targetValue;
  }

  /**
   * Array of transformers used to transform source value
   **/
  public MigrationSetProperty transformers(List<MigrationSetPropertyTransformer> transformers) {
    this.transformers = transformers;
    return this;
  }


  @ApiModelProperty(value = "Array of transformers used to transform source value")
  @JsonProperty("transformers")
  public List<MigrationSetPropertyTransformer> getTransformers() {
    return transformers;
  }
  public void setTransformers(List<MigrationSetPropertyTransformer> transformers) {
    this.transformers = transformers;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSetProperty migrationSetProperty = (MigrationSetProperty) o;
    return Objects.equals(sourceProperty, migrationSetProperty.sourceProperty) &&
        Objects.equals(targetType, migrationSetProperty.targetType) &&
        Objects.equals(targetProperty, migrationSetProperty.targetProperty) &&
        Objects.equals(targetValue, migrationSetProperty.targetValue) &&
        Objects.equals(transformers, migrationSetProperty.transformers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sourceProperty, targetType, targetProperty, targetValue, transformers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetProperty {\n");

    sb.append("    sourceProperty: ").append(toIndentedString(sourceProperty)).append("\n");
    sb.append("    targetType: ").append(toIndentedString(targetType)).append("\n");
    sb.append("    targetProperty: ").append(toIndentedString(targetProperty)).append("\n");
    sb.append("    targetValue: ").append(toIndentedString(targetValue)).append("\n");
    sb.append("    transformers: ").append(toIndentedString(transformers)).append("\n");
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

