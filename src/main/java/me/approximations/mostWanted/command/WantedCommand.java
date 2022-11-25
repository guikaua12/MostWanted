package me.approximations.mostWanted.command;

import lombok.RequiredArgsConstructor;
import me.approximations.mostWanted.Main;
import me.approximations.mostWanted.configuration.Messages;
import me.approximations.mostWanted.util.NumberUtils;
import me.approximations.mostWanted.view.WantedView;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class WantedCommand {
    private final Main plugin;

    @Command(name = "wanted", target = CommandTarget.PLAYER, description = "See all players with a price on their head.")
    public void onWantedCommand(Context<Player> e) {
        Player p = e.getSender();

        plugin.getViewFrame().open(WantedView.class, p);
    }

    @Command(name = "contract", target = CommandTarget.PLAYER, description = "Put a price on a player's head, if someone kills him, the killer will get that money.")
    public void onContractCommand(Context<Player> e, OfflinePlayer target, String price) {
        Player p = e.getSender();
        if(target == null || !target.hasPlayedBefore() || target.getName().equalsIgnoreCase(p.getName())) {
            p.sendMessage(Messages.INVALID_PLAYER);
            return;
        }
        double dPrice = NumberUtils.parse(price);
        if(NumberUtils.isInvalid(dPrice)) {
            p.sendMessage(Messages.INVALID_PRICE);
            return;
        }
    }


}
