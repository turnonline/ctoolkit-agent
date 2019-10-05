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

import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2019-10-05T08:46:04.321Z")
public class MigrationSetPropertyEncodingTransformer extends MigrationSetPropertyTransformer {
  
  private String operation = "encode";
  private String encodingType = "base64";

  /**
   * Operation - encode/decode
   **/
  public MigrationSetPropertyEncodingTransformer operation(String operation) {
    this.operation = operation;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Operation - encode/decode")
  @JsonProperty("operation")
  public String getOperation() {
    return operation;
  }
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * Encoding type
   **/
  public MigrationSetPropertyEncodingTransformer encodingType(String encodingType) {
    this.encodingType = encodingType;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Encoding type")
  @JsonProperty("encodingType")
  public String getEncodingType() {
    return encodingType;
  }
  public void setEncodingType(String encodingType) {
    this.encodingType = encodingType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSetPropertyEncodingTransformer migrationSetPropertyEncodingTransformer = (MigrationSetPropertyEncodingTransformer) o;
    return Objects.equals(operation, migrationSetPropertyEncodingTransformer.operation) &&
        Objects.equals(encodingType, migrationSetPropertyEncodingTransformer.encodingType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operation, encodingType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetPropertyEncodingTransformer {\n");
    
    sb.append("    operation: ").append(toIndentedString(operation)).append("\n");
    sb.append("    encodingType: ").append(toIndentedString(encodingType)).append("\n");
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

