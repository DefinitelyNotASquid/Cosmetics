package com.mith.Cosmetics;


import com.mith.Cosmetics.manager.ConfigManager;
import com.mith.Cosmetics.manager.Messages;
import com.mith.Cosmetics.utils.CosmeticsHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Cosmetics extends JavaPlugin {
	/**
	 * The Cosmetics handler that stores Cosmetics model data.
	 */
	private CosmeticsHandler cosmeticsHandler;
	private static Cosmetics plugin;
	/**
	 * The Cosmetics GUI.
	 */
	CosmeticsChatGui cosmeticsChatGui;

	@Override
	public void onEnable() {

		Messages.getInstance().reload();
		ConfigManager.getInstance().reloadConfiguration();

		plugin = this;
		cosmeticsChatGui = new CosmeticsChatGui(this);
		cosmeticsHandler = new CosmeticsHandler(this);

		// Register the chat listener
		Bukkit.getPluginManager().registerEvents(new Listeners(this), this);

		// Register the "cosmetic" and "cos" commands
		CosmeticsChatCommand cosmeticsChatCommand = new CosmeticsChatCommand(this);
		CosmeticsSkinCommand cosmeticsSkinCommand = new CosmeticsSkinCommand(this);
		CosmeticsDeskinCommand cosmeticsDeskinCommand = new CosmeticsDeskinCommand(this);

		CosmeticsChatTabComplete cosmeticsChatTabComplete = new CosmeticsChatTabComplete();
		getCommand("cosmetics").setExecutor(cosmeticsSkinCommand);
		getCommand("deskin").setExecutor(cosmeticsDeskinCommand);
		getCommand("ct").setExecutor(cosmeticsChatCommand);
		getCommand("ct").setTabCompleter(cosmeticsChatTabComplete);
	}

	@Override
	public void onDisable() {
		saveConfig();
		ConfigManager.getInstance().reloadConfiguration();
		InventoryManager.getInstance().saveGson();
		cosmeticsHandler.disable();
	}

	/**
	 * Gets the cosmetics handler.
	 *
	 * @return The cosmetics handler.
	 */
	public CosmeticsHandler getCosmeticsHandler() {
		return cosmeticsHandler;
	}
	public static Cosmetics getPlugin() { return plugin; }

	public static int copy(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[1024 * 4];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}

		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}
}
