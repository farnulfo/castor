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


package org.exolab.castor.types;

import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.util.*;
import org.exolab.castor.mapping.ValidityException;
/**
 * The TimeDuration Descriptor
 * @author <a href="kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
 *
 */
public class TimeDurationDescriptor
    implements XMLClassDescriptor
{


    /**
     * Used for returning no attribute and no element fields
    **/
    private static final XMLFieldDescriptor[] _noFields
        = new XMLFieldDescriptor[0];

    /**
     * The TypeValidator to use for validation of the described class
    **/
    private TypeValidator validator = null;

    /**
     * The namespace prefix that is to be used when marshalling
    **/
    private String nsPrefix = null;

    /**
     * The namespace URI used for both Marshalling and Unmarshalling
    **/
    private String nsURI = null;


    /**
     * The name of the XML element.
     */
    private static final String _xmlName = "timeDuration";

    private static XMLFieldDescriptorImpl _contentDescriptor = null;

    private static FieldDescriptor[] _fields = null;

    //----------------/
    //- Constructors -/
    //----------------/

    public TimeDurationDescriptor() {
        super();
        if (_contentDescriptor == null) {
            _contentDescriptor = new XMLFieldDescriptorImpl(String.class,
                "content", "content", NodeType.Text);
            //-- setHandler
            _contentDescriptor.setHandler(new TimeDurationFieldHandler());
        }

        if (_fields == null) {
            _fields = new FieldDescriptor[1];
            _fields[0] = _contentDescriptor;
        }

    } //-- TimeDurationDescriptor

    //------------------/
    //- Public Methods -/
    //------------------/

    /**
     * Returns the set of XMLFieldDescriptors for all members
     * that should be marshalled as XML attributes.
     * @return an array of XMLFieldDescriptors for all members
     * that should be marshalled as XML attributes.
    **/
    public XMLFieldDescriptor[]  getAttributeDescriptors() {
        return _noFields;
    } // getAttributeDescriptors

    /**
     * Returns the XMLFieldDescriptor for the member
     * that should be marshalled as text content.
     * @return the XMLFieldDescriptor for the member
     * that should be marshalled as text content.
    **/
    public XMLFieldDescriptor getContentDescriptor() {
        return _contentDescriptor;
    } // getContentDescriptor

    /**
     * Returns the set of XMLFieldDescriptors for all members
     * that should be marshalled as XML elements.
     * @return an array of XMLFieldDescriptors for all members
     * that should be marshalled as XML elements.
    **/
    public XMLFieldDescriptor[]  getElementDescriptors() {
        return _noFields;
    } // getElementDescriptors

    /**
     * @return the namespace prefix to use when marshalling as XML.
    **/
    public String getNameSpacePrefix() {
        return nsPrefix;
    } //-- getNameSpacePrefix

    /**
     * @return the namespace URI used when marshalling and unmarshalling as XML.
    **/
    public String getNameSpaceURI() {
        return nsURI;
    } //-- getNameSpaceURI

    /**
     * Returns a specific validator for the class described by
     * this ClassDescriptor. A null value may be returned
     * if no specific validator exists.
     *
     * @return the type validator for the class described by this
     * ClassDescriptor.
    **/
    public TypeValidator getValidator() {
        return validator;
    } //-- getValidator

    /**
     * Returns the XML Name for the Class being described.
     *
     * @return the XML name.
    **/
    public String getXMLName() {
        return _xmlName;
    } //-- getXMLName

    /**
     * Returns the String representation of this XMLClassDescriptor
     * @return the String representation of this XMLClassDescriptor
    **/
    public String toString() {

        String str = super.toString() +
            "; descriptor for class: TimeDuration";

        //-- add xml name
        str += "; xml name: " + _xmlName;

        return str;
    } //-- toString


    //-------------------------------------/
    //- Implementation of ClassDescriptor -/
    //-------------------------------------/

    /**
     * Returns the Java class represented by this descriptor.
     *
     * @return The Java class
     */
    public Class getJavaClass() {
        return TimeDuration.class;
    } //-- getJavaClass


    /**
     * Returns a list of fields represented by this descriptor.
     *
     * @return A list of fields
     */
    public FieldDescriptor[] getFields() {
        return _fields;
    } //-- getFields



    /**
     * Returns the class descriptor of the class extended by this class.
     *
     * @return The extended class descriptor
     */
    public ClassDescriptor getExtends() {
        return null;
    } //-- getExtends


    /**
     * Returns the identity field, null if this class has no identity.
     *
     * @return The identity field
     */
    public FieldDescriptor getIdentity() {
        return null;
    } //-- getIdentity


    /**
     * Returns the access mode specified for this class.
     *
     * @return The access mode
     */
    public AccessMode getAccessMode() {
        return null;
    } //-- getAccessMode


    /**
     * A specialized FieldHandler for the XML Schema
     * TimeDuration related types
     * @author <a href="blandin@intalio.com">Arnaud Blandin</a>
     * @version $Revision $ $Date $
    **/
    class TimeDurationFieldHandler extends XMLFieldHandler {

        //----------------/
        //- Constructors -/
        //----------------/

        /**
         * Creates a new TimeDurationFieldHandler
        **/
        public TimeDurationFieldHandler() {
            super();
        } //-- TimeFieldHandler

        //------------------/
        //- Public Methods -/
        //------------------/

        /**
         * Returns the value of the field associated with this
         * descriptor from the given target object.
         * @param target the object to get the value from
         * @return the value of the field associated with this
         * descriptor from the given target object.
        **/
        public Object getValue(Object target)
            throws java.lang.IllegalStateException
        {

            //-- check for TimeDuration class  -- add later
            TimeDuration td = (TimeDuration) target;

            return td.toString();
        } //-- getValue

        /**
         * Sets the value of the field associated with this descriptor.
         * @param target the object in which to set the value
         * @param value the value of the field
        **/
        public void setValue(Object target, Object value)
            throws java.lang.IllegalStateException
        {


            if (! (target instanceof TimeDuration) ) {
               //-- throw exception
            }

            TimeDuration time = (TimeDuration) target;

            if (value == null) {
               /// do something
            }

            //-- update current instance of time with new time
            try {
                TimeDuration temp = TimeDuration.parse(value.toString()) ;
                time.setYear(temp.getYear());
                time.setMonth(temp.getMonth());
                time.setDay(temp.getDay());
                time.setHour(temp.getHour());
                time.setMinute(temp.getMinute());
                time.setSeconds(temp.getSeconds());
            }
            catch (java.text.ParseException ex) {
                //-- ignore for now
            }

        } //-- setValue

        public void resetValue(Object target)
            throws java.lang.IllegalStateException
        {
        }


        /**
         * Checks the field validity. Returns successfully if the field
         * can be stored, is valid, etc, throws an exception otherwise.
         *
         * @param object The object
         * @throws ValidityException The field is invalid, is required and
         *  null, or any other validity violation
         * @throws IllegalStateException The Java object has changed and
         *  is no longer supported by this handler, or the handler
         *  is not compatiable with the Java object
         */
        public void checkValidity( Object object )
            throws ValidityException, IllegalStateException
        {
            // nothing to do?
        } //-- checkValidity


        /**
         * Creates a new instance of the object described by this field.
         *
         * @param parent The object for which the field is created
         * @return A new instance of the field's value
         * @throws IllegalStateException This field is a simple type and
         *  cannot be instantiated
         */
        public Object newInstance( Object parent )
            throws IllegalStateException
        {
            return new TimeDuration();
        } //-- newInstance


    } //-- TimeDurationFieldHandler


} //-- TimeDurationDescriptor


