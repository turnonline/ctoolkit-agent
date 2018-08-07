package org.ctoolkit.agent.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-07-31T19:00:18.485Z")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MigrationSetPropertyMapperTransformer.class, name = "mapper"),
        @JsonSubTypes.Type(value = MigrationSetPropertyDateTransformer.class, name = "date"),
})
public class MigrationSetPropertyTransformer implements Serializable {

    private String type = null;

    /**
     * Transformer type
     **/
    public MigrationSetPropertyTransformer type(String type) {
        this.type = type;
        return this;
    }


  @ApiModelProperty(value = "Transformer type")
  @JsonProperty("type")
  public String getType() {
        return type;
    }
  public void setType(String type) {
        this.type = type;
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
    return Objects.equals(type, migrationSetPropertyTransformer.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetPropertyTransformer {\n");

    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

