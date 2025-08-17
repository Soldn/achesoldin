
package ru.achesoldin.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.achesoldin.Achesoldin;
import ru.achesoldin.utils.CheckManager;

public class SCheckCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command for players only.");
            return true;
        }
        Player moderator = (Player) sender;
        if (!moderator.hasPermission("achesoldin.scheck")) {
            moderator.sendMessage("§cNo permission.");
            return true;
        }
        if (args.length != 1) {
            moderator.sendMessage("§eUsage: /scheck <player>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            moderator.sendMessage(Achesoldin.getInstance().getMessages().get("player-not-found"));
            return true;
        }
        if (CheckManager.isOnCheck(target.getUniqueId())) {
            moderator.sendMessage(Achesoldin.getInstance().getMessages().get("already-on-check"));
            return true;
        }

        String worldName = Achesoldin.getInstance().getConfig().getString("check-location.world");
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            moderator.sendMessage("§cWorld "" + worldName + "" not found.");
            return true;
        }
        double x = Achesoldin.getInstance().getConfig().getDouble("check-location.x");
        double y = Achesoldin.getInstance().getConfig().getDouble("check-location.y");
        double z = Achesoldin.getInstance().getConfig().getDouble("check-location.z");
        float yaw = (float) Achesoldin.getInstance().getConfig().getDouble("check-location.yaw");
        float pitch = (float) Achesoldin.getInstance().getConfig().getDouble("check-location.pitch");

        Location loc = new Location(world, x, y, z, yaw, pitch);

        CheckManager.addToCheck(target, moderator);
        target.teleport(loc);

        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1, false, false, false));
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 255, false, false, false));

        String title = Achesoldin.getInstance().getMessages().get("title");
        String subtitle = Achesoldin.getInstance().getMessages().get("subtitle").replace("%moderator%", moderator.getName());
        target.sendTitle(title, subtitle, 10, 20*60*60, 10);
        target.sendMessage(Achesoldin.getInstance().getMessages().get("chat"));

        moderator.sendMessage(Achesoldin.getInstance().getMessages().get("summoned")
                .replace("%player%", target.getName()));
        return true;
    }
}
