/*
 * (C) Copyright Keith Visco 1999  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.clc-marketing.com/xslp/license.txt
 *
 * The program is provided "as is" without any warranty express or
 * implied, including the warranty of non-infringement and the implied
 * warranties of merchantibility and fitness for a particular purpose.
 * The Copyright owner will not be liable for any damages suffered by
 * you as a result of using the Program. In no event will the Copyright
 * owner be liable for any special, indirect or consequential damages or
 * lost profits even if the Copyright owner has been advised of the
 * possibility of their occurrence.
 *
 * $Id$
 */

package org.exolab.adaptx.xslt.functions;

import org.exolab.adaptx.xslt.ProcessorState;
import org.exolab.adaptx.xslt.Names;
import org.exolab.adaptx.xslt.XSLTFunction;

import org.exolab.adaptx.xpath.XPathFunction;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.NodeSet;

/**
 * A class that represents the XSLT current() function call
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class CurrentFunctionCall extends XSLTFunction {
    
    private ProcessorState ps = null;
    
    /**
     * Creates a new current() Function Call
    **/
    public CurrentFunctionCall(ProcessorState ps) {
        super(Names.CURRENT_FN);
        this.ps = ps;
    } //-- CurrentFunctionCall

    /**
     * Invokes the function and returns the XPath result.
     *
     * @param context The XPath context
     * @param params A list of zero or more arguments
     * @return An XPath result (not null)
     * @throws XPathException An error occured while invoking this function
    **/
    public XPathResult call( XPathContext context, XPathResult[] args)
        throws XPathException
    {
        NodeSet nodeSet = new NodeSet(1);
        nodeSet.add(ps.getCurrentNode());
        return nodeSet;
    } //-- call
    
} //-- CurrentFunctionCall
