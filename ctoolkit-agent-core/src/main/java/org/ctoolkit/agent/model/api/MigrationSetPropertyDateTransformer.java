package org.ctoolkit.agent.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-08-06T05:00:11.129Z")
public class MigrationSetPropertyDateTransformer extends MigrationSetPropertyTransformer {
  
  private String format = null;

  /**
   * Date format which will be used to transfer Date value int formatted string - https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html
   **/
  public MigrationSetPropertyDateTransformer format(String format) {
    this.format = format;
    return this;
  }

  
  @ApiModelProperty(value = "Date format which will be used to transfer Date value int formatted string - https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html.  If you wan to transform into date into miliseconds since January 1, 1970, 00:00:00 GMT use 'miliseconds' as format string")
  @JsonProperty("format")
  public String getFormat() {
    return format;
  }
  public void setFormat(String format) {
    this.format = format;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSetPropertyDateTransformer migrationSetPropertyDateTransformer = (MigrationSetPropertyDateTransformer) o;
    return Objects.equals(format, migrationSetPropertyDateTransformer.format);
  }

  @Override
  public int hashCode() {
    return Objects.hash(format);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetPropertyDateTransformer {\n");
    
    sb.append("    format: ").append(toIndentedString(format)).append("\n");
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

