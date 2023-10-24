# DYNMAP

[dynmap](https://github.com/webbukkit/dynmap)

## Installation
1. Installiere `dynmap` und `MC-DS` nach zugehörigen Installationsanleitungen.
2. `dynmap` in den Betriebsmodus MySQL auf abgesetzten Webserver konfigurieren.
3. In den zeile 464-479 der Datei`\plugins\dynmap\configuration.txt` muss der Eintrag für `login:` wie folgt angepasst werden.
   ```
    # URL for client configuration (only need to be tailored for proxies or other non-standard configurations)
    url:
    # configuration URL
    configuration: "standalone/MySQL_configuration.php"
    # update URL
    update: "standalone/MySQL_update.php?world={world}&ts={timestamp}"
    # sendmessage URL
    sendmessage: "standalone/MySQL_sendmessage.php"
    # login URL
    login: "standalone/MCDS_login.php"
    # register URL
    register: "standalone/MySQL_register.php"
    # tiles base URL
    tiles: "standalone/MySQL_tiles.php?tile="
    # markers base URL
    markers: "standalone/MySQL_markers.php?marker="
   ``` 
4. Auf dem Minecraft-Server muss die Datei `\plugins\dynmap\web\login.html` mit der aus diesem Ordner ersetzt werden
5. Ebenfalls muss die Datei `MCDS_login.php` nach `\plugins\dynmap\web\standalone\MCDS_login.php` kopieren.
6. In `MCDS_login.php` müssen die Daten der Datenbank des `MC-DS`-Plugins ergänzt werden
   ```
    /* DB DATEN DES MC-DS */
	$DSdbname = 'vinf_adrian';
	$DSdbhost = '192.168.210.5';
	$DSdbport = 3306;
	$DSdbuserid = 'vinf';
	$DSdbpassword = '4ow17A5Ac1JAJuB5lo5Iseha5eQ7lE';
	$DSdbprefix = 'mcds_';
   ```
7. Minecraft Server neu starten
8. Kopiere das `web` verzeichnis auf den Webserver für die dynmap