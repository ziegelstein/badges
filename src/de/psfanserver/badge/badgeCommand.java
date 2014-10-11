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
		
		//add (/badge add <badgeID> <spielername/uuid>
		if(args[0].equalsIgnoreCase("add")){
			
			if(args.length == 3){
				//badge hinzufügen
			}
			else{
				cs.sendMessage("/badge add <badgeID> <PlayerName/UUID>");
			}
		}
		
		
		//remove (/badge remove <badgeID> <spilername/uuid>
		if(args[0].equalsIgnoreCase("remove")){
			
			if(args.length == 3){
				//badge entfernen
			}
			else{
				cs.sendMessage("/badge remove <badgeID> <PlayerName/UUID>");
			}
		}
		
		
		return false;
	}

}
