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


package org.exolab.castor.jdo.engine;


import org.exolab.castor.persist.Cache;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.mapping.xml.CacheTypeMapping;


public class CacheType {
    int _cacheType = 1;
    //CacheEngine.DEFAULT_CACHE_TYPE;

    int _capacity = 30;
    //CacheEngine.DEFAULT_CACHE_VALUE;

    int DEFAULT_TIME_LIMIT = 30;
    int DEFAULT_COUNT_LIMIT = 30;

    public CacheType( CacheTypeMapping cache ) {
        if ( cache == null ) return;

        if ( cache.getType().equals("none") ) {
            _cacheType = Cache.CACHE_NONE;
            _capacity = cache.getCapacity();

        } else if ( cache.getType().equals("count-limited") ) {
            _cacheType = Cache.CACHE_COUNT_LIMITED;
            _capacity = cache.getCapacity();
            if ( _capacity == 0 ) {
                _capacity = DEFAULT_COUNT_LIMIT;
            }

        } else if ( cache.getType().equals("time-limited") ) {
            _cacheType = Cache.CACHE_TIME_LIMITED;
            _capacity = cache.getCapacity();
            if ( _capacity == 0 ) {
                _capacity = DEFAULT_TIME_LIMIT;
            }

        } else if ( cache.getType().equals("unlimited") ) {
            _cacheType = Cache.CACHE_UNLIMITED;
            _capacity = cache.getCapacity();
        }
    }
    public int getType() {
        return _cacheType;
    }
    public int getCapacity() {
        return _capacity;
    }
}