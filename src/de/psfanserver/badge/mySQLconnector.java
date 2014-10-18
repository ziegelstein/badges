/**
 * 
 */
package de.psfanserver.badge;


import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
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
	 * 
	 * @param uuid
	 * @param name
	 */
	public void createplayer (String uuid, String name) {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		int intuuid = Integer.parseInt(uuid);
		myConnector.doUpdate("INSERT INTO player VALUES ("+ intuuid +", "+name+", "+ dateFormat.format(date));
	}
	
	/**
	 * 
	 * @param bid
	 * @param Name
	 * @param bitemid
	 */
	public void createbadge (int bid, String bname, int bitemid, String enchant, String[] lore) {
		String fullLore = "";
		for (int i = 0;i < lore.length;i++) {
			if (i == 0) {
				fullLore = lore[i];
			}
			else {
				fullLore = fullLore + "/n" + lore[i];
			}
		}
		myConnector.doUpdate("INSERT INTO player VALUES ("+ bid +", "+bname+", "+bitemid+", "+enchant+", "+fullLore);
	}
	
	/**
	 * 
	 * @param uuid
	 * @param bid
	 */
  public void createTransaction (int uuid, int bid) {
	  myConnector.doUpdate("INSERT INTO transaction VALUES ("+ (uuid+bid) + ", " + uuid + ", "+ bid);
  }
	
	// Querryfunctions
	
	/**
	 * 
	 * @param currentPlayer
	 * @return
	 */
	public String[] badgeListReturn (BadgePlayer currentPlayer) {
		return null;
	}
	
	public String badgeSingleReturn (Badge currentBadge) {
		return null;
	}
	
	public String badgeCount (BadgePlayer currentPlayer) {
		return null;
	}
	
	public BadgePlayer badgeListPlayer (Badge urrentBadge) {
		return null;
	}
	
	public Badge badgeReturnLatest () {
		return null;
	}
	
	public BadgePlayer badgeReturnLatestPlayer () {
		return null;
	}
	
}
