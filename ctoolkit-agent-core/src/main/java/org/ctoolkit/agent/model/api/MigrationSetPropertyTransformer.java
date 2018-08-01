package org.ctoolkit.agent.model.api;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-07-31T19:00:18.485Z")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MigrationSetPropertyMapperTransformer.class, name = "mapper"),
})
public class MigrationSetPropertyTransformer   {
}

