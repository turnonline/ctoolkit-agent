package org.ctoolkit.agent.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-07-31T19:00:18.485Z")
public class MigrationSetPropertyMapperTransformerMappings implements Serializable {
  
  private String source = null;
  private String target = null;

  /**
   * Source map value
   **/
  public MigrationSetPropertyMapperTransformerMappings source(String source) {
    this.source = source;
    return this;
  }

  
  @ApiModelProperty(value = "Source map value")
  @JsonProperty("source")
  public String getSource() {
    return source;
  }
  public void setSource(String source) {
    this.source = source;
  }

  /**
   * Target map value
   **/
  public MigrationSetPropertyMapperTransformerMappings target(String target) {
    this.target = target;
    return this;
  }

  
  @ApiModelProperty(value = "Target map value")
  @JsonProperty("target")
  public String getTarget() {
    return target;
  }
  public void setTarget(String target) {
    this.target = target;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSetPropertyMapperTransformerMappings migrationSetPropertyMapperTransformerMappings = (MigrationSetPropertyMapperTransformerMappings) o;
    return Objects.equals(source, migrationSetPropertyMapperTransformerMappings.source) &&
        Objects.equals(target, migrationSetPropertyMapperTransformerMappings.target);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, target);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetPropertyMapperTransformerMappings {\n");
    
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    target: ").append(toIndentedString(target)).append("\n");
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

