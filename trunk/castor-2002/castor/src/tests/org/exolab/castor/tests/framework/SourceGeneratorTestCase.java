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

package org.exolab.castor.tests.framework;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.exolab.castor.tests.framework.testDescriptor.UnitTestCase;
import org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest;

import sun.misc.URLClassPath;

import java.net.URLClassLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.File;

import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.exolab.castor.builder.SourceGenerator;

import java.util.Vector;
import java.util.Enumeration;

/**
 * This class encapsulate all the logic to run the tests patterns for the source
 * generator. It is able to run by itself the source generator and compile the
 * file that have been generated.
 *
 * @author <a href="mailto:gignoux@intalio.com">Sebastien Gignoux</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
 */
public class SourceGeneratorTestCase extends TestCase {

    /**
     * Name of this test
     */
    protected String _name;

    /**
     * The jarTest in which this test is specified
     */
    protected CastorJarTestCase _jarTest;

    /**
     * Contain the information for the configuration of all the test of this jar.
     */
    protected SourceGeneratorTest _sourceGenConf;

    /**
     * The unit test case this class represent
     */
    protected UnitTestCase _unitTest;

    /**
     * Place where the temporary file have to be put
     */
    protected File _outputRootFile;

    /**
     * True if we expect a lot of info on what happen.
     */
    private static boolean _verbose;

    static {
        String v = System.getProperty(TestCaseAggregator.VERBOSE_PROPERTY);
        if (v!=null && v.equals("true"))
            _verbose = true;
        else
            _verbose = false; 
    }

    /**
     * Name of the schema in the jar
     */
    private String _schemaName;

    /**
     * File for the schema in the temp directory
     */
    private File _schemaFile;

    /**
     * Create a new test case for the given setup.
     */
    public SourceGeneratorTestCase(CastorJarTestCase jarTest, UnitTestCase unit, SourceGeneratorTest sourceGen, File outputRoot) {
        super(unit.getName());
        _name           = unit.getName();
        _jarTest        = jarTest;
        _sourceGenConf  = sourceGen;
        _unitTest       = unit;
        _outputRootFile = outputRoot;
    }

    /**
     * Create a new test case with the same setup as the
     * MarshallingFrameworkTestCase given in parameter.
     */
    public SourceGeneratorTestCase(String name, SourceGeneratorTestCase sgtc) {
        super(name);
        _name           = sgtc._name;
        _jarTest        = sgtc._jarTest;
        _sourceGenConf  = sgtc._sourceGenConf;
        _unitTest       = sgtc._unitTest;
        _outputRootFile = sgtc._outputRootFile;
        
    }
    
    /**
     * Create a new MarshallingFrameworkTestCase with the given name.
     */
    public SourceGeneratorTestCase(String name) {
        super(name);
        _name = name;
    }


    /**
     * Return the test suite for this given test setup.
     */
    public Test suite() {

        TestSuite suite  = new TestSuite(_name);
        
         suite.addTest(new SourceGeneratorTestCase("dummy", this));
//         suite.addTest(new SourceGeneratorTestCase("testWithRandomObject", this));
        
        return suite;
    }
    
    /**
     * Setup this test suite. Load the mapping file if any.
     */
    protected void setUp()
        throws java.lang.Exception {

        verbose("\n========================================");
        verbose("Setting up test for '" + _name + "' from '" + _jarTest.getName() + "'");

        // 1. Move the support file into tmp dir
        _schemaName = _sourceGenConf.getSchema();
        assertNotNull("Unable to find the name of the schema", _schemaName);

        copySupportFiles(_outputRootFile);

        _schemaFile = new File(_outputRootFile, _schemaName);

        // 2. Run the source generator
        verbose("Running the source generator");
        SourceGenerator sourceGen = new SourceGenerator();
        sourceGen.setDestDir(_outputRootFile.getAbsolutePath());
        sourceGen.generateSource(new FileReader(_schemaFile), null);

        // 3. Compile the file generated by the source generator
        verbose("Compiling the files");
        Vector fileList = CompilerHelper.findAllJavaFiles(_outputRootFile, new Vector());

        assert("Unable to find the file generated by the source compiler", fileList.size() != 0);
            
        CompilerHelper.compile(_outputRootFile.getAbsolutePath(), fileList, null);

        // 4. Nest the class loader to look into the tmp dir
        ClassLoader loader =  new URLClassLoader(URLClassPath.pathToURLs(_outputRootFile.getAbsoluteFile().toString()),
                                                 _jarTest.getClassLoader());
        _jarTest.setClassLoader(loader);
    }

    
    public void dummy() 
        throws java.lang.Exception {

        System.out.println(_sourceGenConf.getRootObject().getContent());

        System.out.println(_jarTest.getClassLoader().loadClass(_sourceGenConf.getRootObject().getContent()).newInstance());

    }


    /**
     * Clean up the tests.
     */
    protected void tearDown()
        throws java.lang.Exception {
        
        verbose("Test for '" + _name + "' complete");
        verbose("========================================");

    }


    /**
     * print the message if in verbose mode.
     */
    private void verbose(String message) {
        if (_verbose)
            System.out.println(message);
    }

    
    /**
     * Copy all the support files ('.xsd' and '.java' file) located in the jar
     * into the given directory
     */
    private void copySupportFiles(File root) 
        throws IOException, FileNotFoundException {

        JarFile jar = new JarFile(_jarTest.getJarFile());
        for (Enumeration e = jar.entries(); e.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry)e.nextElement();
            if (isSupportFile(entry.getName())) {
                verbose("Moving file '" + entry.getName() + "' into the output directory");
                InputStream src = jar.getInputStream(entry);
                File out = new File(root, entry.getName());
                out.getParentFile().mkdirs();
                copy(src, new FileOutputStream(out));
            }
        }
    }


    /**
     * Return true if the file is a support file for the test. A support file is
     * a schema or a java file.
     */
    private boolean isSupportFile(String name) {
        return ((name.endsWith(".xsd")) || (name.endsWith(".java")));
    }

    /**
     * Copy an InputStream into a OutputStream
     */
    private void copy(InputStream src, OutputStream dst) 
        throws FileNotFoundException, IOException {
        
        // use a 4K buffer
        final int BUF_SIZE = 4096;
        byte[] buf = new byte[BUF_SIZE];
        int read;
        while (true) {
            read = src.read(buf, 0, BUF_SIZE);
            if (read == -1) {
                break;
            }
            dst.write(buf, 0, read);
        }
    }

}
