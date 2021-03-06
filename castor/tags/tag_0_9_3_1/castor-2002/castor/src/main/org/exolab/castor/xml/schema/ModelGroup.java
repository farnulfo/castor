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

import org.exolab.castor.xml.*;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * An XML Schema ModelGroup
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ModelGroup extends Group {


    /**
     * the name of the ModelGroup referenced
     */
     private String groupRef = null;

    /**
     * An ordered list of all ModelGroup definitions
    **/
    private Vector modelDefs;


    /**
     * the schema that contains this model group
     */
     private Schema _schema = null;

    /**
     * Creates a new ModelGroup, with no name
    **/
    public ModelGroup() {
        this(null);
    } //-- ModelGroup

    /**
     * Creates a new ModelGroup definition
     * @param schema, the XML Schema to which this ModelGroup
     * belongs
     */
    public ModelGroup(Schema schema) {
        this(null, schema);
    }

    /**
     * Creates a new ModelGroup with the given name
     * @param name of the ModelGroup
    **/
    public ModelGroup(String name, Schema schema) {
        super(name);
        _schema = schema;
        modelDefs = new Vector();
    } //-- ModelGroup


    /**
     * Adds the given ModelGroup to this ModelGroup
     * @param modelGroup the ModelGroup to add to this ModelGroup
    **/
    public void addModelGroup(ModelGroup modelGroup) {
        if (!modelDefs.contains(modelGroup)) {
            modelDefs.addElement(modelGroup);
        }
    } //-- addModelGroup

    /**
     * Returns an enumeration of all the Particles of this
     * ContentModelGroup
     *
     * @return an enumeration of the Particles contained
     * within this ContentModelGroup
    **/
    public Enumeration enumerate() {
        return this.getContentModelGroup().enumerate();
    } //-- enumerate

    /**
     * Returns an ordered Enumeration of all the ContentModelType
     * definitions (element, group, modelGroupRef)+
    **/
    public Enumeration getDeclarations() {
        return modelDefs.elements();
    } //-- getDeclarations

    /**
     * Sets the reference for this ModelGroup definition
     * @param reference the name of the ModelGroup that this
     * definition references
    **/
    public void setReference(String reference) {
        this.groupRef = reference;
    } //-- setReference

    //-------------------------------/
    //- Implementation of Structure -/
    //-------------------------------/

    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.MODELGROUP;
    } //-- getStructureType

    /**
     * Returns the Id used to Refer to this Object
     * @return the Id used to Refer to this Object
     * @see Referable
    **/
    public String getReferenceId() {
        if (this.getName() != null) return "group:"+this.getName();
        return null;
    } //-- getReferenceId

    /**
     * Returns the reference if any
     * @returns the reference if any
     */
     public ModelGroup getReference() {
        if (groupRef != null)
            return _schema.getModelGroup(groupRef);
        return null;
    } //-- getReference


     /**
      * Returns true if this ModelGroup is referencing another one
      * @returns true if this ModelGroup is referencing another one
      */
     public boolean hasReference() {
         return (groupRef.length() !=0);
     }
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

    /**
     * Returns the schema that contains this modelGroup definition
     */
    public Schema getSchema() {
       return _schema;
    }

} //-- Group