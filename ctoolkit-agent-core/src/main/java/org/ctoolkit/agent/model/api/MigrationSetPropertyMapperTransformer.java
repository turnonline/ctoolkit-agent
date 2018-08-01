package org.ctoolkit.agent.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-07-31T19:00:18.485Z")
public class MigrationSetPropertyMapperTransformer extends MigrationSetPropertyTransformer  {
  
  private List<MigrationSetPropertyMapperTransformerMappings> mappings = new ArrayList<MigrationSetPropertyMapperTransformerMappings>();

  /**
   **/
  public MigrationSetPropertyMapperTransformer mappings(List<MigrationSetPropertyMapperTransformerMappings> mappings) {
    this.mappings = mappings;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("mappings")
  public List<MigrationSetPropertyMapperTransformerMappings> getMappings() {
    return mappings;
  }
  public void setMappings(List<MigrationSetPropertyMapperTransformerMappings> mappings) {
    this.mappings = mappings;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSetPropertyMapperTransformer migrationSetPropertyMapperTransformer = (MigrationSetPropertyMapperTransformer) o;
    return Objects.equals(mappings, migrationSetPropertyMapperTransformer.mappings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mappings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetPropertyMapperTransformer {\n");
    
    sb.append("    mappings: ").append(toIndentedString(mappings)).append("\n");
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

