package me.approximations.mostWanted.configuration;

import me.approximations.mostWanted.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class Messages {
    public static String CONTRACT_SUCCESS;
    public static String INVALID_PLAYER;
    public static String INVALID_PRICE;

    public Messages(Main plugin) {
        FileConfiguration cfg = plugin.getMessagesConfig().getConfig();
        INVALID_PLAYER = ChatColor.translateAlternateColorCodes('&', cfg.getString("invalid-player"));
        INVALID_PRICE = ChatColor.translateAlternateColorCodes('&', cfg.getString("invalid-price"));
    }
}
