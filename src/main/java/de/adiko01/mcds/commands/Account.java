package de.adiko01.mcds.commands;

import de.adiko01.mcds.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static de.adiko01.mcds.storage.PasswortTools.checkWeakPassword;

/**
 * Klasse des Befehls /account
 * @author adiko01
 * @version 1.0
 */
public class Account implements CommandExecutor, TabCompleter {
    /** Die Speicherengin */
    private Storage store;

    /**
     * Der Konstrucktor
     * @param store Die Speicherengin - {@link Storage}
     */
    public Account(Storage store) {
        this.store = store;
    }

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

                if (args.length != 2) {
                    p.sendMessage("Du musst dein Passwort eingeben!\n" +
                            "/account register your-password\n" +
                            "And don't uses spaces in your password!");
                    return false;
                }

                String pw = args[1];
                if (checkWeakPassword(pw)) {
                    p.sendMessage("Dein Passwort steht auf der Liste der unsicheren Passwörter!n" +
                            "Bitte wähle ein anderes!");
                        Bukkit.getLogger().warning("Die Registrierung von " + p.getName() + " ist fehlgeschlagen. - GRUND: Passwort steht auf der Blacklist.");
                    return false;
                }

                if (store.registerPlayer(p, pw)) {
                    Bukkit.getLogger().info(p.getName() + " hat sich erfolgreich registriert.");
                    p.sendMessage("Dein Account wurde erfolgreich angelegt!");
                    return true;
                } else {
                    p.sendMessage("Es ist ein Fehler aufgetreten, bitte versuche es später erneut.");
                    Bukkit.getLogger().warning("Die Registrierung von " + p.getName() + " ist fehlgeschlagen. - GRUND: Unbekannt");
                    return false;
                }

            } else if (args[0].equalsIgnoreCase("changepw")) {
                if (!p.hasPermission("mcds.account.changepw")) {
                    getPermError(commandSender, "mcds.account.changepw");
                    return false;
                }

                if (args.length != 2) {
                    p.sendMessage("Du musst dein Passwort eingeben!\n" +
                            "/account changepw your-password\n" +
                            "And don't uses spaces in your password!");
                    return false;
                }

                String pw = args[1];
                if (checkWeakPassword(pw)) {
                    p.sendMessage("Dein Passwort steht auf der Liste der unsicheren Passwörter!n" +
                            "Bitte wähle ein anderes!");
                    Bukkit.getLogger().warning("Das Ändern des Passwortes von " + p.getName() + " ist fehlgeschlagen. - GRUND: Passwort steht auf der Blacklist.");
                    return false;
                }

                if (store.changePawword(p, pw)) {
                    Bukkit.getLogger().info(p.getName() + " hat erfolgreich sein Passwort geaendert.");
                    p.sendMessage("Dein Passwort wurde erfolgreich geändert!");
                    return true;
                } else {
                    p.sendMessage("Es ist ein Fehler aufgetreten, bitte versuche es später erneut.");
                    Bukkit.getLogger().warning("Das Ändern des Passwortes von " + p.getName() + " ist fehlgeschlagen. - GRUND: Unbekannt");
                    return false;
                }

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
        if (
                args[0].equalsIgnoreCase("register")
                || args[0].equalsIgnoreCase("changepw")
        ) {
            //Commands ohne Vorschlag
            return new ArrayList<>();
        }

        //Liste aller Argumente des Commmand
        String[][] Commands = {
                //command snippet , permission
                {"changepw" , "mcds.account.changepw"},
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
                        + ChatColor.GOLD + "/account changepw [password]:" + ChatColor.RESET + " Change your password" + "\n"
                        + ChatColor.GOLD + "/account help :" + ChatColor.RESET + " Displays this page." + "\n"
                        + ChatColor.GOLD + "/account register [password]:" + ChatColor.RESET + " Register yourself, with the given password" + "\n"
        );
        return false;
    }
}
