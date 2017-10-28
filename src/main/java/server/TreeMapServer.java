package main.java.server;

// These are the classes which receive requests from clients

import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultRecoverable;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.*;

// Classes that need to be declared to implement this
// replicated Map

public class TreeMapServer extends DefaultRecoverable {

    Map<String, Map<String, String>> table;
    private Jedis jedis;
    private List<String> fields;

    public TreeMapServer(int id, String URLServer) {
        table = new TreeMap<>();
        jedis = new Jedis(URLServer, 6379);
        fields = new ArrayList<>();

        fields.add("name");
        fields.add("age");
        fields.add("address");

        //teste
        jedis.set("Joao", "MELHORDOMUNDO");
        System.out.println("Joao" + jedis.get("Joao"));

        new ServiceReplica(id, this, this);

        System.out.println("Ping para o redis" + URLServer + "resposta: " + jedis.ping());


    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: HashMapServer <main.java.server id>");
            System.exit(0);
        }

        new TreeMapServer(Integer.parseInt(args[0]), args[1]);
    }

    @Override
    public byte[][] appExecuteBatch(byte[][] command, MessageContext[] mcs) {

        byte[][] replies = new byte[command.length][];
        for (int i = 0; i < command.length; i++) {
            replies[i] = executeSingle(command[i], mcs[i]);
        }

        return replies;
    }

    private byte[] executeSingle(byte[] command, MessageContext msgCtx) {
        ByteArrayInputStream in = new ByteArrayInputStream(command);
        DataInputStream dis = new DataInputStream(in);
        int reqType;
        try {
            reqType = dis.readInt();
            if (reqType == RequestType.PUT) {
                String id = dis.readUTF();
                int size = Integer.parseInt(dis.readUTF());

                Map<String, String> aux = new HashMap<>();
                String key=null;
                String value;

                for(int i = 0; i < size; i++){
                    key = dis.readUTF();
                    if(fields.contains(key)){
                        value = dis.readUTF();
                        aux.put(key, value);
                    }
                }
                //adicao no fim
                table.put(id, aux);
                String result = jedis.hmset(id, aux);

                byte[] resultBytes = null;
                if (result != null) {
                    resultBytes = result.getBytes();
                }
                return resultBytes;
            } else if (reqType == RequestType.REMOVE) {
                String keyID = dis.readUTF();
                Map<String, String> removedValue = table.remove(keyID);
                long resultValue = jedis.hdel(keyID);

                byte[] resultBytes = removedValue.toString().getBytes();
                return resultBytes;
            } else {
                System.out.println("Unknown request type: " + reqType);
                return null;
            }
        } catch (IOException e) {
            System.out.println("Exception reading data in the replica: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] appExecuteUnordered(byte[] command, MessageContext msgCtx) {
        ByteArrayInputStream in = new ByteArrayInputStream(command);
        DataInputStream dis = new DataInputStream(in);
        int reqType;
        try {
            reqType = dis.readInt();
            if (reqType == RequestType.GET) {
                String key = dis.readUTF();
                // String readValue = table.get(key);
                Map<String, String> map_aux = jedis.hgetAll(key);
                byte[] resultBytes;
                resultBytes = map_aux.entrySet().toString().getBytes();
                return resultBytes;
            } else if (reqType == RequestType.SIZE) {
                long dbSize = jedis.dbSize();
                int size = (int) dbSize;
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(out);
                dos.writeInt(size);
                byte[] sizeInBytes = out.toByteArray();
                return sizeInBytes;
            } else {
                System.out.println("Unknown request type: " + reqType);
                return null;
            }
        } catch (IOException e) {
            System.out.println("Exception reading data in the replica: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void installSnapshot(byte[] state) {
        ByteArrayInputStream bis = new ByteArrayInputStream(state);
        try {
            ObjectInput in = new ObjectInputStream(bis);
            table = (Map<String ,Map<String, String>>) in.readObject();
            in.close();
            bis.close();
        } catch (ClassNotFoundException e) {
            System.out.print("Coudn't find Map: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.print("Exception installing the application state: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public byte[] getSnapshot() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(table);
            out.flush();
            out.close();
            bos.close();
            return bos.toByteArray();
        } catch (IOException e) {
            System.out.println("Exception when trying to take a + "
                    + "snapshot of the application state" + e.getMessage());
            e.printStackTrace();
            return new byte[0];
        }
    }
}
