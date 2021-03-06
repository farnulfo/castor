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
 * Copyright 1999-2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

//-- imported classes and packages
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.schema.*;
import org.exolab.castor.util.Configuration;
import org.xml.sax.*;

import java.net.URL;
import java.util.Vector;

public class ImportUnmarshaller extends SaxUnmarshaller
{
    public ImportUnmarshaller
        (Schema schema, AttributeList atts, Resolver resolver, Locator locator)
		throws SAXException
    {
        super();
        setResolver(resolver);

		//-- Get schemaLocation
		String schemalocation = atts.getValue("schemaLocation");
		if (schemalocation==null)
			throw new SAXException("'schemaLocation' attribute missing on 'import'");
		//if the path is relative Xerces append the "user.Dir"
        //we need to keep the base directory of the document
        // note: URI not supported (just system path)
        if (!new java.io.File(schemalocation).isAbsolute()) {
             String temp = locator.getSystemId();
             if (temp != null) {
               if (schemalocation.startsWith("./"))
                  schemalocation = schemalocation.substring(2);
               if (schemalocation.startsWith("/"))
                  schemalocation = schemalocation.substring(1);
                //remove 'file://'
                temp = temp.substring(7);
                if (java.io.File.separatorChar =='\\')
                    temp = temp.substring(1);
                temp = temp.substring(0,temp.lastIndexOf('/')+1);
                schemalocation = temp + schemalocation;
                temp = null;
             }
        }

		//-- Get namespace
		String namespace = atts.getValue("namespace");
		if (namespace==null)
			throw new SAXException("'namespace' attribute missing on 'import'");

		//-- Is this namespace one the schema knows about?
		if (!schema.isKnownNamespace(namespace))
			throw new SAXException("namespace '"+namespace+"' not declared in schema");

		//-- Schema object to hold import schema
		boolean addSchema = false;
		Schema importedSchema = schema.getImportedSchema(namespace);
		if (importedSchema==null)
		{
			importedSchema = new Schema();
			addSchema = true;
		}

		//-- Have we already imported this XML Schema file?
		if (importedSchema.includeProcessed(schemalocation))
			return;
		importedSchema.addInclude(schemalocation);

		//-- Parser Schema
		Parser parser = null;
		try {
		parser = Configuration.getParser();
		}
		catch(RuntimeException rte) {}
		if (parser == null) {
		    throw new SAXException("Error failed to create parser for import");
		}
		else
		{
			//-- Create Schema object and setup unmarshaller
			SchemaUnmarshaller schemaUnmarshaller = new SchemaUnmarshaller();
			schemaUnmarshaller.setSchema(importedSchema);
			parser.setDocumentHandler(schemaUnmarshaller);
			parser.setErrorHandler(schemaUnmarshaller);
		}

		try {
		    parser.parse(new InputSource(schemalocation));
		}
		catch(java.io.IOException ioe) {
		    throw new SAXException("Error reading import file '"+schemalocation+"': "+ ioe);
		}

		//-- Add schema to list of imported schemas (if not already present)
		if (addSchema)
			schema.addImportedSchema(importedSchema);
	}


    /**
     * Sets the name of the element that this UnknownUnmarshaller handles
     * @param name the name of the element that this unmarshaller handles
    **/
    public String elementName() {
        return SchemaNames.IMPORT;
    } //-- elementName

    /**
     * Returns the Object created by this SaxUnmarshaller
     * @return the Object created by this SaxUnmarshaller
    **/
    public Object getObject() {
        return null;
    } //-- getObject

}
