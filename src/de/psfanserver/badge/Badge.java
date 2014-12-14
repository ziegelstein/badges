package de.psfanserver.badge;

public class Badge {

	int badgeId;
	String badgeName;
	int itemId;
	String enchantment;
	String[] lore;
	
	/**
	 * Full BadgeConstructor with badgeID
	 * @param localbadgeId: is used to identify the Badge
	 * @param localbadgeName: It's for the kis
	 * @param localitemID: Represent the BadgeImage
	 * @param localenchantment: Represent the Enchantment of the BadgeImage
	 * @param locallore: Is the description of the Badge
	 */
	public Badge(int localbadgeId,String localbadgeName, int localitemID,String localenchantment, String[] locallore ) {
		this.badgeId = localbadgeId;
	    this.itemId = localitemID;
	    this.badgeName = localbadgeName;
	    this.enchantment = localenchantment;
	    if (locallore != null) {
	    	this.lore = locallore;
	    }
	    else { this.lore[0] = ""; }
	}
	
	public int getBadgeId() {
		return badgeId;
	}
	public void setBadgeId(int badgeId) {
		this.badgeId = badgeId;
	}
	public String getBadgeName() {
		return badgeName;
	}
	public void setBadgeName(String badgeName) {
		this.badgeName = badgeName;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
	
	
}
