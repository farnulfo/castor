/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.jdo.engine;


import java.util.Vector;
import org.exolab.castor.mapping.Types;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.ClassDesc;
import org.exolab.castor.mapping.FieldDesc;
import org.exolab.castor.mapping.ContainerFieldDesc;
import org.exolab.castor.mapping.MappingHelper;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;


/**
 *
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class JDOMappingHelper
    extends MappingHelper
{


    protected ClassDesc createDescriptor( ClassLoader loader, ClassMapping clsMap )
        throws MappingException
    {
	ClassDesc   clsDesc;
	FieldDesc[] fields;
	FieldDesc   identity;
	Vector      jdoFields;

	if ( clsMap.getSqlTable() == null )
	    throw new IllegalArgumentException( "Class mapping does not include SQL information" );

	clsDesc = super.createDescriptor( loader, clsMap );
	// JDO descriptor must include an identity field.
	identity = clsDesc.getIdentity();
	if ( identity == null )
	    throw new MappingException( "mapping.noIdentity", clsDesc.getJavaClass().getName() );
	// Accumulate all the fields, flatten them, remove the identity
	// field, and use that to create the descriptor.
	jdoFields = new Vector();
	fields = clsDesc.getFields();
	for ( int i = 0 ; i < fields.length ; ++i ) {
	    if ( ! fields[ i ].getFieldName().equals( identity.getFieldName() ) ) {
		if ( fields[ i ] instanceof ContainerFieldDesc ) {
		    FieldDesc[] cFields;
		    
		    cFields = ( (ContainerFieldDesc) fields[ i ] ).getContainedFields();
		    for ( int j = 0 ; j < cFields.length ; ++j )
			jdoFields.add( cFields[ j ] );
		} else {
		    jdoFields.add( fields[ i ] );
		}
	    }
	}
	
	JDOFieldDesc[] array;
	array = new JDOFieldDesc[ jdoFields.size() ];
	jdoFields.copyInto( array );
	return new JDOClassDesc( clsDesc.getJavaClass(), clsMap.getSqlTable().getTableName(),
				 array, identity, (JDOClassDesc) clsDesc.getExtends() );
    }


    protected FieldDesc createFieldDesc( ClassLoader loader, Class javaClass, FieldMapping fieldMap )
	throws MappingException
    {
	FieldDesc  fieldDesc;
	Class      sqlType;
	String     sqlName;

	if ( fieldMap.getSqlInfo() == null )
	    throw new IllegalArgumentException( "Field mapping does not include SQL information" );

	fieldDesc = super.createFieldDesc( loader, javaClass, fieldMap );
	if ( fieldMap.getSqlInfo().getType() == null )
	    sqlType = fieldDesc.getFieldType();
	else
	    sqlType = SQLTypes.typeFromName( fieldMap.getSqlInfo().getType() );
	if ( fieldMap.getSqlInfo().getName() == null )
	    sqlName = SQLTypes.javaToSqlName( fieldDesc.getFieldName() );
	else
	    sqlName = fieldMap.getSqlInfo().getName();
	return new JDOFieldDesc( fieldDesc, sqlName, sqlType, fieldMap.getSqlInfo().getDirty().equals( "check" ) );
    }


}



