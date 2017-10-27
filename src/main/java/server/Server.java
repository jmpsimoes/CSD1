package main.java.server;


import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URI;
import java.security.KeyStore;
import java.security.SecureRandom;


public class Server {

    static final File KEYSTORE = new File("./server.jks");
    static final char[] JKS_PASS = "csd_server".toCharArray();
    static final char[] KEY_PASS = "csd_server".toCharArray();

    public static void main(String[] args) throws Exception {
        int port = 9090;
        URI baseUri = UriBuilder.fromUri("https://127.0.1.1/").port(port).build();

        ResourceConfig config = new ResourceConfig();
        config.register(new ServerResources("sec"));

        SSLContext sslContext = SSLContext.getInstance("TLSv1");

        KeyStore keyStore = KeyStore.getInstance("JKS");

        try (InputStream is = new FileInputStream(KEYSTORE)) {
            keyStore.load(is, KEY_PASS);
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, KEY_PASS);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

        JdkHttpServerFactory.createHttpServer(baseUri, config, sslContext);

        //ServiceProxy serviceProxy = new ServiceProxy(0);

        System.err.println("SSL REST RendezVous Server ready @ " + baseUri + " : host :" + InetAddress.getLocalHost().getHostAddress());

    }

}
