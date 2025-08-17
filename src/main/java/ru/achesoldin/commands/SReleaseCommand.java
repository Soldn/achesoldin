
package ru.achesoldin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import ru.achesoldin.Achesoldin;
import ru.achesoldin.utils.CheckManager;

public class SReleaseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command for players only.");
            return true;
        }
        Player moderator = (Player) sender;
        if (!moderator.hasPermission("achesoldin.release")) {
            moderator.sendMessage("§cNo permission.");
            return true;
        }
        if (args.length != 1) {
            moderator.sendMessage("§eUsage: /srelease <player>");
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            moderator.sendMessage(Achesoldin.getInstance().getMessages().get("player-not-found"));
            return true;
        }
        if (!CheckManager.isOnCheck(target.getUniqueId())) {
            moderator.sendMessage(Achesoldin.getInstance().getMessages().get("not-on-check"));
            return true;
        }
        CheckManager.release(target);
        String msg = Achesoldin.getInstance().getMessages().get("released-by")
                .replace("%player%", target.getName())
                .replace("%moderator%", moderator.getName());
        moderator.sendMessage(msg);
        target.sendMessage(Achesoldin.getInstance().getMessages().get("released"));
        return true;
    }
}
