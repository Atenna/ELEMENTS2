/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.util.ArrayList;

/**
 *
 * @author IVANA.DRABIKOVA
 */
public class DB_Object {
    public String db_object_name;
    public String db_object_type;
    public ArrayList<DB_Object> dependent_objects;
    public ArrayList<String> dependent_procs;
    public ArrayList<String> dependent_views;
    public ArrayList<String> dependent_tables;
    public DB_Object(String object_name, String object_type){
        db_object_name=object_name;
        db_object_type=object_type;
    }
    public String getDB_Object_name(){
        return db_object_name;
    }
    public String getDB_Object_type(){
        return db_object_type;
    }
}
