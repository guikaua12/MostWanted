package me.approximations.mostWanted.listener;

import me.approximations.mostWanted.Main;
import me.approximations.mostWanted.configuration.Messages;
import me.approximations.mostWanted.dao.UserDao;
import me.approximations.mostWanted.model.User;
import me.approximations.mostWanted.util.NumberUtils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerListeners implements Listener {
    private UserDao userDao;
    private Economy econ;

    public PlayerListeners(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        userDao = plugin.getUserDao();
        econ = plugin.getEcon();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Player killer = p.getKiller();
        if(killer == null || killer.getType() != EntityType.PLAYER) return;

        User user = userDao.getUsers().get(p.getName());
        if(!user.isInContract()) return;
        EconomyResponse response = econ.depositPlayer(killer, user.getHeadPrice());

        killer.sendMessage(Messages.KILLED
                .replace("{player}", p.getName())
                .replace("{value}", NumberUtils.format(user.getHeadPrice()))
        );

        user.setHeadPrice(0);
    }
}
