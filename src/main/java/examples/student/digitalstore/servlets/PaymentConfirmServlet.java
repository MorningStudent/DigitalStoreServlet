package examples.student.digitalstore.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import examples.student.digitalstore.util.DbManager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/payment-confirmed/*", name = "PaymentConfirmServlet")
public class PaymentConfirmServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        
        String path = req.getPathInfo();
        String orderHashedId = path.replace("/", "");

        DbManager dbManager = (DbManager) getServletContext().getAttribute("dbManager");
        try {
            Connection conn = dbManager.connect();
            Statement smt = conn.createStatement();
            smt.executeUpdate("UPDATE orders SET payed = true WHERE hashed_id = '" + orderHashedId + "';");

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/pageConfirm.jsp");
        req.setAttribute("orderHashedId", orderHashedId);
        requestDispatcher.forward(req, res);
    }

    
    
}
