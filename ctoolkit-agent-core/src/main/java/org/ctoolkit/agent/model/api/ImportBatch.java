package org.ctoolkit.agent.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-07-31T19:00:18.485Z")
public class ImportBatch   {
  
  private List<PipelineOption> pipelineOptions = new ArrayList<PipelineOption>();
  private List<ImportSet> importSets = new ArrayList<ImportSet>();

  /**
   * Array of pipeline options
   **/
  public ImportBatch pipelineOptions(List<PipelineOption> pipelineOptions) {
    this.pipelineOptions = pipelineOptions;
    return this;
  }

  
  @ApiModelProperty(value = "Array of pipeline options")
  @JsonProperty("pipelineOptions")
  public List<PipelineOption> getPipelineOptions() {
    return pipelineOptions;
  }
  public void setPipelineOptions(List<PipelineOption> pipelineOptions) {
    this.pipelineOptions = pipelineOptions;
  }

  /**
   **/
  public ImportBatch importSets(List<ImportSet> importSets) {
    this.importSets = importSets;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("importSets")
  public List<ImportSet> getImportSets() {
    return importSets;
  }
  public void setImportSets(List<ImportSet> importSets) {
    this.importSets = importSets;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImportBatch importBatch = (ImportBatch) o;
    return Objects.equals(pipelineOptions, importBatch.pipelineOptions) &&
        Objects.equals(importSets, importBatch.importSets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pipelineOptions, importSets);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImportBatch {\n");
    
    sb.append("    pipelineOptions: ").append(toIndentedString(pipelineOptions)).append("\n");
    sb.append("    importSets: ").append(toIndentedString(importSets)).append("\n");
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

