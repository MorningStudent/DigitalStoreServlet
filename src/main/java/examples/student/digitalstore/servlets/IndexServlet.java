package examples.student.digitalstore.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import examples.student.digitalstore.util.DbManager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/home", name = "IndexServlet")
public class IndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        DbManager dbManager = (DbManager) getServletContext().getAttribute("dbManager");
        try {
            Connection conn = dbManager.connect();
            Statement smt = conn.createStatement();
            ResultSet resultSet = smt.executeQuery("SELECT * FROM products;");
            if (resultSet.next()) {
                req.setAttribute("productHashedId", resultSet.getString("hashed_id"));
                req.setAttribute("productName", resultSet.getString("name"));
                req.setAttribute("productImage", resultSet.getString("image"));
                req.setAttribute("productPriceAmount", resultSet.getString("price_amount"));
                req.setAttribute("productPriceCurrency", resultSet.getString("price_currency"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("pageIndex.jsp");
        requestDispatcher.forward(req, res);
    }

    
}
