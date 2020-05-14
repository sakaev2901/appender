package itis;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionConfig {
    private static final String DRIVER_NAME = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String ID = "postgres";
    private static final String PASS = "elvin2901";

    public Connection getConnection() {
        try {
            Class.forName(DRIVER_NAME);
            return DriverManager.getConnection(DB_URL, ID, PASS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void close(AutoCloseable statement) {
        if (statement != null) {
            try {
                statement.close();
            }
            catch (Exception e) {
                throw  new RuntimeException(e);
            }
        }
    }
}
