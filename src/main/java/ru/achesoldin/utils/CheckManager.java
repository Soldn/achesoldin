
package ru.achesoldin.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import ru.achesoldin.Achesoldin;

import java.util.*;

public class CheckManager {
    private static final Map<UUID, Location> originalLocation = new HashMap<>();
    private static final Map<UUID, UUID> moderatorOf = new HashMap<>();
    private static final Map<UUID, Integer> timerTaskId = new HashMap<>();

    public static boolean isOnCheck(UUID uuid) {
        return originalLocation.containsKey(uuid);
    }

    public static void addToCheck(Player target, Player moderator) {
        UUID id = target.getUniqueId();
        if (isOnCheck(id)) return;
        originalLocation.put(id, target.getLocation().clone());
        moderatorOf.put(id, moderator != null ? moderator.getUniqueId() : null);
        Achesoldin.getInstance().getChecksStore().startCheck(target, moderator);
        startTimer(target);
    }

    public static Player getModerator(Player target) {
        UUID modId = moderatorOf.get(target.getUniqueId());
        if (modId == null) return null;
        return Bukkit.getPlayer(modId);
    }

    public static void release(Player target) {
        UUID id = target.getUniqueId();
        Location back = originalLocation.remove(id);
        moderatorOf.remove(id);
        stopTimer(target);

        target.removePotionEffect(PotionEffectType.BLINDNESS);
        target.removePotionEffect(PotionEffectType.SLOW);

        if (back != null) target.teleport(back);
        Achesoldin.getInstance().getChecksStore().setResult(target, "released");
    }

    public static void releaseAll() {
        for (UUID id : new HashSet<>(originalLocation.keySet())) {
            Player p = Bukkit.getPlayer(id);
            if (p != null && p.isOnline()) release(p);
        }
        originalLocation.clear();
        moderatorOf.clear();
    }

    public static void ban(Player target, String reason) {
        stopTimer(target);
        String cmdTpl = Achesoldin.getInstance().getConfig().getString("ban-command", "ban %player% %reason%");
        String cmd = cmdTpl.replace("%player%", target.getName()).replace("%reason%", reason);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        originalLocation.remove(target.getUniqueId());
        moderatorOf.remove(target.getUniqueId());
        Achesoldin.getInstance().getChecksStore().setResult(target, "banned");
    }

    private static void startTimer(Player p) {
        int seconds = Achesoldin.getInstance().getConfig().getInt("timer-seconds", 60);
        final int[] time = {seconds};
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Achesoldin.getInstance(), () -> {
            if (!isOnCheck(p.getUniqueId())) {
                stopTimer(p);
                return;
            }
            if (time[0] <= 0) {
                String reason = Achesoldin.getInstance().getConfig().getString("ban-reason", "Auto block");
                ban(p, reason);
                return;
            }
            String msg = Achesoldin.getInstance().getMessages().get("timer").replace("%time%", String.valueOf(time[0]));
            p.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                    new net.md_5.bungee.api.chat.TextComponent(msg));
            time[0]--;
        }, 0L, 20L);
        timerTaskId.put(p.getUniqueId(), taskId);
    }

    private static void stopTimer(Player p) {
        Integer id = timerTaskId.remove(p.getUniqueId());
        if (id != null) Bukkit.getScheduler().cancelTask(id);
    }
}
