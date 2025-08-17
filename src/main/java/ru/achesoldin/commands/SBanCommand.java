
package ru.achesoldin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import ru.achesoldin.Achesoldin;
import ru.achesoldin.utils.CheckManager;

public class SBanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command for players only.");
            return true;
        }
        Player moderator = (Player) sender;
        if (!moderator.hasPermission("achesoldin.ban")) {
            moderator.sendMessage("§cNo permission.");
            return true;
        }
        if (args.length < 1) {
            moderator.sendMessage("§eUsage: /sban <player> [reason]");
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            moderator.sendMessage(Achesoldin.getInstance().getMessages().get("player-not-found"));
            return true;
        }
        String reason;
        if (args.length >= 2) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                if (i > 1) sb.append(" ");
                sb.append(args[i]);
            }
            reason = sb.toString();
        } else {
            reason = Achesoldin.getInstance().getConfig().getString("ban-reason");
        }
        CheckManager.ban(target, reason);
        String msg = Achesoldin.getInstance().getMessages().get("banned-by")
                .replace("%player%", target.getName())
                .replace("%moderator%", moderator.getName())
                .replace("%reason%", reason);
        moderator.sendMessage(msg);
        return true;
    }
}
