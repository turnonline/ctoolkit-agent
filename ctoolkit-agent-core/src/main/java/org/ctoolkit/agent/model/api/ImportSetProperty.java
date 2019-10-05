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
import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-08-29T18:41:11.119Z")
public class ImportSetProperty implements Serializable {
  
  private String name = null;
  private String type = null;
  private Object value = null;

  /**
   * Property name
   **/
  public ImportSetProperty name(String name) {
    this.name = name;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Property name")
  @JsonProperty("name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Property type - specified for every agent (for instance string, int, date). Also there are these reserved names:  - list - if you want to create list of values (simple or complex) - object - if you want to create embedded object
   **/
  public ImportSetProperty type(String type) {
    this.type = type;
    return this;
  }

  
  @ApiModelProperty(value = "Property type - specified for every agent (for instance string, int, date). Also there are these reserved names:  - list - if you want to create list of values (simple or complex) - object - if you want to create embedded object")
  @JsonProperty("type")
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Property value. It can be simple string or list of ImportSetProperty which can create complex structures
   **/
  public Object value(Object value) {
    this.value = value;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Property value. It can be simple string or list of ImportSetProperty which can create complex structures")
  @JsonProperty("value")
  public Object getValue() {
    return value;
  }
  public void setValue(Object value) {
    this.value = value;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImportSetProperty importSetProperty = (ImportSetProperty) o;
    return Objects.equals(name, importSetProperty.name) &&
        Objects.equals(type, importSetProperty.type) &&
        Objects.equals(value, importSetProperty.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImportSetProperty {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

