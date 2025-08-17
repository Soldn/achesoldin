
package ru.achesoldin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;
import ru.achesoldin.utils.CheckManager;

public class MoveBlockListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if (!CheckManager.isOnCheck(p.getUniqueId())) return;

        if (event.getFrom().distanceSquared(event.getTo()) != 0 ||
            event.getFrom().getYaw() != event.getTo().getYaw() ||
            event.getFrom().getPitch() != event.getTo().getPitch()) {
            event.setTo(event.getFrom());
        }
    }
}
