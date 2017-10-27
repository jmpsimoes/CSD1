package main.java.server;

import main.java.client.*;

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

import static javax.ws.rs.core.Response.Status.*;

/**
 * Implementacao do servidor de rendezvous em REST
 */

@Path("/contacts")
public class ServerResources{


    MapClient mapClient = null;


    public ServerResources(int replica_Port ) {
        mapClient = new MapClient(replica_Port);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getSet(String key) {
        mapClient.get(key);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void register(@QueryParam("key") String key, @QueryParam("value") String value) {

    }


    @DELETE
    @Path("/{id}")
    public void unregister(@PathParam("id") String id, @QueryParam("secret") String secret) {

    }

    @PUT
    @Path("/heartbeat")
    @Consumes(MediaType.APPLICATION_JSON)
    public void heartbeat() {

    }
}