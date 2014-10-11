package de.psfanserver.badge;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	
	public static Main plugin;

	public void onEnable(){
		plugin = this;
		
		
		this.getConfig().addDefault("RightClickEnabled", true);
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		//User Command
		this.getCommand("badges").setExecutor(new badgesCommand());
		
		//Console Command
		this.getCommand("badge").setExecutor(new badgeCommand());
		
		this.getServer().getPluginManager().registerEvents(new InteractEvent(), this);
	}
	
	public void onDisable(){
		
	}
}
