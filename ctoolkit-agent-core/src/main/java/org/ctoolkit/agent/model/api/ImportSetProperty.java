package org.ctoolkit.agent.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-07-31T19:00:18.485Z")
public class ImportSetProperty implements Serializable {
  
  private String name = null;
  private String type = null;
  private String value = null;
  private String multiplicity = null;

  /**
   * Property name
   **/
  public ImportSetProperty name(String name) {
    this.name = name;
    return this;
  }

  
  @ApiModelProperty(value = "Property name")
  @JsonProperty("name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Property type
   **/
  public ImportSetProperty type(String type) {
    this.type = type;
    return this;
  }

  
  @ApiModelProperty(value = "Property type")
  @JsonProperty("type")
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Property value
   **/
  public ImportSetProperty value(String value) {
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

  /**
   * Property multiplicity
   **/
  public ImportSetProperty multiplicity(String multiplicity) {
    this.multiplicity = multiplicity;
    return this;
  }

  
  @ApiModelProperty(value = "Property multiplicity")
  @JsonProperty("multiplicity")
  public String getMultiplicity() {
    return multiplicity;
  }
  public void setMultiplicity(String multiplicity) {
    this.multiplicity = multiplicity;
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
        Objects.equals(value, importSetProperty.value) &&
        Objects.equals(multiplicity, importSetProperty.multiplicity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type, value, multiplicity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImportSetProperty {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    multiplicity: ").append(toIndentedString(multiplicity)).append("\n");
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

