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
 */


package org.exolab.castor.persist;


import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Collection;
import java.util.Vector;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.ObjectDeletedException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.SQLEngine;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.xml.TypeMapping;
import org.exolab.castor.mapping.xml.ManyToMany;
import org.exolab.castor.mapping.loader.MappingLoader;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.mapping.loader.RelationDescriptor;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.persist.spi.CallbackInterceptor;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.util.Messages;



public class FieldMolder {

    public static final short PRIMITIVE = 0;

    public static final short PERSISTANCECAPABLE = 1;

    public static final short ONE_TO_MANY = 2;

    public static final short MANY_TO_MANY = 3;

    private boolean _lazy;

    private boolean _check;

    private boolean _store;

    private boolean _multi;

    private ClassMolder _eMold;

    private ClassMolder _fMold;

    private Class _colClass;

    private String _fType;

    private Class _fClass;

    //private String _manyTable;

    private SQLRelationLoader _manyToManyLoader;

    public String toString() {
        return "FieldMolder: "+_fClass+" inside "+_eMold.getJavaClass();
    }
    public Class getJavaClass() {
        return _fClass; 
    }
    /*
    void setRelationDescriptor( RelationDescriptor rd ) throws MappingException {
        _loader = new SQLRelationLoader( rd, _eMold.getName() );
    }
    public SQLRelationLoader getRelationLoader() {
        return _loader;
    }*/
    public short getFieldType() {
        if ( ! isPersistanceCapable() )
            return PRIMITIVE;

        if ( ! isMulti() )
            return PERSISTANCECAPABLE;

        if ( ! isManyToMany() )
            return ONE_TO_MANY;

        else
            return MANY_TO_MANY;
    }

    public SQLRelationLoader getRelationLoader() {
        return _manyToManyLoader;
    }

    public boolean isStored() {
        return _store;
    }

    public boolean isManyToMany() {
        return _manyToManyLoader != null;
    }

    public boolean isDependent() {
        return _fMold != null && (_fMold.getDepends() == _eMold);
    }

    public boolean isMulti() {
        return _multi;
    }

    public boolean isPersistanceCapable() {
        return _fMold != null;
    }

    public boolean isCheckDirty() {
        return _check;
    }
    public boolean isLazy() {
        return _lazy;
    }
    void setFieldClassMolder( ClassMolder fMold ) {
        _fMold = fMold;
    }    
    public ClassMolder getEnclosingClassMolder() {
        return _eMold;
    }

    public ClassMolder getFieldClassMolder() {
        return _fMold;
    }

    public LockEngine getFieldLockEngine() {
        return (_fMold==null?null:_fMold.getLockEngine());
    }

    public Class getCollectionType() {
        return _colClass;
    }

    public Object getValue( Object object ) {
        Object value;

        try {
            // If field is accessed directly, get it's value, if not
            // need to call get method. It's possible to not have a
            // way to access the field.
            //if ( _handler != null )
            //    value = _handler.getValue( object );
            //else
            if ( _field != null )
                return _field.get( object );
            else if ( _getMethod != null ) {
                // If field has 'has' method, false means field is null
                // and do not attempt to call getValue. Otherwise, 
                if ( _hasMethod != null && ! ( (Boolean) _hasMethod.invoke( object, null ) ).booleanValue() )
                    return null;
                else
                    return _getMethod.invoke( object, null );
            } else
                return null;
        } catch ( IllegalAccessException except ) {
            // This should never happen
            throw new IllegalStateException( Messages.format( "mapping.schemaChangeNoAccess", toString() ) );
        } catch ( InvocationTargetException except ) {
            // This should never happen
            throw new IllegalStateException( Messages.format( "mapping.schemaChangeInvocation",
                                                              toString(), except ) );
        }
    }
    
    public void setValue( Object object, Object value ) {
        // If there is a convertor, apply conversion here.
        System.out.print("FieldMolder.setValue(");
        System.out.print((value!=null?value.getClass().toString():"null")+"/"+value
                +") using ");
        try {
            //if ( _handler != null )
            //    _handler.setValue( object, value );
            //else 
            if ( _field != null ) {
                System.out.println("_field: "+_field+"/"+object);
                _field.set( object, value == null ? _default : value );
            } else if ( _setMethod != null ) {
                System.out.println("_setMethod: "+_setMethod+" "+object);
                if ( value == null && _deleteMethod != null )
                    _deleteMethod.invoke( object, null );
                else {
                    _setMethod.invoke( object, new Object[] { value == null ? _default : value } );
                }
            } else {
                throw new RuntimeException("no method to set value");
            }
            // If the field has no set method, ignore it.
            // If this is a problem, identity it someplace else.
        } catch ( IllegalArgumentException except ) {
            // Graceful way of dealing with unwrapping exception
            if ( value == null )
                throw new IllegalArgumentException( Messages.format( "mapping.typeConversionNull", toString() ) );
            else
                throw new IllegalArgumentException( Messages.format( "mapping.typeConversion",
                                                                     toString(), value.getClass().getName() ) );
        } catch ( IllegalAccessException except ) {
            // This should never happen
            throw new IllegalStateException( Messages.format( "mapping.schemaChangeNoAccess", toString() ) );
        } catch ( InvocationTargetException except ) {
            // This should never happen
            throw new IllegalStateException( Messages.format( "mapping.schemaChangeInvocation",
                                                              toString(), except.getMessage() ) );
        }

    }

    /*
    public void addValue( Object target ) {
    }

    public void removeValue( Object target, Object identity ) {
    }
    */

    // ======================================================
    //  copy from FieldHanlder.java and modified
    // ======================================================

    protected Class getCollectionType( String coll, boolean lazy ) 
            throws MappingException {
        Class type;

        for ( int i=0; i<info.length; i++ ) {
            if ( info[i].name.equals( coll ) ) 
                return info[i].type;
        }
        return null;
        /*
        if ( "collection".equals( coll ) )
            type = ArrayList.class;
        else if ( "vector".equals( coll ) )
            type = Vector.class;
        else 
            throw new MappingException( "Unsupported collection type: " + coll );

        if ( lazy )
            return null;
        else 
            return type;*/
    }

    static class CollectionInfo {
        String name;
        Class type;
        CollectionInfo( String name, Class type ) {
            this.name = name;
            this.type = type;
        }
    }

    static CollectionInfo[] info = 
        { new CollectionInfo( "collection", java.util.Collection.class ),
          new CollectionInfo( "vector", java.util.Vector.class ),
          new CollectionInfo( "hashtable", java.util.Hashtable.class ),
          new CollectionInfo( "set", java.util.Set.class ),
          new CollectionInfo( "map", java.util.Map.class ),
          new CollectionInfo( "array", Object[].class ) };


          //( array | vector | hashtable | collection | set | map )


    /**
     * Creates a single field descriptor. The field mapping is used to
     * create a new stock {@link FieldDescriptor}. Implementations may
     * extend this class to create a more suitable descriptor.
     *
     * @param javaClass The class to which the field belongs
     * @param fieldMap The field mapping information
     * @return The field descriptor
     * @throws MappingException The field or its accessor methods are not
     *  found, not accessible, not of the specified type, etc
     */

    private Field         _field;
    private Method        _getMethod;
    private Method        _setMethod;
    private Method        _hasMethod;
    private Method        _deleteMethod;
    private Method        _createMethod;
    private String        _fieldName;
    private Object        _default;


    public FieldMolder( DatingService ds, ClassMolder eMold, FieldMapping fieldMap, 
            String manyTable, String[] idSQL, int[] idType, String[] relatedIdSQL,
			int[] relatedIdType ) throws MappingException {

        this( ds, eMold, fieldMap );

        _manyToManyLoader = new SQLRelationLoader( manyTable, idSQL, idType, relatedIdSQL, relatedIdType );
    }

    public FieldMolder( DatingService ds, ClassMolder eMold, FieldMapping fieldMap ) 
            throws MappingException {
        try {
            // Set enclosing ClassMolder
            _eMold = eMold;

            // Set isLazy
            _lazy = fieldMap.getLazy();

            if ( fieldMap.getSql() == null 
                    || fieldMap.getSql().getDirty() == null
                    || ! fieldMap.getSql().getDirty().equals("ignore") ) {
                _check = true;
            }


            if ( fieldMap.getSql() == null )
                _store = false;
            else if ( fieldMap.getSql().getName() == null )
                _store = false;
            else if ( fieldMap.getSql().getManyTable() == null ) 
                _store = false;
            else 
                _store = true;

            if ( fieldMap.getCollection() != null )
                _multi = true;
                

            // Set collection type
            if ( fieldMap.getCollection() != null ) {
                _colClass = getCollectionType( fieldMap.getCollection(), _lazy );
                _store = false;
            }
            // Set field name, if it is null, we try to discover it with
            // return type of set/get method.
            _fType = fieldMap.getType();
            
            // Field is in many-to-many relation with enclosing class
            /*
            if ( fieldMap.getSql() != null && fieldMap.getSql().getManyTable() != null ) {
                _manyTable = fieldMap.getSql().getManyTable();
            }*/
            /*
            if ( fieldMap.getManyToMany() != null ) {
                ManyToMany mm = (ManyToMany) fieldMap.getManyToMany();
                TypeMapping[] typeMap = mm.getTypeMapping();
                String relationName = mm.getName();
                String tableName = mm.getMapTo().getTable();
                String type1 = ((ClassMapping)typeMap[0].getType()).getName();
                String sql1  = typeMap[0].getSql();
                String type2 = ((ClassMapping)typeMap[1].getType()).getName();
                String sql2  = typeMap[1].getSql();
                setRelationDescriptor( new RelationDescriptor( relationName, tableName, type1, sql1, type2, sql2 ) );

                _rTable = mm.getMapTo().getTable();

                TypeMapping tm1 = mm.getTypeMapping()[0];
                TypeMapping tm2 = mm.getTypeMapping()[1];
                String sType1 = ((ClassMapping)tm1.getType()).getName();
                String sType2 = ((ClassMapping)tm2.getType()).getName();
                System.out.println(_eMold.getName() + " " + sType1 + " " + sType2 + " " +_fType );
                if ( _eMold.getName().equals( sType1 ) ) {
                    System.out.println("Class type equal type 1");
                    if ( _fType == null ) 
                        _fType = sType2;
                    if ( ! _fType.equals( sType2 ) )
                        throw new MappingException("Relation and field type doesn't match!");
                } else {
                    
                    if ( _fType == null )
                        _fType = sType1;
                    else if ( ! _fType.equals( sType1 ) )
                        throw new MappingException("Relation and field type doesn't match!");
                }
                if ( _colClass == null )
                    throw new MappingException("attribute \"collection\" expected!");
            }*/
           
            Class             javaClass = eMold.getJavaClass();
            Class             fieldType = null;
            String            fieldName;

            if ( fieldMap.getDirect() ) {
                // No accessor, map field directly.
                
                fieldName = fieldMap.getName();
                _field = findField( javaClass, fieldName, _fClass );
                if ( _field == null )
                    throw new MappingException( "mapping.fieldNotAccessible", fieldName, javaClass.getName() );
                if ( _field.getModifiers() != Modifier.PUBLIC && 
                     _field.getModifiers() != ( Modifier.PUBLIC | Modifier.VOLATILE ) )
                    throw new MappingException( "mapping.fieldNotAccessible", _field.getName(),
                                            _field.getDeclaringClass().getName() );
            } else {
                if ( fieldMap.getGetMethod() == null && fieldMap.getSetMethod() == null ) {
                    Class sgClass = _colClass==null? _colClass : _fClass;

                    String name = capitalize( fieldMap.getName() );

                    _getMethod = findAccessor( javaClass, "get" + name, sgClass, true );

                    if ( _getMethod == null )
                        throw new MappingException( "mapping.accessorNotFound",
                                "get" + name, null, javaClass.getName() );

                    if ( _colClass == null && sgClass != _fClass )
                        _fClass = _getMethod.getReturnType();

                    _setMethod = findAccessor( javaClass, "set" + name, sgClass, false );

                    //if ( _setMethod == null )
                    //    throw new MappingException( "mapping.accessorNotFound",
                    //            "set" + name, null, javaClass.getName() );
                } else {
                    // | need modification........
                    // First look up the get accessors
                    if ( fieldMap.getGetMethod() != null ) {
                        _getMethod = findAccessor( javaClass, fieldMap.getGetMethod(), _fClass, true );
                         
                        if ( _getMethod == null )
                            throw new MappingException( "mapping.accessorNotFound",
                                    fieldMap.getGetMethod(), _fClass, javaClass.getName() );

                        if ( _fClass == null )
                            _fClass = _getMethod.getReturnType();
                     }

                     // Second look up the set/add accessor
                     if ( fieldMap.getSetMethod() != null ) {
                         _setMethod = findAccessor( javaClass, fieldMap.getSetMethod(), _fClass, false );
                         if ( _setMethod == null )
                             throw new MappingException( "mapping.accessorNotFound",
                                    fieldMap.getSetMethod(), _fClass, javaClass.getName() );
                         if ( _fClass == null )
                             _fClass = _setMethod.getParameterTypes()[ 0 ];
                     }
                 } 
                             
                 fieldName = fieldMap.getName();
                 if ( fieldName == null )
                   fieldName = ( _getMethod == null ? _setMethod.getName() : _getMethod.getName() );
            }


            // If there is a create method, add it to the field handler
            if ( fieldMap.getCreateMethod() != null ) {
                try {
                    Method method;

                    _createMethod = javaClass.getMethod( fieldMap.getCreateMethod(), null );
                } catch ( Exception except ) {
                    // No such/access to method
                    throw new MappingException( "mapping.createMethodNotFound",
                                                fieldMap.getCreateMethod(), javaClass.getName() );
                }
            } else if ( fieldMap.getName() != null && ! Types.isSimpleType( _fClass ) ) {
                try {
                    Method method;

                    method = javaClass.getMethod( "create" + capitalize( fieldMap.getName() ), null );
                    _createMethod = method;
                } catch ( Exception except ) { }
            }

            // If there is an has/delete method, add them to field handler
            if ( fieldMap.getName() != null ) {
                Method hasMethod = null;
                Method deleteMethod = null;

                try {
                    hasMethod = javaClass.getMethod( "has" + capitalize( fieldMap.getName() ), null );
                    if ( ( hasMethod.getModifiers() & Modifier.PUBLIC ) == 0 ||
                         ( hasMethod.getModifiers() & Modifier.STATIC ) != 0 )
                        hasMethod = null;
                    try {
                        if ( ( hasMethod.getModifiers() & Modifier.PUBLIC ) == 0 ||
                             ( hasMethod.getModifiers() & Modifier.STATIC ) != 0 )
                            deleteMethod = null;
                        deleteMethod = javaClass.getMethod( "delete" + capitalize( fieldMap.getName() ), null );
                    } catch ( Exception except ) { }
                    _hasMethod = hasMethod;
                    _deleteMethod = deleteMethod;
                } catch ( Exception except ) { }
            }

            if ( _field == null && _setMethod == null && _getMethod == null ) {
                throw new MappingException( "_field or _setMethod can't be created" );
            }

            ds.pairFieldClass( this, _fType );
        } catch ( NullPointerException e ) {
            e.printStackTrace();
            throw new MappingException("Unexpected Null pointer!\n"+e);
        }
    }


    /*
    protected TypeInfo getTypeInfo( Class fieldType, CollectionHandler colHandler, FieldMapping fieldMap )
        throws MappingException
    {
        return new TypeInfo( Types.typeFromPrimitive( fieldType ), null, null,
                             fieldMap.getRequired(), null, colHandler );
    }*/


    /**
     * Returns the named field. Uses reflection to return the named
     * field and check the field type, if specified.
     *
     * @param javaClass The class to which the field belongs
     * @param fieldName The name of the field
     * @param fieldType The type of the field if known, or null
     * @return The field, null if not found
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
            else if ( Types.typeFromPrimitive( fieldType ) != Types.typeFromPrimitive( field.getType() ) )
                throw new MappingException( "mapping.fieldTypeMismatch", field, fieldType.getName() );
            return field;
        } catch ( NoSuchFieldException except ) {
        } catch ( SecurityException except ) {
        }
        return null;
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
     * @return The method, null if not found
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
                else if ( ! Types.typeFromPrimitive( fieldType ).isAssignableFrom( Types.typeFromPrimitive( method.getReturnType() ) ) )
                    throw new MappingException( "mapping.accessorReturnTypeMismatch",
                                                method, fieldType.getName() );
            } else {
                // Set method: look for the named method or prepend set to the
                // method name. If the field type is know, look up a suitable
                // method. If the fielf type is unknown, lookup the first
                // method with that name and one parameter.
                if ( fieldType != null ) {
                    try {
                        method = javaClass.getMethod( methodName, new Class[] { fieldType } );
                    } catch ( Exception except ) {
                        method = javaClass.getMethod( methodName, new Class[] { Types.typeFromPrimitive( fieldType ) } );
                    }
                } else {
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
                        return null;
                }
            }
            // Make sure method is public and not abstract/static.
            if ( ( method.getModifiers() & Modifier.PUBLIC ) == 0 ||
                 ( method.getModifiers() & Modifier.STATIC ) != 0 ) 
                throw new MappingException( "mapping.accessorNotAccessible",
                                            methodName, javaClass.getName() );
            return method;
        } catch ( MappingException except ) {
            throw except;
        } catch ( Exception except ) {
        }
        return null;
    }


    /*
    Class resolve( String className ) throws ClassNotFoundException {
        return Types.typeFromName( loader, className );
    }*/

    private String capitalize( String name )
    {
        char first;

        first = name.charAt( 0 );
        if ( Character.isUpperCase( first  ) )
            return name;
        return Character.toUpperCase( first ) + name.substring( 1 );
    }
}
