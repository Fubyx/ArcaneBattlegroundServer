package arc;

import java.net.Socket;

public class PlayerInfo {
    private Socket playerSocket;
    private int id;
    private String name;
    private ClientCommThread thread;

    public PlayerInfo(Socket playerSocket, int id, String name, ClientCommThread thread) {
        this.playerSocket = playerSocket;
        this.id = id;
        this.name = name;
        this.thread = thread;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public Socket getPlayerSocket() {
        return playerSocket;
    }

    public ClientCommThread getThread() {
        return thread;
    }
}
