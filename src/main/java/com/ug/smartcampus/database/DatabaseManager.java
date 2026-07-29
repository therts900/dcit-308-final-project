package com.ug.smartcampus.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.Properties;

/** Small JDBC connection factory for the local SQLite database. */
public final class DatabaseManager implements AutoCloseable {
    private final String url;
    public DatabaseManager() { this(loadProperties()); }
    public DatabaseManager(String url) { this.url = url; }
    private DatabaseManager(Properties p) { this.url = p.getProperty("database.sqlite.url", "jdbc:sqlite:database/smart-campus.db"); }
    private static Properties loadProperties() {
        Properties p = new Properties();
        try (var in = DatabaseManager.class.getClassLoader().getResourceAsStream("application/application.properties")) { if (in != null) p.load(in); }
        catch (IOException e) { throw new IllegalStateException("Unable to load database properties", e); }
        return p;
    }
    public Connection getConnection() throws SQLException { Connection c=DriverManager.getConnection(url); try(Statement s=c.createStatement()){s.execute("PRAGMA foreign_keys = ON");} return c; }
    public void initializeSchema(Path schema) throws SQLException, IOException {
        String sql=Files.readString(schema); try(Connection c=getConnection(); Statement s=c.createStatement()){ for(String statement:sql.split(";")){String trimmed=statement.trim(); if(!trimmed.isEmpty()&&!trimmed.startsWith("--")) s.execute(trimmed); } }
    }
    @Override public void close() { }
}
