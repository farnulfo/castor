/*
 * Copyright 2008 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.query.ejbql;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Junit test for analyzing the performance of CastorQL Parser created by JavaCC
 * 4.0 for reduced EJBQL for more information refer to the issue CASTOR-2396
 * 
 * @author <a href="mailto:mailtoud AT gmail DOT com">Udai Gupta</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.3
 */
public class TestEjbQLParser extends TestCase {
    // --------------------------------------------------------------------------

    @Test
    public static void testParserPerformance() {
        System.out.println("start : Ejb");

        // Modify to test more
        String oql = "Select DisTinct o.item "
                + "from de.jsci.pcv.jdo.LieferantJDO as o "
                + "where o.deleted = false " + "order by o.name, o.id desc "
                + "limit ?1, :2";

        long start = System.nanoTime();
        runParser(oql);
        long stop = System.nanoTime();

        System.out.println("end : Ejb Parsed :" + oql);

        System.out.println(" in " + (stop - start) / 1000000.0 + " milliseconds");
    }

    // --------------------------------------------------------------------------

    /**
     * Method which passes the OQL String to CastorQL Parser
     */
    public static void runParser(final String oql) {

        StringBuffer stringBuffer = new StringBuffer(oql);

        EjbQLParser parser = null;
        try {
            InputStream bis = new ByteArrayInputStream(stringBuffer.toString().getBytes("UTF-8"));
            InputStreamReader isr = new InputStreamReader(bis, "UTF-8");
            SimpleCharStream jSt = new SimpleCharStream(isr);
            EjbQLParserTokenManager tkmgr = new EjbQLParserTokenManager(jSt);
            parser = new EjbQLParser(tkmgr);
            parser.ejbQL();
        } catch (Exception e) {
            e.printStackTrace();
            parser.ReInit(System.in);
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    // --------------------------------------------------------------------------
}