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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 * $Id$
 */

package org.exolab.castor.builder.types;

import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.types.GMonthDay;
import org.exolab.javasource.JType;
import org.exolab.javasource.JClass;

import java.text.ParseException;
import java.util.Enumeration;
/**
 * The XML Schema gYearMonth type
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision $ $Date$
 */

public class XSGMonthDay extends XSType {

    /**
     * The JType represented by this XSType
     */
    private static final JType jType
        = new JClass ("org.exolab.castor.types.GMonthDay");

    private GMonthDay _maxInclusive;
    private GMonthDay _maxExclusive;
    private GMonthDay _minInclusive;
    private GMonthDay _minExclusive;

    public XSGMonthDay() {
       super(XSType.GMONTHDAY_TYPE);
    }

    /**
     * Returns the Java code necessary to create a new instance of the
     * JType associated with this XSType
     */
    public String newInstanceCode() {
         return "new "+getJType().getName()+"();";
    } //-- newInstanceCode

    public JType getJType() {
        return this.jType;
    }

    /**
     * Returns the maximum exclusive value that this XSGMonthDay can hold.
     * @return the maximum exclusive value that this XSGMonthDay can hold. If
     * no maximum exclusive value has been set, Null will be returned
     * @see getMaxInclusive
    **/
    public GMonthDay getMaxExclusive() {
        return _maxExclusive;
    } //-- getMaxExclusive

    /**
     * Returns the maximum inclusive value that this XSGMonthDay can hold.
     * @return the maximum inclusive value that this XSGMonthDay can hold. If
     * no maximum inclusive value has been set, Null will be returned
     * @see getMaxExclusive
    **/
    public GMonthDay getMaxInclusive() {
        return _maxInclusive;
    } //-- getMaxInclusive


    /**
     * Returns the minimum exclusive value that this XSGMonthDay can hold.
     * @return the minimum exclusive value that this XSGMonthDay can hold. If
     * no minimum exclusive value has been set, Null will be returned
     * @see getMinInclusive
     * @see setMaxInclusive
    **/
    public GMonthDay getMinExclusive() {
        return _minExclusive;
    } //-- getMinExclusive

    /**
     * Returns the minimum inclusive value that this XSGMonthDay can hold.
     * @return the minimum inclusive value that this can XSGMonthDay hold. If
     * no minimum inclusive value has been set, Null will be returned
     * @see getMinExclusive
    **/
    public GMonthDay getMinInclusive() {
        return _minInclusive;
    } //-- getMinInclusive

    /**
     * Sets the maximum exclusive value that this XSGMonthDay can hold.
     * @param max the maximum exclusive value this XSGMonthDay can be
     * @see setMaxInclusive
    **/
    public void setMaxExclusive(GMonthDay max) {
        _maxExclusive = max;
        _maxInclusive = null;
    } //-- setMaxExclusive

    /**
     * Sets the maximum inclusive value that this XSGMonthDay can hold.
     * @param max the maximum inclusive value this XSGMonthDay can be
     * @see setMaxExclusive
    **/
    public void setMaxInclusive(GMonthDay max) {
        _maxInclusive = max;
        _maxExclusive = null;
    } //-- setMaxInclusive


    /**
     * Sets the minimum exclusive value that this XSGMonthDay can hold.
     * @param max the minimum exclusive value this XSGMonthDay can be
     * @see setMinInclusive
    **/
    public void setMinExclusive(GMonthDay min) {
        _minExclusive = min;
        _minInclusive = null;
    } //-- setMinExclusive

    /**
     * Sets the minimum inclusive value that this XSGMonthDay can hold.
     * @param max the minimum inclusive value this XSGMonthDay can be
     * @see setMinExclusive
    **/
    public void setMinInclusive(GMonthDay min) {
        _minInclusive = min;
        _minExclusive = null;
    } //-- setMinInclusive

    public boolean hasMinimum() {
        return ( (_minInclusive != null) || (_minExclusive != null) );
    }


    public boolean hasMaximum() {
       return ( (_maxInclusive != null) || (_maxExclusive != null) );
    }

    /**
     * Reads and sets the facets for XSXSGMonthDay
     * override the readFacet method of XSType
     * @param simpletype the Simpletype containing the facets
     * @param xsType the XSType to set the facets of
     * @see org.exolab.castor.builder.xstype#readFacets
     */

    public void setFacets(SimpleType simpleType)
    {
        //-- copy valid facets
        Enumeration enum = getFacets(simpleType);
        while (enum.hasMoreElements()) {

            Facet facet = (Facet)enum.nextElement();
            String name = facet.getName();

            try {
                //-- maxExclusive
                if (Facet.MAX_EXCLUSIVE.equals(name))
                    this.setMaxExclusive(GMonthDay.parseGMonthDay(facet.getValue()));
                //-- maxInclusive
                else if (Facet.MAX_INCLUSIVE.equals(name))
                    this.setMaxInclusive(GMonthDay.parseGMonthDay(facet.getValue()));
                //-- minExclusive
                else if (Facet.MIN_EXCLUSIVE.equals(name))
                    this.setMinExclusive(GMonthDay.parseGMonthDay(facet.getValue()));
                //-- minInclusive
                else if (Facet.MIN_INCLUSIVE.equals(name))
                    this.setMinInclusive(GMonthDay.parseGMonthDay(facet.getValue()));
                //-- pattern
                else if (Facet.PATTERN.equals(name)) {
                    //do nothing for the moment
                    System.out.println("Warning: The facet 'pattern' is not currently supported for XSGMonthDay.");
                }
            } catch (ParseException e) {
                //not possible to set the facet properly
                //This can't happen since a ParseException would have been set
                //during the unmarshalling of the facets
                e.printStackTrace();
                return;
            }
        }//while

    }//setFacets
}