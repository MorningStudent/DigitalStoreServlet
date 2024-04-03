package examples.student.digitalstore.listeners;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import examples.student.digitalstore.util.DbManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DbContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        DbManager dbManager = new DbManager(sce.getServletContext());
        sce.getServletContext().setAttribute("dbManager", dbManager);
        
        ServletContextListener.super.contextInitialized(sce);
    }

    // @Override
    // public void contextDestroyed(ServletContextEvent sce) {
    //     ServletContext context = sce.getServletContext();
    //     Connection conn = (Connection)context.getAttribute("conn");
    //     try {
    //         conn.close();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     ServletContextListener.super.contextDestroyed(sce);
    // }

}
