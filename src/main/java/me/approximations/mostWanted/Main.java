package me.approximations.mostWanted;

import com.jaoow.sql.executor.SQLExecutor;
import lombok.Getter;
import me.approximations.mostWanted.command.WantedCommand;
import me.approximations.mostWanted.dao.SQLProvider;
import me.approximations.mostWanted.dao.UserDao;
import me.approximations.mostWanted.dao.adapter.UserAdapter;
import me.approximations.mostWanted.dao.repository.UserRepository;
import me.approximations.mostWanted.dao.scheduler.AutoSave;
import me.approximations.mostWanted.manager.WantedManager;
import me.approximations.mostWanted.model.User;
import me.approximations.mostWanted.util.ConfigReader;
import me.approximations.mostWanted.view.WantedView;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.saiintbrisson.minecraft.ViewFrame;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Main extends JavaPlugin {
    @Getter
    private static Main instance;

    private SQLExecutor sqlExecutor;
    private UserRepository userRepository;
    private UserDao userDao;
    private Economy econ;
    private ViewFrame viewFrame;
    private BukkitFrame bukkitFrame;
    private WantedManager wantedManager;

    private ConfigReader messagesConfig;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        messagesConfig = new ConfigReader(this, "", "messages.yml");
        messagesConfig.saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            getLogger().severe("Vault not found, disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        instance = this;
        setupDatabase();
        setupView();
        setupCommand();
        wantedManager = new WantedManager(userDao);
    }

    private void setupDatabase() {
        SQLProvider sqlProvider = new SQLProvider(this);
        sqlExecutor = sqlProvider.setupDatabase();
        UserAdapter userAdapter = new UserAdapter();
        sqlExecutor.registerAdapter(User.class, userAdapter);
        userRepository = new UserRepository(this, sqlExecutor);
        userRepository.createTable();

        userDao = new UserDao(userRepository, this);

        sqlProvider.registerEvents();

        AutoSave autoSave = new AutoSave(this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private void setupView() {
        viewFrame = ViewFrame.of(this, new WantedView(this)).register();
    }

    private void setupCommand() {
        bukkitFrame = new BukkitFrame(this);
        bukkitFrame.registerCommands(new WantedCommand(this));
    }
}