/*
 * (C) Copyright Keith Visco 1999  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.kvisco.com/xslp/license.txt
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
 
package org.exolab.adaptx.xpath.engine;

import org.exolab.adaptx.xpath.StringResult;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.expressions.PrimaryExpr;

/**
 * Represents the XPath Literal expression
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
class LiteralExpr extends PrimaryExpr {
    
    //private String       _literal = null;
    private StringResult _result = null;
    
    /**
     * Creates a new LiteralExpr which evaluates to the empty
     * string
    **/
    LiteralExpr() {
        super(PrimaryExpr.LITERAL);
        _result = new StringResult("");
    } //-- LiteralExpr
    
    /**
     * Creates a new LiteralExpr which evaluates to
     * the given string literal
     *
     * @param literal the String to evaluate to
    **/
    LiteralExpr(String literal) {
        super(PrimaryExpr.LITERAL);
        //_literal = literal;
        _result = new StringResult(literal);
    } //-- LiteralExpr
    
    /**
     * Evaluates the expression and returns the XPath result.
     *
     * @param context The XPathContext to use during evaluation.
     * @return The XPathResult (not null).
     * @exception XPathException if an error occured while 
     * evaluating this expression.
    **/
    public XPathResult evaluate( XPathContext context )
        throws XPathException
    {
        return _result;
    } //-- evaluate
    
    /**
     * Returns the String representation of this PrimaryExpr
     * @return the String representation of this PrimaryExpr
    **/
    public String toString() {
        return "'" + _result.stringValue() + "'";
    } //-- toString
    
} //-- LiteralExpr

