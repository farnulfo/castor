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
 * Copyright 2002-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.tests.framework;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.jar.JarFile;

import java.net.URLClassLoader;
import java.net.URL;
import java.net.MalformedURLException;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import org.exolab.castor.tests.framework.testDescriptor.OnlySourceGenerationTest;
import org.exolab.castor.tests.framework.testDescriptor.TestDescriptor;
import org.exolab.castor.tests.framework.testDescriptor.MarshallingTest;
import org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest;
import org.exolab.castor.tests.framework.testDescriptor.SchemaTest;
import org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice;
import org.exolab.castor.tests.framework.testDescriptor.UnitTestCase;

/**
 * Abstracts a test case in the CTF (Castor Test Framework). A CTF test case can
 * be driven by a directory or by a JAR file.
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2004-03-08 17:23:25 -0700 (Mon, 08 Mar
 *          2004) $
 */
public class CastorTestCase extends TestCase {

    public static final short UNKNOWN   = -1;
    public static final short DIRECTORY = 0;
    public static final short JAR       = 1;

    /**
     * Name of the resource for the test descriptor XML document.
     */
    public static final String TEST_DESCRIPTOR = "TestDescriptor.xml";

    /**
     * Name of the resource for the test descriptor XML document if a JAR file is used.
     */
    private static final String TEST_DESCRIPTOR_JAR = "META-INF/TestDescriptor.xml";

    /**
     * File separator for this system.
     */
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    /**
     * True if we desire a lot of information on what is happening during the test.
     */
    private static final boolean _verbose;

    static {
        String v = System.getProperty(TestCaseAggregator.VERBOSE_PROPERTY);
        _verbose = (v != null && v.equals("true"));
        v = null;
    }

    /**
     * True if we dump the stack trace that is generated from any validation
     * exception or marshal exception caused by reading the XML Test
     * Description.
     */
    private static boolean _printStack;
    static {
        String v = System.getProperty(TestCaseAggregator.PRINT_STACK_TRACE);
        _printStack = (v != null && v.equals("true"));
        v = null;
    }

    /**
     * Indicates whether or not the output root directory has been compiled.
     */
    private boolean _compiled = false;

    /**
     * Class loader to use for the jar.
     */
    private ClassLoader _loader;

    /**
     * The test descriptor from the jar.
     */
    private TestDescriptor _testDescriptor;

    /**
     * The file that contains the tests. This can either be a directory or a jar
     * file.
     */
    private final File _testFile;
    /**
     * The Type of the test (directory or jar).
     */
    private final short _type;
    /**
     * Place where the temporary files and other output are created
     */
    private final File _outputRootFile;

    /**
     * Constructs a CTF test case given only a test case name
     * @param name the name of the test case
     */
    public CastorTestCase(String name) {
        super(name);
        _testFile       = null;
        _outputRootFile = null;
        _type           = UNKNOWN;
    }

    /**
     * Constructs a CTF test case given a File (either a JAR file or a
     * directory) and a directory where temporary files will be placed. The test
     * case name will be derived from the file (JAR or directory) name.
     *
     * @param file Either a directory containing TestDescriptor.xml or a JAR
     *            file containing META-INF/TestDescriptor.xml
     * @param outputRoot Directory where temporary files and output will go.
     */
    public CastorTestCase(File file, String outputRoot) {
        super(file.getName());

        if (file.isDirectory()) {
            _type = DIRECTORY;
            _outputRootFile = new File(outputRoot + FILE_SEPARATOR );
        } else {
            _type = JAR;
            try {
                new JarFile(file);
                String fileName = file.getName();
                fileName = fileName.substring(0, fileName.lastIndexOf("."));
                _outputRootFile = new File(outputRoot + FILE_SEPARATOR + fileName);
            } catch (java.util.zip.ZipException e) {
                throw new IllegalStateException(file.getAbsolutePath()+" is not a valid JAR file.");
            } catch (java.io.IOException ie) {
                throw new IllegalStateException(file.getAbsolutePath()+" is not a valid JAR file.");
            }
        }

        // Append to our current class loader the directory or JAR containing our test case
        try {
            URL[] urlList = {file.toURL()};
            _loader =  new URLClassLoader(urlList, this.getClass().getClassLoader());
        } catch (MalformedURLException urle) {
             //should never happen--> failure before
             urle.printStackTrace();
        }
        _testFile = file;
        _outputRootFile.mkdirs();
    }

    public ClassLoader getClassLoader() {
        return _loader;
    }

    public File getTestFile() {
        return _testFile;
    }

    public short getType() {
        return _type;
    }

    /**
     * Returns a boolean that when true indicates the output directory has been
     * compiled. This is useful for preventing the compilation of a directory
     * multiple times when more than one test case exists in a given directory.
     *
     * @return true when the output root directory has already been compiled.
     */
    public boolean isDirectoryCompiled() {
        return _compiled;
    } //-- isDirectoryCompiled


   /**
     * Sets the ClassLoader to use for loading the resources for this test case.
     *
     * @param loader the class loader to use
     */
    public void setClassLoader(ClassLoader loader) {
        _loader = loader;
    }

    /**
     * Sets a flag to indicate the output directory has been compiled. This
     * prevents compiling a directory multiple times unnecessarily when more
     * than one test case exists in a given directory.
     *
     * @param compiled true if the output directory for this test case has been
     *            compiled
     */
    public void setDirectoryCompiled(boolean compiled) {
        _compiled = compiled;
    } //-- setDirectoryCompiled

    /**
     * Assembles and returns a test suite containing all known tests.
     *
     * New tests should be added here!
     *
     * @return A non-null test suite if we can load the test descriptor
     */
    public Test suite() {
        final InputStream descriptor;
        if (_type == JAR) {
            descriptor = _loader.getResourceAsStream(TEST_DESCRIPTOR_JAR);
        } else {
            descriptor = _loader.getResourceAsStream(TEST_DESCRIPTOR);
        }

        if (descriptor == null) {
            verbose("test '" + _testFile.getName() + "' has no TestDescriptor.xml");
            return null;
        }

        try {
            _testDescriptor = TestDescriptor.unmarshal(new InputStreamReader(descriptor));
        } catch (ValidationException ve) {
            verbose("Error reading: " + _testFile.getAbsolutePath());
            verbose("-> " + ve.toString());
            if (_printStack) {
                ve.printStackTrace(System.out);
            }
            fail(ve.toString());
        } catch (MarshalException me) {
            verbose("Error reading: " + _testFile.getAbsolutePath());
            verbose("-> " + me.toString());
            if (_printStack) {
                me.printStackTrace(System.out);
            }
            fail(me.toString());
        } finally {
            try {
                descriptor.close();
            } catch (IOException e) {
                // ignore
            }
        }

        String suiteName = _testDescriptor.getName();
        TestSuite suite = new TestSuite(suiteName);

        verbose("Creating '" + suiteName + "' test suite");

        TestDescriptorChoice choice = _testDescriptor.getTestDescriptorChoice();
        MarshallingTest marshallingTests      = choice.getMarshallingTest();
        SourceGeneratorTest sourceGenTests    = choice.getSourceGeneratorTest();
        SchemaTest schemaTests                = choice.getSchemaTest();
        OnlySourceGenerationTest genOnlyTests = choice.getOnlySourceGenerationTest();

        if (marshallingTests != null) {
            setUpMarshallingTests(suiteName, suite, marshallingTests);
        }
        if (sourceGenTests != null) {
            setUpSourceGeneratorTests(suiteName, suite, sourceGenTests);
        }
        if (schemaTests != null) {
            setUpSchemaTests(suiteName, suite, schemaTests);
        }
        if (genOnlyTests != null) {
            setUpGenerationOnlyTests(suiteName, suite, genOnlyTests);
        }

        return suite;
    }

    /**
     * Loops over all Marshalling tests from our TestDescriptor.xml, configures
     * each test and adds it to our suite.
     *
     * @param suiteName Test Suite name
     * @param suite the Test Suite to add all unit tests to
     * @param mar a collection of Marshalling Unit Tests
     */
    private void setUpMarshallingTests(String suiteName, TestSuite suite, MarshallingTest mar) {
        for (int i=0; i < mar.getUnitTestCaseCount(); ++i) {
            UnitTestCase tc = mar.getUnitTestCase(i);
            MarshallingFrameworkTestCase mftc = new MarshallingFrameworkTestCase(this, tc, mar, _outputRootFile);
            mftc._configuration = mar.getConfiguration();
            mftc.setTestSuiteName(suiteName);
            suite.addTest(mftc.suite());
        }
    }

    /**
     * Loops over all Source Generation tests from our TestDescriptor.xml,
     * configures each test and adds it to our suite.
     *
     * @param suiteName Test Suite name
     * @param suite the Test Suite to add all unit tests to
     * @param sg a collection of Source Generation Unit Tests
     */
    private void setUpSourceGeneratorTests(String suiteName, TestSuite suite, SourceGeneratorTest sg) {
        for (int i=0; i < sg.getUnitTestCaseCount(); ++i) {
            UnitTestCase tc = sg.getUnitTestCase(i);
            SourceGeneratorTestCase sgtc = new SourceGeneratorTestCase(this, tc, sg, _outputRootFile);
            sgtc.setTestSuiteName(suiteName);
            suite.addTest(sgtc.suite());
        }
    }

    /**
     * Loops over all Schema tests from our TestDescriptor.xml, configures each
     * test and adds it to our suite.
     *
     * @param suiteName Test Suite name
     * @param suite the Test Suite to add all unit tests to
     * @param schemaTest a collection of Schema Unit Tests
     */
    private void setUpSchemaTests(String suiteName, TestSuite suite, SchemaTest schemaTest) {
        for (int i=0 ; i<schemaTest.getUnitTestCaseCount(); i++) {
            UnitTestCase tc = schemaTest.getUnitTestCase(i);
            // Little trick: getUnitTestCaseChoice should not be null at this point
            String name = tc.getUnitTestCaseChoice().getSchema();
            if (name.equals("*")) {
                File[] list = _testFile.listFiles();
                for (int j=0; j < list.length; ++j) {
                    String fileName = list[j].getName();
                    // FIXME:  It would be better to use a file filter and to make
                    // sure our SchemaReader can read this file
                    if (fileName.endsWith(FileServices.XSD)) {
                        makeIndividualSchemaTest(suiteName, suite, tc, fileName);
                    }
                }
            } else {
                makeIndividualSchemaTest(suiteName, suite, tc, name);
            }
        }
    }

    /**
     * Loops over all Only-Source-Generation tests from our TestDescriptor.xml,
     * configures each test and adds it to our suite.
     *
     * @param suiteName Test Suite name
     * @param suite the Test Suite to add all unit tests to
     * @param sg a collection of Source Generation Unit Tests
     */
    private void setUpGenerationOnlyTests(String suiteName, TestSuite suite, OnlySourceGenerationTest sg) {
        for (int i=0; i < sg.getUnitTestCaseCount(); ++i) {
            UnitTestCase tc = sg.getUnitTestCase(i);
            OnlySourceGenerationTestCase sgtc = new OnlySourceGenerationTestCase(this, tc, sg, _outputRootFile);
            sgtc.setTestSuiteName(suiteName);
            suite.addTest(sgtc.suite());
        }
    }

    /**
     * Makes an individual Schema test and adds it to our Test Suite
     *
     * @param suiteName Test Suite name
     * @param suite the Test Suite to add all unit tests to
     * @param tc our Test Case
     * @param name Schema name
     */
    private void makeIndividualSchemaTest(String suiteName, TestSuite suite, UnitTestCase tc, String name) {
        tc.setName(suiteName+'#'+name);
        SchemaTestCase stc = new SchemaTestCase(this, tc, _outputRootFile);
        stc.setSchemaName(name);
        suite.addTest(stc);
    }

    /**
     * Prints the provided message if verbose is true.
     *
     * @param message The message to display if verbose is true.
     */
    private void verbose(String message) {
        if (_verbose) {
            System.out.println(message);
        }
    }

}
