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
 * Copyright 2001-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.tests.framework;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;
import junit.framework.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.types.*;
import org.exolab.castor.builder.*;
import org.exolab.castor.tests.framework.testDescriptor.*;
import org.xml.sax.InputSource;

/**
 * This class encapsulate all the logic to run the tests patterns for the source
 * generator. It is able to run by itself the source generator and compile the
 * file that have been generated.
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @author <a href="mailto:gignoux@kernelcenter.org">Sebastien Gignoux</a>
 * @version $Revision$ $Date$
 */
public class SourceGeneratorTestCase extends XMLTestCase {

    /**
     * Contain the information for the configuration of all the test of this jar.
     */
    protected SourceGeneratorTest _sourceGenConf;

    /**
     * Name of the property file to use. Null if any
     */
    private String _propertyFileName;

    /**
     * Name of the collection to use by default. Null if we rely on the default
     * behavior
     */
    private String _fieldInfoFactoryName;

    /**
     * the compilation ANT task
     */
    private Javac _compileTask;

    /**
     * Name of the schema in the jar
     */
    private String _schemaName;

    /**
     * Create a new test case for the given setup.
     */
    public SourceGeneratorTestCase(CastorTestCase test, UnitTestCase unit, SourceGeneratorTest sourceGen, File outputRoot) {
        super(test, unit, outputRoot);
        _sourceGenConf  = sourceGen;
        _hasRandom      = _sourceGenConf.getRoot_Object().getRandom();
        initCompileTask();
    }

    /**
     * Create a new test case with the same setup as the
     * MarshallingFrameworkTestCase given in parameter.
     */
    public SourceGeneratorTestCase(String name, SourceGeneratorTestCase sgtc) {
        super(name, sgtc);
        _sourceGenConf  = sgtc._sourceGenConf;
        initCompileTask();
    }

    /**
     * Create a new MarshallingFrameworkTestCase with the given name.
     */
    public SourceGeneratorTestCase(String name) {
        super(name);
        _name = name;
        initCompileTask();
    }

    private void initCompileTask() {
        if (_compileTask == null) {
            Project project = new Project();
            project.init();
            _compileTask = new Javac();
            project.setBasedir(".");
            _compileTask.setProject(project);
            _compileTask.setFork(true);
        }
    }


    /**
     * Return the test suite for this given test setup.
     */
    public Test suite() {

        TestSuite suite  = new TestSuite(_name);

        // Use the default test implemented in XMLTestCase
        suite.addTest(new SourceGeneratorTestCase("testWithReferenceDocument", this));

        if (_hasRandom)
            suite.addTest(new SourceGeneratorTestCase("testWithRandomObject", this));

        return suite;
    }

    /**
     * Setup this test suite. Load the mapping file if any.
     */
    protected void setUp()
        throws java.lang.Exception {

        verbose("\n========================================");
        verbose("Setting up test for '" + _name + "' from '" + _test.name() + "'");
        verbose("\n========================================");

        // 0. Get information to run the test
        _propertyFileName     = _sourceGenConf.getProperty_File();

        _fieldInfoFactoryName = _sourceGenConf.getFieldInfoFactory();

        _inputName  = _unitTest.getInput();
        _goldFileName = _unitTest.getOutput();

        if (_inputName != null)
            _input  = _test.getClassLoader().getResourceAsStream(_inputName);

        Root_Object rootType = _sourceGenConf.getRoot_Object();
        _rootClassName      = rootType.getContent();
        _hasDump            = rootType.getDump();
        _hasRandom          = rootType.getRandom();

        if (_rootClassName == null)
            throw new Exception("No object root found in test descriptor");

        _schemaName = _sourceGenConf.getSchema();
        _schemaFile = new File(_outputRootFile, _schemaName);

        if ( ! _schemaFile.exists()) {
            // 1. Move the support file into tmp dir
            assertNotNull("Unable to find the name of the schema", _schemaName);

            FileServices.copySupportFiles(_test.getTestFile(), _outputRootFile);

            // 2. Run the source generator
            verbose("-->Running the source generator");
            SourceGenerator sourceGen = null;

            if (_fieldInfoFactoryName != null) {
                Class factoryClass = _test.getClassLoader().loadClass(_fieldInfoFactoryName);
                FieldInfoFactory factory = (FieldInfoFactory)factoryClass.newInstance();
                sourceGen = new SourceGenerator(factory);
            } else
                sourceGen = new SourceGenerator();

            if (_propertyFileName != null) {
                Properties prop = new Properties();
                prop.load(_test.getClassLoader().getResourceAsStream(_propertyFileName));
                sourceGen.setDefaultProperties(prop);
            }
            //don't forget to reset the properties
            else sourceGen.setDefaultProperties(null);
           // equals() is needed to compare two objects
            sourceGen.setEqualsMethod(true);
            sourceGen.setTestable(true);
            sourceGen.setSuppressNonFatalWarnings(true);

            sourceGen.setDestDir(_outputRootFile.getAbsolutePath());
            InputSource source = new InputSource(new FileReader(_schemaFile));
            source.setSystemId(_schemaFile.getAbsolutePath());
            sourceGen.generateSource(source, null);

            // 3. Compile the file generated by the source generator
            verbose("-->Compiling the files");

            //INVOKE ANT API FOR COMPILATION
            _compileTask.setDestdir(_outputRootFile.getAbsoluteFile());
            Path classpath = _compileTask.createClasspath();
            classpath.setPath(System.getProperty("java.class.path"));
            _compileTask.setClasspath(classpath);

            Path sourcepath = _compileTask.createSrc();
            sourcepath.setLocation(_outputRootFile);
            _compileTask.setSrcdir(sourcepath);
            _compileTask.execute();
        }

        // 4. Nest the class loader to look into the tmp dir
        //don't forget to add the previous path
        URL[] urlList = {_test.getTestFile().toURL(), _outputRootFile.toURL()};
        ClassLoader loader =  new URLClassLoader(urlList, _test.getClass().getClassLoader());
        _test.setClassLoader(loader);

        // 5. Set up the root class
        _rootClass =  _test.getClassLoader().loadClass(_rootClassName);
    }

    /**
     * Clean up the tests.
     */
    protected void tearDown()
        throws java.lang.Exception {

        verbose("Test for '" + _name + "' complete");
        verbose("========================================");
    }

}
