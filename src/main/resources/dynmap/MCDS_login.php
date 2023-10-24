<?php

	/* DB DATEN DES MC-DS */
	$DSdbname = '';
	$DSdbhost = '';
	$DSdbport = 3306;
	$DSdbuserid = '';
	$DSdbpassword = '';
	$DSdbprefix = '';

	// Verbindung zur MySQL-Datenbank herstellen
	$mysqli = new mysqli($DSdbhost, $DSdbuserid, $DSdbpassword, $DSdbname, $DSdbport);

	// Überprüfen, ob die Verbindung erfolgreich war
	if ($mysqli->connect_error) {
		die("Verbindung zur MS-DS Datenbank fehlgeschlagen: " . $mysqli->connect_error);
	}
	
	$MyPassword = "";
	
	// SQL-Statement vorbereiten
	$user = $_POST['j_username'];
	$sql = "SELECT PasswortSHA256 FROM " . $DSdbprefix . "Users WHERE Username = ?";
	$stmt = $mysqli->prepare($sql);
	
	if ($stmt) {
		// Parameter binden
		$stmt->bind_param("s", $user);
		
		// Statement ausführen
		$stmt->execute();
		
		// Ergebnisse abrufen
		$stmt->bind_result($PasswortSHA256);
		
		// Ergebnisse anzeigen
		while ($stmt->fetch()) {
			$MyPassword = $PasswortSHA256;
		}
		
		// Statement schließen
		$stmt->close();
	} else {
		echo "Fehler beim Vorbereiten des Statements: " . $mysqli->error;
	}
	
	// Verbindung schließen
	$mysqli->close();
	
	ob_start();
	require_once 'MySQL_funcs.php';
	require 'MySQL_config.php';
	require 'MySQL_getlogin.php';
	ob_end_clean();
	
	session_start();
	
	if (isset($_POST['j_username'])) {
		$userid = $_POST['j_username'];
	} else {
		$userid = '-guest-';
	}
	$good = false;
	
	if (strcmp($userid, '-guest-')) {
		if (isset($_POST['j_password'])) {
			$password = $_POST['j_password'];
		} else {
			$password = '';
		}
		$ctx = hash_init('sha256');
		hash_update($ctx, $password);
		$hash = hash_final($ctx);
		$useridlc = strtolower($userid);
		if ($hash == $MyPassword) {
			$_SESSION['userid'] = $userid;
			$good = true;
		} else {
			$_SESSION['userid'] = '-guest-';
		}
	} else {
		$_SESSION['userid'] = '-guest-';
		$good = true;
	}
	$content = getStandaloneFile('dynmap_reg.php');
	
	/* Prune pending registrations, if needed */
	$lines = explode('\n', $content);
	$newlines[] = array();
	if (!empty($lines)) {
		$cnt = count($lines) - 1;
		$changed = false;
		for ($i = 1; $i < $cnt; $i++) {
			list($uid, $pc, $hsh) = explode('=', rtrim($lines[$i]));
			if ($uid == $useridlc) {
				continue;
			}
			if (array_key_exists($uid, $pendingreg)) {
				$newlines[] = $uid . '=' . $pc . '=' . $hsh;
			} else {
				$changed = true;
			}
		}
		if ($changed) {
			updateStandaloneFile('dynmap_reg.php', implode("\n", $newlines));
		}
	}
	
	if ($good) {
		echo "{ \"result\": \"success\" }";
	} else {
		echo "{ \"result\": \"loginfailed\" }";
	}
	
	cleanupDb();
