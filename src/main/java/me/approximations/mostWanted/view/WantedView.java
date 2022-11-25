package me.approximations.mostWanted.view;

import com.google.common.collect.Lists;
import me.approximations.mostWanted.Main;
import me.approximations.mostWanted.dao.UserDao;
import me.approximations.mostWanted.manager.WantedManager;
import me.approximations.mostWanted.model.User;
import me.approximations.mostWanted.util.ItemBuilder;
import me.saiintbrisson.minecraft.*;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WantedView extends PaginatedView<User> {
    private Main plugin;
    private UserDao userDao;
    private WantedManager wantedManager;

    public WantedView(Main plugin) {
        super(5);
        this.plugin = plugin;
        this.userDao = plugin.getUserDao();
        this.wantedManager = plugin.getWantedManager();
        setCancelOnClick(true);
        setLayout("XXXXXXXXX",
                  "XOOOOOOOX",
                  "<OOOOOOO>",
                  "XOOOOOOOX",
                  "XXXXXXXXX"
        );

        setNextPageItem((c,i) -> {
            if(!c.hasNextPage()) return;
            i.withItem(new ItemBuilder(Material.ARROW).setName("§aNext page").wrap());
        });

        setPreviousPageItem((c,i) -> {
            if(!c.hasPreviousPage()) return;
            i.withItem(new ItemBuilder(Material.ARROW).setName("§aPrevious page").wrap());
        });
    }

    @Override
    protected void onItemRender(@NotNull PaginatedViewSlotContext<User> context, @NotNull ViewItem viewItem, @NotNull User user) {
        viewItem.withItem(
                new ItemBuilder(user.getName(), (byte) 0)
                        .setName("§a"+user.getName())
                        .setLore("§aHead price: §2$§f"+user.getHeadPrice())
                        .wrap()
        );
    }

    @Override
    protected void onRender(@NotNull ViewContext context) {
        context.paginated().setSource(c -> wantedManager.getWantedUsers());
    }
}
