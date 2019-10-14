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

package biz.turnonline.ecosystem.service.beam.pipeline;

import biz.turnonline.ecosystem.model.Agent;
import biz.turnonline.ecosystem.model.Export;
import biz.turnonline.ecosystem.model.api.ImportSet;
import biz.turnonline.ecosystem.model.api.MigrationSet;
import biz.turnonline.ecosystem.model.api.MigrationSetProperty;
import biz.turnonline.ecosystem.model.api.MigrationSetSource;
import biz.turnonline.ecosystem.model.api.MigrationSetTarget;
import biz.turnonline.ecosystem.service.beam.options.MigrationPipelineOptions;
import biz.turnonline.ecosystem.service.connector.ConnectorFacade;
import biz.turnonline.ecosystem.service.converter.BaseConverterRegistrat;
import biz.turnonline.ecosystem.service.converter.MongoConverterRegistrat;
import biz.turnonline.ecosystem.service.enricher.EnricherExecutor;
import biz.turnonline.ecosystem.service.rule.HierarchicalRuleSetResolver;
import biz.turnonline.ecosystem.service.rule.RuleSetResolver;
import biz.turnonline.ecosystem.service.transformer.TransformerExecutor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@RunWith( MockitoJUnitRunner.class )
public class PipelineServiceBeanTest
{
    @Mock
    private ConnectorFacade connectorFacade;

    @Mock
    private PipelineFactory pipelineFactory;

    @Spy
    private Map<Agent, BaseConverterRegistrat> registrats = new HashMap<>();

    @Spy
    private RuleSetResolver ruleSetResolver = new HierarchicalRuleSetResolver();

    @Spy
    private EnricherExecutor enricherExecutor = new EnricherExecutor();

    @Spy
    private TransformerExecutor transformerExecutor = new TransformerExecutor();

    @Mock
    private MigrationPipelineOptions migrationPipelineOptions;

    @InjectMocks
    private PipelineServiceBean service;

    @Before
    public void setUp() throws Exception
    {
        registrats.put( Agent.MONGO, new MongoConverterRegistrat() );
    }

    @Test
    public void migrateBatch()
    {
        when( migrationPipelineOptions.getTargetAgent() ).thenReturn( Agent.MONGO );

        MigrationSet migrationSet = createMigrationSet();
        migrationSet.getProperties().add( createSimple() );
        migrationSet.getProperties().add( createList() );
        migrationSet.getProperties().add( createEmbedded() );
        migrationSet.getProperties().add( createListOfEmbeddedObjects() );

        List<Export> exportList = createMigrationContext();

        List<ImportSet> importSetList = service.transform( migrationSet, exportList );

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        System.out.println( "MigrationSet: " + gson.toJson( migrationSet ) );
        System.out.println( "ImportSet list: " + gson.toJson( importSetList ) );
    }

    // -- private helpers

    private MigrationSet createMigrationSet()
    {
        MigrationSet migrationSet = new MigrationSet();

        MigrationSetSource source = new MigrationSetSource();
        migrationSet.setSource( source );

        MigrationSetTarget target = new MigrationSetTarget();
        target.setNamespace( "partner" );
        target.setKind( "physicalPerson" );
        migrationSet.setTarget( target );

        return migrationSet;
    }

    private MigrationSetProperty createSimple()
    {
        MigrationSetProperty name = new MigrationSetProperty();
        name.setSourceProperty( "name" );
        name.setTargetProperty( "firstName" );
        name.setTargetType( "string" );

        return name;
    }

    private MigrationSetProperty createEmbedded()
    {
        List<MigrationSetProperty> addressChildes = new ArrayList<>();

        MigrationSetProperty address = new MigrationSetProperty();
        address.setTargetProperty( "address" );
        address.setTargetType( "object" );
        address.setTargetValue( addressChildes );

        MigrationSetProperty street = new MigrationSetProperty();
        street.setSourceProperty( "streetLine" );
        street.setTargetProperty( "street" );
        street.setTargetType( "string" );
        addressChildes.add( street );

        MigrationSetProperty city = new MigrationSetProperty();
        city.setSourceProperty( "town" );
        city.setTargetProperty( "city" );
        city.setTargetType( "string" );
        addressChildes.add( city );

        MigrationSetProperty country = new MigrationSetProperty();
        country.setTargetProperty( "country" );
        country.setTargetType( "string" );
        country.setTargetValue( "SK" );
        addressChildes.add( country );

        return address;
    }

    private MigrationSetProperty createList()
    {
        List<MigrationSetProperty> codesChildes = new ArrayList<>();

        MigrationSetProperty address = new MigrationSetProperty();
        address.setTargetProperty( "codes" );
        address.setTargetType( "list" );
        address.setTargetValue( codesChildes );

        MigrationSetProperty codes = new MigrationSetProperty();
        codes.setSourceProperty( "discount[*].code" );
        codes.setTargetType( "string" );
        codesChildes.add( codes );

        return address;
    }

    private MigrationSetProperty createListOfEmbeddedObjects()
    {
        List<MigrationSetProperty> birthNumberChildes = new ArrayList<>();

        MigrationSetProperty birthNumber = new MigrationSetProperty();
        birthNumber.setSourceProperty( "birthNo" );
        birthNumber.setTargetProperty( "value" );
        birthNumber.setTargetType( "string" );
        birthNumberChildes.add( birthNumber );

        MigrationSetProperty birthNumberType = new MigrationSetProperty();
        birthNumberType.setTargetProperty( "type" );
        birthNumberType.setTargetType( "string" );
        birthNumberType.setTargetValue( "BIRTH_NUMBER" );
        birthNumberChildes.add( birthNumberType );

        MigrationSetProperty identification = new MigrationSetProperty();
        identification.setTargetProperty( "identification" );
        identification.setTargetType( "object" );
        identification.setTargetValue( birthNumberChildes );

        MigrationSetProperty identifications = new MigrationSetProperty();
        identifications.setTargetProperty( "identifications" );
        identifications.setTargetType( "list" );
        identifications.setTargetValue( Collections.singletonList( identification ) );

        return identifications;
    }

    private List<Export> createMigrationContext()
    {
        List<Export> exportList = new ArrayList<>();

        Export export = new Export();
        export.put( "name", "John" );
        export.put( "streetLine", "Long street 1" );
        export.put( "town", "New York" );
        export.put( "birthNo", "8103220987" );
        export.put( "discount[0].code", "LKOF" );
        export.put( "discount[1].code", "PPRE" );
        exportList.add( export );

        return exportList;
    }
}