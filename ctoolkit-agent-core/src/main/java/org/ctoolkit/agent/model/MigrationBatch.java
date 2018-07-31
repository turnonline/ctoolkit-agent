package org.ctoolkit.agent.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-07-31T19:00:18.485Z")
public class MigrationBatch   {
  
  private String targetAgentUrl = null;
  private List<PipelineOption> pipelineOptions = new ArrayList<PipelineOption>();
  private List<MigrationSet> migrationSets = new ArrayList<MigrationSet>();

  /**
   * Target agent url
   **/
  public MigrationBatch targetAgentUrl(String targetAgentUrl) {
    this.targetAgentUrl = targetAgentUrl;
    return this;
  }

  
  @ApiModelProperty(value = "Target agent url")
  @JsonProperty("targetAgentUrl")
  public String getTargetAgentUrl() {
    return targetAgentUrl;
  }
  public void setTargetAgentUrl(String targetAgentUrl) {
    this.targetAgentUrl = targetAgentUrl;
  }

  /**
   * Array of pipeline options
   **/
  public MigrationBatch pipelineOptions(List<PipelineOption> pipelineOptions) {
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
   * Array of migration sets
   **/
  public MigrationBatch migrationSets(List<MigrationSet> migrationSets) {
    this.migrationSets = migrationSets;
    return this;
  }

  
  @ApiModelProperty(value = "Array of migration sets")
  @JsonProperty("migrationSets")
  public List<MigrationSet> getMigrationSets() {
    return migrationSets;
  }
  public void setMigrationSets(List<MigrationSet> migrationSets) {
    this.migrationSets = migrationSets;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationBatch migrationBatch = (MigrationBatch) o;
    return Objects.equals(targetAgentUrl, migrationBatch.targetAgentUrl) &&
        Objects.equals(pipelineOptions, migrationBatch.pipelineOptions) &&
        Objects.equals(migrationSets, migrationBatch.migrationSets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(targetAgentUrl, pipelineOptions, migrationSets);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationBatch {\n");
    
    sb.append("    targetAgentUrl: ").append(toIndentedString(targetAgentUrl)).append("\n");
    sb.append("    pipelineOptions: ").append(toIndentedString(pipelineOptions)).append("\n");
    sb.append("    migrationSets: ").append(toIndentedString(migrationSets)).append("\n");
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

