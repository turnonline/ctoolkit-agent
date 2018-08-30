package org.ctoolkit.agent.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-08-30T05:34:23.887Z")
public class MigrationSetPropertyPatternTransformer
        extends MigrationSetPropertyTransformer  {
  
  private String pattern = null;

  /**
   * Pattern used to transform target value
   **/
  public MigrationSetPropertyPatternTransformer pattern( String pattern) {
    this.pattern = pattern;
    return this;
  }

  
  @ApiModelProperty(value = "Pattern used to transform target value")
  @JsonProperty("pattern")
  public String getPattern() {
    return pattern;
  }
  public void setPattern(String pattern) {
    this.pattern = pattern;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSetPropertyPatternTransformer migrationSetPropertyPatternTransformer = (MigrationSetPropertyPatternTransformer ) o;
    return Objects.equals(pattern, migrationSetPropertyPatternTransformer.pattern);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pattern);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetPropertyReplacerTransformer {\n");
    
    sb.append("    pattern: ").append(toIndentedString(pattern)).append("\n");
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

