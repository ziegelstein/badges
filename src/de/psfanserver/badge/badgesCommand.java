package de.psfanserver.badge;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class badgesCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(cs instanceof BadgePlayer == false){
			cs.sendMessage("Du bist kein Spieler");
			return true;
		}
		
		BadgePlayer p = (BadgePlayer)cs;
		
		if(args.length == 0){
			//Zeige eigene Badges
		}
		
		if(args.length == 1){
			String playername = args[0];
			
			if(Bukkit.getOnlinePlayers().contains(playername)){
				
				//Zeige badges von <playername>
			}
			else{
				p.sendMessage("Dieser Spieler ist nicht online.");
				return true;
			}
		}
		
		//wenn keine option passt, Zeige Command usage
		return false;
	}

}
