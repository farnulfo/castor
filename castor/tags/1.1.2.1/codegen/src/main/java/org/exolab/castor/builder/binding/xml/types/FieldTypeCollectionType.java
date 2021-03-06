/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1-M2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.builder.binding.xml.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class FieldTypeCollectionType.
 * 
 * @version $Revision$ $Date$
 */
public class FieldTypeCollectionType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The array type
     */
    public static final int ARRAY_TYPE = 0;

    /**
     * The instance of the array type
     */
    public static final FieldTypeCollectionType ARRAY = new FieldTypeCollectionType(ARRAY_TYPE, "array");

    /**
     * The vector type
     */
    public static final int VECTOR_TYPE = 1;

    /**
     * The instance of the vector type
     */
    public static final FieldTypeCollectionType VECTOR = new FieldTypeCollectionType(VECTOR_TYPE, "vector");

    /**
     * The arraylist type
     */
    public static final int ARRAYLIST_TYPE = 2;

    /**
     * The instance of the arraylist type
     */
    public static final FieldTypeCollectionType ARRAYLIST = new FieldTypeCollectionType(ARRAYLIST_TYPE, "arraylist");

    /**
     * The hashtable type
     */
    public static final int HASHTABLE_TYPE = 3;

    /**
     * The instance of the hashtable type
     */
    public static final FieldTypeCollectionType HASHTABLE = new FieldTypeCollectionType(HASHTABLE_TYPE, "hashtable");

    /**
     * The collection type
     */
    public static final int COLLECTION_TYPE = 4;

    /**
     * The instance of the collection type
     */
    public static final FieldTypeCollectionType COLLECTION = new FieldTypeCollectionType(COLLECTION_TYPE, "collection");

    /**
     * The odmg type
     */
    public static final int ODMG_TYPE = 5;

    /**
     * The instance of the odmg type
     */
    public static final FieldTypeCollectionType ODMG = new FieldTypeCollectionType(ODMG_TYPE, "odmg");

    /**
     * The set type
     */
    public static final int SET_TYPE = 6;

    /**
     * The instance of the set type
     */
    public static final FieldTypeCollectionType SET = new FieldTypeCollectionType(SET_TYPE, "set");

    /**
     * The map type
     */
    public static final int MAP_TYPE = 7;

    /**
     * The instance of the map type
     */
    public static final FieldTypeCollectionType MAP = new FieldTypeCollectionType(MAP_TYPE, "map");

    /**
     * The sortedset type
     */
    public static final int SORTEDSET_TYPE = 8;

    /**
     * The instance of the sortedset type
     */
    public static final FieldTypeCollectionType SORTEDSET = new FieldTypeCollectionType(SORTEDSET_TYPE, "sortedset");

    /**
     * Field _memberTable.
     */
    private static java.util.Hashtable _memberTable = init();

    /**
     * Field type.
     */
    private int type = -1;

    /**
     * Field stringValue.
     */
    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private FieldTypeCollectionType(final int type, final java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate.Returns an enumeration of all possible
     * instances of FieldTypeCollectionType
     * 
     * @return an Enumeration over all possible instances of
     * FieldTypeCollectionType
     */
    public static java.util.Enumeration enumerate(
    ) {
        return _memberTable.elements();
    }

    /**
     * Method getType.Returns the type of this
     * FieldTypeCollectionType
     * 
     * @return the type of this FieldTypeCollectionType
     */
    public int getType(
    ) {
        return this.type;
    }

    /**
     * Method init.
     * 
     * @return the initialized Hashtable for the member table
     */
    private static java.util.Hashtable init(
    ) {
        Hashtable members = new Hashtable();
        members.put("array", ARRAY);
        members.put("vector", VECTOR);
        members.put("arraylist", ARRAYLIST);
        members.put("hashtable", HASHTABLE);
        members.put("collection", COLLECTION);
        members.put("odmg", ODMG);
        members.put("set", SET);
        members.put("map", MAP);
        members.put("sortedset", SORTEDSET);
        return members;
    }

    /**
     * Method readResolve. will be called during deserialization to
     * replace the deserialized object with the correct constant
     * instance.
     * 
     * @return this deserialized object
     */
    private java.lang.Object readResolve(
    ) {
        return valueOf(this.stringValue);
    }

    /**
     * Method toString.Returns the String representation of this
     * FieldTypeCollectionType
     * 
     * @return the String representation of this
     * FieldTypeCollectionType
     */
    public java.lang.String toString(
    ) {
        return this.stringValue;
    }

    /**
     * Method valueOf.Returns a new FieldTypeCollectionType based
     * on the given String value.
     * 
     * @param string
     * @return the FieldTypeCollectionType value of parameter
     * 'string'
     */
    public static org.exolab.castor.builder.binding.xml.types.FieldTypeCollectionType valueOf(
            final java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) {
            obj = _memberTable.get(string);
        }
        if (obj == null) {
            String err = "" + string + " is not a valid FieldTypeCollectionType";
            throw new IllegalArgumentException(err);
        }
        return (FieldTypeCollectionType) obj;
    }

}
