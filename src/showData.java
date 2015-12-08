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
@WebServlet("/show")
public class showData extends HttpServlet {
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
        int i = 1;

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(databaseURL, username, password);
            stmt = conn.createStatement();
            // We shall manage our transaction (because multiple SQL statements issued)
            conn.setAutoCommit(false);

            rset = stmt.executeQuery("SELECT * FROM forum");
            //System.out.println(rset);
            out.println("<html><head><title>Results</title></head><body>");
            out.println("<h2>Here is everything in Database..</h2>");
            StringBuilder outBuf = new StringBuilder();
            outBuf.append("<br />");
            outBuf.append("<table border='1' cellpadding='6'>");
            outBuf.append("<tr><th>ID</th><th>First Name</th><th>Last Name</th><th>ID</th><th>Email</th><th>Phone</th><th>Comment</th></tr>");
            // displaying records
            while (rset.next()) {
                outBuf.append("<tr>");
                outBuf.append("<td>").append(rset.getInt("id")).append("</td>");
                outBuf.append("<td>").append(rset.getString("fname")).append("</td>");
                outBuf.append("<td>").append(rset.getString("lname")).append("</td>");
                outBuf.append("<td>").append(rset.getString("id_num")).append("</td>");
                outBuf.append("<td>").append(rset.getString("email")).append("</td>");
                outBuf.append("<td>").append(rset.getString("phone")).append("</td>");
                outBuf.append("<td>").append(rset.getString("comments")).append("</td>");
                outBuf.append("</tr>");
            }
            outBuf.append("<table>");
            out.println(outBuf.toString());
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