/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

/**
 *
 * @author IVANA.DRABIKOVA
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author liesko
 */
public class DBObjectMapper {

    Connection db_connect;

    public DBObjectMapper(Connection db_connect) throws SQLException {
        this.db_connect = db_connect;
    }

    // TABLE vs VIEWS
    public ArrayList<String> getDBORelationView(String p_name) throws SQLException {
        ArrayList<String> related_list = new ArrayList<>();
        Statement stmt = db_connect.createStatement();
        ResultSet rset = stmt.executeQuery("select name\n"
                + "from user_dependencies\n"
                + "where 1=1\n"
                + "and TYPE like 'VIEW%'\n"
                + "and REFERENCED_TYPE like 'TABLE'\n"
                + "and REFERENCED_NAME like '" + p_name + "'");
        while (rset.next()) {
            related_list.add(rset.getString(1));
        }
        return related_list;
    }
    
// TABLE vs PROCEDURES
    public ArrayList<String> getDBORelationProc(String p_name) throws SQLException {
        ArrayList<String> related_list = new ArrayList<>();
        Statement stmt = db_connect.createStatement();
        ResultSet rset = stmt.executeQuery("select name\n"
                + "from user_dependencies\n"
                + "where 1=1\n"
                + "and TYPE like 'PROCEDURE%'\n"
                + "and REFERENCED_TYPE like 'TABLE'\n"
                + "and REFERENCED_NAME like '" + p_name + "'");
        while (rset.next()) {
            related_list.add(rset.getString(1));
        }
        return related_list;
    }
 // VRATI PROCEDURY KTORE VYUZIVAJU DANY POHLAD
    public ArrayList<String> getDBORelationViewProc(String p_name) throws SQLException {
        ArrayList<String> related_list = new ArrayList<>();
        Statement stmt = db_connect.createStatement();
        ResultSet rset = stmt.executeQuery("select name\n"
                + "from user_dependencies\n"
                + "where 1=1\n"
                + "and TYPE like '%PROCEDURE%'\n"
                + "and REFERENCED_TYPE like 'VIEW'\n"
                + "and REFERENCED_NAME like '" + p_name + "'");
        while (rset.next()) {
            related_list.add(rset.getString(1));
        }
        return related_list;
    }
     // VRATI TABULKY KTORE VYUZIVAJU DANY POHLAD
    public ArrayList<String> getDBORelationViewTab(String pohlad) throws SQLException {
        ArrayList<String> related_list = new ArrayList<>();
        Statement stmt = db_connect.createStatement();
        ResultSet rset = stmt.executeQuery("select name\n"
                + "from user_dependencies\n"
                + "where 1=1\n"
                + "and TYPE like '%TABLE%'\n"
                + "and REFERENCED_TYPE like 'VIEW'\n"
                + "and REFERENCED_NAME like '" + pohlad + "'");
        while (rset.next()) {
            related_list.add(rset.getString(1));
        }
        return related_list;
    }
}

