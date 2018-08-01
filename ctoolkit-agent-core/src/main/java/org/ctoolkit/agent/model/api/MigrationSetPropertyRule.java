package org.ctoolkit.agent.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-07-31T19:00:18.485Z")
public class MigrationSetPropertyRule   {
  
  private String property = null;
  private String operation = null;
  private String value = null;

  /**
   * Property name
   **/
  public MigrationSetPropertyRule property(String property) {
    this.property = property;
    return this;
  }

  
  @ApiModelProperty(value = "Property name")
  @JsonProperty("property")
  public String getProperty() {
    return property;
  }
  public void setProperty(String property) {
    this.property = property;
  }

  /**
   * Operation expression
   **/
  public MigrationSetPropertyRule operation(String operation) {
    this.operation = operation;
    return this;
  }

  
  @ApiModelProperty(value = "Operation expression")
  @JsonProperty("operation")
  public String getOperation() {
    return operation;
  }
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * Property value
   **/
  public MigrationSetPropertyRule value(String value) {
    this.value = value;
    return this;
  }

  
  @ApiModelProperty(value = "Property value")
  @JsonProperty("value")
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
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
    MigrationSetPropertyRule migrationSetPropertyRule = (MigrationSetPropertyRule) o;
    return Objects.equals(property, migrationSetPropertyRule.property) &&
        Objects.equals(operation, migrationSetPropertyRule.operation) &&
        Objects.equals(value, migrationSetPropertyRule.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(property, operation, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetPropertyRule {\n");
    
    sb.append("    property: ").append(toIndentedString(property)).append("\n");
    sb.append("    operation: ").append(toIndentedString(operation)).append("\n");
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

