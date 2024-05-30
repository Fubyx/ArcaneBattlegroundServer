package arc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static int currentClientId = 0;
    private static int currentGameId = 0;
    public static ArrayList<Game> games = new ArrayList<>();

    public static synchronized int incClientId(){
        return currentClientId++;
    }
    public static synchronized int incGameId(){
        return currentGameId++;
    }
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(50000);

        while (true) {

            Socket client = serverSocket.accept();

            ClientCommThread ccT = new ClientCommThread(client);
            ccT.start();
        }

    }
}
