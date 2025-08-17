
package ru.achesoldin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.achesoldin.commands.*;
import ru.achesoldin.listeners.*;
import ru.achesoldin.utils.CheckManager;
import ru.achesoldin.utils.MessageManager;
import ru.achesoldin.utils.ChecksStore;

public class Achesoldin extends JavaPlugin {
    private static Achesoldin instance;
    private MessageManager messages;
    private ChecksStore checksStore;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.messages = new MessageManager(this);
        this.checksStore = new ChecksStore(this);
        this.checksStore.load();

        getCommand("scheck").setExecutor(new SCheckCommand());
        getCommand("srelease").setExecutor(new SReleaseCommand());
        getCommand("sban").setExecutor(new SBanCommand());
        getCommand("sds").setExecutor(new SDSCommand());

        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new MoveBlockListener(), this);
        getServer().getPluginManager().registerEvents(new CommandPreprocessListener(), this);

        Bukkit.getLogger().info("[Achesoldin] enabled!");
    }

    @Override
    public void onDisable() {
        CheckManager.releaseAll();
        checksStore.save();
        Bukkit.getLogger().info("[Achesoldin] disabled!");
    }

    public static Achesoldin getInstance() { return instance; }
    public MessageManager getMessages() { return messages; }
    public ChecksStore getChecksStore() { return checksStore; }
}
