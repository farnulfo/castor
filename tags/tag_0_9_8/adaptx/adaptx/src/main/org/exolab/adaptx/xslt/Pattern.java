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


package org.exolab.adaptx.xslt;


import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathExpression;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.engine.Parser;
import org.exolab.adaptx.xpath.expressions.UnionExpr;
import org.exolab.adaptx.xpath.expressions.PathExpr;

/**
 * This class represents a "pattern" as specified in the 
 * W3C XSLT 1.0 Recommendation.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class Pattern {
        
        
    private UnionExpr _unionExpr = null;
    
    /**
     * Creates a new default pattern. This pattern has
     * zero location paths and will match no nodes.
    **/
    public Pattern() {
        super();
    } //-- Pattern
    
    
    public Pattern(String pattern) throws PatternException {
        super();
        try {
            _unionExpr = Parser.createUnionExpr(pattern);
        }
        catch(XPathException xpe) {
            throw new PatternException(xpe);
        }
    } //-- Pattern
    
    
    /**
     * Returns an array of all LocationPathPatterns contained in 
     * this Pattern.
     *
     * @return an array of LocationPathPattern objects
    **
    public LocationPathPattern[] getLocationPaths() {
        return _patterns;
    } //-- getLocationPaths
    **/
    
    /**
     * Returns the LocationPathPattern with the highest priority
     * that matches the given XPathNode using the given XPathContext.
     *
     * @param node the node to determine a match for
     * @param context the XPathContext
     * @return the LocationPathPattern with the highest priority
     * that matches the given XPathNode using the given XPathContext.
     * Null will be returned if no matching LocationPathPattern objects
     * are contained in this Pattern.
    **/
    public LocationPathPattern getMatchingPattern
        (XPathNode node, XPathContext context)
        throws XPathException 
    {
        if (_unionExpr == null) return null;
        
        PathExpr pathExpr = _unionExpr.getMatchingExpr(node, context);
        if (pathExpr != null) {
            return new PathExprPattern(pathExpr);
        }
        return null;
    } //-- getMatchingPattern
    
    /**
     * Determines if the given node is matched by this Pattern with
     * respect to the given context.
     *
     * @param node the node to determine a match for
     * @param context the XPathContext
     * @return true if the given node is matched by this Pattern
     * @exception XPathException when an error occurs during
     * evaluation
    **/
    public boolean matches( XPathNode node, XPathContext context )
        throws XPathException
    {
        if (_unionExpr == null) return false;
        return _unionExpr.matches(node, context);
    } //-- matches

    /**
     * Returns the String representation of this Pattern. This
     * will be an equivalent string to that which this pattern
     * was created from, or the empty string if this pattern
     * is the default pattern.
     *
     * @return the String representation of this Pattern.
    **/
    public String toString() {
        if (_unionExpr == null) return "";
        return _unionExpr.toString();
    } //-- toString

    
} //-- Pattern


/**
 * This class represents a "Location Path Pattern" as specified in the 
 * W3C XSLT 1.0 Recommendation.
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
class PathExprPattern extends LocationPathPattern {
        
    PathExpr _pathExpr = null;
    
    /** 
     * Creates a new PathExprPattern for the given PathExpr.
     *
     * @param pathExpr the PathExpr to create the PathExprPattern for.
    **/
    public PathExprPattern(PathExpr pathExpr) {
        _pathExpr = pathExpr;
    } //-- PathExprPattern
    
    /**
     * Determines the priority of a PatternExpr as follows:
     * <PRE>
     *  From the 19991116 XSLT 1.0 Recommendation:
     *  + If the pattern has the form of a QName preceded by a
     *    ChildOrAttributeAxisSpecifier or has the form 
     *    processing-instruction(Literal) then the priority is 0.
     *  + If the pattern has the form NCName:* preceded by a 
     *    ChildOrAttributeAxisSpecifier, then the priority is -0.25
     *  + Otherwise if the pattern consists of just a NodeTest 
     *    preceded by a ChildOrAttributeAxisSpecifier then the
     *    priority is -0.5
     *  + Otherwise the priority is 0.5
     * </PRE>
     * @return the priority for this PatternExpr
    **/
    public double getDefaultPriority() {
        return _pathExpr.getDefaultPriority();
    } //-- getDefaultPriority
    
    /**
     * Determines if the given node is matched by this Pattern with
     * respect to the given context.
     *
     * @param node the node to determine a match for
     * @param context the XPathContext
     * @return true if the given node is matched by this Pattern
     * @exception XPathException when an error occurs during
     * evaluation
    **/
    public boolean matches( XPathNode node, XPathContext context )
        throws XPathException
    {
        return _pathExpr.matches(node, context);
    } //-- matches

    /**
     * Returns the String representation of this PathExprPattern.
     *
     * @return the String representation of this PathExprPattern.
    **/
    public String toString() {
        return _pathExpr.toString();
    } //-- toString

} //-- PathExprPattern
