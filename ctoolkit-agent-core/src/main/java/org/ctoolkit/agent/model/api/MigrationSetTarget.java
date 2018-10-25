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
public class MigrationSetTarget implements Serializable {
  
  private String namespace = null;
  private String kind = null;

  /**
   * Target namespace (for elasticsearch it is ‘index’, for sql it is ‘schema’)
   **/
  public MigrationSetTarget namespace(String namespace) {
    this.namespace = namespace;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Target namespace (for elasticsearch it is ‘index’, for sql it is ‘schema’)")
  @JsonProperty("namespace")
  public String getNamespace() {
    return namespace;
  }
  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  /**
   * Target kind name (for elasticsearch it is ‘document’, for sql it is ‘table’)
   **/
  public MigrationSetTarget kind(String kind) {
    this.kind = kind;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Target kind name (for elasticsearch it is ‘document’, for sql it is ‘table’)")
  @JsonProperty("kind")
  public String getKind() {
    return kind;
  }
  public void setKind(String kind) {
    this.kind = kind;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSetTarget migrationSetTarget = (MigrationSetTarget) o;
    return Objects.equals(namespace, migrationSetTarget.namespace) &&
        Objects.equals(kind, migrationSetTarget.kind);
  }

  @Override
  public int hashCode() {
    return Objects.hash(namespace, kind);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetTarget {\n");
    
    sb.append("    namespace: ").append(toIndentedString(namespace)).append("\n");
    sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
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

