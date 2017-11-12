package ninja.dragonheart.OsuBot;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * The purpose of this class is to make it easy to save the list
 * of admins and banned users. There is no need for this code to
 * be in it's own class functionally, however, by creating a new
 * object to store these two lists in rather than just putting it
 * somewhere else I can easily serialize the object making it a lot
 * easier to save and load it all.
 */

public class Lists implements Serializable{
	
	private static final long serialVersionUID = -7031371336406444419L;
	private ArrayList<String> admins = new ArrayList<String>();
	private ArrayList<String> banned = new ArrayList<String>();
	
	////////////////////////////////Constructors////////////////////////////////
	
	//Constructor for new
	public Lists(){
		
	}
	
	//Constructor for loading
	public Lists(ArrayList<String> admins, ArrayList<String> banned){
		this.admins=admins;
		this.banned=banned;
	}
	
	////////////////////////////////Mutators////////////////////////////////
	
	//Admins
	
	//Add admins to admin list
	public void addAdmins(ArrayList<String> toAdd){
		for (int i=0; i<toAdd.size(); i++){
			if (!admins.contains(toAdd.get(i))){
					admins.add(toAdd.get(i));
			}
		}
	}
	
	//Set admin list
	public void setAdmins(ArrayList<String> toSet){
		admins=toSet;
	}
		
	//Remove admins
	public void removeAdmin(String toRemove){
		admins.remove(toRemove);
	}
			
	//Banned Users
				
	//Ban a user
	public void ban(ArrayList<String> toAdd){
		for (int i=0; i<toAdd.size(); i++){
			if (!banned.contains(toAdd.get(i))){
					banned.add(toAdd.get(i));
			}
		}
	}
				
	//Set the ban list
	public void setBannedUsers(ArrayList<String> toSet){
		banned=toSet;
	}
					
	//unban a user
	public void unban(String toRemove){
		banned.remove(toRemove);
	}
			
	////////////////////////////////Accessors////////////////////////////////
	
	public ArrayList<String> getAdmins(){
		return admins;
	}
	
	public ArrayList<String> getBanned(){
		return banned;
	}
}
