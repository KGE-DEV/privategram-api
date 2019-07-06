package com.garrettestrin.sample.data;

import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface SampleDao extends SqlObject {

    @SqlQuery("SELECT id "
        +   "FROM sample "
        +   "WHERE name = :name")
    String getUserId(@Bind("name") String name);
}
