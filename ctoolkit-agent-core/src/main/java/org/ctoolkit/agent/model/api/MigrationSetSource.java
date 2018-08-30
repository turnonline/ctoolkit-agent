package org.ctoolkit.agent.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-08-29T20:13:57.204Z")
public class MigrationSetSource implements Serializable {
  
  private String namespace = null;
  private String kind = null;
  private String idPattern = "{target.namespace}:{target.kind}:{source.id}";
  private Boolean encodeId = true;

  /**
   * Source namespace (for elasticsearch it is ‘index’, for sql it is ‘schema’)
   **/
  public MigrationSetSource namespace(String namespace) {
    this.namespace = namespace;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Source namespace (for elasticsearch it is ‘index’, for sql it is ‘schema’)")
  @JsonProperty("namespace")
  public String getNamespace() {
    return namespace;
  }
  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  /**
   * Source kind name (for elasticsearch it is ‘document’, for sql it is ‘table’)
   **/
  public MigrationSetSource kind(String kind) {
    this.kind = kind;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Source kind name (for elasticsearch it is ‘document’, for sql it is ‘table’)")
  @JsonProperty("kind")
  public String getKind() {
    return kind;
  }
  public void setKind(String kind) {
    this.kind = kind;
  }

  /**
   * Pattern used to create id in target agent. Result of this pattern can be encoded into base64 string and it will be used as a primary id of target kind.
   **/
  public MigrationSetSource idPattern(String idPattern) {
    this.idPattern = idPattern;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Pattern used to create id in target agent. Result of this pattern can be encoded into base64 string and it will be used as a primary id of target kind.")
  @JsonProperty("idPattern")
  public String getIdPattern() {
    return idPattern;
  }
  public void setIdPattern(String idPattern) {
    this.idPattern = idPattern;
  }

  /**
   * Flag if id should be encoded into base64 string
   **/
  public MigrationSetSource encodeId(Boolean encodeId) {
    this.encodeId = encodeId;
    return this;
  }

  
  @ApiModelProperty(value = "Flag if id should be encoded into base64 string")
  @JsonProperty("encodeId")
  public Boolean getEncodeId() {
    return encodeId;
  }
  public void setEncodeId(Boolean encodeId) {
    this.encodeId = encodeId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MigrationSetSource migrationSetSource = (MigrationSetSource) o;
    return Objects.equals(namespace, migrationSetSource.namespace) &&
        Objects.equals(kind, migrationSetSource.kind) &&
        Objects.equals(idPattern, migrationSetSource.idPattern) &&
        Objects.equals(encodeId, migrationSetSource.encodeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(namespace, kind, idPattern, encodeId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetSource {\n");
    
    sb.append("    namespace: ").append(toIndentedString(namespace)).append("\n");
    sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
    sb.append("    idPattern: ").append(toIndentedString(idPattern)).append("\n");
    sb.append("    encodeId: ").append(toIndentedString(encodeId)).append("\n");
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

