package examples.student.digitalstore.servlets;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import examples.student.digitalstore.util.DbManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// HW2* Add route /admin/save-product to save a product which, without any form will save a product HASHING its id - DONE (Verified in POSTMAN)
//  modify the /buy/productId path to use hash - DONE
// *Modified also the /payment-confirmed/orderId so it uses hash instead, and in payment url too
// So now we have only hashed ids everywhere on client side
@WebServlet(urlPatterns = "/admin/save-product", name="AdminServlet")
public class AdminServlet extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String name = req.getParameter("name");
        String image = req.getParameter("image");
        String file = req.getParameter("file");
        System.out.println(name);
        int priceAmount = Integer.valueOf(req.getParameter("priceAmount"));
        String priceCurrency = req.getParameter("priceCurrency");

        DbManager dbManager = (DbManager) getServletContext().getAttribute("dbManager");
        try {
            Connection conn = dbManager.connect();
            Statement smt = conn.createStatement();
            ResultSet resultSet = smt.executeQuery("INSERT INTO products "+
                "(name, image, file, price_amount, price_currency) VALUES "+
                "('" + name + "', '" + image + "', '" + file + "', " + priceAmount + 
                ", '" + priceCurrency + "') RETURNING id;");
            if (resultSet.next()) {
                int productId = resultSet.getInt("id");
                byte[] productIdBytes = BigInteger.valueOf(productId).toByteArray();
                    try {
                        MessageDigest md = MessageDigest.getInstance("SHA-256");
                        md.update(productIdBytes);
                        byte[] hashedBytes = md.digest();
                        String hashedProductId = "";
                        for (byte b : hashedBytes) {
                            hashedProductId += String.format("%02x", b);
                        }
                        smt.executeUpdate("UPDATE products SET hashed_id = '"+ hashedProductId +"' WHERE id = "+ productId +";");
                    } catch (NoSuchAlgorithmException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
            }
            res.getWriter().write("Product saved succesfully!");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    
}
