/*
 * Copyright 2007 Joachim Grueneis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.jaxb.resolver;

import java.util.HashMap;
import java.util.Map;

import org.exolab.castor.xml.ResolverException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Joachim Grueneis, jgrueneis AT codehaus DOT org
 * @version $Id$
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/castor-jaxb-test-context.xml" })
public class JAXBPackageResolverCommandTest {

    @Autowired
    private JAXBPackageResolverCommand command;

    private Map < String, Object > _propertiesMap = 
        new HashMap < String, Object > ();

    /**
     * Test method for {@link org.castor.jaxb.resolver.JAXBPackageResolverCommand#resolve(
     * java.lang.String, java.util.Map)}.
     */
    @Test
    public void testResolve() {
        try {
            Map descriptorMap = command.resolve(null, _propertiesMap);
            Assert.assertTrue("Resolving null should lead to an empty map", 
                    descriptorMap.isEmpty());
        } catch (ResolverException e) {
            Assert.fail("Failed with exception: " + e);
        }
    }

    @Test
    @Ignore
    public void testYetNotImplemented() {
        try {
            command.resolve("org.castor.jaxb.resolver", _propertiesMap);
            Assert.fail("We assume that it is still not yet implemented!");
        } catch (ResolverException e) {
            Assert.fail("Failed with exception: " + e);
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
}
