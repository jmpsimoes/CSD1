package main.java;

import main.java.api.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * Implementacao do servidor de rendezvous em REST
 */

@Path("/contacts")
public class ServerResources implements RendezVousAPI {

    private Map<String, Endpoint> db = new ConcurrentHashMap<>();
    private Map<String, Long> heartbeat_db = new ConcurrentHashMap<>();
    private String secret;
    private static final String PATH = "/sd/rendezvous";


    public ServerResources(String secret) {

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getSet(String key) {

    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void register(@PathParam("id") String id, @QueryParam("secret") String secret, Endpoint endpoint) {

    }


    @DELETE
    @Path("/{id}")
    public void unregister(@PathParam("id") String id, @QueryParam("secret") String secret) {
        if (!db.containsKey(id))
            throw new WebApplicationException(NOT_FOUND);
        else
            db.remove(id);
    }

    @PUT
    @Path("/heartbeat")
    @Consumes(MediaType.APPLICATION_JSON)
    public void heartbeat(Endpoint endpoint) {
        // RECEPÇÃO DE HEARTBEATS
        String id = endpoint.generateId();
        long current_time = System.currentTimeMillis();
        heartbeat_db.put(id, current_time);
        // DETECÇÃO DE FALHAS NOS INDEXERS
        for (String key : heartbeat_db.keySet()) {
            long t = System.currentTimeMillis();
            if ((t - heartbeat_db.get(key)) >= 30000) {
                heartbeat_db.remove(key);
                db.remove(key);

            }
        }

    }
}
