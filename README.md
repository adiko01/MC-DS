# Minecraft Directory Service Plugin

Dieses Plugin stellt einen Verzeichnisdienst für Minecraft.

## Planung

- [ ] Erstelle Config
- [ ] Erstelle anbindung an MariaDB
- [ ] Plane Tabellenlayout
- [ ] Erstelle (wenn nicht vorhanden) Tabelle in der DB
- [ ] Erstelle Registrierung
- [ ] Erstelle Passwort ändern
- [ ] Erstelle zusätzliches Repo mit einer beispielhaften Web GUI

## Config
- [ ] DB Typ --> erstmal nur `mariadb`
- [ ] Verbindung `mariadb`
  - `host`
  - `port`
  - `datenbank`
  - `User`
  - `Password`
  - `Prefix`

## DB Layout
### Tabelle: User
| Spalte             | Datentyp  | Besonderes                            | Bemerkung               |
|--------------------|-----------|---------------------------------------|-------------------------|
| UUID               | TINYTEXT  | PRIMARY KEY (36)                      | Die UUID mit -          |
| Username           | TINYTEXT  |                                       |                         |
| RegisterDate       | TIMESTAMP | Beim erstellen CURRENT_TIMESTAMP()    |                         |
| LastPWChangeTime   | TIMESTAMP |                                       |                         |
| LastPWChangeMethod | INT       | Fremd Key auf ID der Tabelle Services |                         |
| PasswortSHA256     | TEXT      |                                       | Das Passwort als SHA256 |

### Tabelle: Services
| Spalte  | Datentyp  | Besonderes                         | Bemerkung                     |
|---------|-----------|------------------------------------|-------------------------------|
| ID      | INT       | PRIMARY KEY                        |                               |
| Name    | Text      | NOT NULL                           | Der Name des Dienstes         |
| URL     | Text      |                                    | Die URL zum Dienst (optional) |
| Created | TIMESTAMP | Beim erstellen CURRENT_TIMESTAMP() |                               |

## Registrierung
- [ ] `/register [Passwort]`
  - `mcds.own.register`
- [ ] `/ds register [User] [Passwort]`
  - `mcds.other.register`
- [ ] `/password [neues Passwort]`
  - `mcds.own.changepassword`
- [ ] `/ds block [User]`
  - `mcds.admin.blockuser`
  - `mcds.exception.bocking` --> Ausnahme: Kann nicht blockiert werden

## Verbindung zu anderen Plugins

- [ ] dynmap
- [ ] 

### DynMap
Der Login kann durch Anpassen der Files

- `plugins\dynmap\web\login.html` --> Entferne Registrieren
- `plugins\dynmap\`
  - Zeile 463 --> Eigene File
  - Zeile 465 --> Eigene File
- `plugins\dynmap\web\standalone\` --> Eigene Login File
- `plugins\dynmap\web\standalone\` --> Eigene Register File
- `plugins\dynmap\web\standalone\` --> Eigene Config für die Zugangsdaten zur DB

Alternativ:

- `plugins\dynmap\web\login.html` --> Entferne Registrieren
- Plugin bekommt Zugang zur DB der dynmap(s) und schreibt dort die Zugangsdaten im dynmap-Formtat hinein