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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 */
package ctf.jdo.tc1x;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.TimeStampable;

/**
 * Test object
 */
public final class TimeStampableObject implements TimeStampable {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(TimeStampableObject.class);
    
    public static final    int         DEFAULT_ID = 3;
    public static final    String      DEFAULT_VALUE1 = "one";
    public static final    String      DEFAULT_VALUE2 = "two";

    private         int         _id;
    private         String      _value1;
    private         String      _value2;
    private         long        _timeStamp;

    public TimeStampableObject() {
        _id = DEFAULT_ID;
        _value1 = DEFAULT_VALUE1;
        _value2 = DEFAULT_VALUE2;
    }

    public void setId(final int id) { _id = id; }
    public int getId() { return _id; }

    public void setValue1(final String value1) { _value1 = value1; }
    public String getValue1() { return _value1; }

    public void setValue2(final String value2) { _value2 = value2; }
    public String getValue2() { return _value2; }

    public String toString() {
        return _id + " / " + _value1 + " / " + _value2;
    }

    public void jdoSetTimeStamp(final long timeStamp) {
        LOG.debug("CHANGING TIMESTAMP FROM: " + _timeStamp + " TO: " + timeStamp);
        _timeStamp = timeStamp;
    }

    public long jdoGetTimeStamp() {
        LOG.debug("GRABBING TIMESTAMP " + _timeStamp);
        return _timeStamp;
    }
}
