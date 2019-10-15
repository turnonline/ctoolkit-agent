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
public class MigrationSet implements Serializable {
  
  private String author = null;
  private String comment = null;
  private MigrationSetSource source = null;
  private MigrationSetTarget target = null;
  private List<MigrationSetProperty> properties = new ArrayList<MigrationSetProperty>();
  private MigrationSetRuleGroup ruleGroups = null;
  private List<MigrationSetEnricherGroup> enricherGroups;

  /**
   * Migration set author
   **/
  public MigrationSet author(String author) {
    this.author = author;
    return this;
  }

  
  @ApiModelProperty(value = "Migration set author")
  @JsonProperty("author")
  public String getAuthor() {
    return author;
  }
  public void setAuthor(String author) {
    this.author = author;
  }

  /**
   * Optional comment for migration set
   **/
  public MigrationSet comment(String comment) {
    this.comment = comment;
    return this;
  }

  
  @ApiModelProperty(value = "Optional comment for migration set")
  @JsonProperty("comment")
  public String getComment() {
    return comment;
  }
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   **/
  public MigrationSet source(MigrationSetSource source) {
    this.source = source;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("source")
  public MigrationSetSource getSource() {
    return source;
  }
  public void setSource(MigrationSetSource source) {
    this.source = source;
  }

  /**
   **/
  public MigrationSet target(MigrationSetTarget target) {
    this.target = target;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("target")
  public MigrationSetTarget getTarget() {
    return target;
  }
  public void setTarget(MigrationSetTarget target) {
    this.target = target;
  }

  /**
   * Array of operations
   **/
  public MigrationSet properties(List<MigrationSetProperty> properties) {
    this.properties = properties;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Array of operations")
  @JsonProperty("properties")
  public List<MigrationSetProperty> getProperties() {
    return properties;
  }
  public void setProperties(List<MigrationSetProperty> properties) {
    this.properties = properties;
  }

  /**
   **/
  public MigrationSet ruleSet( MigrationSetRuleGroup ruleSet) {
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

  /**
   * Array of enricher groups
   **/
  public MigrationSet enrichers(List<MigrationSetEnricherGroup> enrichers) {
    this.enricherGroups = enrichers;
    return this;
  }

  
  @ApiModelProperty(value = "Array of enricher groups")
  @JsonProperty("enricherGroups")
  public List<MigrationSetEnricherGroup> getEnricherGroups() {
    return enricherGroups;
  }
  public void setEnricherGroups( List<MigrationSetEnricherGroup> enricherGroups ) {
    this.enricherGroups = enricherGroups;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSet migrationSet = (MigrationSet) o;
    return Objects.equals(author, migrationSet.author) &&
        Objects.equals(comment, migrationSet.comment) &&
        Objects.equals(source, migrationSet.source) &&
        Objects.equals(target, migrationSet.target) &&
        Objects.equals(properties, migrationSet.properties) &&
        Objects.equals( ruleGroups, migrationSet.ruleGroups ) &&
        Objects.equals( enricherGroups, migrationSet.enricherGroups );
  }

  @Override
  public int hashCode() {
    return Objects.hash(author, comment, source, target, properties, ruleGroups, enricherGroups );
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSet {\n");
    
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    target: ").append(toIndentedString(target)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
    sb.append("    ruleSet: ").append(toIndentedString( ruleGroups )).append("\n");
    sb.append("    enricherGroups: ").append(toIndentedString( enricherGroups )).append("\n");
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

