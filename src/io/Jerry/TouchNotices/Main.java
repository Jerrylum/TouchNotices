package io.Jerry.TouchNotices;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import io.Jerry.TouchNotices.Util.I18n;
import io.Jerry.TouchNotices.Util.NoticeUtil;

public class Main extends JavaPlugin{
	private static Main plugin;
	private static FileConfiguration MainConfig;
	
	public void onEnable(){
		plugin = this;
		
		MainConfig = getConfig();
		MainConfig.options().copyDefaults(true);
		saveConfig();
		NoticeUtil.run(MainConfig);
		I18n.run();
		getCommand("Notice").setExecutor(new NoticesCMD());

		getLogger().info("enable");
	}

	public void onDisable(){
		getLogger().info("disalbed");
	}
	
	public static Main Plugin(){
		return plugin;
	}
	
	public static FileConfiguration Config(){
		return MainConfig;
	}
}
