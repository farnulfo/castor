/*
 * Copyright 2006 Le Duc Bao, Ralf Joachim
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.castor.ddlgen.engine.mysql;

import org.castor.ddlgen.DDLGenConfiguration;
import org.castor.ddlgen.GeneratorException;
import org.castor.ddlgen.schemaobject.Table;

/**
 * MySQL table.
 * 
 * @author <a href="mailto:leducbao AT gmail DOT com">Le Duc Bao</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-25 16:09:10 -0600 (Tue, 25 Apr 2006) $
 * @since 1.1
 */
public final class MysqlTable extends Table {
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String toCreateDDL() throws GeneratorException {
        String newline = getConfiguration().getStringValue(
                DDLGenConfiguration.NEWLINE_KEY, DDLGenConfiguration.DEFAULT_NEWLINE);
        String engine = getConfiguration().getStringValue(
                DDLGenConfiguration.STORAGE_ENGINE_KEY, null);

        StringBuffer sb = new StringBuffer();
        sb.append(newline).append(newline);
        sb.append("CREATE TABLE ").append(getName()).append(" (");
        sb.append(newline);
        sb.append(fields());
        sb.append(newline);
        sb.append(')');
        if ((engine != null) && !"".equals(engine)) {
            sb.append(" ENGINE=").append(engine);
        }
        sb.append(DDLGenConfiguration.DEFAULT_STATEMENT_DELIMITER);
        return sb.toString();
    }

    /** 
     * {@inheritDoc}
     */
    public String toDropDDL() {
        String newline = getConfiguration().getStringValue(
                DDLGenConfiguration.NEWLINE_KEY, DDLGenConfiguration.DEFAULT_NEWLINE);

        StringBuffer sb = new StringBuffer();
        sb.append(newline).append(newline);
        sb.append("DROP TABLE IF EXISTS ").append(getName());
        sb.append(DDLGenConfiguration.DEFAULT_STATEMENT_DELIMITER);
        return sb.toString();
    }

    //--------------------------------------------------------------------------
}
