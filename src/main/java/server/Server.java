package server;

import dictionary.Dictionary;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Kovalenko Nikita on 03.07.2017.
 */
public class Server {

    // port for socket
    static final int PORT = 4444;

    /**
     * class for handling a user request in a separate thread
     * Created by Kovalenko Nikita on 03.07.2017.
     */
    private class ClientWorker implements Runnable {

        private Socket clientSocket;

        /**
         * @param clientSocket - socket from client to work
         */
        ClientWorker(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
                try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {
                    String[] arrParams = (String[]) in.readObject();
                    Dictionary dictionary = Dictionary.getInstance();
                    out.writeObject(dictionary.operation(arrParams));
                    out.flush();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        try {
            Server server  = new Server();
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * start server
     * @throws IOException - error starting on port or accept
     */
    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        // continuous loop waiting for client connections and servicing them
        while (true) {
            ClientWorker clientWorker;
            clientWorker = new ClientWorker(serverSocket.accept());
            Thread thread = new Thread(clientWorker);
            thread.start();
        }
    }

}
