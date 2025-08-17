
package ru.achesoldin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import ru.achesoldin.Achesoldin;
import ru.achesoldin.utils.CheckManager;

public class SDSCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command for players only.");
            return true;
        }
        Player player = (Player) sender;
        if (!CheckManager.isOnCheck(player.getUniqueId())) {
            player.sendMessage(Achesoldin.getInstance().getMessages().get("not-on-check"));
            return true;
        }
        if (args.length < 1) {
            player.sendMessage("§eUsage: /sds <discord>");
            return true;
        }
        String discord = String.join(" ", args);

        Player moderator = CheckManager.getModerator(player);
        String staffMsg = Achesoldin.getInstance().getMessages().get("moderator-received")
                .replace("%player%", player.getName())
                .replace("%discord%", discord);

        if (moderator != null && moderator.isOnline()) moderator.sendMessage(staffMsg);

        String finalStaffMsg = staffMsg;
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission("achesoldin.staff"))
                .forEach(p -> p.sendMessage(finalStaffMsg));

        Achesoldin.getInstance().getChecksStore().setDiscord(player, discord);

        player.sendMessage(Achesoldin.getInstance().getMessages().get("player-sent"));
        return true;
    }
}
