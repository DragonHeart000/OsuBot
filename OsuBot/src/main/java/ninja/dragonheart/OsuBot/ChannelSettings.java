package ninja.dragonheart.OsuBot;

import java.io.Serializable;

public class ChannelSettings implements Serializable{
	
	private static final long serialVersionUID = 913424558077898062L;
	String server;
	boolean serverSimple;
	String name;
	String oauth;
	String channel;
	
	
	public ChannelSettings(boolean type, String name, String oauth, String channel){
		//type true=twitch, type false=osu
		if (type){
			server="irc.twitch.tv"; //Twitch irc server
		} else {
			server="irc.ppy.sh"; //Osu! irc server
		}
		
		this.serverSimple=type;
		this.name=name;
		this.oauth=oauth;
		this.channel=channel;
	}
	
	
	public String getServer(){
		return server;
	}
	
	public boolean getServerSimple(){
		return serverSimple;
	}
	
	public String getName(){
		return name;
	}
	
	public String getOauth(){
		return oauth;
	}
	
	public String getChannel(){
		return channel;
	}
}
