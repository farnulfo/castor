/*
 * (C) Copyright Keith Visco 2001  All rights reserved.
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


package org.exolab.adaptx.xml.dom2xpn;

import org.exolab.adaptx.xpath.XPathNode;

import org.w3c.dom.*;

/**
 * A Namespace XPathNode, used by the
 * implementation of XPathNode for the W3C DOM API
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
class NamespaceXPathNode extends BaseDOMXPathNode {


    /**
     * The namespace prefix
    **/
    private String _nsPrefix  = null;
    
    /**
     * The namespace URI
    **/
    private String _nsURI     = null;
    
    /**
     * Creates a new NamespaceXPathNode
     *
     * @param prefix the namespace prefix, this may be null.
     * @param namespace the namespace uri, this cannot be null
     * @param parent the XPathNode that is the parent of this XPathNode.
     * This cannot be null.
    **/
    NamespaceXPathNode(String prefix, String namespace) {
        if (namespace == null) {
            String err = "namespace may not be null";
            throw new IllegalArgumentException(err);
        }
        _nsPrefix = prefix;
        _nsURI    = namespace;
    } //-- NamespaceXPathNode
    
    /**
     * Returns the type of this node.
     *
     * @return The type of this node
    **/
    public int getNodeType() {
        return XPathNode.NAMESPACE;
    } //-- getNodeType

    /**
     * Returns the local name of the node. Returns the local
     * name of an element or attribute, the prefix of a namespace
     * node, the target of a processing instruction, or null for
     * all other node types.
     *
     * @return The local name of the node, or null if the node has
     * no name
    **/
    public String getLocalName() {
        return _nsPrefix;
    } //-- getLocalName


    /**
     * Returns the namespace URI the node. Returns the namespace URI
     * of an element, attribute or namespace node,  or null for
     * all other node types.
     *
     * @return The namespace URI of the node, or null if the node has
     * no namespace URI
    **/
    public String getNamespaceURI() {
        return _nsURI;
    } //-- getNamespaceURI

    /**
     * Returns the string value of the node. The string value of a text
     * node or an attribute node is it's text value. The string value of
     * an element or a root node is the concatenation of the string value
     * of all its child nodes. The string value of a namespace node is its
     * namespace URI. The string value of a processing instruction is the
     * instruction, and the string value of a comment is the comment text.
     *
     * @return The string value of the node
    **/
    public String getStringValue() {
        return _nsURI;
    } //-- getStringValue

    /* protected methods */
    
    /**
     * Sets the "next" XPathNode sibling
     *
     * @param node the XPathNode which is the "next" sibling of this
     * XPathNode
    **/    
    void setNext(BaseDOMXPathNode node) {
        
        if ((node != null) && (node.getNodeType() != XPathNode.NAMESPACE)) {
            String err = "only a namespace node can be the sibling of "+
                "another namespace node";
            throw new IllegalArgumentException(err);
        }
        super.setNext(node);
    } //-- setNext

    /**
     * Sets the "previous" XPathNode sibling
     *
     * @param node the XPathNode which is the "previous" sibling of this
     * XPathNode
    **/    
    void setPrevious(BaseDOMXPathNode node) {
        
        if ((node != null) && (node.getNodeType() != XPathNode.NAMESPACE)) {
            String err = "only a namespace node can be the sibling of "+
                "another namespace node";
            throw new IllegalArgumentException(err);
        }
        super.setPrevious(node);
    } //-- setPrevious
    
    
} //-- NamespaceXPathNode
