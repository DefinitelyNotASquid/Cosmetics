package com.mith.Cosmetics.gson;


import com.google.gson.*;
import com.mith.Cosmetics.CosmeticsPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class CosmeticsPlayerSerializer implements JsonSerializer<CosmeticsPlayer>, JsonDeserializer<CosmeticsPlayer> {

    @Override
    public JsonElement serialize(CosmeticsPlayer player, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.addProperty("id", player.getUniqueId().toString());

        Gson gson = new Gson();

        JsonArray offlineItemsArray = new JsonArray();

        for (ItemStack is : player.getOfflineItems()) {
            String serialized = serialize(is);
            offlineItemsArray.add(serialized);
        }

        object.add("offlineItems", offlineItemsArray);
        object.addProperty("hasClaimedTag", player.getFreeTagStatus());
        return object;
    }

    @Override
    public CosmeticsPlayer deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = (JsonObject) element;

        UUID id = UUID.fromString(object.get("id").getAsString());

        Gson gson = new Gson();

        JsonArray array = object.getAsJsonArray("offlineItems");
        List<ItemStack> offlineItems = new ArrayList<>();

        for (JsonElement je : array) {
            try {
                ItemStack is = deserialize(je.getAsString());
                offlineItems.add(is);
            } catch (IOException e) {
                System.out.println("[Cosmetics] Failed to deserialize an item for player \"" + id + "\"");
            }
        }

        Boolean hasClaimedTag = Boolean.valueOf(object.get("hasClaimedTag").getAsString());

        return new CosmeticsPlayer(id, offlineItems, hasClaimedTag);
    }

    /**
     * Taken from https://gist.github.com/graywolf336/8153678#file-bukkitserialization-java
     */
    public static String serialize(ItemStack... items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.length);

            // Save every element in the list
            for (int i = 0; i < items.length; i++) {
                dataOutput.writeObject(items[i]);
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * Taken from https://gist.github.com/graywolf336/8153678#file-bukkitserialization-java
     */
    public static ItemStack deserialize(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items[0];
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}