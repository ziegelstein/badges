package de.psfanserver.badge;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class badgeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(args.length == 0){
			return false;
		}
		
		try{
			int id = Integer.parseInt(args[0]);
		} catch(NumberFormatException nfe){
			cs.sendMessage("Keine gültige ID, es sind nur Ziffern erlaubt.");
			return true;
		}
		
		if(args.length == 1){
			
			//Zeige informationen Über ein Badge (Name, Item, Lore, Enchantment)
			
			return true;
		}
		
		if(args[1].equalsIgnoreCase("create")){
			//erstellt Badge mit der ID "id"
		} else if(args[1].equalsIgnoreCase("name")){
			//zeigt oder setzt den Namen für das Badge
			
		} else if(args[1].equalsIgnoreCase("item")){
			//zeigt oder setzt das Item für das Badge
			
		} else if(args[1].equalsIgnoreCase("lore")){
			//zeigt oder setzt die Lores für das Badge
			
		} else if(args[1].equalsIgnoreCase("enchantments")){
			//zeigt oder setzt die enchantments für das Badge
			
		} else if(args[1].equalsIgnoreCase("users")){
			//zeigt alle Spieler die Badge "id" haben
			
		} else if(args[1].equalsIgnoreCase("user")){
			//Zeigt an ob ein Spieler das Badge hat; bzw. setzt oder entfernt ein Badge von einem Spieler
			// badge <id> user add/remove <name/uuid/"all">
		} else{
			cs.sendMessage("Argument "+args[1]+" nicht gefunden.");
			return true;
		}
		return false;
	}

}
