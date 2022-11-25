package me.approximations.mostWanted.view;

import com.google.common.collect.Lists;
import me.approximations.mostWanted.Main;
import me.approximations.mostWanted.dao.UserDao;
import me.approximations.mostWanted.manager.WantedManager;
import me.approximations.mostWanted.model.User;
import me.approximations.mostWanted.util.ItemBuilder;
import me.approximations.mostWanted.util.TypeUtil;
import me.saiintbrisson.minecraft.*;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WantedView extends PaginatedView<User> {
    private final String CURRENT_FILTER_KEY = "CURRENT_FILTER_KEY";
    private final String SOURCE_KEY = "SOURCE_KEY";
    private Main plugin;
    private UserDao userDao;
    private WantedManager wantedManager;

    public WantedView(Main plugin) {
        super(5, "Contracts");
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
        List<User> source = wantedManager.getWantedUsers();
        context.paginated().setSource(c -> source);
        context.set(SOURCE_KEY, source);
        context.set(CURRENT_FILTER_KEY, 0);
        context.update();
    }

    @Override
    protected void onUpdate(@NotNull ViewContext context) {
        context.slot(40).withItem(new ItemBuilder(TypeUtil.getMaterialFromLegacy("INK_SACK"), 10)
                .setName("§aCurrent filter")
                .setLore("",
                        getCurrentFilterColor(getCurrentFilter(context), 0)+"Highest",
                        getCurrentFilterColor(getCurrentFilter(context), 1)+"Lowest"
                )
                .wrap()
        ).onClick(click -> {
            List<User> newSource = click.get(SOURCE_KEY);
            if(click.isLeftClick()) {
                newSource.sort( (u1,u2) -> Double.compare(u1.getHeadPrice(), u2.getHeadPrice()) );
                nextFilter(click);
            }else if(click.isRightClick()) {
                newSource.sort( (u2,u1) -> Double.compare(u1.getHeadPrice(), u2.getHeadPrice()) );
                previousFilter(click);
            }

            click.update();
        });
    }

    private int getCurrentFilter(ViewContext context) {
        return context.get(CURRENT_FILTER_KEY);
    }

    private int nextFilter(ViewContext context) {
        int currentFilter = getCurrentFilter(context);
        int nextFilter = Math.min(currentFilter+1, 1);
        context.set(CURRENT_FILTER_KEY, nextFilter);
        return nextFilter;
    }

    private int previousFilter(ViewContext context) {
        int currentFilter = getCurrentFilter(context);
        int previousFilter = Math.max(currentFilter-1, 0);
        context.set(CURRENT_FILTER_KEY, previousFilter);
        return previousFilter;
    }

    private String getCurrentFilterColor(int current, int reset) {
        return current == reset ? "§a▶ " : "§7 ";
    }
}
