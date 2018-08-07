package org.ctoolkit.agent.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-08-06T19:03:45.711Z")
public class MigrationSetPropertyBlobTransformer extends MigrationSetPropertyTransformer {
  
  private Boolean encodeToBase64 = false;

  /**
   * Flag used to encode transformed string into base64
   **/
  public MigrationSetPropertyBlobTransformer encodeToBase64(Boolean encodeToBase64) {
    this.encodeToBase64 = encodeToBase64;
    return this;
  }

  
  @ApiModelProperty(value = "Flag used to encode transformed string into base64")
  @JsonProperty("encodeToBase64")
  public Boolean getEncodeToBase64() {
    return encodeToBase64;
  }
  public void setEncodeToBase64(Boolean encodeToBase64) {
    this.encodeToBase64 = encodeToBase64;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSetPropertyBlobTransformer migrationSetPropertyBlobTransformer = (MigrationSetPropertyBlobTransformer) o;
    return Objects.equals(encodeToBase64, migrationSetPropertyBlobTransformer.encodeToBase64);
  }

  @Override
  public int hashCode() {
    return Objects.hash(encodeToBase64);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetPropertyBlobTransformer {\n");
    
    sb.append("    encodeToBase64: ").append(toIndentedString(encodeToBase64)).append("\n");
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

