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
package org.castor.cpa.test.test70;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Ignore;

@Ignore
public final class TestCollection {

    private Collection<CollectionItem> _item;
    
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(final int id) {
        _id = id;
    }

    public boolean containsItem(final CollectionItem item) {
        if ((_item == null) || (_item.size() == 0)) {
            return false;
        }
        return _item.contains(item);
    }

    public Iterator<CollectionItem> itemIterator() {
        if ((_item == null) || (_item.size() == 0)) {
            return new ArrayList<CollectionItem>().iterator();
        }
        return _item.iterator();
    }

    public void removeItem(final CollectionItem item) {
        if (_item != null) {
            _item.remove(item);
            item.setTestCol(null);
        }
    }

    public int itemSize() {
        if (_item == null) {
            return 0;
        }
        return _item.size();
    }

    public void setItem(final Collection<CollectionItem> item) {
        _item = item;
    }

    public Collection<CollectionItem> getItem() {
        return _item;
    }

    public void addItem(final CollectionItem item) {
        if (_item == null) {
            _item = new ArrayList<CollectionItem>();
        }
        _item.add(item);
        item.setTestCol(this);
    }
}
