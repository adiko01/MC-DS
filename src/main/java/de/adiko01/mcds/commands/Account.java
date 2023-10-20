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
        //Prüfe, ob ein Spieler handelt
        if (args.length < 1) {
            return showHelp(commandSender);
        } else if (args[0].equalsIgnoreCase("help")) {
            //Die Hilfe wurde von Non-Player Sender angefragt
            showHelp(commandSender);
            return true;
        } else if (commandSender instanceof Player) {
            //ECHTE BEFEHLE
            Player p = (Player) commandSender;

            if (args[0].equalsIgnoreCase("register")) {
                if (!p.hasPermission("mcds.account.register")) {
                    getPermError(commandSender, "mcds.account.register");
                    return false;
                }
                //TODO REGISTRIERE und prüfe Passwort
                return true;
            }
        } else {
            //Zeige Fehler, dass ein Spieler den Befehl ausführen muss
            commandSender.sendMessage(ChatColor.RED + "You need to ba a player to be able to use this command." + ChatColor.RESET);
            return false;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        //Liste aller Argumente des Commmand
        String[][] Commands = {
                //command snippet , permission
                {"help" , "mcds.account.help"},
                {"register" , "mcds.account.register"}
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

    /**
     * Shows the Help
     * @param commandSender Sender
     * @return false every time!
     * @since 1.0
     */
    private boolean showHelp(CommandSender commandSender) {
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
                        + ChatColor.GOLD + "/account register [password]:" + ChatColor.RESET + " Register yourself, with the given password" + "\n"
        );
        return false;
    }
}
