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


import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.KeyGeneratorFactoryRegistry;
import org.exolab.castor.persist.spi.KeyGenerator;
import org.exolab.castor.persist.spi.KeyGeneratorFactory;
import org.exolab.castor.persist.spi.PersistenceFactory;


/**
 * Registry for {@link KeyGenerator} instances.
 * Is used to create no more than one instance of the give type with 
 * the given parameters.
 * 
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @author <a href="mailto:ferret AT frii dot com">Bruce Snyder</a>
 * @version $Revision$ $Date: 2006-04-10 16:39:24 -0600 (Mon, 10 Apr 2006) $
 */
final class KeyGeneratorRegistry {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static Log _log = LogFactory.getFactory().getInstance(KeyGeneratorRegistry.class);

    /**
     * Association between key generator names (aliases) and instances.
     */
    private Hashtable _keyGenerators = new Hashtable();

    /**
     * Returns a key generator based upon the specified (key generator) description
     * for the specified persistence factory.
     *
     * @param factory The persistence factory
     * @param descriptor The key generator description
     * @param sqlType SQL type identifier.
     * @return The {@link KeyGenerator} instance to be used.
     */
    public KeyGenerator getKeyGenerator(final PersistenceFactory factory,
            final KeyGeneratorDescriptor descriptor, final int sqlType) throws MappingException {
        KeyGeneratorFactory keyGeneratorFactory;
        String keyGeneratorName = descriptor.getName() + " " + sqlType;
        
        // check whether there's already a valid KeyGenerator instance registered.
        KeyGenerator keyGenerator = (KeyGenerator) _keyGenerators.get(keyGeneratorName);

        if (keyGenerator == null) {
            keyGeneratorFactory = KeyGeneratorFactoryRegistry.getKeyGeneratorFactory(
                    descriptor.getKeyGeneratorFactoryName());

            if (keyGeneratorFactory != null) {
                keyGenerator = 
                    keyGeneratorFactory.getKeyGenerator(factory, descriptor.getParams(), sqlType);
                if (keyGenerator != null) {
                    if (_log.isDebugEnabled()) {
                        _log.debug("Key generator " + descriptor.getKeyGeneratorFactoryName()
                                + " has been instantiated, parameters: " + descriptor.getParams());
                    }
                }
            }
            if (keyGenerator == null) {
                /*
                 * Don't throw exception here, just notify and continue without
                 * key generator
                 */
                _log.warn(Messages.format(
                        "mapping.noKeyGen", descriptor.getKeyGeneratorFactoryName()));
                return null;
            }
            _keyGenerators.put(keyGeneratorName, keyGenerator);
        }
        
        return keyGenerator;
    }
}
