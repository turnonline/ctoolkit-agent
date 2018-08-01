package org.ctoolkit.agent.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-07-31T19:00:18.485Z")
public class MigrationSetProperty   {
  
  private String sourceProperty = null;
  private String targetType = null;
  private String targetProperty = null;
  private String targetValue = null;
  private String targetMultiplicity = null;
  private MigrationSetPropertyRuleSet ruleSet = null;
  private List<MigrationSetPropertyTransformer> transformers = new ArrayList<MigrationSetPropertyTransformer>();

  /**
   * Source property name
   **/
  public MigrationSetProperty sourceProperty(String sourceProperty) {
    this.sourceProperty = sourceProperty;
    return this;
  }

  
  @ApiModelProperty(value = "Source property name")
  @JsonProperty("sourceProperty")
  public String getSourceProperty() {
    return sourceProperty;
  }
  public void setSourceProperty(String sourceProperty) {
    this.sourceProperty = sourceProperty;
  }

  /**
   * Data type name in target agent (for instance 'varchar' in sql database or 'text' in elasticsearch)
   **/
  public MigrationSetProperty targetType(String targetType) {
    this.targetType = targetType;
    return this;
  }

  
  @ApiModelProperty(value = "Data type name in target agent (for instance 'varchar' in sql database or 'text' in elasticsearch)")
  @JsonProperty("targetType")
  public String getTargetType() {
    return targetType;
  }
  public void setTargetType(String targetType) {
    this.targetType = targetType;
  }

  /**
   * Target property name
   **/
  public MigrationSetProperty targetProperty(String targetProperty) {
    this.targetProperty = targetProperty;
    return this;
  }

  
  @ApiModelProperty(value = "Target property name")
  @JsonProperty("targetProperty")
  public String getTargetProperty() {
    return targetProperty;
  }
  public void setTargetProperty(String targetProperty) {
    this.targetProperty = targetProperty;
  }

  /**
   * Target value (set if you want to override value taken from source agent)
   **/
  public MigrationSetProperty targetValue(String targetValue) {
    this.targetValue = targetValue;
    return this;
  }

  
  @ApiModelProperty(value = "Target value (set if you want to override value taken from source agent)")
  @JsonProperty("targetValue")
  public String getTargetValue() {
    return targetValue;
  }
  public void setTargetValue(String targetValue) {
    this.targetValue = targetValue;
  }

  /**
   * Property multiplicity (default is single)
   **/
  public MigrationSetProperty targetMultiplicity(String targetMultiplicity) {
    this.targetMultiplicity = targetMultiplicity;
    return this;
  }

  
  @ApiModelProperty(value = "Property multiplicity (default is single)")
  @JsonProperty("targetMultiplicity")
  public String getTargetMultiplicity() {
    return targetMultiplicity;
  }
  public void setTargetMultiplicity(String targetMultiplicity) {
    this.targetMultiplicity = targetMultiplicity;
  }

  /**
   **/
  public MigrationSetProperty ruleSet(MigrationSetPropertyRuleSet ruleSet) {
    this.ruleSet = ruleSet;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("ruleSet")
  public MigrationSetPropertyRuleSet getRuleSet() {
    return ruleSet;
  }
  public void setRuleSet(MigrationSetPropertyRuleSet ruleSet) {
    this.ruleSet = ruleSet;
  }

  /**
   **/
  public MigrationSetProperty transformers(List<MigrationSetPropertyTransformer> transformers) {
    this.transformers = transformers;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("transformers")
  public List<MigrationSetPropertyTransformer> getTransformers() {
    return transformers;
  }
  public void setTransformers(List<MigrationSetPropertyTransformer> transformers) {
    this.transformers = transformers;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSetProperty migrationSetProperty = (MigrationSetProperty) o;
    return Objects.equals(sourceProperty, migrationSetProperty.sourceProperty) &&
        Objects.equals(targetType, migrationSetProperty.targetType) &&
        Objects.equals(targetProperty, migrationSetProperty.targetProperty) &&
        Objects.equals(targetValue, migrationSetProperty.targetValue) &&
        Objects.equals(targetMultiplicity, migrationSetProperty.targetMultiplicity) &&
        Objects.equals(ruleSet, migrationSetProperty.ruleSet) &&
        Objects.equals(transformers, migrationSetProperty.transformers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sourceProperty, targetType, targetProperty, targetValue, targetMultiplicity, ruleSet, transformers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetProperty {\n");
    
    sb.append("    sourceProperty: ").append(toIndentedString(sourceProperty)).append("\n");
    sb.append("    targetType: ").append(toIndentedString(targetType)).append("\n");
    sb.append("    targetProperty: ").append(toIndentedString(targetProperty)).append("\n");
    sb.append("    targetValue: ").append(toIndentedString(targetValue)).append("\n");
    sb.append("    targetMultiplicity: ").append(toIndentedString(targetMultiplicity)).append("\n");
    sb.append("    ruleSet: ").append(toIndentedString(ruleSet)).append("\n");
    sb.append("    transformers: ").append(toIndentedString(transformers)).append("\n");
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

