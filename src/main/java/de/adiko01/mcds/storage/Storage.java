package de.adiko01.mcds.storage;

import org.bukkit.entity.Player;

/**
 * Das Oberklasse der Storageengin
 * @author adiko01
 * @version 1.0
 */
abstract public class Storage {

    /**
     * Teste die Verbindung zum Speicher.
     * Wenn der Storage keine Connection aufbauen muss,
     * wird true zurückgegeben.
     * @return Status true | false
     * @since 1.0
     */
    abstract public boolean createConnection();

    /**
     * Regestriere einen Spieler in der DB
     * @param p The {@link Player}
     * @param Password The Password
     * @return Erfolg
     * @since 1.0
     */
    abstract public boolean registerPlayer(Player p, String Password);
}
