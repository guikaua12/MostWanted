package me.approximations.mostWanted.command;

import lombok.RequiredArgsConstructor;
import me.approximations.mostWanted.Main;
import me.approximations.mostWanted.configuration.Messages;
import me.approximations.mostWanted.dao.UserDao;
import me.approximations.mostWanted.dao.repository.UserRepository;
import me.approximations.mostWanted.model.User;
import me.approximations.mostWanted.util.NumberUtils;
import me.approximations.mostWanted.view.WantedView;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class WantedCommand {
    private final Main plugin;
    private final Economy econ;
    private final UserDao userDao;
    private final UserRepository userRepository;

    @Command(name = "contracts", target = CommandTarget.PLAYER, description = "See all players with a price on their head.")
    public void onWantedCommand(Context<Player> e) {
        Player p = e.getSender();

        plugin.getViewFrame().open(WantedView.class, p);
    }

    @Command(name = "contract", target = CommandTarget.PLAYER, description = "Put a price on a player's head, if someone kills him, the killer will get that money.")
    public void onContractCommand(Context<Player> e, @Optional OfflinePlayer target, @Optional String price) {
        Player p = e.getSender();
        if(e.argsCount() < 2) {
            p.sendMessage(Messages.COMMANDS);
        }else if(e.argsCount() == 2) {
            if(target == null || !target.hasPlayedBefore() || target.getName().equalsIgnoreCase(p.getName())) {
                p.sendMessage(Messages.INVALID_PLAYER);
                return;
            }
            double dPrice = NumberUtils.parse(price);
            if(NumberUtils.isInvalid(dPrice)) {
                p.sendMessage(Messages.INVALID_PRICE);
                return;
            }
            double minimumPrice = plugin.getConfig().getDouble("general.minimum-price");
            if(dPrice < minimumPrice) {
                p.sendMessage(Messages.PRICE_BELOW_MINIMUM
                        .replace("{minimumPrice}", NumberUtils.format(minimumPrice))
                );
                return;
            }
            if(!econ.has(p, dPrice)) {
                p.sendMessage(Messages.NOT_ENOUGH_MONEY);
                return;
            }

            User user;
            if(target.isOnline()) {
                user = userDao.getUsers().get(target.getName());
                if(user.isInContract()) {
                    p.sendMessage(Messages.ALREADY_IN_CONTRACT
                            .replace("{player}", target.getName())
                    );
                    return;
                }

                user.setHeadPrice(dPrice);
            }else {
                user = userRepository.get(target.getName());
                if(user == null) {
                    p.sendMessage(Messages.INVALID_PLAYER);
                    return;
                }
                if(user.isInContract()) {
                    p.sendMessage(Messages.ALREADY_IN_CONTRACT
                            .replace("{player}", target.getName())
                    );
                    return;
                }
                user.setHeadPrice(dPrice);
                userRepository.update(user);
            }

            EconomyResponse r = econ.withdrawPlayer(p, dPrice);

            p.sendMessage(Messages.CONTRACT_SUCCESS
                    .replace("{price}", NumberUtils.format(dPrice))
                    .replace("{player}", target.getName())
            );
        }
    }


}
