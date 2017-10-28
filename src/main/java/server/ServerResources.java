package main.java.server;

import bftsmart.tom.ServiceProxy;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.*;

/**
 * Implementacao do servidor de rendezvous em REST
 */

@Path("/")
public class ServerResources{


    ServiceProxy clientProxy = null;


    public ServerResources() {
        clientProxy = new ServiceProxy(0);
    }

    @GET
    @Path("/gs/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void getSet(@QueryParam("id") String key) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        try {
            dos.writeInt(RequestType.GET);
            dos.writeBytes(key);
            byte[] reply = clientProxy.invokeUnordered(out.toByteArray());
            if (reply != null) {
                String previousValue = new String(reply);
                System.out.println(previousValue);
            }
        } catch (IOException ioe) {
            System.out.println("Exception putting value into hashmap: " + ioe.getMessage());
        }
    }

    @PUT
    @Path("/ps/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void put(@QueryParam("id") String key, Map<String, String> set) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        try {
            dos.writeInt(RequestType.PUT);
            dos.writeUTF(key);
            Iterator<String> it = set.keySet().iterator();
            while(it.hasNext()) {
                String temp = it.next();
                dos.writeBytes(temp);
                dos.writeBytes(set.get(temp));
            }
            byte[] reply = clientProxy.invokeOrdered(out.toByteArray());
            if (reply != null) {
                String previousValue = new String(reply);
                System.out.println(previousValue);
            }
        } catch (IOException ioe) {
            System.out.println("Exception putting value into hashmap: " + ioe.getMessage());
        }
    }


    @DELETE
    @Path("/rm/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void remove(@PathParam("id") String key) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        try {
            dos.writeInt(RequestType.REMOVE);
            dos.writeBytes(key);
            byte[] reply = clientProxy.invokeOrdered(out.toByteArray());
            if (reply != null) {
                String previousValue = new String(reply);
                System.out.println(previousValue);
            }
        } catch (IOException ioe) {
            System.out.println("Exception putting value into hashmap: " + ioe.getMessage());
        }
    }
}