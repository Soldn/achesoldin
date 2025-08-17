
package ru.achesoldin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.entity.Player;
import ru.achesoldin.Achesoldin;
import ru.achesoldin.utils.CheckManager;

public class CommandPreprocessListener implements Listener {
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        if (!CheckManager.isOnCheck(p.getUniqueId())) return;

        String msg = event.getMessage().toLowerCase();
        if (msg.startsWith("/sds")) return;

        event.setCancelled(true);
        p.sendMessage(Achesoldin.getInstance().getMessages().get("only-sds"));
    }
}
