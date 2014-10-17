package de.psfanserver.badge;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A nacked class to connect to an mySQL-Database
 * @author Felix
 *
 */
public class MySQL {
	public Connection con;
	
	/**
	 * Main function, implements the connection to an database-server. Should run frequently
	 * @param host
	 * probably localhost
	 * @param port
	 * probably 3306
	 * @param database
	 * e.g. minecraft
	 * @param user
	 * e.g. minecraftuser
	 * @param password
	 * 1. Give password
	 * 2. ?????
	 * 3. Profit!
	 * @param createTables
	 * Is an Array wich contain the information about the tables we want to create
	 */
	public void connect(String host, String port, String database, String user, String password, String[] createTables) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(ClassNotFoundException e) {
            System.err.println("Konnte den MySQL-Driver nicht finden!");
            return;
        }
        
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + user + "&password=" + password + "&autoReconnect=true");
            if(!(con.isClosed())) {
                System.out.println("Erfolgreich mit MySQL verbunden!");
                createTables(createTables);
                return;
            }
        } catch(SQLException e) {
            System.err.println("Konnte nicht mit MySQL verbinden! Error: " + e.getMessage());
            return;
        }
    }
	
	/**
	 * Why would someone do something like this?
	 */
	public void close() {
	    try {
	        if(con != null && (!(con.isClosed()))) {
	            con.close();
	            if(con.isClosed()) {
	                System.out.println("MySQL-Verbindung geschlossen.");
	            }
	        }
	    } catch(SQLException e) {
	        System.err.println("Fehler beim Schliessen der MySQL-Verbindung.");
	    }
	}
	
	
	/**
	 * This function create new Tables if they dont already exist
	 * =============================
	 * con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `player` " +
			"(uuid INT NOT NULL PRIMARY KEY, " +
			"pname VARCHAR(20)" +
			")");
    		con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `transaction` " +
			"(transid INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
			"puuid INT REFERENCES player (uuid),"+ 
			"badgeid INT REFERENCES badge (bid)," +
			"transtime TIMESTAMP" +
			")");
    		con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `badge` " +
    				"(bid INT NOT NULL PRIMARY KEY, " +
    				"blore TEXT (255)" +
    				"bblockid INT"+
    				"bname VARCHAR (255)"+
    				"benchant VARCHAR (255)"+
    				")");
    				===============================
	 * @param createTables contain information about the tables
	 * @return
	 */
	private boolean createTables(String[] createTables) {
    	try {
    		for (int i = 0; i <= createTables.length;i++) {
    			con.createStatement().executeUpdate(createTables[i]);
    		}
		return true;
   		
	} catch (SQLException e) {
		System.err.println("Fehler beim Erstellen der Tabelle!");
		e.printStackTrace();
		return false;
	}
		
    }
	
	
	/**
	 * Update an tuple
	 * @param sql
	 * contain the command
	 */
    public void doUpdate(String sql) {
    	try {
    		con.createStatement().executeUpdate(sql);
    	}catch (SQLException e) {
    		System.err.println("Konnte das Update ("+sql+") nicht ausführen!");
    	}
    }

	
}
