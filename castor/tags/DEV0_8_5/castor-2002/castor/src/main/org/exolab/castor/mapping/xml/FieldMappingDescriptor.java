/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.8.3 (2000502)</a>,
 * using an XML Schema.
 * $Id
 */

package org.exolab.castor.mapping.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.FieldValidator;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;

/**
 * 
 * @version $Revision$ $Date$
**/
public class FieldMappingDescriptor implements org.exolab.castor.xml.XMLClassDescriptor {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    private org.exolab.castor.xml.XMLFieldDescriptor[] elements;

    private org.exolab.castor.xml.XMLFieldDescriptor[] attributes;

    private org.exolab.castor.xml.util.XMLFieldDescriptorImpl contentDesc;

    private java.lang.String nsPrefix;

    private java.lang.String nsURI;

    private java.lang.String xmlName;

    private org.exolab.castor.xml.XMLFieldDescriptor identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public FieldMappingDescriptor() {
        xmlName = "field";
        XMLFieldDescriptorImpl  desc           = null;
        XMLFieldHandler         handler        = null;
        FieldValidator          fieldValidator = null;
        //-- initialize attribute descriptors
        
        attributes = new XMLFieldDescriptorImpl[8];
        //-- _collection
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_collection", "collection", NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getCollection();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setCollection( (java.lang.String) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return null;
            }
        } );
        desc.setHandler(handler);
        attributes[0] = desc;
        
        //-- validation code for: _collection
        fieldValidator = new FieldValidator();
        fieldValidator.setValidator(new StringValidator());
        desc.setValidator(fieldValidator);
        
        //-- _setMethod
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_setMethod", "set-method", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getSetMethod();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setSetMethod( (java.lang.String) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new java.lang.String();
            }
        } );
        desc.setHandler(handler);
        attributes[1] = desc;
        
        //-- validation code for: _setMethod
        fieldValidator = new FieldValidator();
        fieldValidator.setValidator(new NameValidator(NameValidator.NMTOKEN));
        desc.setValidator(fieldValidator);
        
        //-- _createMethod
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_createMethod", "create-method", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getCreateMethod();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setCreateMethod( (java.lang.String) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new java.lang.String();
            }
        } );
        desc.setHandler(handler);
        attributes[2] = desc;
        
        //-- validation code for: _createMethod
        fieldValidator = new FieldValidator();
        fieldValidator.setValidator(new NameValidator(NameValidator.NMTOKEN));
        desc.setValidator(fieldValidator);
        
        //-- _getMethod
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_getMethod", "get-method", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getGetMethod();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setGetMethod( (java.lang.String) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new java.lang.String();
            }
        } );
        desc.setHandler(handler);
        attributes[3] = desc;
        
        //-- validation code for: _getMethod
        fieldValidator = new FieldValidator();
        fieldValidator.setValidator(new NameValidator(NameValidator.NMTOKEN));
        desc.setValidator(fieldValidator);
        
        //-- _transient
        desc = new XMLFieldDescriptorImpl(boolean.class, "_transient", "transient", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return new Boolean(target.getTransient());
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setTransient( ((Boolean)value).booleanValue());
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return null;
            }
        } );
        desc.setHandler(handler);
        attributes[4] = desc;
        
        //-- validation code for: _transient
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- _type
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_type", "type", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getType();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setType( (java.lang.String) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new java.lang.String();
            }
        } );
        desc.setHandler(handler);
        attributes[5] = desc;
        
        //-- validation code for: _type
        fieldValidator = new FieldValidator();
        fieldValidator.setValidator(new NameValidator(NameValidator.NMTOKEN));
        desc.setValidator(fieldValidator);
        
        //-- _required
        desc = new XMLFieldDescriptorImpl(boolean.class, "_required", "required", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return new Boolean(target.getRequired());
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setRequired( ((Boolean)value).booleanValue());
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return null;
            }
        } );
        desc.setHandler(handler);
        attributes[6] = desc;
        
        //-- validation code for: _required
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- _name
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_name", "name", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getName();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setName( (java.lang.String) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new java.lang.String();
            }
        } );
        desc.setHandler(handler);
        desc.setRequired(true);
        attributes[7] = desc;
        
        //-- validation code for: _name
        fieldValidator = new FieldValidator();
        fieldValidator.setMinOccurs(1);
        fieldValidator.setValidator(new NameValidator(NameValidator.NMTOKEN));
        desc.setValidator(fieldValidator);
        
        //-- initialize element descriptors
        
        elements = new XMLFieldDescriptorImpl[4];
        //-- _description
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_description", "description", NodeType.Element);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getDescription();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setDescription( (java.lang.String) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return null;
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(false);
        elements[0] = desc;
        
        //-- validation code for: _description
        fieldValidator = new FieldValidator();
        fieldValidator.setValidator(new StringValidator());
        desc.setValidator(fieldValidator);
        
        //-- _sql
        desc = new XMLFieldDescriptorImpl(Sql.class, "_sql", "sql", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getSql();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setSql( (Sql) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new Sql();
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(false);
        elements[1] = desc;
        
        //-- validation code for: _sql
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- _xml
        desc = new XMLFieldDescriptorImpl(Xml.class, "_xml", "xml", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getXml();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setXml( (Xml) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new Xml();
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(false);
        elements[2] = desc;
        
        //-- validation code for: _xml
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- _ldap
        desc = new XMLFieldDescriptorImpl(Ldap.class, "_ldap", "ldap", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getLdap();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setLdap( (Ldap) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new Ldap();
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(false);
        elements[3] = desc;
        
        //-- validation code for: _ldap
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
    } //-- org.exolab.castor.mapping.xml.FieldMappingDescriptor()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public org.exolab.castor.mapping.AccessMode getAccessMode() {
        return null;
    } //-- org.exolab.castor.mapping.AccessMode getAccessMode() 

    /**
    **/
    public org.exolab.castor.xml.XMLFieldDescriptor[] getAttributeDescriptors() {
        return attributes;
    } //-- org.exolab.castor.xml.XMLFieldDescriptor[] getAttributeDescriptors() 

    /**
    **/
    public org.exolab.castor.xml.XMLFieldDescriptor getContentDescriptor() {
        return contentDesc;
    } //-- org.exolab.castor.xml.XMLFieldDescriptor getContentDescriptor() 

    /**
    **/
    public org.exolab.castor.xml.XMLFieldDescriptor[] getElementDescriptors() {
        return elements;
    } //-- org.exolab.castor.xml.XMLFieldDescriptor[] getElementDescriptors() 

    /**
    **/
    public org.exolab.castor.mapping.ClassDescriptor getExtends() {
        return null;
    } //-- org.exolab.castor.mapping.ClassDescriptor getExtends() 

    /**
    **/
    public org.exolab.castor.mapping.FieldDescriptor[] getFields() {
        int size = attributes.length + elements.length;
        if (contentDesc != null) ++size;
        
        FieldDescriptor[] fields = new FieldDescriptor[size];
        int c = 0;
        for (int i = 0; i < attributes.length; i++)
            fields[c++] = attributes[i];
        
        for (int i = 0; i < elements.length; i++)
            fields[c++] = elements[i];
        
        if (contentDesc != null) fields[c] = contentDesc;
        
        return fields;
    } //-- org.exolab.castor.mapping.FieldDescriptor[] getFields() 

    /**
    **/
    public org.exolab.castor.mapping.FieldDescriptor getIdentity() {
        return identity;
    } //-- org.exolab.castor.mapping.FieldDescriptor getIdentity() 

    /**
    **/
    public java.lang.Class getJavaClass() {
        return org.exolab.castor.mapping.xml.FieldMapping.class;
    } //-- java.lang.Class getJavaClass() 

    /**
    **/
    public java.lang.String getNameSpacePrefix() {
        return nsPrefix;
    } //-- java.lang.String getNameSpacePrefix() 

    /**
    **/
    public java.lang.String getNameSpaceURI() {
        return nsURI;
    } //-- java.lang.String getNameSpaceURI() 

    /**
    **/
    public org.exolab.castor.xml.TypeValidator getValidator() {
        return null;
    } //-- org.exolab.castor.xml.TypeValidator getValidator() 

    /**
    **/
    public java.lang.String getXMLName() {
        return xmlName;
    } //-- java.lang.String getXMLName() 

}
