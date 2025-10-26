import java.sql.*;

public class mysql {
    public Connection c;
    public Statement s;

    public mysql() {
        try {
            // (Optional on JDBC 4+, but harmless)
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url  = "jdbc:mysql://localhost:3306/atmdb"
                        + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            String user = "atmuser";
            String pass = "StrongPass#123";

            c = DriverManager.getConnection(url, user, pass);
            // Keep the same Statement behavior the project expects
            s = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

        } catch (Exception e) {
            // Print full stack so you can see exact cause (auth, driver, url, etc.)
            e.printStackTrace();
        }
    }
}
