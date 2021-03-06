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

package org.exolab.javasource;

/**
 * Represents a primitive or class type.
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class JType {

    /**
     * JType for a boolean (Boolean).
     */
    public static final JType BOOLEAN = new JType("boolean", "Boolean");

    /**
     * JType instance for a byte (Byte).
     */
    public static final JType BYTE = new JType("byte", "Byte");

    /**
     * JType instance for a char (Char).
     */
    public static final JType CHAR = new JType("char", "Character");

    /**
     * JType instance for a double (Double).
     */
    public static final JType DOUBLE = new JType("double", "Double");

    /**
     * JType instance for a float (Float).
     */
    public static final JType FLOAT = new JType("float", "Float");

    /**
     * JType instance for a int (Integer).
     */
    public static final JType INT = new JType("int", "Integer");

    /**
     * JType instance for a long (Long).
     */
    public static final JType LONG = new JType("long", "Long");

    /**
     * JType instance for a short (Short).
     */
    public static final JType SHORT = new JType("short", "Short");

    /**
     * Fully qualified of the Java type represented.
     */
    private String _name = null;

    /**
     * Only populated for primitive types and indicates the wrapper Object class
     * name for this primitive type.
     */
    private String _wrapperName = null;

    /**
     * Creates a new JType with the given name.
     *
     * @param name
     *            the name of the type
     */
    protected JType(final String name) {
        super();
        this._name = name;
    } // -- JType

    /**
     * Creates a new JType for a primitive with the given name and wrapper name.
     * This constructor is private so it can only be used by the primitives
     * defined here.
     *
     * @param name
     *            the name of the type
     * @param wrapperName
     *            the name of the wrapper Object type for this primitive type
     */
    private JType(final String name, final String wrapperName) {
        this(name);
        this._wrapperName = wrapperName;
    }

    /**
     * Returns the unqualified Java type name (i.e. without package).
     *
     * @return the unqualified Java type name.
     */
    public final String getLocalName() {
        // -- use getName method in case it's been overloaded
        String name = getName();

        if (name == null) {
            return null;
        }
        int idx = name.lastIndexOf('.');
        if (idx >= 0) {
            name = name.substring(idx + 1);
        }
        return name;
    }

    /**
     * Returns the qualified Java type name.
     *
     * @return the qualified Java type name.
     */
    public final String getName() {
        return this._name;
    }

    /**
     * Return the name of the wrapper object for a primitive type, null for
     * non-primitive types.
     *
     * @return the name of the wrapper object for a primitive type, null for
     *         non-primitive types.
     */
    public final String getWrapperName() {
        return this._wrapperName;
    }

    /**
     * Checks to see if this JType represents a primitive type.
     *
     * @return true if this JType represents a primitive type, otherwise false.
     */
    public final boolean isPrimitive() {
        return ((this == JType.BOOLEAN) || (this == JType.BYTE)
                || (this == JType.CHAR) || (this == JType.DOUBLE)
                || (this == JType.FLOAT) || (this == JType.INT)
                || (this == JType.LONG) || (this == JType.SHORT));
    }

    /**
     * Returns true if this type represents an Array.  Always returns false
     * unless overridden by an extending class.
     * @return true if this type represents an Array.
     */
    public boolean isArray() {
        return false;
    }

    /**
     * Returns the String representation of this JType, which is simply the name
     * of this type.
     *
     * @return the String representation of this JType.
     */
    public String toString() {
        return this._name;
    }

    /**
     * Change the package this JType belongs to. This method is protected to
     * allow subtypes, such as JClass to alter the package to which this JType
     * belongs.
     *
     * @param newPackage
     *            the new package to which this JType belongs <BR>
     *            <B>Note:</B> The package name cannot be changed on a
     *            primitive type.
     */
    protected final void changePackage(final String newPackage) {
        if (this._name == null || this.isPrimitive()) {
            return;
        }

        String localName = null;
        int idx = _name.lastIndexOf('.');
        if (idx >= 0) {
            localName = this._name.substring(idx + 1);
        } else {
            localName = this._name;
        }

        if ((newPackage == null) || (newPackage.length() == 0)) {
            this._name = localName;
        } else {
            this._name = newPackage + "." + localName;
        }
    }

}
