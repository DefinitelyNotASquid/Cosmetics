package com.mith.Cosmetics;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class CosmeticsPlayer {

    private UUID id;
    private List<ItemStack> offlineItems;
    private Boolean hasClaimedTag;

    public CosmeticsPlayer(UUID id,List<ItemStack> offlineItems, Boolean hasClaimedTag) {
        this.id = id;
        this.offlineItems = offlineItems;
        this.hasClaimedTag = hasClaimedTag;
    }

    public UUID getUniqueId() {
        return id;
    }

    public List<ItemStack> getOfflineItems() {
        return offlineItems;
    }

    public Boolean getFreeTagStatus(){return hasClaimedTag;}

    public void setFreeTagStatus(Boolean value){
        this.hasClaimedTag = value;
    }

    public Player getOnlinePlayer() {
        return Bukkit.getPlayer(id);
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(id);
    }
}
