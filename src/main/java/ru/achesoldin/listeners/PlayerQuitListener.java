
package ru.achesoldin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;
import ru.achesoldin.Achesoldin;
import ru.achesoldin.utils.CheckManager;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if (!CheckManager.isOnCheck(p.getUniqueId())) return;

        String reason = Achesoldin.getInstance().getConfig().getString("ban-reason");
        CheckManager.ban(p, reason);

        String msg = Achesoldin.getInstance().getMessages().get("auto-ban")
                .replace("%player%", p.getName());

        Bukkit.getOnlinePlayers().stream()
                .filter(pl -> pl.hasPermission("achesoldin.staff"))
                .forEach(pl -> pl.sendMessage(msg));
    }
}
