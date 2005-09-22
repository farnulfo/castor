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
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999-2004(C) Intalio, Inc. All Rights Reserved.
 *
 * This file was originally developed by Keith Visco during the
 * course of employment at Intalio Inc.
 * All portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) 2005 Keith Visco. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.xml;


import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MapItem;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.loader.CollectionHandlers;
import org.exolab.castor.mapping.loader.MappingLoader;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;
import org.exolab.castor.mapping.loader.TypeInfo;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.xml.BindXml;
import org.exolab.castor.mapping.xml.MapTo;
import org.exolab.castor.mapping.xml.Property;
import org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType;
import org.exolab.castor.mapping.xml.types.CollectionType;
import org.exolab.castor.util.LocalConfiguration;

import org.exolab.castor.xml.handlers.ContainerFieldHandler;
import org.exolab.castor.xml.handlers.ToStringFieldHandler;
import org.exolab.castor.xml.util.ClassDescriptorResolverImpl;
import org.exolab.castor.xml.util.ContainerElement;
import org.exolab.castor.xml.util.XMLClassDescriptorImpl;
import org.exolab.castor.xml.util.XMLClassDescriptorAdapter;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;
import org.exolab.castor.xml.validators.NameValidator;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * An XML implementation of mapping helper. Creates XML class
 * descriptors from the mapping file.
 *
 * @author <a href="keith AT kvisco DOT com">Keith Visco</a>
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class XMLMappingLoader
    extends MappingLoader
{


    /**
     * The default xml prefix used on certain 
     * attributes such as xml:lang, xml:base, etc.
     */
    private static final String XML_PREFIX = "xml:";
    

    /**
     * Empty array of class types used for reflection
    **/
    private static final Class[] EMPTY_ARGS = new Class[0];

    /**
     * The NCName Schema type
    **/
    private static final String NCNAME = "NCName";

    /**
     * The string argument for the valueOf method, used
     * for introspection.
    **/
    private static final Class[] STRING_ARG = { String.class };

    /**
     * Factory method name for type-safe enumerations. This
     * is primarily for allowing users to map classes that
     * were created by Castor's SourceGenerator.
    **/
    private static final String VALUE_OF = "valueOf";


    /**
     * A reference to the internal ClassDescriptorResolver
     */
    private ClassDescriptorResolverImpl _cdResolver;

    /**
     * naming conventions
     */
    private XMLNaming _naming = null;

    /**
     * The default NodeType for primitives
     */
    private NodeType _primitiveNodeType = null;


    /**
     * Creates a new XMLMappingLoader
     */
    public XMLMappingLoader( ClassLoader loader, PrintWriter logWriter )
        throws MappingException
    {
        super( loader, logWriter );
        
        LocalConfiguration config = LocalConfiguration.getInstance();
        
        _primitiveNodeType = config.getPrimitiveNodeType();
        _naming            = config.getXMLNaming();

    } //-- XMLMappingLoader

    private void createResolver() {
        _cdResolver = new ClassDescriptorResolverImpl();
        _cdResolver.setIntrospection(false);
        _cdResolver.setLoadPackageMappings(false);
    }
    
    protected void resolveRelations( ClassDescriptor clsDesc )
        throws MappingException
    {
        FieldDescriptor[] fields;

        fields = clsDesc.getFields();
        for ( int i = 0 ; i < fields.length ; ++i ) {
            if (fields[i].getClassDescriptor() != null) continue;
            ClassDescriptor   relDesc;
            
            relDesc = getDescriptor( fields[ i ].getFieldType() );
            if ( relDesc == NoDescriptor ) {
                // XXX Error message should come here
            }
            else if ( relDesc != null &&
                      relDesc instanceof XMLClassDescriptor &&
                      fields[ i ] instanceof XMLFieldDescriptorImpl )
            {
		        ( (XMLFieldDescriptorImpl) fields[ i ] ).setClassDescriptor( (XMLClassDescriptor) relDesc );

		        //-- removed kvisco 20010814
                // ( (XMLFieldDescriptorImpl) fields[ i ] ).setNodeType( NodeType.Element );
            }
        }
        if ( clsDesc instanceof XMLClassDescriptorImpl )
            ( (XMLClassDescriptorImpl) clsDesc ).sortDescriptors();
    }


    protected ClassDescriptor createDescriptor( ClassMapping clsMap )
        throws MappingException
    {
        ClassDescriptor clsDesc;
        String          xmlName;

        if (clsMap.getAutoComplete()) {
            if ((clsMap.getMapTo() == null) &&
                (clsMap.getFieldMappingCount() == 0) &&
                (clsMap.getIdentityCount() == 0))
            {
                //-- if we make it here we simply try
                //-- to load a compiled mapping
                if (_cdResolver == null) {
                    createResolver();
                }
                try {
                    clsDesc = _cdResolver.resolve(clsMap.getName());
                    if (clsDesc != null) 
                        return clsDesc;
                }
                catch(ResolverException rx) {}
                
            }
        }
        
        // Use super class to create class descriptor. Field descriptors will be
        // generated only for supported fields, see createFieldDesc later on.
        clsDesc = super.createDescriptor( clsMap );
        MapTo mapTo = clsMap.getMapTo();
        if (( mapTo == null) || (mapTo.getXml() == null)) {
            String clsName = clsDesc.getJavaClass().getName();
            int idx = clsName.lastIndexOf('.');
            if (idx >= 0) {
                clsName = clsName.substring(idx+1);
            }
            xmlName = _naming.toXMLName( clsName );
        }
        else {
            xmlName = clsMap.getMapTo().getXml();

        }

        XMLClassDescriptorImpl xmlClassDesc
            = new XMLClassDescriptorAdapter( clsDesc, xmlName, _primitiveNodeType );

        if (clsMap.getAutoComplete()) {

            XMLClassDescriptor referenceDesc = null;
            
            Class type = xmlClassDesc.getJavaClass();
            
            //-- check compiled descriptors 
            if (_cdResolver == null) {
                createResolver();
            }
            try {
                referenceDesc = _cdResolver.resolve(type);
            }
            catch(ResolverException rx) {
                throw new MappingException(rx);
            }
            
            if (referenceDesc == null) {
                Introspector introspector = new Introspector();
                try {
                    referenceDesc = introspector.generateClassDescriptor(type);
                    if (clsMap.getExtends() != null) {
                        //-- clear parent from introspected descriptor since
                        //-- a mapping was provided in the mapping file
                        ((XMLClassDescriptorImpl)referenceDesc).setExtends(null);
                    }
                } catch (MarshalException mx) {
                    String error = "unable to introspect class '" + 
                        type.getName() + "' for auto-complete: ";
                    throw new MappingException(error + mx.getMessage());
                }
            }

            //-- check for identity
            String identity = "";
            if (clsMap.getIdentityCount() > 0)
                identity = clsMap.getIdentity(0);

            
            FieldDescriptor[] fields = xmlClassDesc.getFields();

            // Attributes
            XMLFieldDescriptor[] introFields = referenceDesc.getAttributeDescriptors();
            for (int i = 0; i<introFields.length; ++i) {
                if (!isMatchFieldName(fields, introFields[i].getFieldName())) {
                    // If there is no field with this name, we can add it
                    if (introFields[i].getFieldName().equals(identity)) {
                        xmlClassDesc.setIdentity(introFields[i]);
                    }
                    else {
                        xmlClassDesc.addFieldDescriptor(introFields[i]);
                    }
                }
            }

            // Elements
            introFields = referenceDesc.getElementDescriptors();
            for (int i = 0; i<introFields.length; ++i) {
                if (!isMatchFieldName(fields, introFields[i].getFieldName())) {
                    // If there is no field with this name, we can add it
                    if (introFields[i].getFieldName().equals(identity)) {
                        xmlClassDesc.setIdentity(introFields[i]);
                    }
                    else {
                        xmlClassDesc.addFieldDescriptor(introFields[i]);
                    }
                }
            }

            // Content
            XMLFieldDescriptor field = referenceDesc.getContentDescriptor();
            if (field!= null)
                if (!isMatchFieldName(fields, field.getFieldName()))
                    // If there is no field with this name, we can add
                    xmlClassDesc.addFieldDescriptor(field);


        } //-- End of auto-complete


        //-- copy ns-uri + ns-prefix
        if (mapTo != null) {
            xmlClassDesc.setNameSpacePrefix(mapTo.getNsPrefix());
            xmlClassDesc.setNameSpaceURI(mapTo.getNsUri());
        }
        return xmlClassDesc;
    } //-- createDescriptor


    /**
     * Match if a field named <code>fieldName</code> is in fields
     */
    private boolean isMatchFieldName(FieldDescriptor[] fields, String fieldName) {
        for (int i=0; i< fields.length; ++i)
            if (fields[i].getFieldName().equals(fieldName))
                return true;

        return false;
    } //-- method: isMatchFieldName


    protected FieldDescriptor createFieldDesc( Class javaClass, FieldMapping fieldMap )
        throws MappingException
    {
        
        FieldDescriptor        fieldDesc;
        CollectionType         colType  = fieldMap.getCollection();
        String                 xmlName  = null;
        NodeType               nodeType = null;
        String                 match    = null;
        XMLFieldDescriptorImpl xmlDesc;
        boolean                isReference = false;
        boolean                isXMLTransient = false;

        //-- handle special case for HashMap/Hashtable
        if ((fieldMap.getType() == null) && (colType != null)) {
            if ((colType == CollectionType.HASHTABLE) ||
                (colType == CollectionType.MAP)) 
            {
                fieldMap.setType(MapItem.class.getName());
            }
        }

        // Create an XML field descriptor
        fieldDesc = super.createFieldDesc( javaClass, fieldMap );

        BindXml xml = fieldMap.getBindXml();

        boolean deriveNameByClass = false;

        if (xml != null) {
            //-- xml name
            xmlName = xml.getName();

            //-- node type
            if ( xml.getNode() != null )
                nodeType = NodeType.getNodeType( xml.getNode().toString() );

            //-- matches
            match = xml.getMatches();

            //-- reference
            isReference = xml.getReference();

            //-- XML transient
            isXMLTransient = xml.getTransient();

            //-- autonaming
            BindXmlAutoNamingType autoName = xml.getAutoNaming();
            if (autoName != null) {
                deriveNameByClass = (autoName == BindXmlAutoNamingType.DERIVEBYCLASS);
            }

        }
        
        //-- transient
        //-- XXXX -> if it's transient we probably shouldn't do all
        //-- XXXX -> the unecessary work
        isXMLTransient = isXMLTransient || fieldDesc.isTransient();
        
        //-- 

        //-- handle QName for xmlName
        String namespace = null;
        if ((xmlName != null) && (xmlName.length() > 0)){
            if (xmlName.charAt(0) == '{') {
                int idx = xmlName.indexOf('}');
                if (idx < 0) {
                    throw new MappingException("Invalid QName: " + xmlName);
                }
                namespace = xmlName.substring(1, idx);
                xmlName = xmlName.substring(idx+1);
            }
            else if (xmlName.startsWith(XML_PREFIX)) {
                namespace = Namespaces.XML_NAMESPACE;
                xmlName = xmlName.substring(4);
            }
        }

        if (nodeType == null) {
            if (isPrimitive(javaClass))
                nodeType = _primitiveNodeType;
            else
                nodeType = NodeType.Element;
        }

        //-- Create XML name if necessary. Note if name is to be derived
        //-- by class..we just make sure we set the name to null...
        //-- the Marshaller does this during runtime. This allows
        //-- Collections to be handled properly.
        if ((!deriveNameByClass) && ((xmlName == null) && (match == null)))
        {
            xmlName = _naming.toXMLName( fieldDesc.getFieldName() );
            match = xmlName + ' ' + fieldDesc.getFieldName();
        }

        xmlDesc = new XMLFieldDescriptorImpl( fieldDesc, xmlName, nodeType, _primitiveNodeType );

        //-- transient?
        xmlDesc.setTransient(isXMLTransient);

        //--set a default fieldValidator
        xmlDesc.setValidator(new FieldValidator());
        
        //-- enable use parent namespace if explicit one doesn't exist
        xmlDesc.setUseParentsNamespace(true);

        //-- If deriveNameByClass we need to reset the name to
        //-- null because XMLFieldDescriptorImpl tries to be smart
        //-- and automatically creates the name.
        if (deriveNameByClass) {
            xmlDesc.setXMLName(null);
        }

        //-- namespace
        if (namespace != null) {
            xmlDesc.setNameSpaceURI(namespace);
        }

        //-- matches
        if (match != null) {
            xmlDesc.setMatches(match);
            //-- special fix for xml-name since XMLFieldDescriptorImpl
            //-- will create a default name based off the field name
            if (xmlName == null) xmlDesc.setXMLName(null);
        }

        //-- reference
        xmlDesc.setReference(isReference);

        xmlDesc.setContainer(fieldMap.getContainer());

        if (xml != null) {
            
            //-- has class descriptor for type specified
            if (xml.getClassMapping() != null) {
                ClassDescriptor cd = createDescriptor(xml.getClassMapping());
                xmlDesc.setClassDescriptor((XMLClassDescriptor)cd);
            }
            
            //-- has location path?
            if (xml.getLocation() != null) {
                xmlDesc.setLocationPath(xml.getLocation());
            }
            //is the value type needs specific handling
            //such as QName or NCName support?
            String xmlType = xml.getType();
            xmlDesc.setSchemaType(xmlType);
            xmlDesc.setQNamePrefix(xml.getQNamePrefix());
            TypeValidator validator = null;
            if (NCNAME.equals(xmlType)) {
                validator = new NameValidator(NameValidator.NCNAME);
                xmlDesc.setValidator(new FieldValidator(validator));
            }
            
            //-- special properties?
            Property[] props = xml.getProperty();
            if ((props != null) && (props.length > 0)) {
                for (int pIdx = 0; pIdx < props.length; pIdx++) {
                    Property prop = props[pIdx];
                    xmlDesc.setProperty(prop.getName(), prop.getValue());
                }
            }
        }

        //-- Get collection type
        if (colType == null) {
            //-- just in case user forgot to use collection="..."
            //-- in the mapping file
            Class type = fieldDesc.getFieldType();
            if (CollectionHandlers.hasHandler(type)) {
                String typeName = CollectionHandlers.getCollectionName(type);
                colType = CollectionType.valueOf(typeName);
            }
        }
        
        //-- isMapped item
        if (colType != null) {    
            if ((colType == CollectionType.HASHTABLE) ||
                (colType == CollectionType.MAP))
            {
                //-- Make sure user is not using an addMethod
                //-- before setting the mapped field to true.
                String methodName = fieldMap.getSetMethod();
                if (methodName != null) {
                    if (!methodName.startsWith("add")) {
                        xmlDesc.setMapped(true);
                    }
                }
                else xmlDesc.setMapped(true);
            }

            
            //-- special NodeType.Namespace handling
            //-- prevent FieldHandlerImpl from using CollectionHandler
            //-- during calls to #getValue
            if ((nodeType == NodeType.Namespace) || (xmlDesc.isMapped())) {
                Object handler = xmlDesc.getHandler();
                if (handler instanceof FieldHandlerImpl) {
                    FieldHandlerImpl handlerImpl = (FieldHandlerImpl)handler;
                    handlerImpl.setConvertFrom(new IdentityConvertor());
                }
            }
            //-- wrap collection in element?
            if (nodeType == NodeType.Element) {
                if (fieldMap.hasContainer() && (!fieldMap.getContainer())) {
                    xmlDesc = wrapCollection(xmlDesc);
                }
            }
        }
        
        //-- is Type-Safe Enumeration?
        //-- This is not very clean, we should have a way
        //-- to specify something is a type-safe enumeration
        //-- without having to guess.
        else if ((!isReference) && (!isXMLTransient)) {
            Class fieldType = xmlDesc.getFieldType();
            if (!isPrimitive(fieldType)) {
                //-- make sure no default constructor
                Constructor cons = null;
                try {
                    cons = fieldType.getConstructor(EMPTY_ARGS);
                    if (!Modifier.isPublic(cons.getModifiers())) {
                        cons = null;
                    }
                }
                catch(NoSuchMethodException nsmx) {
                    //-- Do nothing
                }
                try {
                    if (cons == null) {
                        //-- make sure a valueOf factory method
                        //-- exists and no user specified handler exists
                        Method method = fieldType.getMethod(VALUE_OF, STRING_ARG);
                        Class returnType = method.getReturnType();
                        if ((returnType != null) && fieldType.isAssignableFrom(returnType)) {
                            if (fieldMap.getHandler() == null) {
                                //-- Use EnumFieldHandler
                                //-- mapping loader now supports a basic EnumFieldHandler
                                //-- for xml we simply need to make sure the toString()
                                //-- method is called during getValue()
                                //FieldHandler handler = xmlDesc.getHandler();
                                //handler = new EnumFieldHandler(fieldType, handler);
                                
                                FieldHandler handler = new ToStringFieldHandler(fieldType, xmlDesc.getHandler());
                                
                                xmlDesc.setHandler(handler);
                                xmlDesc.setImmutable(true);
                            }
                        }
                    }
                }
                catch(NoSuchMethodException nsmx) {
                    //-- Do nothing
                }
            }
        }
        
        //-- constructor argument?
        String setter = fieldMap.getSetMethod();
        if (setter != null) {
            if (setter.startsWith("%")) {
                int index = 0;
                setter = setter.substring(1);
                index = Integer.parseInt(setter);
                if ((index < 1) || (index > 9)) {
                    throw new MappingException("mapper.invalidParameterIndex", setter);
                }
                //-- adjust index to base zero
                --index;
                xmlDesc.setConstructorArgumentIndex(index);
            }
        }

        return xmlDesc;
    }

    /**
     * Sets whether or not to look for and load package specific
     * mapping files (".castor.xml" files).
     * 
     * @param loadPackageMappings a boolean that enables or
     * disables the loading of package specific mapping files
     */
    public void setLoadPackageMappings(boolean loadPackageMappings) 
    {
        if (_cdResolver == null) {
            createResolver();
        }
        _cdResolver.setLoadPackageMappings(loadPackageMappings);
    } //-- setLoadPackageMappings
    

    protected TypeInfo getTypeInfo( Class fieldType, CollectionHandler colHandler, FieldMapping fieldMap )
        throws MappingException
    {
        return new TypeInfo( fieldType, null, null, null,
                             fieldMap.getRequired(), null, colHandler, false );                             
    }

    /**
     * This method allows a collection to be treated as a first class 
     * object (and not a container) so that it has an element representation 
     * in the marshalled XML.
     */
     private XMLFieldDescriptorImpl wrapCollection
        (XMLFieldDescriptorImpl fieldDesc)
            throws MappingException
    {
        //-- If we have a field 'c' that is a collection and
        //-- we want to wrap that field in an element <e>, we
        //-- need to create a field descriptor for 
        //-- an object that represents the element <e> and
        //-- acts as a go-between from the parent of 'c' 
        //-- denoted as P(c) and 'c' itself
        //  
        //   object model: P(c) -> c
        //   xml : <p><e><c></e><p>
        
        //-- Make new class descriptor for the field that
        //-- will represent the container element <e>
        Class type = ContainerElement.class;
        XMLClassDescriptorImpl classDesc = new XMLClassDescriptorImpl(type);
        //-- make copy of fieldDesc and add it to our new class descriptor
        XMLFieldDescriptorImpl newFieldDesc 
            = new XMLFieldDescriptorImpl(fieldDesc, 
                                         fieldDesc.getXMLName(), 
                                         fieldDesc.getNodeType(),
                                         _primitiveNodeType);
        //-- nullify xmlName so that auto-naming will be enabled, 
        //-- we can't do this in the constructor because
        //-- XMLFieldDescriptorImpl will create a default one.
        newFieldDesc.setXMLName(null);
        newFieldDesc.setMatches("*");
        
        //-- add the field descriptor to our new class descriptor
        classDesc.addFieldDescriptor(newFieldDesc);        
        //-- reassociate the orignal class descriptor (for 'c') 
        // of fieldDesc with our new classDesc
        fieldDesc.setClassDescriptor(classDesc);
        
        //-- wrap the field handler in a special container field
        //-- handler that will actually do the delgation work 
        FieldHandler handler = new ContainerFieldHandler(fieldDesc.getHandler());
        newFieldDesc.setHandler(handler);
        fieldDesc.setHandler(handler);
        
        //-- Change fieldType of original field descriptor and
        //-- return new descriptor
        return new ContainerElementFieldDescriptor(fieldDesc, _primitiveNodeType);
    } //-- createWrapperDescriptor
     
    /**
     * Returns true if the given class should be treated as a primitive
     * type
     * @return true if the given class should be treated as a primitive
     * type
    **/
    protected static boolean isPrimitive(Class type) {

        if (type.isPrimitive()) return true;

        if ((type == Boolean.class) || (type == Character.class))
            return true;

        return (type.getSuperclass() == Number.class);

    } //-- isPrimitive

    /**
     * A special TypeConvertor that simply returns the object
     * given. This is used for preventing the FieldHandlerImpl
     * from using a CollectionHandler when getValue is called.
    **/
    class IdentityConvertor implements TypeConvertor {
        public Object convert( Object object, String param )
            throws ClassCastException
        {
            return object;
        }
    } //-- class: IdentityConvertor

    /**
     * An extended XMLFieldDescriptor that allows us to change 
     * the fieldType, needed for container element support
     */
    class ContainerElementFieldDescriptor extends XMLFieldDescriptorImpl {
        
        ContainerElementFieldDescriptor(XMLFieldDescriptorImpl fieldDesc, NodeType primitiveNodeType) 
            throws MappingException
        {
            super(fieldDesc, fieldDesc.getXMLName(), fieldDesc.getNodeType(),
                primitiveNodeType);
        }
        
        public Class getFieldType() {
            return org.exolab.castor.xml.util.ContainerElement.class;
        }
    } //-- class: ContainerElementFieldDescriptor

    
} //-- class: XMLMappingLoader



