package io.Jerry.TouchNotices.api;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.plugin.Plugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import io.Jerry.TouchNotices.Util.Util;

public class NoticeApi {
	private static HashMap<Plugin,List<Notice>> list = new HashMap<Plugin,List<Notice>>();
	
	public static Notice createNotice(String str, Plugin p){
		Notice N = new Notice(str);
		List<Notice> local_list = list.get(p);
		if(local_list == null){
			local_list = new ArrayList<Notice>();
		}
		local_list.add(N);
		list.put(p, local_list);
		
		return N;
	}
	
	public static List<Notice> getNotices(Plugin p){
		return list.get(p);
	}
	
	public static HashMap<Plugin,List<Notice>> getNotices(){
		return list;
	}
	
	public static void unloadAll(){
		list = new HashMap<Plugin,List<Notice>>();
	}
	
	public static void unloadAll(Plugin p){
		list.put(p, null);
	}
	
	public static String decode(String xml) throws Exception{
		return decode("<xml>" + xml + "</xml>",null);
	}
	
	public static String decode(String xml,Notice n) throws Exception{
		if(xml == null) throw new NullPointerException("String could not be null");
		String Return = "";
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse( new InputSource( new StringReader( xml ) ));
		document.getDocumentElement().normalize();
		
		NodeList nodeList = document.getElementsByTagName("p");
		if(nodeList == null){
			throw new IllegalArgumentException("<p> Tag could not be found");
		}
		
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				 Element elem = (Element) node;

				 if(i == 1){
					Return = Return + ",extra:[";
				 }else if(i > 1){
					 Return = Return + "},";
				 }
				 Return = Return + "{\"text\":\"" + elem.getTextContent().replace("\"", "\\\"") + "\"";
				 
				 NamedNodeMap attr = elem.getAttributes();
				 String attrReturn = "";
				 for(int j = 0; j < attr.getLength() ; j ++){
					 Node node2 = attr.item(j);
					 if(node2.getNodeType() == Node.ATTRIBUTE_NODE){
						 attrReturn = attrReturn + "," + getName(
								node2.getNodeName().replace("\"", "\\\""),
								node2.getTextContent().replace("\"", "\\\""),
								n
							);
					 }
					 
				 }
				 
				 if(Util.getCount(attrReturn, "hoverEvent") > 1|| Util.getCount(attrReturn, "clickEvent") > 1){
					 throw new IllegalArgumentException("Could not have same event listener");
				 }
				 Return = Return + attrReturn;
				 

			}
		}
		
		if(nodeList.getLength() > 1){
			Return = Return + "]";
		}
		return Return + "}";
	}
	
	private static String getName(String name, String msg,Notice n){
		String Return = null;
		if(name.equalsIgnoreCase("suggest_command") || name.equalsIgnoreCase("suggestcommand") || name.equalsIgnoreCase("sc")){
			Return = "\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + msg + "\"}";
		}else if(name.equalsIgnoreCase("run_command") || name.equalsIgnoreCase("runcommand") || name.equalsIgnoreCase("rc")){
			Return = "\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + msg + "\"}";
		}else if(name.equalsIgnoreCase("open_url") || name.equalsIgnoreCase("openurl") || name.equalsIgnoreCase("ou")){
			Return = "\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + msg + "\"}";
		}else if(name.equalsIgnoreCase("change_page") || name.equalsIgnoreCase("changepage") || name.equalsIgnoreCase("cp")){
			Return = "\"clickEvent\":{\"action\":\"change_page\",\"value\":\"" + msg + "\"}";
		}else if(name.equalsIgnoreCase("show_text") || name.equalsIgnoreCase("showtext") || name.equalsIgnoreCase("st")){
			Return = "\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + msg + "\"}";
		}else if(name.equalsIgnoreCase("show_achievement") || name.equalsIgnoreCase("showachievement") || name.equalsIgnoreCase("sa")){
			Return = "\"hoverEvent\":{\"action\":\"show_achievement\",\"value\":\"" + msg + "\"}";
		}else if(name.equalsIgnoreCase("show_Item") || name.equalsIgnoreCase("showItem") || name.equalsIgnoreCase("si")){
			Return = "\"hoverEvent\":{\"action\":\"show_Item\",\"value\":\"" + msg + "\"}";
		}else if(name.equalsIgnoreCase("show_entity") || name.equalsIgnoreCase("showEntity") || name.equalsIgnoreCase("se")){
			Return = "\"hoverEvent\":{\"action\":\"show_entity\",\"value\":\"" + msg + "\"}";
		}
		
		
		if(Return == null){
			throw new IllegalArgumentException("Unknow keyword \"" + name + "\"");
		}
		return Return;
	}
	
}
