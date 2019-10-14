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

package org.ctoolkit.agent.annotation;

import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
@Singleton
public class LoggableInterceptor
        implements MethodInterceptor<Object, Object>
{
    @Override
    public Object intercept( MethodInvocationContext<Object, Object> ctx )
    {
        Object retVal;
        Logger log = LoggerFactory.getLogger( ctx.getDeclaringType().getClass() );
        Date start = new Date();

        Loggable annotation = ctx.getDeclaringType().getAnnotation( Loggable.class );

        try
        {
            if ( log.isDebugEnabled() && annotation.value() )
            {
                log.debug( printBefore( ctx ) );
            }

            retVal = ctx.proceed();

            if ( log.isDebugEnabled() && annotation.value() )
            {
                long duration = new Date().getTime() - start.getTime();
                log.debug( printAfter( ctx, retVal, duration ) );
            }
        }
        catch ( Throwable throwable )
        {
            if ( log.isErrorEnabled() && annotation.value() )
            {
                log.error( printError( ctx, throwable ), throwable );
            }

            throw throwable;
        }

        return retVal;
    }

    private String printBefore( MethodInvocationContext<Object, Object> ctx )
    {
        return "IN -> " + methodInSignature( ctx );
    }

    private String printAfter( MethodInvocationContext<Object, Object> ctx, Object returnObject, long duration )
    {
        return "OUT [" + duration + "ms] -> " + methodInSignature( ctx ) + " >>> " + methodOutSignature( ctx, returnObject );
    }

    private String printError( MethodInvocationContext<Object, Object> ctx, Throwable throwable )
    {
        return "ERROR -> Error occur during executing method: " + methodInSignature( ctx ) + " | REASON > " + throwable.getMessage();
    }

    private String methodInSignature( MethodInvocationContext<Object, Object> ctx )
    {
        Method method = ctx.getTargetMethod();

        String beanName = ctx.getDeclaringType().getName();
        String methodName = method.getName();

        return beanName + "." + methodName + "(" + ctx.getParameters() + ")";
    }

    private Object methodOutSignature( MethodInvocationContext<Object, Object> ctx, Object retValue )
    {
        Method method = ctx.getTargetMethod();

        boolean isVoid = method.getReturnType().getName().equals( Void.class.getSimpleName().toLowerCase() );

        if ( isVoid )
        {
            return "[void]";
        }

        return retValue;
    }
}
