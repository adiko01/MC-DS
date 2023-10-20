package de.adiko01.mcds.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**Das Objekt f&uuml;r eine MariaDB verbindung
 * @author adiko01
 * @version 1.0
 */
public class MariaDB extends Storage{

    /**
     * Der Host
     * @since 1.0
     */
    private final String Host;

    /**
     * Der Port
     * @since 1.0
     */
    private final int Port;

    /**
     * Die Datenbank
     * @since 1.0
     */
    private final String Database;

    /**
     * Der Username
     * @since 1.0
     */
    private final String Username;

    /**
     * Das Password
     * @since 1.0
     */
    private final String Password;

    /**
     * Der Tabellenprefix
     * @since 1.0
     */
    private final String Prefix;

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


            //Erstelle die Tabelle Services
            String SQL = "CREATE TABLE IF NOT EXISTS `" + Prefix + "Services` (" +
                    "  `ID` int(11) NOT NULL AUTO_INCREMENT," +
                    "  `Name` text NOT NULL," +
                    "  `URL` text DEFAULT NULL," +
                    "  `Created` timestamp NULL DEFAULT current_timestamp()," +
                    "  PRIMARY KEY (`ID`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;";
            sendData(SQL);

            //Erstellen der Tabelle Users
             SQL = "CREATE TABLE IF NOT EXISTS `" + Prefix + "Users` (" +
                    "  `UUID` tinytext NOT NULL," +
                    "  `Username` tinytext NOT NULL," +
                    "  `RegisterTime` timestamp NOT NULL DEFAULT current_timestamp()," +
                    "  `PwChangeTime` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()," +
                    "  `PwChangeService` int(11) NOT NULL," +
                    "  `PasswortSHA256` text NOT NULL," +
                    "  PRIMARY KEY (`UUID`(36))," +
                    "  KEY `FK_Users_Services` (`PwChangeService`)," +
                    "  CONSTRAINT `FK_Users_Services` FOREIGN KEY (`PwChangeService`) REFERENCES `mcds_Services` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;";
            sendData(SQL);

            //Erstelle den Eintrag der Services für das Plugin auf dem MC Server
            SQL = "INSERT INTO `" + Prefix + "Services` (`ID`, `Name`) SELECT 1, 'MC-DS Plugin Minecraft' WHERE NOT EXISTS (SELECT 1 FROM `" + Prefix + "Services` WHERE id = 1);";
            sendData(SQL);

            return true;
        } catch (ClassNotFoundException | SQLException e) {
            return false;
        }
    }

    /**
     * Führe ein SQL Statement aus
     * @param Query Das SQL-Query
     * @return {@code true}, wenn erfolgreich
     */
    private boolean sendData(String Query) {

        try {
            if (conn == null || conn.isClosed()) {
                createConnection();
            }
            Statement statement = conn.createStatement();
            statement.executeUpdate(Query);
            statement.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
