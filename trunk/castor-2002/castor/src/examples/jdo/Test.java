package jdo;


import myapp.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import org.odmg.Implementation;
import org.odmg.Database;
import org.odmg.Transaction;
import org.odmg.OQLQuery;
import org.exolab.testing.Timing;
import org.exolab.castor.util.Logger;
import org.exolab.castor.jdo.ODMG;



public class Test
{


    public static final String DatabaseFile = "database.xml";
    public static final String MappingFile  = "mapping.xml";
    public static final String Usage = "Usage: example jdo";


    public static void main( String[] args )
    {
	PrintWriter   logger;
	
	logger = Logger.getSystemLogger();

	try {
	    ODMG          odmg;
	    Database      db;

	    odmg = new ODMG();
	    logger.println( "Reading Java-SQL mapping from " + MappingFile );
	    odmg.loadMapping( Test.class.getResource( MappingFile ).toString() );

	    postgresql.PostgresqlDataSource ds = new postgresql.PostgresqlDataSource();
	    ds.setUser( "test" ); ds.setPassword( "test" ); ds.setDatabaseName( "test" );
	    org.exolab.castor.jdo.engine.DatabaseSource.registerDataSource( "test", ds, null );

	    /*
	    logger.println( "Reading database sources from " + DatabaseFile );
	    odmg.loadMapping( Test.class.getResource( DatabaseFile ).toString() );
	    */

	    // Run the ODMG API test, see odmgTest()
	    db = odmg.newDatabase();
	    db.open( "test", db.OPEN_READ_WRITE );
	    odmgTest( odmg, db, logger );
	    db.close();

	} catch ( Exception except ) {
	    logger.println( except );
	    except.printStackTrace( logger );
	}
    }


    public static void odmgTest( Implementation odmg, Database db, PrintWriter logger )
        throws Exception
    {
	Product       product;
	ProductGroup  group;
	Computer      computer;
	OQLQuery      productOql;
	OQLQuery      groupOql;
	OQLQuery      computerOql;
	Enumeration   enum;
	Transaction   tx;

	// Must be associated with an open transaction in order to
	// use the ODMG database
	tx = odmg.newTransaction();
	tx.begin();
	logger.println( "Begin transaction" );

	// Create OQL queries for all three object types
	productOql = odmg.newOQLQuery();
	productOql.create( "SELECT p FROM myapp.Product p WHERE id = $1" );
	groupOql = odmg.newOQLQuery();
	groupOql.create( "SELECT g FROM myapp.ProductGroup g WHERE id = $1" );
	computerOql = odmg.newOQLQuery();
	computerOql.create( "SELECT c FROM myapp.Computer c WHERE id = $1" );


	// Look up the group and if found in the database,
	// delete this object from the database
	groupOql.bind( new Integer( 3 ) );
	group = (ProductGroup) groupOql.execute();
	if ( group != null ) {
	    logger.println( "Deleting existing group: " + group );
	    db.deletePersistent( group );
	}
	
	// Look up the product and if found in the database,
	// delete this object from the database
	productOql.bind( new Integer( 4 ) );
	product = (Product) productOql.execute();
	if ( product != null ) {
	    logger.println( "Deleting existing product: " + product );
	    db.deletePersistent(  product );
	}
	
	// Look up the computer and if found in the database,
	// delete this object from the database
	computerOql.bind( new Integer( 6 ) );
	computer = (Computer) computerOql.execute();
	if ( computer != null ) {
	    logger.println( "Deleting existing computer: " + computer );
	    db.deletePersistent( computer );
	}


	// Checkpoint commits all the updates to the database
	// but leaves the transaction (and locks) open
	logger.println( "Transaction checkpoint" );
	tx.checkpoint();

	
	// If no such group exists in the database, create a new
	// object and persist it
	groupOql.bind( new Integer( 3 ) );
	group = (ProductGroup) groupOql.execute();
	if ( group == null ) {
	    group = new ProductGroup();
	    group.id = 3;
	    group.name = "new group";
	    logger.println( "Creating new group: " + group );
	    db.makePersistent( group );
	} else {
	    logger.println( "Query result: " + group );
	}
	
	// If no such product exists in the database, create a new
	// object and persist it
	// Note: product uses group, so group object has to be
	//       created first, but can be persisted later
	productOql.bind( new Integer( 4 ) );
	product = (Product) productOql.execute();
	if ( product == null ) {
	    product = new Product();
	    product.id = 4;
	    product.name = "new product";
	    product.price = 55;
	    product.group = group;
	    product.inventory = new ProductInventory();
	    product.inventory.quantity = 50;
	    product.inventory.product = product;
	    logger.println( "Creating new product: " + product );
	    db.makePersistent( product );
	} else {
	    logger.println( "Query result: " + product );
	}

	// If no such computer exists in the database, create a new
	// object and persist it
	// Note: computer uses group, so group object has to be
	//       created first, but can be persisted later
	computerOql.bind( new Integer( 6 ) );
	computer = (Computer) computerOql.execute();
	if ( computer == null ) {
	    computer = new Computer();
	    computer.id = 6;
	    computer.cpu = "Pentium";
	    computer.name = "new product";
	    computer.price = 300;
	    computer.group = group;
	    computer.inventory = new ProductInventory();
	    computer.inventory.quantity = 60;
	    computer.inventory.product = computer;
	    logger.println( "Creating new computer: " + computer );
	    db.makePersistent( computer );
	} else {
	    logger.println( "Query result: " + computer );
	}


	logger.println( "Commit transaction" );
	tx.commit();

	tx.begin();
	productOql.create( "SELECT p FROM myapp.Product p" );
	enum = (Enumeration) productOql.execute();
	while( enum.hasMoreElements() ) {
	    logger.println( "Query result: " + enum.nextElement() );
	}
	tx.commit();
    }


}


