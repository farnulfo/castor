package ptf.jdo.rel1toN;

import java.text.DecimalFormat;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.engine.DatabaseImpl;

public final class TestLoadBiNto1 extends TestCase {
    private static final String JDO_CONF_FILE = "bi-jdo-conf.xml";
    private static final String DATABASE_NAME = "rel1toN_bi";
    
    private static final DecimalFormat df = new DecimalFormat("#,##0");
    
    private static final Log LOG = LogFactory.getLog(TestLoadBiNto1.class);
    private static boolean _logHeader = false;
    
    private JDOManager _jdo = null;
    
    private String[]   _tests = new String[8]; 
    private long[][]   _times = new long[8][5];

    public static Test suite() throws Exception {
        String config = TestLoadBiNto1.class.getResource(JDO_CONF_FILE).toString();
        JDOManager.loadConfiguration(config, TestLoadBiNto1.class.getClassLoader());
        
        TestSuite suite = new TestSuite("Test load n:1 with bidirectional mapping");

        suite.addTest(new TestLoadBiNto1("testReadWriteEmpty"));
        suite.addTest(new TestLoadBiNto1("testReadWriteCached"));
        suite.addTest(new TestLoadBiNto1("testReadWriteOidEmpty"));
        suite.addTest(new TestLoadBiNto1("testReadWriteOidCached"));

        suite.addTest(new TestLoadBiNto1("testReadOnlyEmpty"));
        suite.addTest(new TestLoadBiNto1("testReadOnlyCached"));
        suite.addTest(new TestLoadBiNto1("testReadOnlyOidEmpty"));
        suite.addTest(new TestLoadBiNto1("testReadOnlyOidCached"));

        suite.addTest(new TestLoadBiNto1("testReadOnlyOidOnly"));

        return suite;
    }

    public TestLoadBiNto1() {
        super();
    }
    
    public TestLoadBiNto1(final String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();

        if (!_logHeader) {
            LOG.info(format("", "begin", "result", "iterate", "commit", "close"));
            _logHeader = true;
        }

        _jdo = JDOManager.createInstance(DATABASE_NAME);
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testReadWriteEmpty() throws Exception {
        long start = System.currentTimeMillis();
        
        DatabaseImpl db = (DatabaseImpl) _jdo.getDatabase();
        db.getCacheManager().expireCache();
        db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = db.getOQLQuery(
                "SELECT o FROM " + Service.class.getName() + " o order by o.id");
        QueryResults results = query.execute();
        
        long result = System.currentTimeMillis();
        
        int count = 0;
        while (results.hasMore()) {
            Object object = results.next();

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        db.commit();
        
        long commit = System.currentTimeMillis();
        
        db.close();

        long close = System.currentTimeMillis();
        
        LOG.info(format("ReadWriteEmpty",
                         df.format(begin - start),
                         df.format(result- begin),
                         df.format(iterate - result),
                         df.format(commit - iterate),
                         df.format(close - commit)));
    }
    
    public void testReadWriteCached() throws Exception {
        long start = System.currentTimeMillis();
        
        DatabaseImpl db = (DatabaseImpl) _jdo.getDatabase();
        db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = db.getOQLQuery(
                "SELECT o FROM " + Service.class.getName() + " o order by o.id");
        QueryResults results = query.execute();
        
        long result = System.currentTimeMillis();
        
        int count = 0;
        while (results.hasMore()) {
            Object object = results.next();

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        db.commit();
        
        long commit = System.currentTimeMillis();
        
        db.close();

        long close = System.currentTimeMillis();
        
        LOG.info(format("ReadWriteCached",
                         df.format(begin - start),
                         df.format(result- begin),
                         df.format(iterate - result),
                         df.format(commit - iterate),
                         df.format(close - commit)));
    }
    
    public void testReadOnlyEmpty() throws Exception {
        long start = System.currentTimeMillis();
        
        DatabaseImpl db = (DatabaseImpl) _jdo.getDatabase();
        db.getCacheManager().expireCache();
        db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = db.getOQLQuery(
                "SELECT o FROM " + Service.class.getName() + " o order by o.id");
        QueryResults results = query.execute(Database.ReadOnly);
        
        long result = System.currentTimeMillis();
        
        int count = 0;
        while (results.hasMore()) {
            Object object = results.next();

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        db.commit();
        
        long commit = System.currentTimeMillis();
        
        db.close();

        long close = System.currentTimeMillis();
        
        LOG.info(format("ReadOnlyEmpty",
                         df.format(begin - start),
                         df.format(result- begin),
                         df.format(iterate - result),
                         df.format(commit - iterate),
                         df.format(close - commit)));
    }
    
    public void testReadOnlyCached() throws Exception {
        long start = System.currentTimeMillis();
        
        DatabaseImpl db = (DatabaseImpl) _jdo.getDatabase();
        db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = db.getOQLQuery(
                "SELECT o FROM " + Service.class.getName() + " o order by o.id");
        QueryResults results = query.execute(Database.ReadOnly);
        
        long result = System.currentTimeMillis();
        
        int count = 0;
        while (results.hasMore()) {
            Object object = results.next();

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        db.commit();
        
        long commit = System.currentTimeMillis();
        
        db.close();

        long close = System.currentTimeMillis();
        
        LOG.info(format("ReadOnlyCached",
                         df.format(begin - start),
                         df.format(result- begin),
                         df.format(iterate - result),
                         df.format(commit - iterate),
                         df.format(close - commit)));
    }

    public void testReadWriteOidEmpty() throws Exception {
        long start = System.currentTimeMillis();
        
        DatabaseImpl db = (DatabaseImpl) _jdo.getDatabase();
        db.getCacheManager().expireCache();
        db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = db.getOQLQuery(
                "CALL SQL select PTF_SERVICE.ID as ID " +
                "from PTF_SERVICE order by PTF_SERVICE.ID " +
                "AS ptf.jdo.rel1toN.OID");
        QueryResults results = query.execute(Database.ReadOnly);
        
        long result = System.currentTimeMillis();
        
        int count = 0;
        while (results.hasMore()) {
            OID oid = (OID) results.next();
            Object object = db.load(Service.class, oid.getId());

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        db.commit();
        
        long commit = System.currentTimeMillis();
        
        db.close();

        long close = System.currentTimeMillis();
        
        LOG.info(format("ReadWriteOidEmpty",
                         df.format(begin - start),
                         df.format(result- begin),
                         df.format(iterate - result),
                         df.format(commit - iterate),
                         df.format(close - commit)));
    }
    
    public void testReadWriteOidCached() throws Exception {
        long start = System.currentTimeMillis();
        
        DatabaseImpl db = (DatabaseImpl) _jdo.getDatabase();
        db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = db.getOQLQuery(
                "CALL SQL select PTF_SERVICE.ID as ID " +
                "from PTF_SERVICE order by PTF_SERVICE.ID " +
                "AS ptf.jdo.rel1toN.OID");
        QueryResults results = query.execute(Database.ReadOnly);
        
        long result = System.currentTimeMillis();
        
        int count = 0;
        while (results.hasMore()) {
            OID oid = (OID) results.next();
            Object object = db.load(Service.class, oid.getId());

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        db.commit();
        
        long commit = System.currentTimeMillis();
        
        db.close();

        long close = System.currentTimeMillis();
        
        LOG.info(format("ReadWriteOidCached",
                         df.format(begin - start),
                         df.format(result- begin),
                         df.format(iterate - result),
                         df.format(commit - iterate),
                         df.format(close - commit)));
    }
    
    public void testReadOnlyOidEmpty() throws Exception {
        long start = System.currentTimeMillis();
        
        DatabaseImpl db = (DatabaseImpl) _jdo.getDatabase();
        db.getCacheManager().expireCache();
        db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = db.getOQLQuery(
                "CALL SQL select PTF_SERVICE.ID as ID " +
                "from PTF_SERVICE order by PTF_SERVICE.ID " +
                "AS ptf.jdo.rel1toN.OID");
        QueryResults results = query.execute(Database.ReadOnly);
        
        long result = System.currentTimeMillis();
        
        int count = 0;
        while (results.hasMore()) {
            OID oid = (OID) results.next();
            Object object = db.load(Service.class, oid.getId(), Database.ReadOnly);

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        db.commit();
        
        long commit = System.currentTimeMillis();
        
        db.close();

        long close = System.currentTimeMillis();
        
        LOG.info(format("ReadOnlyOidEmpty",
                         df.format(begin - start),
                         df.format(result- begin),
                         df.format(iterate - result),
                         df.format(commit - iterate),
                         df.format(close - commit)));
    }
    
    public void testReadOnlyOidCached() throws Exception {
        long start = System.currentTimeMillis();
        
        DatabaseImpl db = (DatabaseImpl) _jdo.getDatabase();
        db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = db.getOQLQuery(
                "CALL SQL select PTF_SERVICE.ID as ID " +
                "from PTF_SERVICE order by PTF_SERVICE.ID " +
                "AS ptf.jdo.rel1toN.OID");
        QueryResults results = query.execute(Database.ReadOnly);
        
        long result = System.currentTimeMillis();
        
        int count = 0;
        while (results.hasMore()) {
            OID oid = (OID) results.next();
            Object object = db.load(Service.class, oid.getId(), Database.ReadOnly);

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        db.commit();
        
        long commit = System.currentTimeMillis();
        
        db.close();

        long close = System.currentTimeMillis();
        
        LOG.info(format("ReadOnlyOidCached",
                         df.format(begin - start),
                         df.format(result- begin),
                         df.format(iterate - result),
                         df.format(commit - iterate),
                         df.format(close - commit)));
    }
    
    public void testReadOnlyOidOnly() throws Exception {
        long start = System.currentTimeMillis();
        
        DatabaseImpl db = (DatabaseImpl) _jdo.getDatabase();
        db.begin();
        
        long begin = System.currentTimeMillis();
        
        OQLQuery query = db.getOQLQuery(
                "CALL SQL select PTF_SERVICE.ID as ID " +
                "from PTF_SERVICE order by PTF_SERVICE.ID " +
                "AS ptf.jdo.rel1toN.OID");
        QueryResults results = query.execute(Database.ReadOnly);
        
        long result = System.currentTimeMillis();
        
        int count = 0;
        while (results.hasMore()) {
            OID oid = (OID) results.next();

            count++;
        }
        
        long iterate = System.currentTimeMillis();
        
        db.commit();
        
        long commit = System.currentTimeMillis();
        
        db.close();

        long close = System.currentTimeMillis();
        
        LOG.info(format("ReadOnlyOidOnly",
                         df.format(begin - start),
                         df.format(result- begin),
                         df.format(iterate - result),
                         df.format(commit - iterate),
                         df.format(close - commit)));
    }
    
    private static String format(String head, String begin, String result,
                                 String iterate, String commit, String close) {
        
        StringBuffer sb = new StringBuffer();
        sb.append(format(head, 20, true));
        sb.append(format(begin, 10, false));
        sb.append(format(result, 10, false));
        sb.append(format(iterate, 10, false));
        sb.append(format(commit, 10, false));
        sb.append(format(close, 10, false));
        return sb.toString();
    }
    
    private static String format(String str, int len, boolean after) {
        StringBuffer sb = new StringBuffer();
        if (str != null) {
            sb.append(str);
            for (int i = str.length(); i < len; i++) {
                if (after) {
                    sb.append(' ');
                } else {
                    sb.insert(0, ' ');
                }
            }
        } else {
            for (int i = 0; i < len; i++) {
                if (after) {
                    sb.append(' ');
                } else {
                    sb.insert(0, ' ');
                }
            }
        }
        return sb.toString();
    }
}
