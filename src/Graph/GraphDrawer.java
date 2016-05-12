/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph;

import Database.DBObjectMapper;
import Database.DB_Object;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

/**
 *
 * @author IVANA.DRABIKOVA
 */
public class GraphDrawer {

    Connection conn;
    String user;
    ArrayList<DB_Object> list_of_tables;
    ArrayList<DB_Object> list_of_views;
    ArrayList<DB_Object> list_of_procs;
    DBObjectMapper mapper;
    MultiGraph graph;

    public GraphDrawer(Connection conn, String user) throws SQLException {
        this.conn = conn;
        list_of_tables = new ArrayList<>();
        list_of_views = new ArrayList<>();
        list_of_procs = new ArrayList<>();
        mapper = new DBObjectMapper(conn);
        this.user = user;

        graph = new MultiGraph("Dependency Model");
        graph.addAttribute("ui.title", "Dependency Model");
        graph.addAttribute("ui.stylesheet", "node { fill-color: rgb(200,200,200); size: 100px, 20px; shape: box;}");
        graph.addAttribute("layout.gravity", 0.05);

        Statement stmt = conn.createStatement();
        // arraylist of tables
        ResultSet rset = stmt.executeQuery("SELECT DISTINCT TABLE_NAME\n"
                + "FROM tabs\n"
                + "WHERE 1=1\n");
               // + "AND TABLE_NAME LIKE 'TAB%'");
        while (rset.next()) {
            list_of_tables.add(new DB_Object(rset.getString(1), "TABLE"));
            graph.addNode(rset.getString(1));
            System.out.println("TABLE " + rset.getString(1));
        }

        // arraylist of views
        rset = stmt.executeQuery("select VIEW_NAME from ALL_VIEWS\n"
                + "where 1=1\n"
                + "AND OWNER LIKE '%"+user.toUpperCase()+"%' -- DB OWNER"
                + "and VIEW_NAME LIKE '%%'");
        while (rset.next()) {
            list_of_views.add(new DB_Object(rset.getString(1), "VIEW"));
            graph.addNode(rset.getString(1));
            System.out.println("VIEW " + rset.getString(1));
            graph.getNode(rset.getString(1)).addAttribute("ui.style", "fill-color: rgb(140,211,70);");
        }

        // arraylist of procedures
        rset = stmt.executeQuery("select OBJECT_NAME\n"
                + "from ALL_OBJECTS \n"
                + "where 1=1\n"
                + "and upper(OBJECT_TYPE) = upper('PROCEDURE') \n"
                + "AND OWNER LIKE '%"+user.toUpperCase()+"%' -- DB OWNER \n");
               // + "AND OBJECT_NAME LIKE 'PROC%'");
        while (rset.next()) {
            list_of_procs.add(new DB_Object(rset.getString(1), "PROC"));
            graph.addNode(rset.getString(1));
            System.out.println("PROC " + rset.getString(1));
            graph.getNode(rset.getString(1)).addAttribute("ui.style", "fill-color: rgb(102,204,255);");
        }

        // mapovanie TAB - VIEW, TAB-PROC
        for (int i = 0; i < list_of_tables.size(); i++) {
            list_of_tables.get(i).dependent_views = (mapper.getDBORelationView(list_of_tables.get(i).getDB_Object_name()));
            list_of_tables.get(i).dependent_procs = (mapper.getDBORelationProc(list_of_tables.get(i).getDB_Object_name()));
        }
        // mapovanie VIEW - PROC
        for (int i = 0; i < list_of_views.size(); i++) {
            list_of_views.get(i).dependent_procs = (mapper.getDBORelationViewProc(list_of_views.get(i).getDB_Object_name()));
        }

        // print list of tables
        String tableName = "";
        String viewName = "";
        String procName = "";
        for (int i = 0; i < list_of_tables.size(); i++) {
            for (int j = 0; j < list_of_tables.get(i).dependent_views.size(); j++) {
                tableName = list_of_tables.get(i).getDB_Object_name();
                viewName = list_of_tables.get(i).dependent_views.get(j);
                graph.addEdge(tableName + viewName, tableName, viewName);
                System.out.println("TABLE + VIEW "+tableName+viewName);
            }
        }
        for (int i = 0; i < list_of_tables.size(); i++) {
            for (int j = 0; j < list_of_tables.get(i).dependent_procs.size(); j++) {
                tableName = list_of_tables.get(i).getDB_Object_name();
                procName = list_of_tables.get(i).dependent_procs.get(j);
                graph.addEdge(tableName + procName, tableName, procName);
                System.out.println("TABLE + PROC "+tableName+viewName);
            }
        }
        // mapovanie VIEW - PROC
        for (int i = 0; i < list_of_views.size(); i++) {
            list_of_views.get(i).dependent_procs = (mapper.getDBORelationViewProc(list_of_views.get(i).getDB_Object_name()));
        }
        for (int i = 0; i < list_of_views.size(); i++) {
            for (int j = 0; j < list_of_views.get(i).dependent_procs.size(); j++) {
                viewName = list_of_views.get(i).getDB_Object_name();
                procName = list_of_views.get(i).dependent_procs.get(j);
                graph.addEdge(viewName + procName, viewName, procName);
                System.out.println("VIEW + PROC "+tableName+viewName);
            }
        }

        //graph.addEdge("CA", "C", "A");
        for (Node node : graph) {
            node.addAttribute("ui.label", node.getId());
        }

    }

    public void drawGraph(MultiGraph g) throws SQLException {
        g.display();
    }
    
    public void setgraphProperty(MultiGraph g) {
        g.addAttribute("ui.title", "Dependency Model");
        g.addAttribute("ui.stylesheet", "node { fill-color: rgb(200,200,200); size: 100px, 20px; shape: box;}");
        g.addAttribute("layout.gravity", 0.05);
    }
    
    public void drawAll() {
        graph.display();
    }
    
    public void drawViewDependency(String viewnamelike) throws SQLException {
        MultiGraph graphVD = new MultiGraph("View Dependency Model");
        setgraphProperty(graphVD);
        
        Statement stmt = conn.createStatement();
        // arraylist of tables
        ResultSet rset = stmt.executeQuery("SELECT DISTINCT TABLE_NAME\n"
                + "FROM tabs\n"
                + "WHERE 1=1\n");
        while (rset.next()) {
            list_of_tables.add(new DB_Object(rset.getString(1), "TABLE"));
            //graphVD.addNode(rset.getString(1));
        }

        // arraylist of views
        rset = stmt.executeQuery("select VIEW_NAME from ALL_VIEWS\n"
                + "where 1=1\n"
                + "AND OWNER LIKE '%"+user+"%' -- DB OWNER \n"
                + "and VIEW_NAME LIKE '%"+viewnamelike+"%'");
        while (rset.next()) {
            list_of_views.add(new DB_Object(rset.getString(1), "VIEW"));
        }

        // arraylist of procedures
        rset = stmt.executeQuery("select OBJECT_NAME\n"
                + "from ALL_OBJECTS \n"
                + "where 1=1\n"
                + "and upper(OBJECT_TYPE) = upper('PROCEDURE') \n"
                + "AND OWNER LIKE '%"+user+"%' -- DB OWNER \n");
        while (rset.next()) {
            list_of_procs.add(new DB_Object(rset.getString(1), "PROC"));
            //graphVD.addNode(rset.getString(1));
            //graphVD.getNode(rset.getString(1)).addAttribute("ui.style", "fill-color: rgb(102,204,255);");
        }

        /* Mapovanie zavislych objektov */
        // mapovanie VIEW - TAB
        for (int i = 0; i < list_of_views.size(); i++) {
            list_of_views.get(i).dependent_tables = (mapper.getDBORelationViewTab(list_of_views.get(i).getDB_Object_name()));
        }
        // mapovanie VIEW - PROC
        for (int i = 0; i < list_of_views.size(); i++) {
            list_of_views.get(i).dependent_procs = (mapper.getDBORelationViewProc(list_of_views.get(i).getDB_Object_name()));
        }
        
        /* Naplnanie uzlov grafu */
        String viewName, tableName, procName;
        for (int i = 0; i < list_of_views.size(); i++) {
            viewName = list_of_views.get(i).getDB_Object_name();
            graphVD.addNode(viewName);
            graphVD.getNode(viewName).addAttribute("ui.style", "fill-color: rgb(140,211,70);");
            for (int j = 0; j < list_of_views.get(i).dependent_procs.size(); j++) {
                procName = list_of_views.get(i).dependent_procs.get(j);
                graphVD.addNode(procName);
                graphVD.getNode(procName).addAttribute("ui.style", "fill-color: rgb(102,204,255);");
            }
        }
        for (int i = 0; i < list_of_views.size(); i++) {
            for (int j = 0; j < list_of_views.get(i).dependent_tables.size(); j++) {
                tableName = list_of_views.get(i).dependent_tables.get(j);
                graphVD.addNode(tableName);
                graphVD.getNode(tableName).addAttribute("ui.style", "fill-color: rgb(200,200,200);");
            }
        }

        /* Naplnenie hran grafu */
        //  Zoznam tabuliek zavisejucich od pohladu
        for (int i = 0; i < list_of_views.size(); i++) {
            for (int j = 0; j < list_of_views.get(i).dependent_tables.size(); j++) {
                viewName = list_of_views.get(i).getDB_Object_name();
                tableName = list_of_views.get(i).dependent_tables.get(j);
                graphVD.addEdge(tableName + viewName, tableName, viewName);
            }
        }
        //  Zoznam procedur vytvorenych na zaklade pohladu
        for (int i = 0; i < list_of_views.size(); i++) {
            for (int j = 0; j < list_of_views.get(i).dependent_procs.size(); j++) {
                viewName = list_of_views.get(i).getDB_Object_name();
                procName = list_of_views.get(i).dependent_procs.get(j);
                graphVD.addEdge(procName + viewName, procName, viewName);
            }
        }

        //graph.addEdge("CA", "C", "A");
        for (Node node : graphVD) {
            node.addAttribute("ui.label", node.getId());
        }
        
        graphVD.display();
    }
}
