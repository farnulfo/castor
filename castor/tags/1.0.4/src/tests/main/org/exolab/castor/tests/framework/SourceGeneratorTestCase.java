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
 * Copyright 2001-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.tests.framework;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.exolab.castor.builder.FieldInfoFactory;
import org.exolab.castor.builder.SourceGenerator;
import org.exolab.castor.tests.framework.testDescriptor.RootType;
import org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest;
import org.exolab.castor.tests.framework.testDescriptor.UnitTestCase;
import org.xml.sax.InputSource;

/**
 * This class encapsulate all the logic to run the tests patterns for the source
 * generator. It is able to run the source generator by itself and then compile
 * the file that have been generated.
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @author <a href="mailto:gignoux@kernelcenter.org">Sebastien Gignoux</a>
 * @version $Revision$ $Date: 2005-02-28 17:22:46 -0700 (Mon, 28 Feb 2005) $
 */
public class SourceGeneratorTestCase extends XMLTestCase {

    /**
     * Contains the information for the configuration for all the tests in this
     * jar.
     */
    protected final SourceGeneratorTest _sourceGenConf;

    /**
     * Name of the property file to use, null if none.
     */
    private String _propertyFileName;

    /**
     * Name of the collection to use by default, null if we rely on the default
     * behavior.
     */
    private String _fieldInfoFactoryName;

    /**
     * Name of the schema in the jar.
     */
    private String _schemaName;

    /**
     * Creates a new test case for the given setup.
     *
     * @param test the reference to the jar/directory
     * @param unit the UnitTestCase that wraps the configuration for this XML
     *            Test case.
     * @param sourceGen the Source Generator test to be executed
     * @param outputRoot the directory that contains the files needed for the
     *            test
     */
    public SourceGeneratorTestCase(CastorTestCase test, UnitTestCase unit, SourceGeneratorTest sourceGen, File outputRoot) {
        super(test, unit, outputRoot);
        _sourceGenConf  = sourceGen;
        _hasRandom      = _sourceGenConf.getRoot_Object().getRandom();
    }

    /**
     * Creates a new test case with the same setup as the
     * SourceGeneratorTestCase given in parameter.
     *
     * @param name name for the test case
     * @param sgtc the SourceGeneratorTestCase whose configuration we wish to
     *            copy for a new SourceGeneratorTestCase
     */
    public SourceGeneratorTestCase(String name, SourceGeneratorTestCase sgtc) {
        super(name, sgtc);
        _sourceGenConf = sgtc._sourceGenConf;
    }

    /**
     * Create a new SourceGeneratorTestCase with the given name.
     * @param name name for the test case
     */
    public SourceGeneratorTestCase(String name) {
        super(name);
        _sourceGenConf = null;
    }

    /**
     * Returns the test suite for this given test setup.
     * @return the test suite for this given test setup.
     */
    public Test suite() {
        TestSuite suite  = new TestSuite(_name);

        // Use the default test implemented in XMLTestCase
        String name = getTestSuiteName();
        name = (name != null) ? name + "#" + _name : _name;

        suite.addTest(new TestWithReferenceDocument(name, this));

        if (_hasRandom) {
            suite.addTest(new TestWithRandomObject(name, this));
        }

        return suite;
    }

    /**
     * Sets up this test suite.
     * @throws java.lang.Exception if anything goes wrong
     */
    protected void setUp() throws java.lang.Exception {
        verbose("\n================================================");
        verbose("Test suite '"+_test.getName()+"': setting up test '" + _name+"'");
        verbose("================================================\n");

        // 0. Get information to run the test
        _propertyFileName     = _sourceGenConf.getProperty_File();
        _fieldInfoFactoryName = _sourceGenConf.getCollection().toString();

        RootType rootType = _sourceGenConf.getRoot_Object();
        _rootClassName    = rootType.getContent();
        _hasDump          = rootType.getDump();
        _hasRandom        = rootType.getRandom();

        if (_rootClassName == null) {
            throw new Exception("No object root found in test descriptor");
        }

        // 1. Run the source generator
        verbose("-->Running the source generator");
        SourceGenerator sourceGen = null;

        if (_fieldInfoFactoryName != null) {
            FieldInfoFactory factory = new FieldInfoFactory(_fieldInfoFactoryName);
            sourceGen = new SourceGenerator(factory);
        } else {
            sourceGen = new SourceGenerator();
        }

        if (_propertyFileName != null) {
            Properties prop = new Properties();
            prop.load(_test.getClassLoader().getResourceAsStream(_propertyFileName));
            sourceGen.setDefaultProperties(prop);
        } else {
            //don't forget to reset the properties
            sourceGen.setDefaultProperties(null);
        }

        //. Move the files in the tmp directory
        FileServices.copySupportFiles(_test.getTestFile(), _outputRootFile);

        String bindingName = _sourceGenConf.getBindingFile();
        if (bindingName != null && bindingName.length() >0) {
            File bindingFile = new File(_outputRootFile, bindingName);

            if ( !bindingFile.exists()) {
                fail("Unable to find the specified binding file: " + bindingName);
            }

            verbose("using binding file: " + bindingFile.getAbsolutePath());
            InputSource source = new InputSource(new FileReader(bindingFile));
            source.setSystemId(bindingFile.getAbsolutePath());
            sourceGen.setBinding(source);

            bindingFile = null;
            bindingName = null;
        }
        // equals() is needed to compare two objects
        sourceGen.setEqualsMethod(true);
        sourceGen.setTestable(true);
        sourceGen.setSuppressNonFatalWarnings(true);
        sourceGen.setFailOnFirstError(true);

        sourceGen.setDestDir(_outputRootFile.getAbsolutePath());

        String[] schemas = _sourceGenConf.getSchema();
        for (int i=0; i<schemas.length; i++) {
            _schemaName = schemas[i];
            _schemaFile = new File(_outputRootFile, _schemaName);

            if ( !_schemaFile.exists()) {
                assertNotNull("Unable to find the schema:", _schemaName);
            }

            InputSource source = new InputSource(new FileReader(_schemaFile));
            source.setSystemId(_schemaFile.getAbsolutePath());

            try {
                sourceGen.generateSource(source, null);
            } catch (Exception e) {
                if (_failure != null && checkExceptionWasExpected(e)) {
                    assertTrue(_failure.getContent());
                    return;
                }
                if (_printStack) {
                    e.printStackTrace(System.out);
                }
                fail("Source Generator threw an exception: " + e.getMessage());
            }

            // 2. Compile the file generated by the source generator
            verbose("-->Compiling the files in " + _outputRootFile);
            Compiler compiler = new JavaCCompiler(_outputRootFile);
            try {
                compiler.compileDirectory();
            } catch (CompilationException e) {
                if (_failure != null && checkExceptionWasExpected(e)) {
                    assertTrue(_failure.getContent());
                    return;
                }
                if (_printStack) {
                    e.printStackTrace(System.out);
                }
                fail("Build Failed: " + e.getMessage());
            }

            // 3. Nest the class loader to look into the tmp dir
            //don't forget to add the previous path
            URL[] urlList = {_test.getTestFile().toURL(), _outputRootFile.toURL()};
            ClassLoader loader =  new URLClassLoader(urlList, _test.getClass().getClassLoader());
            _test.setClassLoader(loader);
        }

        // 4. Set up the root class
        _rootClass =  _test.getClassLoader().loadClass(_rootClassName);
    }

    /**
     * Cleans up after this unit test (nothing to do except provide output).
     * @throws java.lang.Exception never
     */
    protected void tearDown() throws java.lang.Exception {
        verbose("\n================================================");
        verbose("Test suite '"+_test.getName()+"': test '" + _name+"' complete.");
        verbose("================================================\n");
    }

}
