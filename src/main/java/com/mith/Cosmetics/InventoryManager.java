package com.mith.Cosmetics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mith.Cosmetics.gson.CosmeticsPlayerSerializer;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Level;

public class InventoryManager {

    private static InventoryManager instance = new InventoryManager();
    private final File FILE = new File("plugins/Cosmetics", "players.json");
    private final Gson gson = new GsonBuilder().registerTypeAdapter(CosmeticsPlayer.class, new CosmeticsPlayerSerializer()).create();

    private List<CosmeticsPlayer> players;

    private InventoryManager() {
        createFile();
        loadGson();
    }

    public static InventoryManager getInstance() {
        return instance;
    }

    private void createFile() {
        if (!FILE.getParentFile().isDirectory()) {
            FILE.getParentFile().mkdir();
        }

        if (!FILE.exists()) {
            try {
                FILE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadGson() {
        Scanner scanner = null;

        try {
            scanner = new Scanner(FILE);

            String json = scanner.nextLine();

            players = gson.fromJson(json, CosmeticsPlayerList.class).getPlayers();
        } catch (Exception ex) {
            if (!(ex instanceof NoSuchElementException)) {
                Bukkit.getLogger().log(Level.SEVERE, "[Cosmetics] Error loading players!", ex);
            }

            players = new ArrayList<>();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    public void saveGson() {
        try {
            PrintWriter writer = new PrintWriter(FILE);
            writer.write(gson.toJson(new CosmeticsPlayerList(players)));

            writer.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public CosmeticsPlayer getPlayer(UUID id) {
        for (CosmeticsPlayer ap : players) {
            if (ap.getUniqueId().equals(id))
                return ap;
        }

        return null;
    }

    public void createPlayer(UUID id) {
        CosmeticsPlayer ap = getPlayer(id);

        if (ap == null) {
            players.add(new CosmeticsPlayer(id, new ArrayList<>(), false));
            saveGson();
        }
    }
}
