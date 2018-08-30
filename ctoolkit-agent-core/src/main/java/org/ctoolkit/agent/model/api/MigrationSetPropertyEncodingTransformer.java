package org.ctoolkit.agent.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-08-30T12:17:03.356Z")
public class MigrationSetPropertyEncodingTransformer extends MigrationSetPropertyTransformer {
  
  private String operation = "encode";
  private String encodingType = "base64";

  /**
   * Operation - encode/decode
   **/
  public MigrationSetPropertyEncodingTransformer operation(String operation) {
    this.operation = operation;
    return this;
  }

  
  @ApiModelProperty(value = "Operation - encode/decode")
  @JsonProperty("operation")
  public String getOperation() {
    return operation;
  }
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * Encoding type
   **/
  public MigrationSetPropertyEncodingTransformer type(String type) {
    this.encodingType = type;
    return this;
  }

  
  @ApiModelProperty(value = "Encoding type")
  @JsonProperty("encodingType")
  public String getEncodingType() {
    return encodingType;
  }
  public void setEncodingType( String encodingType ) {
    this.encodingType = encodingType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSetPropertyEncodingTransformer migrationSetPropertyEncodingTransformer = (MigrationSetPropertyEncodingTransformer) o;
    return Objects.equals(operation, migrationSetPropertyEncodingTransformer.operation) &&
        Objects.equals( encodingType, migrationSetPropertyEncodingTransformer.encodingType );
  }

  @Override
  public int hashCode() {
    return Objects.hash(operation, encodingType );
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetPropertyEncodingTransformer {\n");
    
    sb.append("    operation: ").append(toIndentedString(operation)).append("\n");
    sb.append("    encodingType: ").append(toIndentedString( encodingType )).append("\n");
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

