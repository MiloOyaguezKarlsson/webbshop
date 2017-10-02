/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.te4.services;

import javax.ejb.EJB;
import javax.json.JsonArray;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nu.te4.beans.ProductBean;

/**
 *
 * @author milooyaguez karlsson
 */
@Path("produkter")
public class ProductService {
    
    @EJB
    ProductBean productBean;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts(){
        JsonArray products = productBean.getProducts();
        if(products != null){
            return Response.ok(products).build();
        } else{
            return Response.serverError().build();
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postProduct(String body){
        if(productBean.postProduct(body)){
            return Response.status(Response.Status.CREATED).build();
        } else{
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
}
