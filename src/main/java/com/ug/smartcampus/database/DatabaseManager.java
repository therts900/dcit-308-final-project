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
        String sql = Files.readString(schema);
        // Strip full-line comments before splitting on ';'. The previous approach split on
        // ';' first, then only skipped a chunk if the *entire* trimmed chunk started with
        // "--". That silently dropped any statement immediately preceded by a comment with
        // no semicolon separating them (e.g. a CREATE TABLE right after an explanatory
        // comment block) because the comment + statement together still started with "--".
        StringBuilder withoutComments = new StringBuilder();
        for (String line : sql.split("\n")) {
            if (!line.strip().startsWith("--")) withoutComments.append(line).append('\n');
        }
        try (Connection c = getConnection(); Statement s = c.createStatement()) {
            for (String statement : withoutComments.toString().split(";")) {
                String trimmed = statement.trim();
                if (!trimmed.isEmpty()) s.execute(trimmed);
            }
        }
    }
    @Override public void close() { }
}
