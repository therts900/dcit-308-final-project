package com.ug.smartcampus.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public final class DatabaseSupport {
    private DatabaseSupport() { }
    public static LocalDateTime date(ResultSet r, String column) throws SQLException { var value=r.getTimestamp(column); return value == null ? null : value.toLocalDateTime(); }
}
