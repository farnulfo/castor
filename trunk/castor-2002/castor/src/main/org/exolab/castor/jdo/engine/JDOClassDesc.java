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


import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.ClassDesc;
import org.exolab.castor.mapping.FieldDesc;
import org.exolab.castor.mapping.ContainerFieldDesc;
import org.exolab.castor.mapping.RelationDesc;
import org.exolab.castor.mapping.AccessMode;


/**
 * JDO class descriptors. Extends {@link ClassDesc} to include the
 * table name and other SQL-related information. All fields are of
 * type {@link JDOFieldDesc}, identity field is not included in the
 * returned field list, and contained fields are flattened out for
 * efficiency (thus all fields are directly accessible).
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class JDOClassDesc
    extends ClassDesc
{


    /**
     * True if any of the fields is marked for dirty checking.
     */
    private boolean _dirtyCheck;


    /**
     * The name of the SQL table.
     */
    private String  _tableName;


    /**
     * Constructs a new JDO descriptor. The field list must consist only of
     * JDO field, must not include the identity field, all contained fields
     * must be represented as {@link JDOContainedFieldDesc}. Order of fields
     * is not important. The identity field must be of type {@link JDOFieldDesc}
     * or {@link ContainerFieldDesc}.
     * 
     * @param clsDesc The class descriptor
     * @param tableName The SQL table name
     * @param fields Field descriptors
     * @throws MappingException Invalid mapping information
     */
    public JDOClassDesc( ClassDesc clsDesc, String tableName )
        throws MappingException
    {
        super( clsDesc.getJavaClass(), clsDesc.getFields(), clsDesc.getRelations(),
               clsDesc.getIdentity(), clsDesc.getExtends(), clsDesc.getAccessMode() );

        if ( tableName == null )
            throw new IllegalArgumentException( "Argument 'tableName' is null" );
        _tableName = tableName;
        if ( getIdentity() == null )
            throw new MappingException( "mapping.noIdentity", getJavaClass().getName() );
        if ( ! ( getIdentity() instanceof JDOFieldDesc ) &&
             ! ( getIdentity() instanceof ContainerFieldDesc ) )
            throw new IllegalArgumentException( "Identity field must be of type JDOFieldDesc or ContainerFieldDesc" );
        if ( getExtends() != null && ! ( getExtends() instanceof JDOClassDesc ) )
            throw new IllegalArgumentException( "Extended class does not have a JDO descriptor" );

        FieldDesc[] fields;

        fields = getFields();
        for ( int i = 0 ; i < fields.length ; ++i ) {
            if ( fields[ i ] instanceof JDOFieldDesc && 
                 ( (JDOFieldDesc) fields[ i ] ).isDirtyCheck() )
                _dirtyCheck = true;
        }
    }


    /**
     * Constructor used by derived classes.
     */
    protected JDOClassDesc( JDOClassDesc clsDesc )
        throws MappingException
    {
        super( clsDesc );
        _tableName = clsDesc._tableName;
        _dirtyCheck = clsDesc._dirtyCheck;
    }


    /**
     * Returns true if at least one field requires dirty checking.
     *
     * @return True if at least one field requires dirty checking
     */
    public boolean isDirtyCheck()
    {
        return _dirtyCheck;
    }


    /**
     * Returns the table name to which this object maps.
     *
     * @return Table name
     */
    public String getTableName()
    {
        return _tableName;
    }


    /**
     * Mutator method can only be used by {@link JDOMappingHelper}.
     */
    final void setJDOFields( JDOFieldDesc[] fields )
    {
        setFields( fields );
        _dirtyCheck = false;
        for ( int i = 0 ; i < fields.length ; ++i ) {
            if ( fields[ i ].isDirtyCheck() )
                _dirtyCheck = true;
        }
    }


    public String toString()
    {
        return super.toString() + " AS " + _tableName;
    }


}


