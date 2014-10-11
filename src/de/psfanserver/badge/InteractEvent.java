package de.psfanserver.badge;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class InteractEvent implements Listener{
	
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e){
		if(!(e.getRightClicked() instanceof Player)){
			return;
		}
		if(Main.plugin.getConfig().getBoolean("RightClickEnabled")){
			Player p = e.getPlayer();
			Player o = (Player) e.getRightClicked();
			
			//Zeige Badges an
		}
	}

}
