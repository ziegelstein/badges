/**
 * 
 */
package de.psfanserver.badge;


import java.util.ArrayList;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.bukkit.command.Command;

/**
 * How to deal with this class:
 * First you initialize the Database with void connect
 * You should run this method periodic
 * 
 * After that you can add Player with createPlayer; Badges with createbadge
 * and give a player a badge with createTransaction
 * 
 * You can use a lot of different methods to get the information you want - I sort them into
 * three different categories:
 * > General - Common queries, mainly to get the relations between badge and players
 * > Tool - More or less use-full methods, mainly for intern usage
 * > Searching - Searching queries to get a specific badge or player or transaction 
 * 
 * The PlugIn contain 3 Tables, player for the players, badge for badges and transaction 
 * for the connection between players and badges.
 * 
 * German [Nur fuer Buli <3]
 * Wie diese Klasse benutzt werden sollte:
 * Als erste initialisierst du die Datenbank(verbindung) mit "public void connect"
 * Du solltest regelmaessig die Verbindung mit dieser Methode ueberpruefen.
 * 
 * Als naechstes findest du verschiedene Methoden um Datensaetze zu erstellen, Du brauchst
 * immer ein vollstaendiges Datenobjekt (Badge, BadgePlayer) um einen Datensatz zu erstellen.
 * Du kannst Spieler mit "public void createplayer" erstellen, Badges mit "public void createbadge" und
 * ein Badge einem Spieler mit "public void createTransaction" zuweisen.
 * 
 * Danach findest du viele Verschiedene Suchabfrage Methoden. Nomalerweise brauchen sie ein Datenobjekt und
 * geben immer ihre Informationen als Datenobjekt zurück. Ich habe die Suchabfrage Methoden in drei Klassen
 * unterteilt:
 * > Allgemein - Gewoehnliche Abfragen - wirst du vermutlich am meisten benutzen
 * > Hilfswerkzeuge - Fuer dich vermutlich nutzlos, sind eher fuer den interen Gebrauch
 * > Suche - Geben dir die moeglichkeit bestimmte Datensatze, auch mit unvollstaendingen Informationen
 *   (Also UUID / Name etc.) Datenobjekte zu erhalten.
 *   
 * Das PlugIn besitzt drei Tabellen, player fuer die Spieler, badge fuer die badges und transaction fuer
 * die verbindungen zwischen Spieler und Badges.
 * 
 * 
 * @author felix
 *
 */
public class mySQLconnector {

	/**
	 * @param args
	 */
	public mySQLconnector() {

	}
    public MySQL myConnector;
	
    
    
    // mainfunctions
    
    /**
     * Run this method at plugin-start
     * Create three Tables.
     * payer contain the uuid and the Name all players
     * badge contain the bid (the unique identifier), bname (the Badge Name), bblockId (the ID of the Bagdeimage), 
     * benchant (the enchantment of the Badge Image) and blore (the badge description)
     * 
     * @param dbname
     * @param dbuser
     * @param dbpw
     */
	public void connect (String dbname, String dbuser,String dbpw  ) {
		        String[] tables = new String[3];
		        tables[0] = "CREATE TABLE IF NOT EXISTS `player` " +
		        			"(uuid INT NOT NULL PRIMARY KEY, " +
		        			"pname VARCHAR(20))";
	    		tables[1] = "CREATE TABLE IF NOT EXISTS `transaction` " +
	    					"(transid INT NOT NULL PRIMARY KEY, " +
	    					"puuid INT REFERENCES player (uuid),"+ 
	    					"badgeid INT REFERENCES badge (bid)," +
	    					"transtime TIMESTAMP)";
	    		tables[2] = "CREATE TABLE IF NOT EXISTS `badge` " +
	    				  	"(bid INT NOT NULL PRIMARY KEY, " +
	    				  	"bname VARCHAR (255)"+
	    				  	"bblockid INT"+
	    				  	"benchant VARCHAR (255))"+
	    				  	"blore TEXT (255)";
	    		myConnector.connect("localhost","3306",dbname,dbuser,dbpw,tables);
	}
	
	
	
	// updatefunctions
	
	/**
	 * Create an Player dataSet
	 * @param uuid
	 * @param name
	 */
	public void createplayer (String uuid, String name) {
		int intuuid = Integer.parseInt(uuid);
		myConnector.doUpdate("INSERT INTO player VALUES ("+ intuuid +", "+name);
	}
	
	/**
	 * Create an Badge dataSet
	 * @param bid
	 * @param Name
	 * @param bitemid
	 */
	public void createbadge (int bid, String bname, int bitemid, String enchant, String[] lore) {
		if (this.checkBadgeItem(bitemid)) {
			// Warns if ItemID is already used but don't stop the process
			cs.sendMessage("Warnung: Item wird schon verwendet!");
		}
		String fullLore = "";
		for (int i = 0;i < lore.length;i++) {
			if (i == 0) {
				fullLore = lore[i];
			}
			else {
				fullLore = fullLore + "/n" + lore[i];
			}
		}
		myConnector.doUpdate("INSERT INTO badge VALUES ("+ bid +", "+bname+", "+bitemid+", "+enchant+", "+fullLore);
	}
	
	/**
	 * Create a new Transaction. The transaction Id is the uuid and the badgeId combined. So never ever
	 * give a player a badge twice!!
	 * @param uuid the unique ID of a Player. Is equal to the Minecraft UUID
	 * @param bid the BadgeID. Is created from the Plugin
	 */
  public void createTransaction (String uuid, int bid) {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		// Check if Badge is already given:
		if (this.checkTransaction(uuid, bid)) {
			cs.sendMessage("Badge NIEMALS zweimal an den selben Spieler vergeben, du Mongo! ");
		}
		else {
			myConnector.doUpdate("INSERT INTO transaction VALUES ("+ (uuid+bid) + ", " + uuid + ", "+ bid+", "+ dateFormat.format(date));
		}
  }
	// Write an Updater wich can delete Badges / Player / Transaction
  
  
  
	// Querryfunctions
    // General
	
	/**
	 * 
	 * Give you all Badges of a Player in an ArrayList
	 * @param currentPlayer
	 * @return
	 * @throws SQLException 
	 */
	public ArrayList<Badge> badgeListReturn (BadgePlayer currentPlayer) {
		/*
		 * The First Line generate a ResultSet (An Object which Contain the query)
		 * We need to send a valid SQL query-statement to myConnector (in the method getQuerry)
		 * In this Case we Select every Row in the badge-table which have the same bid as the query in the transaction table 
		 * (Where the uuid is equal the currentPlayer)
		 */
		ResultSet result = myConnector.getQuerry("SELECT b.* FROM badge b WHERE b.bid IN (SELECT t.bid FROM transaction t WHERE t.puuid = '"+currentPlayer.getUuid()+"'");
		/*
		 * After that we create our returnlist
		 * (The Name returnString have historic reasons)
		 */
		ArrayList<Badge> returnString = new ArrayList<Badge>();
		// We need to catch potential errors
		try {
			/*
			 * This is a beautiful while-loop and a even more beautiful .next method
			 * the next method not only push the object-pointer (of result) to the next row, no!
			 * It also return a bool if a next row exist. Beautiful!
			 */
			while (result.next()) {
				/*
				 * Here you can see a lots of data fetching from the curent loaded row (in the result object)
				 */
			  int bid = result.getInt("bid");
			  String bname = result.getString("bname");
			  int bblockid = result.getInt("bblockid");
			  String benchant = result.getString("benchant");
			  String blore = result.getString("blore");
			    /*
			     * If we have all the datas we need, we can create a new Badge Object (with the fetched data)
			     * and add the created object to the returnList
			     */
			  Badge temp = new Badge(bid, bname, bblockid, benchant,loreConverter(blore));
			  returnString.add(temp);
			}
			/*
			 * Thats all! We have a complete ArrayList of Rows which match with our SQL-Statement
			 */
          return returnString;
          } catch(SQLException e) {
        	  // Some Error handling, hope we will never need it.
        	  System.err.println("Fehler in mySQLconnector.badgeListReturn");
            e.printStackTrace();
			return null;
		}
	}

	
	/**
	 * I don't know what this method was planned for, so please don't use it :)
	 * @param currentBadge
	 * @return
	 */
	public String badgeSingleReturn (Badge currentBadge) {
		return null;
	}
	
	/**
	 * Count the Number of Players who own a specific badge
	 * @param currentPlayer
	 * @return
	 */
	public int badgeCount (BadgePlayer currentPlayer) {
		// Select all Rows from the transaction-table where the Uuid is equal with currentPlayer
		ResultSet result = myConnector.getQuerry("SELECT * FROM transaction WHERE uuid = '"+currentPlayer.getUuid()+"'");
		try {
			// Solution stolen from http://stackoverflow.com/questions/162571/how-do-i-get-the-row-count-in-jdbc
			int rowCount = result.last() ? result.getRow() : 0;
			return rowCount;
		} catch(SQLException e) {
			System.err.println("Fehler in mySQLconnector.badgeCount");
            e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * List all Player who own the currentBadge
	 * @param currentBadge
	 * @return Array List is the best list!
	 */
	public ArrayList<BadgePlayer> badgeListPlayer (Badge currentBadge) {
		//Select all Rows from the Player Table where the uuid is equal with the uuid in transaction (after filter all rows in transaction where the badgeId is equal with the currentBadge)
		ResultSet result = myConnector.getQuerry("SELECT p.* FROM player p WHERE p.uuid IN (SELECT t.uuid FROM transaction t WHERE t.badgeid = "+ currentBadge.getBadgeId());
		ArrayList<BadgePlayer> returnList = new ArrayList<BadgePlayer>();
		try {
			//Start to get all results
			while (result.next()) {
				// Fetch Data from the current Row
				String uuid = result.getNString("uuid");
				String pname = result.getString("pname");
				BadgePlayer temp = new BadgePlayer(uuid, pname);
				returnList.add(temp);
			}
			return returnList;
	    } catch(SQLException e) {
		System.err.println("Fehler in mySQLconnector.badgeListPlayer");
        e.printStackTrace();
		return null;
	    }
	}
	
	//Help
	
	/**
	 * Return the latest Badge, using the last operation of the ResultSet
	 * (Set the Pointer the last Row of the badge table)
	 * @return a Badge
	 */
	public Badge badgeReturnLatestBadge () {
		//Select all rows from badge-table
		ResultSet result = myConnector.getQuerry("SELECT * FROM badge");
		try {
			//Go to the last (and latest) row
			result.last();
			//Fetch the information
			  int bid = result.getInt("bid");
			  String bname = result.getString("bname");
			  int bblockid = result.getInt("bblockid");
			  String benchant = result.getString("benchant");
			  String blore = result.getString("blore");
			  return new Badge(bid, bname, bblockid, benchant,loreConverter(blore));
		} catch(SQLException e) {
			System.err.println("Fehler in mySQLconnector.badgeReturnLatestBadge");
	        e.printStackTrace();
			return null;
		    }
	}
	
	/**
	 * Return the latest User
	 * @return BadgePlayer
	 */
	public BadgePlayer badgeReturnLatestPlayer () {
		// Select all rows from player-table
		ResultSet result = myConnector.getQuerry("SELECT * FROM player");
		try {
			// Go to the Last (and latest) row
			result.last();
			String uuid = result.getString("uuid");
			String pname = result.getString("pname");
			return new BadgePlayer(uuid, pname);
		} catch(SQLException e) {
			System.err.println("Fehler in mySQLconnector.badgeReturnLatestPlayer");
	        e.printStackTrace();
			return null;
		    }
	}
	
	/**
	 * Check if a Badge with the given ID exist
	 * @param badgeID
	 * @return
	 */
	public boolean checkBadgeID (int badgeID) {
		return myConnector.doCheck("SELECT * FROM bagde WHERE bid = "+ badgeID);
	}
	
	/**
	 * Check if a Badge with the given Name exist
	 * @param badgeName
	 * @return
	 */
	public boolean checkBadgeName (String badgeName) {
		return myConnector.doCheck("SELECT * FROM badge WHERE bname = '"+ badgeName +"'");
	}
	
	/**
	 * Check if given ItemID exists (Usefull if you want to check if Item is already taken
	 * @param bblockID
	 * @return
	 */
	public boolean checkBadgeItem (int bblockID) {
		return myConnector.doCheck("SELECT * FROM badge WHERE bblockid = "+bblockID);
	}
	
	/**
	 * Check if Player with UUID exists
	 * @param uuid
	 * @return
	 */
	public boolean checkPlayerUUID (String uuid) {
		return myConnector.doCheck("SELECT * FROM player WHERE uuid = '"+ uuid +"'");
	}
	
	/**
	 * Check if Player with given Name exists
	 * @param pname
	 * @return
	 */
	public boolean checkPlayerName (String pname) {
		return myConnector.doCheck("SELECT * FROM player WHERE pname = '"+pname+"'");
	}
	
	/**
	 * Importand Method, Check if a badge is already given to a player
	 * @param uuid
	 * @param badgeId
	 * @return
	 */
	public boolean checkTransaction (String uuid, int badgeId) {
		return myConnector.doCheck("SELECT * FROM transaction WHERE puuid ='"+uuid+"' AND badgeid = "+ badgeId);
	}
	
	// Search
	
	/**
	 * It's easy, you give me an ID and i will give you an brand new BadgeObject!
	 * @param bid
	 * @return
	 */
	public Badge searchBadgeFromID (int bid) {
		//Check if Badge even exist
		if (this.checkBadgeID(bid)) {
			// ToDo: Write Comment
		ResultSet result = myConnector.getQuerry("SELECT * FROM badge WHERE bid = "+ bid);
		try {
			// Gathering Data
			result.first();
			int badgeId = result.getInt("bid");
			String badgeName = result.getString("bname");
			int badgeItem = result.getInt("bblockid");
			String badgeEnchant = result.getString("benchant");
			String badgeLore = result.getString("blore");
			return new Badge(badgeId, badgeName, badgeItem, badgeEnchant, this.loreConverter(badgeLore));
		} catch(SQLException e) {
			// Some Errorhandling
			System.err.println("Fehler in mySQLconnector.searchBadgeFromID");
	        e.printStackTrace();
			return null;
		    }
		} else { cs.sendMessage("Badge existiert nicht"); }
	}
	
	/**
	 * Need an String Name, will return an ArrayList in Case there is more than one Badge with the same Name
	 * @param Name
	 * @return
	 */
	public ArrayList<Badge> searchBadgesFromName (String Name) {
		//Check if Badge even exist
		if (this.checkBadgeName(Name)) {
			// ToDo: Write good Comment
		ResultSet result = myConnector.getQuerry("SELECT * FROM badge WHERE bname = '"+ Name +"'");
		ArrayList<Badge> returnList = new ArrayList<Badge>();
		try {
			// Gathering Data
			while (result.next()) {
			int badgeId = result.getInt("bid");
			String badgeName = result.getString("bname");
			int badgeItem = result.getInt("bblockid");
			String badgeEnchant = result.getString("benchant");
			String badgeLore = result.getString("blore");
			Badge temp = new Badge(badgeId, badgeName, badgeItem, badgeEnchant, this.loreConverter(badgeLore));
			returnList.add(temp);
			}
			return returnList;
		} catch(SQLException e) {
			// Some Errorhandling
			System.err.println("Fehler in mySQLconnector.searchBadgeFromID");
	        e.printStackTrace();
			return null;
		    }
		} else { cs.sendMessage("Badge existiert nicht"); }
	}
	
	/**
	 * Give you all Badges wich use the specific item
	 * @param Item
	 * @return
	 */
	public ArrayList<Badge> searchBadgesFromItem (int Item) {
		//Check if Badge even exist
		if (this.checkBadgeItem(Item)) {
			// ToDo: Write good use-full comment
		ResultSet result = myConnector.getQuerry("SELECT * FROM badge WHERE bblockid = "+ Item);
		ArrayList<Badge> returnList = new ArrayList<Badge>();
		try {
			// Gathering Data
			while (result.next()) {
			int badgeId = result.getInt("bid");
			String badgeName = result.getString("bname");
			int badgeItem = result.getInt("bblockid");
			String badgeEnchant = result.getString("benchant");
			String badgeLore = result.getString("blore");
			Badge temp = new Badge(badgeId, badgeName, badgeItem, badgeEnchant, this.loreConverter(badgeLore));
			returnList.add(temp);
			}
			return returnList;
		} catch(SQLException e) {
			// Some Errorhandling
			System.err.println("Fehler in mySQLconnector.searchBadgeFromID");
	        e.printStackTrace();
			return null;
		    }
		} else { cs.sendMessage("Es gibt kein Badge mit diesem Item"); }
	}
	
	/**
	 * Give you all Badges of an Player if you have the Name of the Player
	 * @param pname
	 * @return
	 */
	public ArrayList<Badge> searchAllBadgesFromPlayerName (String pname) {
		// Check if Player with this Name even exist
		if (this.checkPlayerName(pname)) {
		// ToDo: Explain the following SQL Statement AlSO We need to test the Statement
		ResultSet result = myConnector.getQuerry("SELECT b.* FROM badge b WHERE b.bid IN (SELECT t.bid FROM transaction t WHERE t.puuid IN (SELECT p.uuid FROM player p WHERE p.pname = '" +pname+ "'))");
		ArrayList<Badge> returnString = new ArrayList<Badge>();
		// We need to catch potential errors
		try {
			while (result.next()) {
			  int bid = result.getInt("bid");
			  String bname = result.getString("bname");
			  int bblockid = result.getInt("bblockid");
			  String benchant = result.getString("benchant");
			  String blore = result.getString("blore");
			  Badge temp = new Badge(bid, bname, bblockid, benchant,loreConverter(blore));
			  returnString.add(temp);
			}
          return returnString;
          } catch(SQLException e) {
        	  // Some Error handling, hope we will never need it.
        	  System.err.println("Fehler in mySQLconnector.badgeListReturn");
            e.printStackTrace();
			return null;
		} // Nope he don't exist
		} else { cs.sendMessage("Dieser Spieler existiert nicht!"); }
	}
	
	/**
	 * You can use this method if you search for all Player who owns a specific badge but you have only the badgeName
	 * @param BadgeName
	 * @return
	 */
	public ArrayList<BadgePlayer> searchPlayersFromBadgeName (String badgeName) {
		if (this.checkBadgeName(badgeName)) {
		//ToDo: Test and Explain SQL Statement
		ResultSet result = myConnector.getQuerry("SELECT p.* FROM player p WHERE p.uuid IN (SELECT t.uuid FROM transaction t WHERE t.badgeid IN (SELECT b.bid FROM badge b WHERE b.bname = '"+ badgeName +"' ) )");
		ArrayList<BadgePlayer> returnList = new ArrayList<BadgePlayer>();
		try {
			//Start to get all results
			while (result.next()) {
				// Fetch Data from the current Row
				String uuid = result.getNString("uuid");
				String pname = result.getString("pname");
				BadgePlayer temp = new BadgePlayer(uuid, pname);
				returnList.add(temp);
			}
			return returnList;
	    } catch(SQLException e) {
		System.err.println("Fehler in mySQLconnector.badgeListPlayer");
        e.printStackTrace();
		return null;
	    } //Nope the Badge don't exist
		} else { cs.sendMessage("Es existiert kein Badge mit diesem Namen"); }
	}
	
	
	
	
	// Some helper methods
	
	private String[] loreConverter(String blore) {
		// Get the blore into a StringArray Type
		  // First we need to count the Number of Rows
		  int bloreRowCounter = 0;
		  for (int i = 0; i < blore.length();i++) {
			  if (String.valueOf(blore.charAt(i)) + String.valueOf(blore.charAt(i+1)) == "/n" ) {
				  bloreRowCounter++;
			  }
		  }
		  // After that we can Create a String Array
		  String[] bloreArray = new String[bloreRowCounter];
		  // Now we fill the Array with Lines
		  int bloreExtraCounter = 0;
		  int stringPointer = 0;
		  for (int i = 0; i < blore.length();i++) {
			  if (String.valueOf(blore.charAt(i)) + String.valueOf(blore.charAt(i+1)) == "/n" ) {
				  if (bloreExtraCounter <= bloreRowCounter) {
					  bloreArray[bloreExtraCounter] = blore.substring(stringPointer, i-1);
					  stringPointer = i + 2;
					  bloreExtraCounter ++;
				  }
			  }
		  }
		  //Ta Da - We are done!
		  return bloreArray;
	}
	
}
