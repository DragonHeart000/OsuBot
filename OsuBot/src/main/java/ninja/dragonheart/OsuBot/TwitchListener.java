package ninja.dragonheart.OsuBot;


import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

public class TwitchListener extends ListenerAdapter{

	public Queue<String> songRequests = new LinkedList<String>();
	public ArrayList<String> admins = new ArrayList<String>();
	public ArrayList<String> banned = new ArrayList<String>();
    private String message=null;
    private String nick=null;
	
	@Override
    public void onGenericMessage(GenericMessageEvent event) {
		
		//update the admin list and ban list
		admins=osuBotMain.getAdmins();
		banned=osuBotMain.getBanned();
		//set event.getMessage() and event.getUser().getNick(); to a local vars for efficiency.
        message=event.getMessage();
        nick=event.getUser().getNick();
		
            if (message.equals("test")){ //Allow users to make sure that the bot has been loaded.
                    event.respondWith("Osu!Bot 3.1.0b is loaded and ready!");
            } 
            
            
            //Accepting Song Requests
            
            //For /s/
            else if (message.length()>=25 && message.substring(0,3).equalsIgnoreCase("!sr") && message.substring(4,25).equalsIgnoreCase("https://osu.ppy.sh/s/") && banned.contains(nick)) { //Check if user is banned before processing the request
        		event.respondWith("@" + nick + " I'm sorry but you have been banned from submiting song requests by an admin.");
        	} else if (message.length()>=25 && message.substring(0,3).equalsIgnoreCase("!sr") && message.substring(4,25).equalsIgnoreCase("https://osu.ppy.sh/s/")){
        		songRequests.add(message.substring(3));
        		event.respondWith("@" + nick + " Your request has been received!");
        		//!sr https://osu.ppy.sh/s/
        	} else if (message.length()>=4 && message.substring(0,3).equalsIgnoreCase("!sr")){
        		event.respondWith("@" + nick + " I'm sorry, that is not a valid use of the !sr system. Please make sure your request looks like the following: !sr https://osu.ppy.sh/s/******");
        	}
        	
        	//alt song request command
            else if (message.length()>=34 && message.substring(0,12).equalsIgnoreCase("!songrequest") && message.substring(13,34).equals("https://osu.ppy.sh/s/") && banned.contains(nick)) { //Check if user is banned before processing the request
        		event.respondWith("@" + nick + " I'm sorry but you have been banned from submiting song requests by an admin.");
        	} else if (message.length()>=34 && message.substring(0,12).equalsIgnoreCase("!songrequest") && message.substring(13,34).equals("https://osu.ppy.sh/s/")){
        		songRequests.add(message.substring(12));
        		event.respondWith("@" + nick + " Your request has been received!");
        		//!sr https://osu.ppy.sh/s/
        	} else if (message.length()>=12 && message.substring(0,12).equalsIgnoreCase("!songrequest")){
        		event.respondWith("@" + nick + " I'm sorry, that is not a valid use of the !songrequest system. Please make sure your request looks like the following: !sr https://osu.ppy.sh/s/******");
        	}
            
            
          //For /b/
            else if (message.length()>=25 && message.substring(0,3).equalsIgnoreCase("!sr") && message.substring(4,25).equalsIgnoreCase("https://osu.ppy.sh/b/") && banned.contains(nick)) { //Check if user is banned before processing the request
        		event.respondWith("@" + nick + " I'm sorry but you have been banned from submiting song requests by an admin.");
        	} else if (message.length()>=25 && message.substring(0,3).equalsIgnoreCase("!sr") && message.substring(4,25).equalsIgnoreCase("https://osu.ppy.sh/b/")){
        		songRequests.add(message.substring(3));
        		event.respondWith("@" + nick + " Your request has been received!");
        		//!sr https://osu.ppy.sh/b/
        	} else if (message.length()>=4 && message.substring(0,3).equalsIgnoreCase("!sr")){
        		event.respondWith("@" + nick + " I'm sorry, that is not a valid use of the !sr system. Please make sure your request looks like the following: !sr https://osu.ppy.sh/b/******");
        	}
        	
        	//alt song request command with /b/
            else if (message.length()>=34 && message.substring(0,12).equalsIgnoreCase("!songrequest") && message.substring(13,34).equals("https://osu.ppy.sh/b/") && banned.contains(nick)) { //Check if user is banned before processing the request
        		event.respondWith("@" + nick + " I'm sorry but you have been banned from submiting song requests by an admin.");
        	} else if (message.length()>=34 && message.substring(0,12).equalsIgnoreCase("!songrequest") && message.substring(13,34).equals("https://osu.ppy.sh/b/")){
        		songRequests.add(message.substring(12));
        		event.respondWith("@" + nick + " Your request has been received!");
        		//!sr https://osu.ppy.sh/b/
        	} else if (message.length()>=12 && message.substring(0,12).equalsIgnoreCase("!songrequest")){
        		event.respondWith("@" + nick + " I'm sorry, that is not a valid use of the !songrequest system. Please make sure your request looks like the following: !sr https://osu.ppy.sh/b/******");
        	}
            
            
            
            //List admins
        	else if (message.equalsIgnoreCase("!osuAdmins")){
        		String toRespondWith="@" + nick + " Here is a list of all my admins: ";
        		if (!(admins.size()==0)){
        			for (int i=0; i<admins.size(); i++){
        				if (i==0){
        					toRespondWith=toRespondWith+admins.get(i);
        				} else {
        					toRespondWith=toRespondWith+", "+admins.get(i);
        				}
        			}
        		} else {
        			toRespondWith="@" + nick + " Looks like I was not setup correctly as there are no admins... This could be a mess.";
        		}
        		event.respondWith(toRespondWith);
        	}

             //List banned
        	else if (message.equalsIgnoreCase("!osuBanned") || message.equalsIgnoreCase("!osuBanList")){
        		String toRespondWith="@" + nick + " Here is a list of all banned users: ";
        		if (!(banned.size()==0)){
        			for (int i=0; i<banned.size(); i++){
        				if (i==0){
        					toRespondWith=toRespondWith+banned.get(i);
        				} else {
        					toRespondWith=toRespondWith+", "+banned.get(i);
        				}
        			}

        		} else {
        			toRespondWith="@" + nick + " There are no banned users, I guess you have all been good little kids.";
        		}
        		event.respondWith(toRespondWith);
        	}
            
        

            //Help Command
            else if (message.length()==5 && message.equalsIgnoreCase("!help")){
                event.respondWith("@"+nick+" For a list of commands that work with the most recent version of Osu!Bot check out this link: https://github.com/DragonHeart000/OsuBot/wiki");  //Set this site up later
                //here is a list of commands you can use that I work with: !songRequest [INSERT LINK] (Request a song), !sr [INSERT LINK] (Request a song), !help (You kinda already know this one), !osuAdmins
            }

            //ADMIN TOOLS

            //For next Song Command
        	else if (message.length()==5 && message.equalsIgnoreCase("!next") && admins.contains(nick)){
        		try {
        			event.respondWith("Next song is: "+songRequests.remove());
        		} catch (java.util.NoSuchElementException nu){
        			event.respondWith("Sorry but there are no songs queued right now! Come on guys lets get some requests!");
        		}
        	} else if (message.length()==5 && message.equalsIgnoreCase("!next")){
        		event.respondWith("@"+nick+" Sorry, you do not have permissions to use this command.");
        	}
            //Ban a user

            else if (message.length()>5 && message.substring(0, 4).equalsIgnoreCase("!ban") && admins.contains(nick)){
            	ArrayList<String> toBan = new ArrayList<String>();
                toBan.add(message.substring(5, message.length()).toLowerCase());
                osuBotMain.ban(toBan);
                event.respondWith(message.substring(5, message.length()).toLowerCase() + " has been banned! Screw that guy!");
            } else if (message.length()>5 && message.substring(0, 4).equalsIgnoreCase("!ban")){
            	event.respondWith("@" + nick + " Sorry but you don't have acess to that command, I can ban you for spam though if you'd like.");
            }
            
            //Unban a user
            else if (message.length()>7 && message.substring(0, 6).equalsIgnoreCase("!unban") && admins.contains(nick)){
                osuBotMain.unban(message.substring(7, message.length()).toLowerCase());
                event.respondWith(message.substring(7, message.length()).toLowerCase() + " has been brought back from oblivion, let's hope they don't go back!");
            } else if (message.length()>7 && message.substring(0, 6).equalsIgnoreCase("!unban")){
            	event.respondWith("@" + nick + " Sorry but you don't have acess to that command, but we can try starting a riot for the unbanning of .");
            }
            
    }
	
}
