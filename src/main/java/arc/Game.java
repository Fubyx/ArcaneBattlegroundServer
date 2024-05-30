package arc;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Game {
    private int gameId;
    private ArrayList<Entity> gameEntities;
    private ArrayList<Player> players;
    private boolean started = false;
    private Entity tapToPublish = null;
    public Game(int gameId, Player host){
        this.gameId = gameId;
        players = new ArrayList<>();
        players.add(host);
        gameEntities = new ArrayList<>();
    }

    public int getGameId() {
        return gameId;
    }

    public boolean addPlayer(Player player) throws IOException {
        if(started || players.size() > 6)
            return false;
        for (Player p:players) {

            ObjectOutputStream o = new ObjectOutputStream(p.getPlayerSocket().getOutputStream());
            o.writeChars("Joined: " + player.getName());
            /*
            if (p.getName().equals(player.getName())){
                player.setName(player.getName() + "_");
            }//*/
        }
        players.add(player);
        return true;
    }
    public void start() throws IOException {
        this.started = true;

        switch (players.size()){
            case 1 -> {
                String id = "player" + players.get(0).getId();
                Entity e = new Entity(0, 1, id);
                gameEntities.add(e);
            }
            case 2 -> {
                String id = "player" + players.get(0).getId();
                Entity e = new Entity(0, 1, id);
                gameEntities.add(e);
                id = "player" + players.get(1).getId();
                e = new Entity(4, 11, id);
                gameEntities.add(e);
            }
            case 3 -> {
                String id = "player" + players.get(0).getId();
                Entity e = new Entity(3, 1, id);
                gameEntities.add(e);
                id = "player" + players.get(1).getId();
                e = new Entity(0, 8, id);
                gameEntities.add(e);
                id = "player" + players.get(2).getId();
                e = new Entity(5, 10, id);
                gameEntities.add(e);
            }
            case 4 -> {
                String id = "player" + players.get(0).getId();
                Entity e = new Entity(4, 0, id);
                gameEntities.add(e);
                id = "player" + players.get(1).getId();
                e = new Entity(0, 4, id);
                gameEntities.add(e);
                id = "player" + players.get(2).getId();
                e = new Entity(5, 8, id);
                gameEntities.add(e);
                id = "player" + players.get(3).getId();
                e = new Entity(0, 11, id);
                gameEntities.add(e);
            }
            case 5 -> {
                String id = "player" + players.get(0).getId();
                Entity e = new Entity(5, 0, id);
                gameEntities.add(e);
                id = "player" + players.get(1).getId();
                e = new Entity(0, 3, id);
                gameEntities.add(e);
                id = "player" + players.get(2).getId();
                e = new Entity(5, 6, id);
                gameEntities.add(e);
                id = "player" + players.get(3).getId();
                e = new Entity(0, 9, id);
                gameEntities.add(e);
                id = "player" + players.get(4).getId();
                e = new Entity(5, 11, id);
                gameEntities.add(e);
            }
            case 6 -> {
                String id = "player" + players.get(0).getId();
                Entity e = new Entity(0, 0, id);
                gameEntities.add(e);
                id = "player" + players.get(1).getId();
                e = new Entity(5, 2, id);
                gameEntities.add(e);
                id = "player" + players.get(2).getId();
                e = new Entity(0, 4, id);
                gameEntities.add(e);
                id = "player" + players.get(3).getId();
                e = new Entity(5, 6, id);
                gameEntities.add(e);
                id = "player" + players.get(4).getId();
                e = new Entity(0, 9, id);
                gameEntities.add(e);
                id = "player" + players.get(5).getId();
                e = new Entity(5, 12, id);
                gameEntities.add(e);
            }
            case 7 -> {
                String id = "player" + players.get(0).getId();
                Entity e = new Entity(0, 0, id);
                gameEntities.add(e);
                id = "player" + players.get(1).getId();
                e = new Entity(5, 2, id);
                gameEntities.add(e);
                id = "player" + players.get(2).getId();
                e = new Entity(0, 4, id);
                gameEntities.add(e);
                id = "player" + players.get(3).getId();
                e = new Entity(5, 6, id);
                gameEntities.add(e);
                id = "player" + players.get(4).getId();
                e = new Entity(0, 8, id);
                gameEntities.add(e);
                id = "player" + players.get(5).getId();
                e = new Entity(5, 10, id);
                gameEntities.add(e);
                id = "player" + players.get(6).getId();
                e = new Entity(0, 12, id);
                gameEntities.add(e);
            }
        }

        for (Player p:players) {
            ObjectOutputStream o = new ObjectOutputStream(p.getPlayerSocket().getOutputStream());
            o.writeObject(this);
        }
    }
    public void removePlayer(int id) throws IOException {
        for (Player p:players) {
            if(p.getId() == id){
                players.remove(p);
                p.getThread().interrupt();
                p.getPlayerSocket().close();
                return;
            }
        }
    }
    public void endGame(int id) throws IOException {
        if(players.get(0).getId() == id){
            for (Player p:players) {
                p.getThread().interrupt();
                p.getPlayerSocket().close();
            }
            players.clear();
        }else{
            removePlayer(id);
        }
    }

    public Entity getTapToPublish() {
        return tapToPublish;
    }

    public void publish(Entity tap) throws IOException {
        tapToPublish = tap;
        this.notifyAll();
        /*
        for (Player p:players) {
            if(p.getId() != playerToExclude){
                ObjectOutputStream o = new ObjectOutputStream(p.getPlayerSocket().getOutputStream());
                o.writeObject(tap);
            }
        }//*/

    }

    public int getPlayerCount(){
        return players.size();
    }

    @Override
    public boolean equals(Object obj) {
        return ((Game)obj).gameId == this.gameId;
    }
}
