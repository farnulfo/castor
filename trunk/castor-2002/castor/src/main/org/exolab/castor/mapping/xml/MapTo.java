/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.8.10</a>, using an
 * XML Schema.
 * $Id$
 */

package org.exolab.castor.mapping.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 * 
 * @version $Revision$ $Date$
**/
public class MapTo implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _table;

    private java.lang.String _xml;

    private java.lang.String _nsUri;

    private java.lang.String _nsPrefix;

    private java.lang.String _ldapDn;

    private java.lang.String _ldapOc;


      //----------------/
     //- Constructors -/
    //----------------/

    public MapTo() {
        super();
    } //-- org.exolab.castor.mapping.xml.MapTo()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public java.lang.String getLdapDn()
    {
        return this._ldapDn;
    } //-- java.lang.String getLdapDn() 

    /**
    **/
    public java.lang.String getLdapOc()
    {
        return this._ldapOc;
    } //-- java.lang.String getLdapOc() 

    /**
    **/
    public java.lang.String getNsPrefix()
    {
        return this._nsPrefix;
    } //-- java.lang.String getNsPrefix() 

    /**
    **/
    public java.lang.String getNsUri()
    {
        return this._nsUri;
    } //-- java.lang.String getNsUri() 

    /**
    **/
    public java.lang.String getTable()
    {
        return this._table;
    } //-- java.lang.String getTable() 

    /**
    **/
    public java.lang.String getXml()
    {
        return this._xml;
    } //-- java.lang.String getXml() 

    /**
    **/
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * 
     * @param out
    **/
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.DocumentHandler handler)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.DocumentHandler) 

    /**
     * 
     * @param _ldapDn
    **/
    public void setLdapDn(java.lang.String _ldapDn)
    {
        this._ldapDn = _ldapDn;
    } //-- void setLdapDn(java.lang.String) 

    /**
     * 
     * @param _ldapOc
    **/
    public void setLdapOc(java.lang.String _ldapOc)
    {
        this._ldapOc = _ldapOc;
    } //-- void setLdapOc(java.lang.String) 

    /**
     * 
     * @param _nsPrefix
    **/
    public void setNsPrefix(java.lang.String _nsPrefix)
    {
        this._nsPrefix = _nsPrefix;
    } //-- void setNsPrefix(java.lang.String) 

    /**
     * 
     * @param _nsUri
    **/
    public void setNsUri(java.lang.String _nsUri)
    {
        this._nsUri = _nsUri;
    } //-- void setNsUri(java.lang.String) 

    /**
     * 
     * @param _table
    **/
    public void setTable(java.lang.String _table)
    {
        this._table = _table;
    } //-- void setTable(java.lang.String) 

    /**
     * 
     * @param _xml
    **/
    public void setXml(java.lang.String _xml)
    {
        this._xml = _xml;
    } //-- void setXml(java.lang.String) 

    /**
     * 
     * @param reader
    **/
    public static org.exolab.castor.mapping.xml.MapTo unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.MapTo) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.MapTo.class, reader);
    } //-- org.exolab.castor.mapping.xml.MapTo unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator.validate(this, null);
    } //-- void validate() 

}
