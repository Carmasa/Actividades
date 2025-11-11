package org.example.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String SQL_QUERY="Select * from empresa.persona";
    private static final String URL="";
    private static final String USER="";
    private static final String PASS="";

    public static void main(String[] args) {

        Connection conn=null;
        Statement stm=null;
        ResultSet rs=null;

        try{
            // 1. Crear conexion
            conn = DriverManager.getConnection(
                    "jbdc:mysql://127.0.0.1:3307/empresa",
                    "root", " ");

            // 2. Sentencias
            stm = conn.createStatement();
            rs = stm.executeQuery("Select * from coche");

            // 3. Procesar resultados
            List<Coche> car = new ArrayList<>();
            while(rs.next()){
                Integer id=rs.getInt("id");
                String modelo=rs.getString("modelo");
                Double cv=rs.getDouble("cv");

                Coche coche = new Coche(id,modelo,cv);
                car.add(coche);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}