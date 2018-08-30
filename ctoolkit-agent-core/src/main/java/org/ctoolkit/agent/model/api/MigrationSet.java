package org.ctoolkit.agent.model.api;

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
  private String query = null;
  private MigrationSetSource source = null;
  private MigrationSetTarget target = null;
  private List<MigrationSetProperty> properties = new ArrayList<MigrationSetProperty>();
  private MigrationSetPropertyRuleSet ruleSet = null;

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
   * Query which will narrow source kind records (partial changes). Keep 'null' if all records should be processed (initial import)
   **/
  public MigrationSet query(String query) {
    this.query = query;
    return this;
  }

  
  @ApiModelProperty(value = "Query which will narrow source kind records (partial changes). Keep 'null' if all records should be processed (initial import)")
  @JsonProperty("query")
  public String getQuery() {
    return query;
  }
  public void setQuery(String query) {
    this.query = query;
  }

  /**
   * Source metadata definition
   **/
  public MigrationSet source(MigrationSetSource source) {
    this.source = source;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Source metadata definition")
  @JsonProperty("source")
  public MigrationSetSource getSource() {
    return source;
  }
  public void setSource(MigrationSetSource source) {
    this.source = source;
  }

  /**
   * Target metadata definition
   **/
  public MigrationSet target(MigrationSetTarget target) {
    this.target = target;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Target metadata definition")
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
   * Rule set is set of logical  (and, or) and mathematical (eq, lt, regexp) rules used to filter certain rows from migration
   **/
  public MigrationSet ruleSet(MigrationSetPropertyRuleSet ruleSet) {
    this.ruleSet = ruleSet;
    return this;
  }

  
  @ApiModelProperty(value = "Rule set is set of logical  (and, or) and mathematical (eq, lt, regexp) rules used to filter certain rows from migration")
  @JsonProperty("ruleSet")
  public MigrationSetPropertyRuleSet getRuleSet() {
    return ruleSet;
  }
  public void setRuleSet(MigrationSetPropertyRuleSet ruleSet) {
    this.ruleSet = ruleSet;
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
        Objects.equals(query, migrationSet.query) &&
        Objects.equals(source, migrationSet.source) &&
        Objects.equals(target, migrationSet.target) &&
        Objects.equals(properties, migrationSet.properties) &&
        Objects.equals(ruleSet, migrationSet.ruleSet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(author, comment, query, source, target, properties, ruleSet);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSet {\n");
    
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    query: ").append(toIndentedString(query)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    target: ").append(toIndentedString(target)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
    sb.append("    ruleSet: ").append(toIndentedString(ruleSet)).append("\n");
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

