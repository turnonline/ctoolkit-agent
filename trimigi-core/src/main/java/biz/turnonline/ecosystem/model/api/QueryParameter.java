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





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2019-10-06T13:41:49.043Z")
public class QueryParameter implements Serializable
{
  
  private String name = null;
  private String value = null;
  private String converter = "string";

  /**
   * Query parameter name
   **/
  public QueryParameter name(String name) {
    this.name = name;
    return this;
  }

  
  @ApiModelProperty(value = "Query parameter name")
  @JsonProperty("name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Query parameter value
   **/
  public QueryParameter value(String value) {
    this.value = value;
    return this;
  }

  
  @ApiModelProperty(value = "Query parameter value")
  @JsonProperty("value")
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Query parameter converter
   **/
  public QueryParameter converter(String converter) {
    this.converter = converter;
    return this;
  }

  
  @ApiModelProperty(value = "Query parameter converter")
  @JsonProperty("converter")
  public String getConverter() {
    return converter;
  }
  public void setConverter(String converter) {
    this.converter = converter;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QueryParameter queryParameter = (QueryParameter) o;
    return Objects.equals(name, queryParameter.name) &&
        Objects.equals(value, queryParameter.value) &&
        Objects.equals(converter, queryParameter.converter);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, value, converter);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueryParameter {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    converter: ").append(toIndentedString(converter)).append("\n");
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

