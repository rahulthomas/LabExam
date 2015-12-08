import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 * Created by rahul on 08/12/2015.
 */

@WebServlet("/submit")

public class labexam extends HttpServlet {


    private String databaseURL, username, password;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        databaseURL = context.getInitParameter("databaseURL");
        username = context.getInitParameter("username");
        password = context.getInitParameter("password");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        String sqlStr = null;

        try {
            out.println("<html><head><title>Error</title></head><body>");
            // Retrieve and process request parameters
            String custFName = request.getParameter("fname").trim();
            String custLName = request.getParameter("lname").trim();
            String custID = request.getParameter("id_num").trim();
            String custEmail = request.getParameter("email").trim();
            String custPhone = request.getParameter("phone").trim();
            String custComment = request.getParameter("comments").trim();

            if(custFName.isEmpty())
                custFName = "null";
            if(custLName.isEmpty())
                custLName = "null";
            if(custID.isEmpty())
                custID = "null";
            if(custEmail.isEmpty())
                custEmail = "null";
            if(custPhone.isEmpty())
                custPhone = "null";
            if(custComment.isEmpty())
                custComment = "null";

                out.println("<html><head><title>Confirmation</title></head><body>");
                out.println("<h2>Thank you for your FeedBack! :)</h2>");
                // We shall build our output in a buffer, so that it will not be interrupted
                //  by error messages.
                StringBuilder outBuf = new StringBuilder();
                // Display the name, email and phone (arranged in a table)
                outBuf.append("<table>");
                outBuf.append("<tr><td>First Name:</td><td>").append(custFName).append("</td></tr>");
                outBuf.append("<tr><td>Last Name:</td><td>").append(custLName).append("</td></tr>");
                outBuf.append("<tr><td>ID:</td><td>").append(custID).append("</td></tr>");
                outBuf.append("<tr><td>Email:</td><td>").append(custEmail).append("</td></tr>");
                outBuf.append("<tr><td>Phone Number:</td><td>").append(custPhone).append("</td></tr>");
                outBuf.append("<tr><td>Comments:</td><td>").append(custLName).append("</td></tr></table>");

                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(databaseURL, username, password);
                stmt = conn.createStatement();
                // We shall manage our transaction (because multiple SQL statements issued)
                conn.setAutoCommit(false);

                // Print the book(s) ordered in a table
                outBuf.append("<br />");
                outBuf.append("<table border='1' cellpadding='6'>");
                outBuf.append("<tr><th>First Name</th><th>Last Name</th><th>ID</th><th>Email</th><th>Phone</th><th>Comment</th></tr>");


                sqlStr = "INSERT INTO forum (fname, lname,id_num, email, phone, comments)values ("
                        + "'" + custFName + "', '" + custLName + "', '"
                        + custID + "', '" + custEmail + "', '" + custPhone + "', '" + custComment + "')";
                System.out.println(sqlStr);  // for debugging
                stmt.executeUpdate(sqlStr);

                // Display this book ordered
                outBuf.append("<tr>");
                outBuf.append("<td>").append(custFName).append("</td>");
                outBuf.append("<td>").append(custLName).append("</td>");
                outBuf.append("<td>").append(custID).append("</td>");
                outBuf.append("<td>").append(custEmail).append("</td>");
                outBuf.append("<td>").append(custPhone).append("</td>");
                outBuf.append("<td>").append(custComment).append("</td></tr>");
                out.println(outBuf.toString());

                out.println("<h3>Thank you.</h3>");

                // Commit for ALL the books ordered.
                conn.commit();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}