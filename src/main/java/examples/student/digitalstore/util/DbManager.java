package examples.student.digitalstore.util;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.servlet.ServletContext;

public class DbManager {

    private ServletContext context;
    private HikariConfig config;
    private HikariDataSource dataSource;

    public DbManager(ServletContext context) {
        this.context = context;
    }

    public Connection connect() throws SQLException, ClassNotFoundException {
        if (config == null) {
            Class.forName("org.postgresql.Driver");
            config = new HikariConfig();
            config.setJdbcUrl(context.getInitParameter("url"));
            config.setUsername(context.getInitParameter("user"));
            config.setPassword(context.getInitParameter("password"));
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            dataSource = new HikariDataSource(config);
        }
        return dataSource.getConnection();
    }
    
}
