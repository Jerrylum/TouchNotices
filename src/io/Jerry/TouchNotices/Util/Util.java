package io.Jerry.TouchNotices.Util;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

public class Util {
	public static String version = Bukkit.getServer().getClass().getPackage().getName().substring(23);
	
	public static int getCount(String a, String b){
		int i = 0;
		String str = new String(a);
		while(str.contains(b)){
			str = str.replaceFirst(b, "");
			i++;
		}
		
		return i;
	}
	
	public static List<Player> getOnlinePlayers() {
	    List<Player> list = Lists.newArrayList();
	    for (World world : Bukkit.getWorlds()) {
	        list.addAll(world.getPlayers());
	    }
	    return list;
	}
	
	private static String getServerVersion(){
		return version;
	}
	
	private static Class<?> getNmsClass(String nmsClassName) throws ClassNotFoundException{
		if ((nmsClassName.equals("ChatSerializer")) && !getServerVersion().equals("v1_8_R1") && !getServerVersion().contains("1_7")) {
			nmsClassName = "IChatBaseComponent$ChatSerializer";
		}
		return Class.forName("net.minecraft.server." + getServerVersion() + "." + nmsClassName);
	}
	
	private static void sendPacket(Player player, Object packet){
	    try{
	    	Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
	    	Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
	    	playerConnection.getClass().getMethod("sendPacket", new Class[] { getNmsClass("Packet") }).invoke(playerConnection, new Object[] { packet });
	    }catch (Exception e){
	    	Bukkit.broadcastMessage("§3Bug> §f傳送封包時發生錯誤 " + e.getMessage());
	    	e.printStackTrace();
	    }
	}
	
	public static void sendAction(String msg,List<Player> player){
	    try{
	    	for(Player p : player){
	    		Object icbc = getNmsClass("ChatSerializer").getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { msg.replace("{player}", p.getName()) });
				Object ppoc = getNmsClass("PacketPlayOutChat").getConstructor(new Class[] { getNmsClass("IChatBaseComponent"), Byte.TYPE }).newInstance(new Object[] { icbc, Byte.valueOf("1") });      
	    		
        		sendPacket(p,ppoc);	
	    	}
    		
	   }catch (Exception e){
	    	Bukkit.broadcastMessage("§3Bug> §f封包發生錯誤 " + e.getMessage());
	    	e.printStackTrace();
	    }
	}
}
