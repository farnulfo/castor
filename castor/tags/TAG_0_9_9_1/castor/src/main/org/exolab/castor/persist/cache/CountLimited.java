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
 * $Id$
 */

package org.exolab.castor.persist.cache;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * CustomCache is a count limted least-recently-used <tt>Map</tt>.
 * <p>
 * Every object being put in the Map will live until the
 * map is full. If the map is full, a least-recently-used object 
 * will be disposed. 
 * <p>
 * Method {@link #dispose(Object)} will be called whenever an 
 * old object is diposed. Developer can get notify by overriding
 * the dispose method {@link #dispose(Object)}.
 *
 * @author <a href="tyip@leafsoft.com">Thomas Yip</a>
 * @author <a href="werner DOT guttmann AT gmx DOT com">Werner Guttmann</a>
 * @version $Revision$ $Date$
 */
public class CountLimited 
extends AbstractBaseCache
implements Cache
{
	
	private final static int LRU_OLD = 0;
	private final static int LRU_NEW = 1;
	
	private Hashtable mapKeyPos = null;
	private Object[] keys = null;
	private Object[] values = null;
	private int[] status = null;
	private int cur;
    
    public static int DEFAULT_SIZE = 30;
	
	/**
	 * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
	 * Commons Logging</a> instance used for all logging.
	 */
	private static Log _log = LogFactory.getFactory().getInstance(CountLimited.class);
	
    
    public CountLimited () {
    	super();
        setCapacity(DEFAULT_SIZE);
    }
    
	public CountLimited (int capacity) {
    	super();
    	setCapacity(capacity);
	}
	
	/**
	 * Maps the specified <code>key</code> to the specified 
	 * <code>value</code> in this Map. Neither the key nor the 
	 * value can be <code>null</code>. 
	 * <p>
	 * The value can be retrieved by calling the <code>get</code> method 
	 * with a key that is equal to the original key, before it is diposed
	 * when the Map is full. 
	 * <p>
	 * @param      key     the Map key.
	 * @param      value   the value.
	 * @return     the previous value of the specified key in this Map,
	 *             or <code>null</code> if it did not have one.
	 * @exception  NullPointerException  if the key or value is
	 *               <code>null</code>.
	 */
	public synchronized Object put( Object key, Object value ) {
		
		if (_log.isDebugEnabled()) {
			_log.trace ("Putting entry into cache for key " + key + " to " + value);
		}
		
		Object oldPos = mapKeyPos.get(key);
		
		if ( oldPos != null ) {
			int pos = ((Integer)oldPos).intValue();
			Object oldObject = values[pos];
			values[pos] = value;
			status[pos] = LRU_NEW;
			
			
			dispose( oldObject );
			
			return oldObject;
		} else {
			
			// skip to new pos -- for Cache, change walkStatus() to get lock....
			while (walkStatus() != LRU_OLD) {
				// skip to new position   
			}
			
			Object intvalue;// = null;
			if ( keys[cur] != null ) {
				intvalue = mapKeyPos.remove(keys[cur]);
				//				if ( intvalue == null )
				//					intvalue = new Integer(cur);
			} else {
				intvalue = new Integer(cur);
			}
			Object oldObject = values[cur];
			keys[cur] = key;
			values[cur] = value;
			status[cur] = LRU_NEW;
			mapKeyPos.put(key, intvalue);
			cur++;
			if ( cur >= getCapacity() ) cur = 0;
			if ( oldObject != null )
				dispose( oldObject );
			return oldObject;
		}
	}
	
	/**
	 *Returns the value to which the specified key is mapped in this Map.
	 *@param key - a key in the Map.
	 *@return the value to which the key is mapped in this Map; null if 
	 * the key is not mapped to any value in this Map.
	 */
	public synchronized Object get( Object key ) {
		Object intvalue = mapKeyPos.get(key);
		Object cachedObject = null;
		if ( intvalue != null ) {
			int pos = ((Integer)intvalue).intValue();
			status[pos] = LRU_NEW;
			cachedObject = values[pos]; 
		}
		
		if (_log.isDebugEnabled()) {
			_log.trace ("Returning cache entry for key " + key + ": " + cachedObject);
		}
		
		return cachedObject;
	}
	
	/**
	 * Removes the key (and its corresponding value) from this 
	 * Map. This method does nothing if the key is not in the Map.
	 *
	 * @param   key   the key that needs to be removed.
	 * @return  the value to which the key had been mapped in this Map,
	 *          or <code>null</code> if the key did not have a mapping.
	 */
	public synchronized Object remove( Object key ) {
		Object intvalue = mapKeyPos.remove(key);
		Object removedObject = null;
		if ( intvalue != null ) {
			int pos = ((Integer)intvalue).intValue();
			removedObject = values[pos];
			keys[pos] = null;
			values[pos] = null;
			status[pos] = LRU_OLD;
		}
		if (_log.isDebugEnabled()) {
			_log.trace ("Removing cache entry for key " + key + ": " + removedObject);
		}
		
		return removedObject;
	}
	
	/**
	 * Returns an enumeration of the values in this LRU map.
	 * Use the Enumeration methods on the returned object to fetch the elements
	 * sequentially.
	 *
	 * @return  an enumeration of the values in this Map.
	 * @see     java.util.Enumeration
	 */
	public Enumeration elements() {
		return new ValuesEnumeration(values);
	}
	
	
	/**
	 * Remove the object identified by key from the cache.
	 *
	 * @param   key   the key that needs to be removed.
	 */
	public void expire(Object key) 
	{
		if (_log.isDebugEnabled()) {
			_log.trace ("Expiring cache entry for key " + key);
		}
		if ( remove(key) == null ) {
			// log.trace ("CustomCache LRU expire: "+key+" not found");
		}
		else {
			// log.trace ("CustomCache LRU expire: "+key+" removed from cache");
		}
		dispose(key);
	}
	
	/**
	 * This method is called when an object is disposed.
	 * Override this method if you interested in the disposed object.
	 *
	 * @param o - the disposed object
	 */
	protected void dispose( Object o ) {
		if (_log.isDebugEnabled()) {
			_log.trace ("Disposing object " + o);
		}
	}
	
	private int walkStatus() {
		int s = status[cur];
		if ( s == LRU_NEW ) {
			status[cur] = LRU_OLD;
			cur++;
			if ( cur >= getCapacity() ) cur = 0;
			return LRU_NEW;
		}
		return LRU_OLD;
	}
	
	private class ValuesEnumeration implements Enumeration {
		private int cur;
		private Object[] values;
		private ValuesEnumeration( Object[] v ) {
			Vector t = new Vector(v.length);
			for ( int i=0; i<v.length; i++ ) {
				if ( v[i] != null ) {
					t.add(v[i]);
				}
			}
			values = t.toArray();
		}
		public boolean hasMoreElements() {
			if ( values != null && values.length > cur ) 
				return true;
			return false;
		}
		public Object nextElement() throws NoSuchElementException {
			if ( values == null || values.length <= cur )
				throw new NoSuchElementException();
			return values[cur++];
		}
	}
	
	/**
	 * Indicates whether the cache holds value object mapped to the specified key.
	 * @param key - A key identifying a value object.
	 * @return True if the cache holds a value object for the specified key, false otherwise.
	 * @see org.exolab.castor.persist.cache.Cache#contains(java.lang.Object)
	 */
	public boolean contains(Object key) {
		return (this.get(key) != null);
	}
	
	/**
	 * Sets the cache capacity.
	 * @param capacity the cache capacity.
	 * @see org.exolab.castor.persist.cache.Cache#setCapacity(int)
	 */
	public void setCapacity (int capacity) {
		if(getCapacity() == 0) {
		    // cache has not yet been initialized
	        keys = new Object[capacity];
	        values = new Object[capacity];
	        status = new int[capacity];
	        mapKeyPos = new Hashtable(capacity);

	        super.setCapacity (capacity);
		} else if(getCapacity() > capacity){
		    // cache has been initialized but capacity will decrease
		    // we do nothing and leave capazity as is
		} else if(getCapacity() < capacity){
		    // cache has been initialized but capacity will increased
		    Object[] nkeys = new Object[capacity];
		    System.arraycopy(keys, 0, nkeys, 0, getCapacity());
	        keys = nkeys;
	        
		    Object[] nvalues = new Object[capacity];
		    System.arraycopy(values, 0, nvalues, 0, getCapacity());
	        values = nvalues;
	        
		    int[] nstatus = new int[capacity];
		    System.arraycopy(status, 0, nstatus, 0, getCapacity());
	        status = nstatus;

	        // mapKeyPos will resize automatically

	        super.setCapacity (capacity);
		}
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.persist.cache.Cache#size()
	 */
	public int size() {
		return mapKeyPos.size();
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.persist.cache.Cache#clear()
	 */
	public void clear() {
		mapKeyPos.clear();
		// TODO [WG]: clear keys, values and status object arrays ? 
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.persist.cache.Cache#isEmpty()
	 */
	public boolean isEmpty() {
		return mapKeyPos.isEmpty();
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.persist.cache.Cache#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key) {
		return mapKeyPos.containsKey(key);
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.persist.cache.Cache#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value) {
		return mapKeyPos.containsValue(value);
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.persist.cache.Cache#values()
	 */
	public Collection values() {
		return mapKeyPos.values();
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.persist.cache.Cache#putAll(java.util.Map)
	 */
	public void putAll(Map aMap) {
		mapKeyPos.putAll (aMap);
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.persist.cache.Cache#entrySet()
	 */
	public Set entrySet() {
		return mapKeyPos.entrySet();
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.persist.cache.Cache#keySet()
	 */
	public Set keySet() {
		return mapKeyPos.keySet();
	}
    
    public void initialize() {
//      nothing to do
    }

    public void close() {
        _log.debug ("Closing " + getCacheType() + "instance for " + getClassName());
    }
}
