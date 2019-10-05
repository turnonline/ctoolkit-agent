/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ctoolkit.agent.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2019-10-05T08:46:04.321Z")
public class MigrationSetPropertyDateTransformer extends MigrationSetPropertyTransformer {
  
  private String format = null;
  private Boolean epoch = false;
  private String timeZone = "GMT";

  /**
   * Date format which will be used to transfer Date value into formatted String or formatted String to Date - https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html.
   **/
  public MigrationSetPropertyDateTransformer format(String format) {
    this.format = format;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Date format which will be used to transfer Date value into formatted String or formatted String to Date - https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html.")
  @JsonProperty("format")
  public String getFormat() {
    return format;
  }
  public void setFormat(String format) {
    this.format = format;
  }

  /**
   * If you wan to transform into date into miliseconds since January 1, 1970, 00:00:00 GMT use ‘epoch’=true. Default is false.
   **/
  public MigrationSetPropertyDateTransformer epoch(Boolean epoch) {
    this.epoch = epoch;
    return this;
  }

  
  @ApiModelProperty(value = "If you wan to transform into date into miliseconds since January 1, 1970, 00:00:00 GMT use ‘epoch’=true. Default is false.")
  @JsonProperty("epoch")
  public Boolean getEpoch() {
    return epoch;
  }
  public void setEpoch(Boolean epoch) {
    this.epoch = epoch;
  }

  /**
   * Time zone used in simple date formatter. Default is 'GMT'
   **/
  public MigrationSetPropertyDateTransformer timeZone(String timeZone) {
    this.timeZone = timeZone;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Time zone used in simple date formatter. Default is 'GMT'")
  @JsonProperty("timeZone")
  public String getTimeZone() {
    return timeZone;
  }
  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
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
    return Objects.equals(format, migrationSetPropertyDateTransformer.format) &&
        Objects.equals(epoch, migrationSetPropertyDateTransformer.epoch) &&
        Objects.equals(timeZone, migrationSetPropertyDateTransformer.timeZone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(format, epoch, timeZone);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MigrationSetPropertyDateTransformer {\n");
    
    sb.append("    format: ").append(toIndentedString(format)).append("\n");
    sb.append("    epoch: ").append(toIndentedString(epoch)).append("\n");
    sb.append("    timeZone: ").append(toIndentedString(timeZone)).append("\n");
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

