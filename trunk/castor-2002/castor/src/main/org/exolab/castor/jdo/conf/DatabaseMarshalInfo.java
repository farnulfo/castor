/*
 * Add code header here
 * $Id$ 
 */

package org.exolab.castor.jdo.conf;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.lang.reflect.Method;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalDescriptor;
import org.exolab.castor.xml.SimpleMarshalDescriptor;

/**
 * @author 
 * @version $Revision$ $Date$
**/
public class DatabaseMarshalInfo implements org.exolab.castor.xml.MarshalInfo {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    /**
     * 
    **/
    private MarshalDescriptor[] elements;

    /**
     * 
    **/
    private MarshalDescriptor[] attributes;

    /**
     * 
    **/
    private SimpleMarshalDescriptor contentDesc;


      //----------------/
     //- Constructors -/
    //----------------/

    public DatabaseMarshalInfo() {
        SimpleMarshalDescriptor desc = null;
        Class[] emptyClassArgs = new Class[0];
        Class[] classArgs = new Class[1];
        //-- initialize attributes
        
        attributes = new MarshalDescriptor[1];
        //-- vDbName
        desc = new SimpleMarshalDescriptor(String.class, "vDbName", "db-name");
        desc.setDescriptorType(DescriptorType.attribute);
        try {
            desc.setReadMethod(Database.class.getMethod("getDbName", emptyClassArgs));
            classArgs[0] = String.class;
            desc.setWriteMethod(Database.class.getMethod("setDbName", classArgs));
        }
        catch(java.lang.NoSuchMethodException nsme) {};
        
        attributes[0] = desc;
        
        //-- initialize elements
        
        elements = new MarshalDescriptor[4];
        //-- vDriver
        desc = new SimpleMarshalDescriptor(Driver.class, "vDriver", "driver");
        desc.setDescriptorType(DescriptorType.element);
        try {
            desc.setReadMethod(Database.class.getMethod("getDriver", emptyClassArgs));
            classArgs[0] = Driver.class;
            desc.setWriteMethod(Database.class.getMethod("setDriver", classArgs));
        }
        catch(java.lang.NoSuchMethodException nsme) {};
        
        elements[0] = desc;
        
        //-- vDataSource
        desc = new SimpleMarshalDescriptor(DataSource.class, "vDataSource", "data-source");
        desc.setDescriptorType(DescriptorType.element);
        try {
            desc.setReadMethod(Database.class.getMethod("getDataSource", emptyClassArgs));
            classArgs[0] = DataSource.class;
            desc.setWriteMethod(Database.class.getMethod("setDataSource", classArgs));
        }
        catch(java.lang.NoSuchMethodException nsme) {};
        
        elements[1] = desc;
        
        //-- vDataSourceRef
        desc = new SimpleMarshalDescriptor(DataSourceRef.class, "vDataSourceRef", "data-source-ref");
        desc.setDescriptorType(DescriptorType.element);
        try {
            desc.setReadMethod(Database.class.getMethod("getDataSourceRef", emptyClassArgs));
            classArgs[0] = DataSourceRef.class;
            desc.setWriteMethod(Database.class.getMethod("setDataSourceRef", classArgs));
        }
        catch(java.lang.NoSuchMethodException nsme) {};
        
        elements[2] = desc;

        //-- vMappingList
        desc = new SimpleMarshalDescriptor(Mapping.class, "vMappingList", "mapping");
        desc.setDescriptorType(DescriptorType.element);
        desc.setMultivalued( true );
        try {
            desc.setReadMethod(Database.class.getMethod("getMappings", emptyClassArgs));
            classArgs[0] = Mapping.class;
            desc.setWriteMethod(Database.class.getMethod("addMapping", classArgs));
        }
        catch(java.lang.NoSuchMethodException nsme) {
            System.out.println( nsme );
        };
        
        elements[3] = desc;
        
    } //-- DatabaseMarshalInfo()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
    **/
    public MarshalDescriptor[] getAttributeDescriptors() {
        return attributes;
    } //-- MarshalDescriptor[] getAttributeDescriptors() 

    /**
     * 
    **/
    public Class getClassType() {
        return Database.class;
    } //-- Class getClassType() 

    /**
     * 
    **/
    public MarshalDescriptor[] getElementDescriptors() {
        return elements;
    } //-- MarshalDescriptor[] getElementDescriptors() 

    /**
     * 
    **/
    public Method getValidateMethod() {
        return null;
    } //-- Method getValidateMethod() 

    /**
     * 
    **/
    public MarshalDescriptor getContentDescriptor() {
        return contentDesc;
    } //-- MarshalDescriptor getContentDescriptor() 

    /**
     * 
    **/
    public String getNameSpacePrefix() {
        return null;
    } //-- String getNameSpacePrefix() 

    /**
     * 
    **/
    public String getNameSpaceURI() {
        return null;
    } //-- String getNameSpaceURI() 

    public org.exolab.castor.xml.ValidationRule[] getValidationRules()
    {
        return null;
    }

}
