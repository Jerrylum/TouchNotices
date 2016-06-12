package io.Jerry.TouchNotices.Util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import io.Jerry.TouchNotices.Main;
import io.Jerry.TouchNotices.api.Notice;

public class NoticeUtil {
	private static FileConfiguration NoticeConfig;
	private static List<Notice> NoticeList;
	private static int Number = 0;
	private static int Task = -1;
	
	public static void run(FileConfiguration c){
		Number = 0;
		NoticeConfig = c;
		NoticeList = new ArrayList<Notice>();
		for(String str : NoticeConfig.getStringList("Notices")){
			NoticeList.add(new Notice(str));
		}
		
		if(Task != -1){
			Bukkit.getScheduler().cancelTask(Task);
		}
		Task = new BukkitRunnable(){
			@Override
			public void run() {
				if(NoticeList.isEmpty()){
					Number = 0;
					return;
				}
				if(NoticeList.size() <= Number){
					Number = 0;
				}
				
				List<Player> list = Util.getOnlinePlayers();
				Util.sendAction(NoticeList.get(Number).toString(), list);
				Number ++;
			}
		}.runTaskTimer(Main.Plugin(), 0, 20 * NoticeConfig.getInt("Delay",1)).getTaskId();
	}
	
	public static List<Notice> getNotices(){
		return NoticeList;
	}
	
	public static void addNotice(String str){
		NoticeList.add(new Notice(str.replace('&', '¡±')));
		List<String> list = new ArrayList<String>();
		for(Notice n : NoticeList){
			list.add(n.getRawMessage());
		}
		NoticeConfig.set("Notices", list);
		saveConfig();
	}
	
	public static void delNotice(Notice notice){
		NoticeList.remove(notice);
		List<String> list = new ArrayList<String>();
		for(Notice n : NoticeList){
			list.add(n.getRawMessage());
		}
		NoticeConfig.set("Notices", list);
		saveConfig();
	}
	
	public static void setDelay(int i){
		NoticeConfig.set("Delay", i);
		saveConfig();
		run(NoticeConfig);
	}
	
	public static void saveConfig(){
		Main.Plugin().saveConfig();
	}
}
