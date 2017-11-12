package ninja.dragonheart.OsuBot;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class osuBotMain extends Application{
	
	public static Lists userList=new Lists();
	
	public static void main(String [] args) throws Exception{
		
		Application.launch(args);
		
	}
	
	@Override
    public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("osubotsetup.fxml"));
		Parent osuBotSetup=loader.load();
		Controller localControllerVar = loader.getController();
		stage.setTitle("Osu!Bot 3.1.0b");
		Scene scene = new Scene(osuBotSetup, 640, 360);
		stage.setScene(scene);
		stage.setResizable(false);
		//scene.getStylesheets().add("default.css"); //Set this dynamically based on user preference
		stage.getIcons().add(new Image(getClass().getResourceAsStream("tempiconwhilstmakingit.png")));
		stage.setOnHidden(event -> {
			try {
				localControllerVar.exit();
			} catch (Exception e){
				System.out.println("There could be an error as the program did not close properly!");
				Platform.exit();
			}
		});
		stage.show();
    }
	
	public static ArrayList<String> getAdmins(){
		return userList.getAdmins();
	}
	
	public static ArrayList<String> getBanned(){
		return userList.getBanned();
	}
	
	//Admins
	
		//Add admins to admin list
		public static void addAdmins(ArrayList<String> toAdd){
			userList.addAdmins(toAdd);;
			FileHandleing.saveLists(userList, "C://osuBot/lists.bin");  //Whenever we do something with the list of admins or banned users save it!
		}
		
		//Set admin list
		public static void setAdmins(ArrayList<String> toSet){
			userList.setAdmins(toSet);
			FileHandleing.saveLists(userList, "C://osuBot/lists.bin");
		}
			
		//Remove admins
		public static void removeAdmin(String toRemove){
			userList.removeAdmin(toRemove);
			FileHandleing.saveLists(userList, "C://osuBot/lists.bin");
		}
			
		//Banned Users
			
		//Ban a user
		public static void ban(ArrayList<String> toAdd){
			userList.ban(toAdd);
			FileHandleing.saveLists(userList, "C://osuBot/lists.bin");
		}
				
		//Set the ban list
		public static void setBannedUsers(ArrayList<String> toSet){
			userList.setBannedUsers(toSet);
			FileHandleing.saveLists(userList, "C://osuBot/lists.bin");
		}
				
		//unban a user
		public static void unban(String toRemove){
			userList.unban(toRemove);
			FileHandleing.saveLists(userList, "C://osuBot/lists.bin");
		}
		
		
		//For loading new lists
		public static void loadNewLists(){
			userList=FileHandleing.readLists("C://osuBot/lists.bin");
		}
		
		public static void clearLists(){
			clearAdmins();
			clearBanned();
		}
		
		public static void clearAdmins(){
			userList.setAdmins(new ArrayList<String>());
			FileHandleing.saveLists(userList, "C://osuBot/lists.bin");
		}
		
		public static void clearBanned(){
			userList.setBannedUsers(new ArrayList<String>());
			FileHandleing.saveLists(userList, "C://osuBot/lists.bin");
		}

}
