package io.Jerry.TouchNotices;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.Jerry.TouchNotices.Util.I18n;
import io.Jerry.TouchNotices.Util.NoticeUtil;
import io.Jerry.TouchNotices.Util.Util;
import io.Jerry.TouchNotices.api.Notice;
import io.Jerry.TouchNotices.api.NoticeApi;

public class NoticesCMD implements CommandExecutor {
	public static String[] helplist = {
			"/Notice ¡±7list","/Notice list",I18n.t("Notices.getlist"),
			"/Notice ¡±7add {" + I18n.t("Notices.text") + "}",
				"/Notice add {" + I18n.t("Notices.text") + "}",
				I18n.t("Notices.add"),
			"/Notice ¡±7remove <" + I18n.t("Notices.index") + ">",
				"/Notice remove <" + I18n.t("Notices.index") + ">",
				I18n.t("Notices.remove"),
			"/Notice ¡±7delay <" + I18n.t("Notices.sec") + ">",
				"/Notice delay <" + I18n.t("Notices.sec") + ">",
				I18n.t("Notices.delay"),
			"/Notice ¡±7broadcast <" + I18n.t("Notices.index") + ">",
				"/Notice broadcast <" + I18n.t("Notices.index") + ">",
				I18n.t("Notices.broadcast"),
			"/Notice ¡±7say <" + I18n.t("Notices.text") + ">",
				"/Notice say <" + I18n.t("Notices.text") + ">",
				I18n.t("Notices.say"),
			"/Notice ¡±7reload","/Notice reload",I18n.t("Notices.reload")};
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if((sender.isOp() && sender instanceof Player) == false){
			return false;
		}
		
		Player p = (Player)sender;
		
		if(args.length >=1 && args.length <= 2 && args[0].equalsIgnoreCase("list")){
			List<Notice> list = NoticeUtil.getNotices();
			if(list.isEmpty()){
				p.sendMessage("¡±3Notice> ¡±f" + I18n.t("CMD.nolist"));
				return true;
			}
			
			int k = 1;
			if(args.length == 2){
				try{
					k = Integer.parseInt(args[1]);
					if( (k-1) * 10 >= list.size()){
						p.sendMessage("¡±3Notice> ¡±f" + I18n.t("CMD.nopages") + k);
						return true;
					}
				}catch(Exception ex){
					p.sendMessage("¡±3Notice> ¡±f" + I18n.t("CMD.wrong"));
					return true;
				}
			}
			
			int mix = (k-1)*10;
			int max = k*10;
			String str;
			
			p.sendMessage("¡±3Notice> ¡±f" + I18n.t("Notices.list"));
			for(int i = mix; i < max && i < list.size(); i ++){
				str = list.get(i).getRawMessage();
				Util.sendAction("{\"text\":\"" + (i+1) + ". " + str + "\","
						+ "\"clickEvent\":"
						+ "{\"action\":\"suggest_command\",\"value\":\"/Notice remove " + i +1 + "\"},"
						+ "\"hoverEvent\":"
						+ "{\"action\":\"show_text\",\"value\":\"" + I18n.t("Notices.delete") + "\"}}",Arrays.asList(p));
			}
			p.sendMessage("¡±7²Ä" + k + "/" + ( ((int)(list.size() / 10)) + 1) + "­¶");
			return true;
		}
		
		if(args.length == 2){
			if(args[0].equalsIgnoreCase("remove")){
				int id;
				try{
					id = Integer.parseInt(args[1]);
				}catch(Exception ex){
					p.sendMessage("¡±3Notice> ¡±f" + I18n.t("CMD.wrong"));
					return true;
				}
				
				List<Notice> list = NoticeUtil.getNotices();
				if(list.size() < id || id <= 0){
					p.sendMessage("¡±3Notice> ¡±f" + I18n.t("CMD.noindex") + id);
					return true;
				}
				
				NoticeUtil.delNotice(list.get(id-1));
				p.sendMessage("¡±3Notice> ¡±f" + I18n.t("CMD.delete"));
				return true;
			}else if(args[0].equalsIgnoreCase("delay")){
				int id;
				try{
					id = Integer.parseInt(args[1]);
				}catch(Exception ex){
					p.sendMessage("¡±3Notice> ¡±f" + I18n.t("CMD.wrong"));
					return true;
				}
				
				NoticeUtil.setDelay(id);
				p.sendMessage("¡±3Notice> ¡±f" + I18n.t("CMD.set"));
				return true;
			}else if(args[0].equalsIgnoreCase("broadcast")){
				int id;
				try{
					id = Integer.parseInt(args[1]);
				}catch(Exception ex){
					p.sendMessage("¡±3Notice> ¡±f" + I18n.t("CMD.wrong"));
					return true;
				}
				
				List<Notice> list = NoticeUtil.getNotices();
				if(list.size() < id || id <= 0){
					p.sendMessage("¡±3Notice> ¡±f" + I18n.t("CMD.noindex") + id);
					return true;
				}

				list.get(id-1).send(Util.getOnlinePlayers());
				return true;
			}
		}
		
		if(args.length > 1){
			String str = "";
			for(int i = 1; i < args.length; i ++){
				str = str + (str.length() > 0 ? " " : "") + args[i];
			}
			
			if(args[0].equalsIgnoreCase("add")){
				try{
					if(NoticeApi.decode(str.replace('&', '¡±')).equals("")){
						throw new Exception();
					}
				}catch(Exception ex){
					p.sendMessage("¡±3Notice> ¡±f" + I18n.t("CMD.wrong"));
					return true;
				}
				
				NoticeUtil.addNotice(str);
				p.sendMessage("¡±3Notice> ¡±f" + I18n.t("CMD.add"));
				return true;
			}else if(args[0].equalsIgnoreCase("say")){
				try{
					if(NoticeApi.decode(str.replace('&', '¡±')).equals("")){
						throw new Exception();
					}
				}catch(Exception ex){
					p.sendMessage("¡±3Notice> ¡±f" + I18n.t("CMD.wrong"));
					return true;
				}
				
				new Notice(str.replace('&', '¡±')).send(Util.getOnlinePlayers());
				return true;
			}
		}
		
		if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
			NoticeApi.unloadAll();
			Main.Plugin().reloadConfig();
			NoticeUtil.run(Main.Config());
			p.sendMessage("¡±3Notice> ¡±f" + I18n.t("CMD.reload"));
			return true;
		}
		
		p.sendMessage("¡±3Notice> ¡±fHelp");
		for(int i = 0; i + 2 < helplist.length; i = i +3){
			Util.sendAction("{\"text\":\"" + helplist[i] + "\","
					+ "\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + helplist[i+1] + "\"},"
					+ "\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""
						+ helplist[i+2]
						+ "\n"+ I18n.t("Notices.click")
					+ "\"}}", Arrays.asList(p));
		}
		return true;
	}

}
