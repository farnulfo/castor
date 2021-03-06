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

package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.ValidationException;

import java.util.Enumeration;
import java.util.Vector;

/**
 * A class which represents the AppInfo element
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$ 
**/
public class AppInfo extends Structure {
    
    
    /**
     * The text content of this AppInfo
    **/
    private String content = null;
    
    /**
     * List of any elements
    **/
    private Vector objects = null;
    
    /**
     * The source attribute
    **/
    private String source = null;
    
    /**
     * Creates a new AppInfo
    **/
    public AppInfo() {
        objects = new Vector(3);
    } //-- Annotation
    
    /**
     * Adds the given Object to this AppInfo
     * @param object the Object to add
    **/
    public void add(Object object) {
        if (object != null)
            objects.addElement(object);
    } //-- add
    
    /**
     * Returns the String content of this AppInfo
     * @return the String content of this AppInfo
    **/
    public String getContent() {
        return content;
    } //-- getContent
    
    /**
     * Returns an Enumeration of all objects contained by this AppInfo.
     * @return an Enumeration of all objects contained by this AppInfo.
    **/
    public Enumeration getObjects() {
        return objects.elements();
    } //-- getObjects
    
    /**
     * Returns the source property of this AppInfo
     * @return the source property of this AppInfo
    **/
    public String getSource() {
        return source;
    } //-- getSource
    
    /**
     * Removes the given Object from this AppInfo
     * @param object the Object to remove
    **/
    public void remove(Object object) {
        if (object != null) objects.removeElement(object);
    } //-- remove

    /**
     * Sets the String content for this AppInfo
     * @param content the String content for this AppInfo
    **/
    public void setContent(String content) {
        this.content = content;
    } //-- setContent
    
    /**
     * Sets the source property for this AppInfo
     * @param source the value of the source property
    **/
    public void setSource(String source) {
        this.source = source;
    } //-- setSource
    
    //-------------------------------/
    //- Implementation of Structure -/
    //-------------------------------/
    
    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.APPINFO;
    } //-- getStructureType
    
    /**
     * Checks the validity of this Schema defintion.
     * @exception ValidationException when this Schema definition
     * is invalid.
    **/
    public void validate()
        throws ValidationException 
    {
        //-- do nothing
    } //-- validate
    
} //-- AppInfo