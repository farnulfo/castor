/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1-M2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.builder.binding.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ComponentBindingTypeChoice.
 * 
 * @version $Revision$ $Date$
 */
public class ComponentBindingTypeChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _javaClass.
     */
    private org.exolab.castor.builder.binding.xml.ClassType _javaClass;

    /**
     * Field _interface.
     */
    private org.exolab.castor.builder.binding.xml.Interface _interface;

    /**
     * Field _member.
     */
    private org.exolab.castor.builder.binding.xml.FieldType _member;

    /**
     * Field _enumDef.
     */
    private org.exolab.castor.builder.binding.xml.EnumBindingType _enumDef;

    /**
     * Field _contentMember.
     */
    private org.exolab.castor.builder.binding.xml.ContentMemberType _contentMember;


      //----------------/
     //- Constructors -/
    //----------------/

    public ComponentBindingTypeChoice() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'contentMember'.
     * 
     * @return the value of field 'ContentMember'.
     */
    public org.exolab.castor.builder.binding.xml.ContentMemberType getContentMember(
    ) {
        return this._contentMember;
    }

    /**
     * Returns the value of field 'enumDef'.
     * 
     * @return the value of field 'EnumDef'.
     */
    public org.exolab.castor.builder.binding.xml.EnumBindingType getEnumDef(
    ) {
        return this._enumDef;
    }

    /**
     * Returns the value of field 'interface'.
     * 
     * @return the value of field 'Interface'.
     */
    public org.exolab.castor.builder.binding.xml.Interface getInterface(
    ) {
        return this._interface;
    }

    /**
     * Returns the value of field 'javaClass'.
     * 
     * @return the value of field 'JavaClass'.
     */
    public org.exolab.castor.builder.binding.xml.ClassType getJavaClass(
    ) {
        return this._javaClass;
    }

    /**
     * Returns the value of field 'member'.
     * 
     * @return the value of field 'Member'.
     */
    public org.exolab.castor.builder.binding.xml.FieldType getMember(
    ) {
        return this._member;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(
            final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(
            final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'contentMember'.
     * 
     * @param contentMember the value of field 'contentMember'.
     */
    public void setContentMember(
            final org.exolab.castor.builder.binding.xml.ContentMemberType contentMember) {
        this._contentMember = contentMember;
    }

    /**
     * Sets the value of field 'enumDef'.
     * 
     * @param enumDef the value of field 'enumDef'.
     */
    public void setEnumDef(
            final org.exolab.castor.builder.binding.xml.EnumBindingType enumDef) {
        this._enumDef = enumDef;
    }

    /**
     * Sets the value of field 'interface'.
     * 
     * @param _interface
     * @param interface the value of field 'interface'.
     */
    public void setInterface(
            final org.exolab.castor.builder.binding.xml.Interface _interface) {
        this._interface = _interface;
    }

    /**
     * Sets the value of field 'javaClass'.
     * 
     * @param javaClass the value of field 'javaClass'.
     */
    public void setJavaClass(
            final org.exolab.castor.builder.binding.xml.ClassType javaClass) {
        this._javaClass = javaClass;
    }

    /**
     * Sets the value of field 'member'.
     * 
     * @param member the value of field 'member'.
     */
    public void setMember(
            final org.exolab.castor.builder.binding.xml.FieldType member) {
        this._member = member;
    }

    /**
     * Method unmarshalComponentBindingTypeChoice.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.builder.binding.xml.ComponentBindingTypeChoice
     */
    public static org.exolab.castor.builder.binding.xml.ComponentBindingTypeChoice unmarshalComponentBindingTypeChoice(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.builder.binding.xml.ComponentBindingTypeChoice) Unmarshaller.unmarshal(org.exolab.castor.builder.binding.xml.ComponentBindingTypeChoice.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate(
    )
    throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
