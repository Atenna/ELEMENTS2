/*
 This class establish a new connection o a database within which you desire
 to find out dependencies of ETL
 */
package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author IVANA.DRABIKOVA
 */
public class Connect {

    private Connection conn;
    private String connection_string, user, password;

    public Connect(String url, String user, String pwd) throws SQLException {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            conn = DriverManager.getConnection("jdbc:oracle:thin:@" + url + ":orcl", user, pwd);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (conn != null) {
            System.out.println("Connection was successful.");
        } else {
            System.out.println("Failed to make connection.");
        }
    }
    
    public Connection getConnection() {
        return conn;
    }
    
    public boolean connected() {
        return conn != null;
    }

    public String getName() {
        return user;
    }
}
