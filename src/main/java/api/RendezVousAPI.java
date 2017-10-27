package main.java.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Interface do servidor que mantem lista de servidores.
 */
public interface RendezVousAPI {


	/**
	 * Regista novo servidor.
	 */
	@POST
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	void register(@PathParam("id") String id, @QueryParam("secret") String secret, Endpoint endpoint);

	/**
	 * De-regista servidor, dado o seu id.
	 */
	@DELETE
	@Path("/{id}")
	void unregister(@PathParam("id") String id, @QueryParam("secret") String secret);
}


