import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class PlayersStorage {
    private static ArrayList<Player> players = new ArrayList<>();

    public static void addPlayer(Player player) {
        players.add(player);
    }

    public static void removePlayer(Player player) {
        players.remove(player);
    }

    public static Player newPlayer(String name, String password) {
        var player = new Player();
        player.name = name;
        player.password = password;

        return player;
    }

    public static void editPlayer(Player player, String name) {
        var idx = players.indexOf(player);
        var newPlayer = new Player();
        newPlayer.name = name;
        newPlayer.password = player.password;

        players.set(idx, newPlayer);
    }

    public static Player editPlayer(Player player, Player new_player) {
        var idx = players.indexOf(player);
        players.set(idx, new_player);

        return new_player;
    }

    public static List<Player> getPlayers() {
        return players;
    }

    public static Player getByName(String name) {
        for (Player player: players) {
            if (player.name.equals(name)) {
                return player;
            }
        }
        return null;
    }

    private static void load_from_file() throws IOException {
        Gson gson = new Gson();
        JsonReader reader;
        try {
            reader = new JsonReader(new FileReader("./players.json"));
        } catch (Exception e) {
            if (e instanceof FileNotFoundException) {
                System.out.println("Database File not found!");
            }

            e.printStackTrace();
            System.exit(2);
            return;
        }

        Player[] _accounts = gson.fromJson(reader, Player[].class);
        players = new ArrayList<Player>(Arrays.asList(_accounts));
    }

    public static void load() {
        if (!Files.exists(Paths.get("players.json"))) {
            return;
        }
        try {
            load_from_file();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save_to_file() throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(players.toArray());

        Files.write(Paths.get("players.json"), json.getBytes());
    }

    public static void save()  {
        try {
            save_to_file();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
