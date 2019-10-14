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
public class MigrationSetDatabaseSelectEnricher extends MigrationSetEnricher
{

  private String query = null;
  private List<NamedParameter> namedParameters = new ArrayList<NamedParameter>();

  /**
   * Query which will be passed to database select. Result properties of query will be passed to context with enricher name prefix, for instance if enrichers name is 'getPersonByEmail' and one of result property is 'name' the resulted context key will be 'getPersonByEmail.name'.
   **/
  public MigrationSetDatabaseSelectEnricher query(String query) {
    this.query = query;
    return this;
  }


  @ApiModelProperty(value = "Query which will be passed to database select. Result properties of query will be passed to context with enricher name prefix, for instance if enrichers name is 'getPersonByEmail' and one of result property is 'name' the resulted context key will be 'getPersonByEmail.name'.")
  @JsonProperty("query")
  public String getQuery() {
    return query;
  }
  public void setQuery(String query) {
    this.query = query;
  }

  /**
   * List of named parameters passed to query
   **/
  public MigrationSetDatabaseSelectEnricher namedParameters(List<NamedParameter> namedParameters) {
    this.namedParameters = namedParameters;
    return this;
  }


  @ApiModelProperty(value = "List of named parameters passed to query")
  @JsonProperty("namedParameters")
  public List<NamedParameter> getNamedParameters() {
    return namedParameters;
  }
  public void setNamedParameters(List<NamedParameter> namedParameters) {
    this.namedParameters = namedParameters;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSetDatabaseSelectEnricher migrationSetDatabaseSelectEnricher = (MigrationSetDatabaseSelectEnricher) o;
    return Objects.equals(query, migrationSetDatabaseSelectEnricher.query) &&
        Objects.equals(namedParameters, migrationSetDatabaseSelectEnricher.namedParameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(query, namedParameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetDatabaseSelectEnricher {\n");

    sb.append("    query: ").append(toIndentedString(query)).append("\n");
    sb.append("    namedParameters: ").append(toIndentedString(namedParameters)).append("\n");
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

