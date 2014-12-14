package de.psfanserver.badge;

public class BadgePlayer {

	String uuid;
    String name;
    
    BadgePlayer (String localuuid, String localname) {
    	this.uuid = localuuid;
    	this.name = localname;
    }
    
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
