package main.java.client;

// This is the class which sends requests to replicas

import bftsmart.tom.ServiceProxy;
import main.java.RequestType;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

// Classes that need to be declared to implement this
// replicated Map

public class MapClient implements Map<String, String> {

    ServiceProxy clientProxy = null;
    private Jedis jedis;

    public MapClient(int clientId) {
        System.out.println("----------" + "\n" + "Client ID: " + clientId + "\n" + "----------");
        clientProxy = new ServiceProxy(clientId);
        jedis = new Jedis("127.0.1.1");
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<String> keySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String put(String key, String value) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        try {
            dos.writeInt(RequestType.PUT);
            dos.writeUTF(key);
            dos.writeUTF(value);
            byte[] reply = clientProxy.invokeOrdered(out.toByteArray());
            if (reply != null) {
                String previousValue = new String(reply);
                return previousValue;
            }
            return null;
        } catch (IOException ioe) {
            System.out.println("Exception putting value into hashmap: " + ioe.getMessage());
            return null;
        }
    }

    @Override
    public String get(Object key) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeInt(RequestType.GET);
            dos.writeUTF(String.valueOf(key));
            String key_aux = String.valueOf(key);
            byte[] answer = clientProxy.invokeUnordered(out.toByteArray());
            String value = new String(answer);
            return value;
        } catch (IOException ioe) {
            System.out.println("Exception getting value from the hashmap: " + ioe.getMessage());
            return " ";
        }
    }

    @Override
    public String remove(Object key) {
        // Connection. Still needed.
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        try {
            dos.writeInt(RequestType.REMOVE);
            dos.writeUTF(String.valueOf(key));
            byte[] reply = clientProxy.invokeOrdered(out.toByteArray());
            String removedValue = String.valueOf(reply);
            if (reply != null) {
                return removedValue;
            }
            return removedValue;
        } catch (IOException ioe) {
            System.out.println("Exception removing value from the hashmap: " + ioe.getMessage());
            return null;
        }
    }

    @Override
    public int size() {
        // no need
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeInt(RequestType.SIZE);
            byte[] reply = clientProxy.invokeUnordered(out.toByteArray());
            ByteArrayInputStream in = new ByteArrayInputStream(reply);
            DataInputStream dis = new DataInputStream(in);
            int size = dis.readInt();
            return size;
        } catch (IOException ioe) {
            System.out.println("Exception getting the size the hashmap: " + ioe.getMessage());
            return -1;
        }
    }
}

// DONE
// get set!
// put set!
// remove set!

//TODO
// add element
// write element (?)
// isElement
// read element (?)