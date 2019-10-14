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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2019-10-05T08:46:04.321Z")
public class MigrationSetRestEnricher  extends MigrationSetEnricher
{
  
  private String url = null;
  private List<QueryParameter> queryParameters = new ArrayList<QueryParameter>();

  /**
   * Url of remote call. Only GET is supported. Result properties of rest call will be passed to context with enricher name prefix, for instance if enrichers name is 'getPersonByEmail' and one of result is plain text with value 'name' the resulted context key will be 'getPersonByEmail.name'.  If response is json it will be flatted into context values prefixed with enricher name
   **/
  public MigrationSetRestEnricher url(String url) {
    this.url = url;
    return this;
  }

  
  @ApiModelProperty(value = "Url of remote call. Only GET is supported. Result properties of rest call will be passed to context with enricher name prefix, for instance if enrichers name is 'getPersonByEmail' and one of result is plain text with value 'name' the resulted context key will be 'getPersonByEmail.name'.  If response is json it will be flatted into context values prefixed with enricher name")
  @JsonProperty("url")
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * List of query parameters
   **/
  public MigrationSetRestEnricher queryParameters(List<QueryParameter> queryParameters) {
    this.queryParameters = queryParameters;
    return this;
  }

  
  @ApiModelProperty(value = "List of query parameters")
  @JsonProperty("queryParameters")
  public List<QueryParameter> getQueryParameters() {
    return queryParameters;
  }
  public void setQueryParameters(List<QueryParameter> queryParameters) {
    this.queryParameters = queryParameters;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSetRestEnricher migrationSetRestEnricher = (MigrationSetRestEnricher) o;
    return Objects.equals(url, migrationSetRestEnricher.url) &&
        Objects.equals(queryParameters, migrationSetRestEnricher.queryParameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url, queryParameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetRestEnricher {\n");
    
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    queryParameters: ").append(toIndentedString(queryParameters)).append("\n");
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

