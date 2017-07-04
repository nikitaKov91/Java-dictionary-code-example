package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created by Kovalenko Nikita on 03.07.2017.
 */
public class Client {

    // port for socket
    static final int PORT = 4444;

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.start(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * main method
     * @param args - ip, operation, params for operation
     * @throws IOException - socket error
     * @throws ClassNotFoundException - error on read object
     */
    public void start(String[] args) throws IOException, ClassNotFoundException {
        if (args.length < 2) {
            throw new IllegalArgumentException("Not enough arguments");
        }
        Socket socket = new Socket(args[0], PORT);
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            // cut the ip from arguments
            String[] arrTemp = Arrays.copyOfRange(args, 1, args.length);
            out.writeObject(arrTemp);
            out.flush();
            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                Object object = in.readObject();
                System.out.println(object);
            }
        }
        socket.close();
    }

}
