package org.ctoolkit.agent.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-08-29T18:41:11.119Z")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MigrationSetPropertyMapperTransformer.class, name = "mapper"),
        @JsonSubTypes.Type(value = MigrationSetPropertyDateTransformer.class, name = "date"),
        @JsonSubTypes.Type(value = MigrationSetPropertyBlobTransformer.class, name = "blob"),
        @JsonSubTypes.Type(value = MigrationSetPropertyPatternTransformer.class, name = "pattern"),
        @JsonSubTypes.Type(value = MigrationSetPropertyEncodingTransformer.class, name = "encoding"),
})
public class MigrationSetPropertyTransformer implements Serializable {
  
  private String phase = "pre-convert";

  /**
   * Phase in which transformer should be applied
   **/
  public MigrationSetPropertyTransformer phase(String phase) {
    this.phase = phase;
    return this;
  }

  
  @ApiModelProperty(value = "Phase in which transformer should be applied")
  @JsonProperty("phase")
  public String getPhase() {
    return phase;
  }
  public void setPhase(String phase) {
    this.phase = phase;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSetPropertyTransformer migrationSetPropertyTransformer = (MigrationSetPropertyTransformer) o;
    return Objects.equals(phase, migrationSetPropertyTransformer.phase);
  }

  @Override
  public int hashCode() {
    return Objects.hash(phase);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetPropertyTransformer {\n");
    
    sb.append("    phase: ").append(toIndentedString(phase)).append("\n");
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

