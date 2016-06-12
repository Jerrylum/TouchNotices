package io.Jerry.TouchNotices.api;

import java.util.List;

import org.bukkit.entity.Player;

import io.Jerry.TouchNotices.Util.Util;

public class Notice {
	private String msg;
	private long code;
	
	public Notice(String str) {
		msg = str;
	}
	
	public String getRawMessage(){
		return msg;
	}
	
	public void setRawMessage(String str){
		msg = str;
	}
	
	public long getCode(){
		return code;
	}
	
	public String toString(){
		try {
			return NoticeApi.decode("<xml>" + msg + "</xml>");
		} catch (Exception e) {
			return "Failed to decode: " + e.getMessage();
		}
	}

	public void send(List<Player> p){
		Util.sendAction(toString(), p);
	}
}
