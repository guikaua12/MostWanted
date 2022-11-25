package me.approximations.mostWanted.configuration;

import me.approximations.mostWanted.Main;
import me.approximations.mostWanted.util.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class Messages {
    public static String INVALID_PLAYER;
    public static String INVALID_PRICE;
    public static String NOT_ENOUGH_MONEY;
    public static String CONTRACT_SUCCESS;
    public static String KILLED;
    public static String ALREADY_IN_CONTRACT;
    public static String COMMANDS;
    public static String PRICE_BELOW_MINIMUM;

    public static void init(Main plugin) {
        FileConfiguration cfg = plugin.getMessagesConfig().getConfig();
        INVALID_PLAYER = ChatColor.translateAlternateColorCodes('&', cfg.getString("invalid-player"));
        INVALID_PRICE = ChatColor.translateAlternateColorCodes('&', cfg.getString("invalid-price"));
        NOT_ENOUGH_MONEY = ChatColor.translateAlternateColorCodes('&', cfg.getString("not-enough-money"));
        CONTRACT_SUCCESS = ChatColor.translateAlternateColorCodes('&', cfg.getString("contract-success"));
        KILLED = ChatColor.translateAlternateColorCodes('&', cfg.getString("killed"));
        ALREADY_IN_CONTRACT = ChatColor.translateAlternateColorCodes('&', cfg.getString("already-in-contract"));
        PRICE_BELOW_MINIMUM = ChatColor.translateAlternateColorCodes('&', cfg.getString("price-below-minimum"));
        COMMANDS = ChatColor.translateAlternateColorCodes('&', cfg.getString("list-of-commands"));
    }
}
