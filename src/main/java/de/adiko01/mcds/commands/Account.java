package de.adiko01.mcds.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse des Befehls /account
 * @author adiko01
 * @version 1.0
 */
public class Account implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        boolean showHELP = false;

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("help")) {
                showHELP = true;
            } else {
                showHELP = true;
            }
        } else {
            showHELP = true;
        }

        if (showHELP) {
            //Prüfe, ob der Spieler atp.help besitzt
            if (commandSender instanceof Player) {
                Player p = (Player) commandSender;
                if (!p.hasPermission("mcds.account.help")) {
                    getPermError(commandSender, "mcds.account.help");
                    return false;
                }
            }

            commandSender.sendMessage(
                    ChatColor.YELLOW +"-------------- Help: /account ----------------------------" + ChatColor.RESET + "\n"
                            + ChatColor.GOLD + "Description:" + ChatColor.RESET + " Below is a list of all /ds commands:" + "\n"
                            + ChatColor.GOLD + "/account help :" + ChatColor.RESET + " Displays this page." + "\n"
            );
            return false;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        //Liste aller Argumente des Commmand
        String[][] Commands = {
                //command snippet , permission
                {"help" , "mcds.account.help"}
        };

        //Liste, welche zurückgegeben werden soll
        ArrayList<String> Ret = new ArrayList<>();
        //Aktueller Snippet des Arg
        String CurrentCommand = args[args.length-1];

        Player p;
        if (commandSender instanceof Player) {
            p = (Player) commandSender;
        } else {
            p = null;
        }

        for (String MCom[] : Commands) {
            if (MCom[0].startsWith(CurrentCommand)) {
                if ((p != null) && !p.hasPermission(MCom[1])) {
                    continue;
                }
                Ret.add(MCom[0]);
            }
        }

        return Ret;
    }

    /**
     * Zeigt einen Fehler, dass eine Permission fehlt
     * @param commandSender Der Sender
     * @param permission Die fehlende permission
     * @since 1.0
     */
    private void getPermError (CommandSender commandSender, String permission) {
        commandSender.sendMessage(ChatColor.RED + "This is not allowed! - You need " + ChatColor.BLUE + permission + ChatColor.RESET);
    }
}
