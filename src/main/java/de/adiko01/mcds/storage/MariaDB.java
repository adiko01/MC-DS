package de.adiko01.mcds.storage;

/**Das Objekt f&uuml;r eine MariaDB verbindung
 * @author adiko01
 * @version 1.0
 */
public class MariaDB extends Storage{

    /**
     * Der Host
     * @since 1.0
     */
    private String Host;

    /**
     * Der Port
     * @since 1.0
     */
    private int Port;

    /**
     * Die Datenbank
     * @since 1.0
     */
    private String Database;

    /**
     * Der Username
     * @since 1.0
     */
    private String Username;

    /**
     * Das Password
     * @since 1.0
     */
    private String Password;

    /**
     * Der Tabellenprefix
     * @since 1.0
     */
    private String Prefix;

    /**
     * Speichere die benötigten Variablen in dem Objekt
     * @param Host Der Host
     * @param Port Der Port
     * @param Database Die Datenbank
     * @param Username Der Benutzername
     * @param Password Das Passwort
     * @param Prefix Der Prefix
     * @since 1.0
     */
    public MariaDB(String Host, int Port, String Database, String Username, String Password, String Prefix) {
        this.Host = Host;
        this.Port = Port;
        this.Database = Database;
        this.Username = Username;
        this.Password = Password;
        this.Prefix = Prefix;
    }

    public boolean createConnection() {
        //TODO Baue die Verbindung auf und gebe true zurück, wenn erfolgreich
        return false;
    }
}
