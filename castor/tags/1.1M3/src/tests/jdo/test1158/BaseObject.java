/*
 * Copyright 2005 Nick Stuart
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
package jdo.test1158;

import org.exolab.castor.jdo.TimeStampable;

/**
 * @author nstuart
 */
public class BaseObject implements TimeStampable{
    private int _id;
    private String _description;
    private boolean _saved;
    private long _timestamp;

    public int getId() { return _id; }
    public void setId(int id) { _id = id; }

    public String getDescription() { return _description; }
    public void setDescription(String description) { _description = description; }

    public boolean isSaved() { return _saved; }
    public void setSaved(boolean saved) { _saved = saved; }

    public long getTimestamp() { return _timestamp; }
    public void setTimestamp(long timestamp) { _timestamp = timestamp; }

    public void jdoSetTimeStamp(long timestamp) { setTimestamp(timestamp); }
    public long jdoGetTimeStamp() { return getTimestamp(); }
}
