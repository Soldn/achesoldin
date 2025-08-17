
package ru.achesoldin.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.achesoldin.Achesoldin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChecksStore {
    private final Achesoldin plugin;
    private File file;
    private FileConfiguration data;

    public ChecksStore(Achesoldin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "checks.yml");
    }

    public void load() {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        data = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try { data.save(file); } catch (IOException e) { e.printStackTrace(); }
    }

    private String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    }

    public void setDiscord(Player p, String discord) {
        String base = "players." + p.getName() + ".last_check";
        data.set(base + ".discord", discord);
        save();
    }

    public void startCheck(Player p, Player moderator) {
        String base = "players." + p.getName() + ".last_check";
        data.set(base + ".moderator", moderator != null ? moderator.getName() : "console");
        data.set(base + ".date", now());
        data.set(base + ".result", "in_progress");
        save();
    }

    public void setResult(Player p, String result) {
        String base = "players." + p.getName() + ".last_check";
        data.set(base + ".result", result);
        save();
    }
}
