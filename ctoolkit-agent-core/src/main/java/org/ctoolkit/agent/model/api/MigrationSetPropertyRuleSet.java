package org.ctoolkit.agent.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-07-31T19:00:18.485Z")
public class MigrationSetPropertyRuleSet   {
  
  private String operation = "and";
  private List<MigrationSetPropertyRule> rules = new ArrayList<MigrationSetPropertyRule>();

  /**
   * Rule logical operation
   **/
  public MigrationSetPropertyRuleSet operation(String operation) {
    this.operation = operation;
    return this;
  }

  
  @ApiModelProperty(value = "Rule logical operation")
  @JsonProperty("operation")
  public String getOperation() {
    return operation;
  }
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * Array of rules
   **/
  public MigrationSetPropertyRuleSet rules(List<MigrationSetPropertyRule> rules) {
    this.rules = rules;
    return this;
  }

  
  @ApiModelProperty(value = "Array of rules")
  @JsonProperty("rules")
  public List<MigrationSetPropertyRule> getRules() {
    return rules;
  }
  public void setRules(List<MigrationSetPropertyRule> rules) {
    this.rules = rules;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSetPropertyRuleSet migrationSetPropertyRuleSet = (MigrationSetPropertyRuleSet) o;
    return Objects.equals(operation, migrationSetPropertyRuleSet.operation) &&
        Objects.equals(rules, migrationSetPropertyRuleSet.rules);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operation, rules);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetPropertyRuleSet {\n");
    
    sb.append("    operation: ").append(toIndentedString(operation)).append("\n");
    sb.append("    rules: ").append(toIndentedString(rules)).append("\n");
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

