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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder;

import org.exolab.javasource.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 * This class holds the necessary information
 * so that the source generator can properly create
 * the necessary Classes for the Object model.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ClassInfo extends XMLInfo {

    private Vector    _atts      = null;
    private Vector    _elements  = null;
    private FieldInfo _textField = null;

    /**
     * A reference to the JClass that this ClassInfo describes
    **/
    private JClass _class = null;

    /**
     * Creates a new ClassInfo
     * @param jClass the JClass which this ClassInfo describes
    **/
    public ClassInfo(JClass jClass) {
        super(XMLInfo.ELEMENT_TYPE);
        if (jClass == null) {
            String err = "JClass passed to constructor of ClassInfo "+
                "must not be null.";
            throw new IllegalArgumentException(err);
        }
        this._class = jClass;
    } //-- ClassInfo


    //------------------/
    //- Public Methods -/
    //------------------/

    /**
     * Adds the given FieldInfo to this ClassInfo
     * @param fieldInfo the FieldInfo to add
    **/
    public void addFieldInfo(FieldInfo fieldInfo) {

        if (fieldInfo == null) return;

        switch(fieldInfo.getNodeType()) {
            case FieldInfo.ATTRIBUTE_TYPE:
                if (_atts == null) _atts = new Vector(3);
                if (!_atts.contains(fieldInfo))
                    _atts.addElement(fieldInfo);
                break;
            case FieldInfo.TEXT_TYPE:
                _textField = fieldInfo;
                break;
            default:
                if (_elements == null) _elements = new Vector(5);
                if (!_elements.contains(fieldInfo))
                    _elements.addElement(fieldInfo);
                break;
        }
    } //-- addFieldInfo

    /**
     * Adds the given set of FieldInfos to this ClassInfo
     * @param fields an Array of FieldInfo objects
    **/
    public void addFieldInfo(FieldInfo[] fields) {
        for (int i = 0; i < fields.length; i++)
            addFieldInfo(fields[i]);
    } //-- addFieldInfo

    /**
     * @return true if Classes created with this ClassInfo allow
     * content
    **/
    public boolean allowContent() {
        return (_textField != null);
    } //-- allowsTextContent

    /**
     * Returns true if the given FieldInfo is contained within this ClassInfo
     * @return true if the given FieldInfo is contained within this ClassInfo
    **/
    public boolean contains(FieldInfo fieldInfo) {
        boolean val = false;

        if (fieldInfo == null) return false;

        switch(fieldInfo.getNodeType()) {

            case FieldInfo.ATTRIBUTE_TYPE:
                if (_atts != null)
                    return _atts.contains(fieldInfo);
                break;
            case FieldInfo.TEXT_TYPE:
                return (fieldInfo == _textField);
            default:
                if (_elements != null)
                    return _elements.contains(fieldInfo);
                break;
        }

        //if (sourceInfo != null)
        //    return sourceInfo.contains(fieldInfo);

        return false;
    } //-- contains

    /**
     * Returns an array of XML attribute associated fields
     * @return an array of XML attribute associated fields
    **/
    public FieldInfo[] getAttributeFields() {
        FieldInfo[] fields = null;
        if (_atts != null) {
            fields = new FieldInfo[_atts.size()];
            _atts.copyInto(fields);
        }
        else fields = new FieldInfo[0];
        return fields;
    } //-- getAttributeFields

    /**
     * Returns an array of XML element associated fields
     * @return an array of XML element associated fields
    **/
    public FieldInfo[] getElementFields() {
        FieldInfo[] members = null;
        if (_elements != null) {
            members = new FieldInfo[_elements.size()];
            _elements.copyInto(members);
        }
        else members = new FieldInfo[0];
        return members;
    } //-- getElementFields

    /**
     * Returns the JClass described by this ClassInfo
     * @return the JClass which is described by this ClassInfo
    **/
    public JClass getJClass() {
        return _class;
    } //-- getJClass

    /**
     * Returns the FieldInfo for the XML text associated field.
     * @return the FieldInfo for the text content associated field,
     * this may be null.
    **/
    public FieldInfo getTextField() {
        return _textField;
    } //-- getTextField



} //-- ClassInfo
