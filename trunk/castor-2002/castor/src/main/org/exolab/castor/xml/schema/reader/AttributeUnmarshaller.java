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

package org.exolab.castor.xml.schema.reader;

//-- imported classes and packages
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.schema.*;
import org.xml.sax.*;

/**
 * The base class for unmarshallers
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class AttributeUnmarshaller extends SaxUnmarshaller {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    /**
     * The current SaxUnmarshaller
    **/
    private SaxUnmarshaller unmarshaller;

    /**
     * The current branch depth
    **/
    private int depth = 0;

    /**
     * The Attribute reference for the Attribute we are constructing
    **/
    private AttributeDecl _attribute = null;

    private Schema _schema = null;

      //----------------/
     //- Constructors -/
    //----------------/

    public AttributeUnmarshaller
        (Schema schema, AttributeList atts, Resolver resolver)
    {
        super();
        this._schema = schema;

        setResolver(resolver);

        _attribute = new AttributeDecl(schema, atts.getValue("name"));

        //-- handle remaining attributes
        String attValue = null;

        //-- value
        String defaultValue = atts.getValue(SchemaNames.VALUE_ATTR);
        _attribute.setDefaultValue(defaultValue);

        //-- use
        String useValue = atts.getValue(SchemaNames.USE_ATTR);
        if (useValue == null) {
            useValue = "default";
        }
        _attribute.setUseValue(useValue);

        //-- type
        attValue = atts.getValue("type");
        if (attValue != null) {
            _attribute.setSimpleTypeReference(attValue);
        }

    } //-- AttributeUnmarshaller

      //-----------/
     //- Methods -/
    //-----------/


    /**
     * Returns the name of the element that this SaxUnmarshaller
     * handles
     * @return the name of the element that this SaxUnmarshaller
     * handles
    **/
    public String elementName() {
        return SchemaNames.ATTRIBUTE;
    } //-- elementName

    /**
     *
    **/
    public AttributeDecl getAttribute() {
        return _attribute;
    } //-- getAttribute

    /**
     * Returns the Object created by this SaxUnmarshaller
     * @return the Object created by this SaxUnmarshaller
    **/
    public Object getObject() {
        return getAttribute();
    } //-- getObject

    /**
     *
     * @param name
     * @param atts
    **/
    public void startElement(String name, AttributeList atts)
        throws org.xml.sax.SAXException
    {
        //-- Do delagation if necessary
        if (unmarshaller != null) {
            unmarshaller.startElement(name, atts);
            ++depth;
            return;
        }

        if (SchemaNames.ANNOTATION.equals(name)) {
            unmarshaller = new AnnotationUnmarshaller(atts);
        }
        else if (SchemaNames.SIMPLE_TYPE.equals(name)) {
            unmarshaller = new SimpleTypeUnmarshaller(_schema,
                                                      atts,
                                                      getResolver());
        }
        else {
            illegalElement(name);
        }

    } //-- startElement

    /**
     *
     * @param name
    **/
    public void endElement(String name)
        throws org.xml.sax.SAXException
    {

        //-- Do delagation if necessary
        if ((unmarshaller != null) && (depth > 0)) {
            unmarshaller.endElement(name);
            --depth;
            return;
        }

        //-- call unmarshaller finish to perform any necessary cleanup
        unmarshaller.finish();

        if (SchemaNames.ANNOTATION.equals(name)) {
            Annotation ann = (Annotation) unmarshaller.getObject();
            _attribute.addAnnotation(ann);
        }
        else if (SchemaNames.SIMPLE_TYPE.equals(name)) {
            SimpleType simpleType =
                ((SimpleTypeUnmarshaller)unmarshaller).getSimpleType();
            _attribute.setSimpleType(simpleType);
        }

        unmarshaller = null;

    } //-- endElement

    public void characters(char[] ch, int start, int length)
        throws SAXException
    {
        //-- Do delagation if necessary
        if (unmarshaller != null) {
            unmarshaller.characters(ch, start, length);
        }
    } //-- characters

} //-- AttributeUnmarshaller
