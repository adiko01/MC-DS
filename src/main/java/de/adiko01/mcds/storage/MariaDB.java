package de.adiko01.mcds.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
     * Die Verbindung zur Datenbank
     * @since 1.0
     */
    private Connection conn;

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

        try {
            if (conn != null && !conn.isClosed()) {
                return true; // Verbindung besteht bereits
            }

            // Lade den MariaDB JDBC-Treiber
            Class.forName("org.mariadb.jdbc.Driver");

            // Erstelle die Verbindungs-URL
            String url = "jdbc:mariadb://" + Host + ":" + Port + "/" + Database;

            // Stelle die Verbindung zur Datenbank her
            conn = DriverManager.getConnection(url, Username, Password);

            System.out.println("Verbindung zur Datenbank hergestellt!");
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
