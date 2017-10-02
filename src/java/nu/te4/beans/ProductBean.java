/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.te4.beans;

import com.mysql.jdbc.Connection;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import nu.te4.utilities.ConnectionFactory;

/**
 *
 * @author milooyaguez karlsson
 */
@Stateless
public class ProductBean {
    public JsonArray getProducts(){
        try {
            Connection connection = ConnectionFactory.getConnection();
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM produkter";
            ResultSet data = stmt.executeQuery(sql);
            
            JsonArrayBuilder products = Json.createArrayBuilder();
            while(data.next()){
                String namn = data.getString("namn");
                String beskrivning = data.getString("beskrivning");
                int pris = data.getInt("pris");
                int lagersaldo = data.getInt("lagersaldo");
                JsonObject product = Json.createObjectBuilder()
                        .add("namn", namn)
                        .add("beskrivning", beskrivning)
                        .add("pris", pris)
                        .add("lagersaldo", lagersaldo)
                        .build();
                products.add(product);
            }
            
            connection.close();
            return products.build();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
    
    public boolean postProduct(String body){
        try {
            JsonReader reader = Json.createReader(new StringReader(body));
            JsonObject product = reader.readObject();
            reader.close();
            
            String namn = product.getString("namn");
            String beskrivning = product.getString("beskrivning");
            int pris = product.getInt("pris");
            int lagersaldo = product.getInt("lagersaldo");
            
            Connection connection = ConnectionFactory.getConnection();
            Statement stmt = connection.createStatement();
            String sql = String.format("INSERT INTO produkter VALUES (NULL, '%s', '%s', %d, %d)"
                    , namn, beskrivning, pris, lagersaldo);
            stmt.executeUpdate(sql);
            connection.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}
