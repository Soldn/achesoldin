
package ru.achesoldin.utils;

import ru.achesoldin.Achesoldin;

public class MessageManager {
    private final Achesoldin plugin;
    private final String langKey;

    public MessageManager(Achesoldin plugin) {
        this.plugin = plugin;
        this.langKey = plugin.getConfig().getString("language", "ru");
    }

    public String get(String key) {
        String path = "messages." + langKey + "." + key;
        String raw = plugin.getConfig().getString(path, "&c[" + key + "]");
        return raw.replace("&", "§");
    }
}
