package examples.student.digitalstore.servlets;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;

import examples.student.digitalstore.util.DbManager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = "/buy/*", name = "PurchaseServlet")
public class PurchaseServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String path = req.getPathInfo();
        String productHashedId = path.replace("/", "");
        req.setAttribute("productHashedId", productHashedId);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/pagePurchase.jsp");
        requestDispatcher.forward(req, res);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        DbManager dbManager = (DbManager) getServletContext().getAttribute("dbManager");

        int quantity = Integer.valueOf(req.getParameter("quantity"));
        String productHashedId = req.getParameter("productHashedId");
        String cost_currency = null;
        Integer price_amount = null;
        Integer cost_amount = null;
        Integer product_id = null;

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        PaymentLink paymentLink = null;
        
        //HW1: get the cost details from the DB! - DONE
        // *Did also suitable changes into the Stripe product and price params
        try {
            Connection conn = dbManager.connect();
            Statement smt = conn.createStatement();
            ResultSet resultSet = smt
                    .executeQuery("SELECT id, price_amount, price_currency FROM products WHERE hashed_id='" + productHashedId + "';");
            if (resultSet.next()) {
                product_id = resultSet.getInt("id");
                cost_currency = resultSet.getString("price_currency");
                price_amount = resultSet.getInt("price_amount");
                cost_amount = price_amount * quantity;
            }
            resultSet = smt.executeQuery("INSERT INTO clients (name, email, phone) VALUES ('" +
                                                    name + "', '" + email + "', '" + phone + "') RETURNING id;");
            if (resultSet.next()) {
                int client_id = resultSet.getInt("id");
                resultSet = smt.executeQuery("INSERT INTO orders " +
                                            "(client_id, product_id, quantity, cost_amount, cost_currency) VALUES " +
                                            "("+ client_id + "," + product_id + "," + quantity + "," + cost_amount +
                                            ", '" + cost_currency + "') RETURNING id;");
                if (resultSet.next()) {
                    int order_id = resultSet.getInt("id");

                    // HASHING order_id
                    byte[] order_bytes = BigInteger.valueOf(order_id).toByteArray();
                    String hashed_order_id = "";
                    try {
                        MessageDigest md = MessageDigest.getInstance("SHA-256");
                        md.update(order_bytes);
                        byte[] hashed_bytes = md.digest();
                        for (byte b : hashed_bytes) {
                            hashed_order_id += String.format("%02x", b);
                        }
                        smt.executeUpdate("UPDATE orders SET hashed_id = '"+ hashed_order_id +"' WHERE id = "+ order_id +";");

                        
                    } catch (NoSuchAlgorithmException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    req.setAttribute("orderHashedId", hashed_order_id);

                    Stripe.apiKey = "sk_test_51OyATSBzKXnZTYhL0mrpkHs4OBZWiKIoV7br8JjoDEIdWHs3eb1ii8VossfHKnJUs4VQIx7NNfrCf16rcyRQqs4u0038c1AMC1";
                    ProductCreateParams productParams = ProductCreateParams.builder()
                            .setName("A Way of Life / William Osler").build();
                    try {
                        Product product = Product.create(productParams);
                        PriceCreateParams priceParams = PriceCreateParams.builder()
                                .setCurrency(cost_currency)
                                .setUnitAmount(Integer.toUnsignedLong(price_amount))
                                .setProduct(product.getId())
                                .build();
                        try {
                            Price price = Price.create(priceParams);
                            PaymentLinkCreateParams paymentLinkParams = PaymentLinkCreateParams.builder()
                                    .addLineItem(
                                            PaymentLinkCreateParams.LineItem.builder()
                                                    .setPrice(price.getId())
                                                    .setQuantity(Integer.toUnsignedLong(quantity))
                                                    .build())
                                    .setAfterCompletion(
                                            PaymentLinkCreateParams.AfterCompletion.builder()
                                                    .setType(PaymentLinkCreateParams.AfterCompletion.Type.REDIRECT)
                                                    .setRedirect(
                                                            PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
                                                                    .setUrl("http://localhost:8080/digital-store-serv/payment-confirmed/" + hashed_order_id)
                                                                    .build())
                                                    .build())
                                    .build();

                            paymentLink = PaymentLink.create(paymentLinkParams);

                        } catch (StripeException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } catch (StripeException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            res.sendRedirect(paymentLink.getUrl());

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // RequestDispatcher requestDispatcher = req.getRequestDispatcher("pageOrderPlaced.jsp");
        // requestDispatcher.forward(req, res);
    }

}
