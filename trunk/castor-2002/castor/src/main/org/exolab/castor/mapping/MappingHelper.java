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


package org.exolab.castor.mapping;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.exolab.castor.mapping.xml.Mapping;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.xml.ContainerMapping;
import org.exolab.castor.mapping.xml.Include;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.util.Messages;


/**
 * Assists in the construction of descriptors. Can be used as a mapping
 * resolver to the engine. Engines will implement their own mapping
 * scheme typically by extending this class.
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public abstract class MappingHelper
    implements MappingResolver
{


    /**
     * All class descriptors added so far, keyed by Java class.
     */
    private Hashtable  _clsDescs = new Hashtable();


    /**
     * Log writer to report progress. May be null.
     */
    private PrintWriter _logWriter;


    /**
     * The class loader to use.
     */
    private ClassLoader _loader;


    /**
     * The entity resolver to use. May be null.
     */
    private DTDResolver _resolver = new DTDResolver();


    /**
     * Constructs a new mapping helper. This constructor is used by
     * a derived class.
     *
     * @param loader The class loader to use, null for the default
     */
    protected MappingHelper( ClassLoader loader )
    {
        if ( loader == null )
            loader = getClass().getClassLoader();
        _loader = loader;
    }


    public ClassDesc getDescriptor( Class type )
    {
        return (ClassDesc) _clsDescs.get( type );
    }


    public Enumeration listDescriptors()
    {
        return _clsDescs.elements();
    }


    public Enumeration listObjectTypes()
    {
        return _clsDescs.keys();
    }


    /**
     * Returns the log writer. If not null, errors and other messages
     * should be directed to the log writer.
     */
    protected PrintWriter getLogWriter()
    {
        return _logWriter;
    }


    /**
     * Sets the log writer. If not null, errors and other messages
     * will be directed to that log writer.
     *
     * @param logWriter The log writer to use
     */
    public void setLogWriter( PrintWriter logWriter )
    {
        if ( logWriter != null )
            _logWriter = logWriter;
    }


    /**
     * Sets the entity resolver. The entity resolver can be used to
     * resolve external entities and cached documents that are used
     * from within mapping files.
     *
     * @param resolver The entity resolver to use
     */
    public void setEntityResolver( EntityResolver resolver )
    {
        if ( resolver != null )
            _resolver = new DTDResolver( resolver );
    }


    /**
     * Sets the base URL for the mapping and related files. If the base
     * URL is known, files can be included using relative names. Any URL
     * can be passed, if the URL can serve as a base URL it will be used.
     *
     * @param url The base URL
     */
    public void setBaseURL( String url )
    {
        try {
            _resolver.setBaseURL( new URL( url ) );
        } catch ( MalformedURLException except ) { }
    }


    /**
     * Loads the mapping from the specified URL. If an entity resolver
     * was specified, will use the entity resolver to resolve the URL.
     * This method is also used to load mappings referenced from another
     * mapping or configuration file.
     *
     * @param url The URL of the mapping file
     * @throws IOException An error occured when reading the mapping
     *  file
     * @throws MappingException The mapping file is invalid
     */
    public void loadMapping( String url )
        throws IOException, MappingException
    {
        InputSource source;

        try {
            source = _resolver.resolveEntity( null, url );
            if ( source == null )
                source = new InputSource( url );
            if ( _logWriter != null )
                _logWriter.println( Messages.format( "mapping.loadingFrom", url ) );
            loadMapping( source );
        } catch ( SAXException except ) {
            loadMapping( new InputSource( url ) );
        }
    }


    /**
     * Loads the mapping from the specified input source. This method
     * loads the mapping objects from the specified input source and
     * calls {@link #loadMapping(Mapping)}.
     *
     * @param source The input source
     * @throws IOException An error occured when reading the mapping
     *  file
     * @throws MappingException The mapping file is invalid
     */
    public void loadMapping( InputSource source )
        throws IOException, MappingException
    {
        Unmarshaller unm;
        
        unm = new Unmarshaller( Mapping.class );
        try {
            unm.setEntityResolver( _resolver );
            if ( _logWriter != null )
                unm.setLogWriter( _logWriter );
            loadMapping( (Mapping) unm.unmarshal( source ) );
        } catch ( MappingException except ) {
            throw except;
        } catch ( MarshalException except ) {
            throw new MappingException( except );
        } catch ( Exception except ) {
            throw new MappingException( except );
        }
    }


    /**
     * Loads the mapping from the specified mapping object. Calls {@link
     * #createDescriptor} to create each descriptor and {@link
     * #addDescriptor} to store it. Also loads all the included mapping
     * files.
     *
     * @param mapping The mapping information
     * @throws MappingException The mapping file is invalid
     */
    protected void loadMapping( Mapping mapping )
        throws MappingException
    {
        Enumeration   enum;

        // Load all the included mapping files first.
        enum = mapping.enumerateIncludes();
        while ( enum.hasMoreElements() ) {
            Include       include;

            include = (Include) enum.nextElement();
            try {
                loadMapping( include.getHref() );
            } catch ( IOException except ) {
                throw new MappingException( except );
            }
        }
        
        // Load the mapping for all the classes.
        enum = mapping.enumerateClassMapping();
        while ( enum.hasMoreElements() ) {
            ClassMapping  clsMap;
            ClassDesc     clsDesc;

            clsMap = (ClassMapping) enum.nextElement();
            clsDesc = createDescriptor( clsMap );
            addDescriptor( clsDesc );
            // If the return value is NoDescriptor then the derived
            // class was not successful in constructing a descriptor.
            if ( clsDesc == ClassDesc.NoDescriptor && _logWriter != null )
                _logWriter.println( Messages.format( "mapping.ignoringMapping", clsMap.getClassName() ) );
        }

        // Iterate through all the types. In the first step, resolve
        // all relations in the descriptors and keep all the descriptors.
        Vector types;
        
        types = new Vector();
        enum = _clsDescs.keys();
        while ( enum.hasMoreElements() )
            types.add( enum.nextElement() );
        for ( int i = 0 ; i < types.size() ; ++i ) {
            ClassDesc clsDesc;
            Class     type;
            
            type = (Class) types.elementAt( i );
            clsDesc = getDescriptor( type );
            if ( clsDesc != ClassDesc.NoDescriptor )
                resolveRelations( clsDesc );
        }
        // In the second stage, clean all the descriptors, removing those
        // for which there is insufficient mapping.
        for ( int i = 0 ; i < types.size() ; ++i ) {
            ClassDesc clsDesc;
            Class     type;
            
            type = (Class) types.elementAt( i );
            clsDesc = getDescriptor( type );
            if ( clsDesc == ClassDesc.NoDescriptor )
                _clsDescs.remove( type );
        }
    }


    /**
     * Adds a class descriptor. Will throw a mapping exception if a
     * descriptor for this class already exists.
     *
     * @param clsDesc The descriptor to add
     * @throws MappingException A descriptor for this class already
     *  exists
     */
    protected void addDescriptor( ClassDesc clsDesc )
        throws MappingException
    {
        if ( _clsDescs.contains( clsDesc.getJavaClass() ) )
            throw new MappingException( "mapping.duplicateDescriptors", clsDesc.getJavaClass().getName() );
        _clsDescs.put( clsDesc.getJavaClass(), clsDesc );
    }


    protected void resolveRelations( ClassDesc clsDesc )
        throws MappingException
    {
        FieldDesc[]    fields;
        Vector         allRels;
        Vector         allFields;
        RelationDesc[] rels;

        allRels = new Vector();
        allFields = new Vector();
        fields = clsDesc.getFields();
        for ( int i = 0 ; i < fields.length ; ++i ) {
            if ( Types.isSimpleType( fields[ i ].getFieldType() ) ) {
                // Simple type -- this is a field
                allFields.add( fields[ i ] );
            } else {
                ClassDesc relClass;

                relClass = getDescriptor( fields[ i ].getFieldType() );
                if ( relClass == null ) {
                    // Not simple type but no class desc -- this field must be serializable
                    if ( Types.isSerializable( fields[ i ].getFieldType() ) )
                        allFields.add( fields[ i ] );
                    else
                        throw new MappingException( "The field " + fields[ i ] +
                                                    " is not a simple type, a relation or a serialzable object" );
                } else if ( relClass == ClassDesc.NoDescriptor ) {
                    // NoDescriptor was found for this field, meaning the
                    // field could not be mapped to this engine.
                    throw new MappingException( "The field " + fields[ i ] +
                                                " requires a class descriptor of type " + fields[ i ].getFieldType().getName() +
                                                " -- no such descriptor could be created for this engine" );
                } else {
                    // This is definitely a relation, no go figure out which type.
                    RelationDesc relDesc;
                    boolean      attached;

                    // One-one relation: related object is pointing back to this object.
                    attached = ( relClass.getIdentity().getFieldType() == clsDesc.getJavaClass() ) ;
                    relDesc = new RelationDesc( relClass, fields[ i ], attached );
                    allRels.add( relDesc );
                }
            }
        }

        if ( fields.length != allFields.size() ) {
            // Update the field and relations of the descriptor
            fields = new FieldDesc[ allFields.size() ];
            allFields.copyInto( fields );
            rels = clsDesc.getRelations();
            for ( int i = 0 ; i < rels.length ; ++i )
                allRels.add( rels[ i ] );
            rels = new RelationDesc[ allRels.size() ];
            allRels.copyInto( rels );
            clsDesc.setFields( fields );
            clsDesc.setRelations( rels );
        }

        ClassDesc idClsDesc;
        FieldDesc identity;

        idClsDesc = getDescriptor( clsDesc.getIdentity().getFieldType() );
        if ( idClsDesc != null )
            clsDesc.setRelatedIdentity( idClsDesc.getIdentity() );
    }


    /**
     * Creates a new descriptor. The class mapping information is used
     * to create a new stock {@link ClassDesc}. Implementations may
     * extend this class to create a more suitable descriptor.
     *
     * @param clsMap The class mapping information
     * @throws MappingException An exception indicating why mapping for
     *  the class cannot be created
     */
    protected ClassDesc createDescriptor( ClassMapping clsMap )
        throws MappingException
    {
        FieldDesc[]      fields;
        FieldDesc        identity;
        Enumeration      enum;
        Class            javaClass;
        ClassDesc        extend;
        ContainerMapping contMaps[];
        Vector           relations;
        
        // Obtain the Java class.
        try {
            javaClass = _loader.loadClass( clsMap.getClassName() );
        } catch ( ClassNotFoundException except ) {
            throw new MappingException( "mapping.classNotFound", clsMap.getClassName() );
        }
        
        // If this class extends another class, need to obtain the extended
        // class and make sure this class indeed extends it.
        if ( clsMap.getExtends() != null ) {
            try {
                extend = getDescriptor( _loader.loadClass( clsMap.getExtends() ) );
                if ( extend == null )
                    throw new MappingException( "mapping.extendsMissing",
                                                clsMap.getExtends(), javaClass.getName() );
                if ( extend == ClassDesc.NoDescriptor )
                    throw new MappingException( "mapping.extendsNoMapping",
                                                clsMap.getExtends(), javaClass.getName() );
            } catch ( ClassNotFoundException except ) {
                throw new MappingException( except );
            }
        } else
            extend = null;
        
        // Get field descriptors first. Note: order must be preserved for fields,
        // but not for relations or container fields. Add all the container fields
        // in there.
        fields = createFieldDescs( javaClass, clsMap.getFieldMapping() );
        contMaps = clsMap.getContainerMapping();
        if ( contMaps != null && contMaps.length > 0 ) {
            FieldDesc[] allFields;

            allFields = new FieldDesc[ fields.length + contMaps.length ];
            for ( int i = 0 ; i < fields.length ; ++i )
                allFields[ i ] = fields[ i ];
            for ( int i = 0 ; i < contMaps.length ; ++i )
                allFields[ i + fields.length ] = createContainerFieldDesc( javaClass, contMaps[ i ] );
            fields = allFields;
        }

        // Make sure there are no two fields with the same name.
        // Crude but effective way of doing this.
        for ( int i = 0 ; i < fields.length ; ++i ) {
            for ( int j = i + 1 ; j < fields.length ; ++j ) {
                if ( fields[ i ].getFieldName().equals( fields[ j ].getFieldName() ) )
                    throw new MappingException( "The field " + fields[ i ].getFieldName() +
                                                " appears twice in the descriptor for " +
                                                javaClass.getName() );
            }
        }
        
        // Obtain the identity field from one of the above fields.
        // The identity field is removed from the list of fields.
        identity = null;
        if ( clsMap.getIdentity() != null ) {
            for ( int i = 0 ; i < fields.length ; ++i ) {
                if ( fields[ i ].getFieldName().equals( clsMap.getIdentity().getFieldRef() ) ) {
                    identity = fields[ i ];
                    identity.setRequired( true );
                    fields[ i ] = fields[ fields.length - 1 ];

                    // Remove identity field from list of fields.
                    FieldDesc[] newFields;

                    newFields = new FieldDesc[ fields.length - 1 ];
                    for ( int j = 0 ; j < fields.length - 1 ; ++j )
                        newFields[ j ] = fields[ j ];
                    fields = newFields;
                    break;
                }
            }
            if ( identity == null )
                throw new MappingException( "mapping.identityMissing", clsMap.getIdentity().getFieldRef(),
                                            javaClass.getName() );
        }

        // Create the class descriptor.
        return new ClassDesc( javaClass, fields, new RelationDesc[ 0 ], identity, extend,
                              AccessMode.getAccessMode( clsMap.getAccessMode() ) );
    }


    /**
     * Create field descriptors. The class mapping information is used
     * to create descriptors for all the fields in the class, except
     * for container fields. Implementations may extend this method to
     * create more suitable descriptors, or create descriptors only for
     * a subset of the fields.
     *
     * @param javaClass The class to which the fields belong
     * @param fieldMaps The field mappings
     * @throws MappingException An exception indicating why mapping for
     *  the class cannot be created
     */
    protected FieldDesc[] createFieldDescs( Class javaClass, FieldMapping[] fieldMaps )
        throws MappingException
    {
        FieldDesc[] fields;
        
        if ( fieldMaps == null || fieldMaps.length == 0 )
            return new FieldDesc[ 0 ];
        fields = new FieldDesc[ fieldMaps.length ];
        for ( int i = 0 ; i < fieldMaps.length ; ++i ) {
            fields[ i ] = createFieldDesc( javaClass, fieldMaps[ i ] );
        }
        return fields;
    }


    /**
     * Create container field descriptor. The contained mapping is used
     * to create a single field descriptor which includes several field
     * descriptors for all contained fields.
     *
     * @param javaClass The class to which the field belongs
     * @param fieldMap The field mapping
     * @throws MappingException An exception indicating why mapping for
     *  the class cannot be created
     */
    protected ContainerFieldDesc createContainerFieldDesc( Class javaClass, ContainerMapping fieldMap )
        throws MappingException
    {
        FieldDesc   container;
        Class       fieldType;
        FieldDesc[] fields;
        
        // If field type supplied in mapping, use it
        if ( fieldMap.getType() != null ) {
            try {
                fieldType = Types.typeFromName( _loader, fieldMap.getType() );
            } catch ( ClassNotFoundException except ) {
                throw new MappingException( "mapping.classNotFound", fieldMap.getType() );
            }
        } else
            fieldType = null;
        
        if ( fieldMap.getGetMethod() != null ||
             fieldMap.getSetMethod() != null ) {
            // Accessors, map field using either methods.
            Method getMethod = null;
            Method setMethod = null;
            
            if ( fieldMap.getGetMethod() != null ) {
                getMethod = findAccessor( javaClass, fieldMap.getGetMethod(), fieldType, true );
                // Use return type for parameter type checking, if known
                fieldType = getMethod.getReturnType();
            }
            if ( fieldMap.getSetMethod() != null ) {
                setMethod = findAccessor( javaClass, fieldMap.getSetMethod(), fieldType, true );
                if ( fieldType == null )
                    fieldType = setMethod.getParameterTypes()[ 0 ];
            }
            container = new FieldDesc( fieldMap.getName(), fieldType, getMethod, setMethod,
                                       fieldMap.getRequired() );
        } else {
            // No accessor, map field directly.
            Field field;
            
            field = findField( javaClass, fieldMap.getName(), fieldType );
            fieldType = field.getType();
            container = new FieldDesc( field, fieldMap.getRequired() );
        }
        
        // Container type must be constructable.
        if ( ! Types.isConstructable( fieldType ) )
            throw new MappingException( "mapping.classNotConstructable", fieldType.getName() );
        // Create descriptors for all the fields.
        fields = createFieldDescs( fieldType, fieldMap.getFieldMapping() );
        return new ContainerFieldDesc( container, fields );
    }
    
    
    /**
     * Creates a single field descriptor. The field mapping is used to
     * create a new stock {@link FieldDesc}. Implementations may
     * extend this class to create a more suitable descriptor.
     *
     * @param javaClass The class to which the field belongs
     * @param fieldMap The field mapping information
     * @return The field descriptor
     * @throws MappingException The field or its accessor methods are not
     *  found, not accessible, not of the specified type, etc
     */
    protected FieldDesc createFieldDesc( Class javaClass, FieldMapping fieldMap )
        throws MappingException
    {
        Class fieldType;
        
        // If field type supplied in mapping, use it
        if ( fieldMap.getType() != null ) {
            try {
                fieldType = Types.typeFromName( _loader, fieldMap.getType() );
            } catch ( ClassNotFoundException except ) {
                throw new MappingException( "mapping.classNotFound", fieldMap.getType() );
            }
        } else
            fieldType = null;
        
        if ( fieldMap.getGetMethod() != null ||
             fieldMap.getSetMethod() != null ) {
            // Accessors, map field using either methods.
            Method getMethod = null;
            Method setMethod = null;
            
            if ( fieldMap.getGetMethod() != null ) {
                getMethod = findAccessor( javaClass, fieldMap.getGetMethod(), fieldType, true );
                // Use return type for parameter type checking, if known
                fieldType = getMethod.getReturnType();
            }
            if ( fieldMap.getSetMethod() != null ) {
                setMethod = findAccessor( javaClass, fieldMap.getGetMethod(), fieldType, true );
                if ( fieldType == null )
                    fieldType = setMethod.getParameterTypes()[ 0 ];
            }
            return new FieldDesc( fieldMap.getName(), fieldType, getMethod, setMethod,
                                  fieldMap.getRequired() );
        } else {
            // No accessor, map field directly.
            Field field;
            
            field = findField( javaClass, fieldMap.getName(), fieldType );
            return new FieldDesc( field, fieldMap.getRequired() );
        }
    }


    /**
     * Returns the named field. Uses reflection to return the named
     * field and check the field type, if specified.
     *
     * @param javaClass The class to which the field belongs
     * @param fieldName The name of the field
     * @param fieldType The type of the field if known, or null
     * @return The field
     * @throws MappingException The field is not accessible or is not of the
     *  specified type
     */
    private Field findField( Class javaClass, String fieldName, Class fieldType )
        throws MappingException
    {
        Field field;
        
        try {
            // Look up the field based on its name, make sure it's only modifier
            // is public. If a type was specified, match the field type.
            field = javaClass.getField( fieldName );
            if ( field.getModifiers() != Modifier.PUBLIC &&
                 field.getModifiers() != ( Modifier.PUBLIC | Modifier.VOLATILE ) )
                throw new MappingException( "mapping.fieldNotAccessible", fieldName, javaClass.getName() );
            if ( fieldType == null )
                fieldType = Types.typeFromPrimitive( field.getType() );
            else if ( fieldType != Types.typeFromPrimitive( field.getType() ) )
                throw new MappingException( "mapping.fieldTypeMismatch", field, fieldType.getName() );
            return field;
        } catch ( NoSuchFieldException except ) {
        } catch ( SecurityException except ) {
        }
        // No such/access to field
        throw new MappingException( "mapping.fieldNotAccessible", fieldName, javaClass.getName() );
    }


    /**
     * Returns the named accessor. Uses reflection to return the named
     * accessor and check the return value or parameter type, if
     * specified.
     *
     * @param javaClass The class to which the field belongs
     * @param methodName The name of the accessor method
     * @param fieldType The type of the field if known, or null
     * @param isGetMethod True if get method, false if set method
     * @return The method
     * @throws MappingException The method is not accessible or is not of the
     *  specified type
     */
    private static Method findAccessor( Class javaClass, String methodName,
                                        Class fieldType, boolean getMethod )
        throws MappingException
    {
        Method   method;
        Method[] methods;
        int      i;
        
        try {
            if ( getMethod ) {
                // Get method: look for the named method or prepend get to the
                // method name. Look up the field and potentially check the
                // return type.
                method = javaClass.getMethod( methodName, new Class[ 0 ] );
                if ( fieldType == null )
                    fieldType = Types.typeFromPrimitive( method.getReturnType() );
                else if ( fieldType != Types.typeFromPrimitive( method.getReturnType() ) )
                    throw new MappingException( "mapping.accessorReturnTypeMismatch",
                                                method, fieldType.getName() );
            } else {
                // Set method: look for the named method or prepend set to the
                // method name. If the field type is know, look up a suitable
                // method. If the fielf type is unknown, lookup the first
                // method with that name and one parameter.
                if ( fieldType != null )
                    method = javaClass.getMethod( methodName, new Class[] { fieldType } );
                else {
                    methods = javaClass.getMethods();
                    method = null;
                    for ( i = 0 ; i < methods.length ; ++i ) {
                        if ( methods[ i ].getName().equals( methodName ) &&
                             methods[ i ].getParameterTypes().length == 1 ) {
                            method = methods[ i ];
                            break;
                        }
                    }
                    if ( method == null )
                        throw new NoSuchMethodException();
                }
            }
            // Make sure method is public and not abstract/static.
            if ( method.getModifiers() != Modifier.PUBLIC &&
                 method.getModifiers() != ( Modifier.PUBLIC | Modifier.SYNCHRONIZED ) )
                throw new MappingException( "mapping.accessorNotAccessible",
                                            methodName, javaClass.getName() );
            return method;
        } catch ( NoSuchMethodException except ) {
        } catch ( SecurityException except ) {
        }
        // No such/access to method
        throw new MappingException( "mapping.accessorNotAccessible",
                                    methodName, javaClass.getName() );
    }


}
