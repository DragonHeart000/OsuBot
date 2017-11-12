package ninja.dragonheart.OsuBot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class Controller implements Initializable{
	
	//All variables declared in osubotsetup.fxml
	@FXML
	private AnchorPane setupPane; //Container for core content on scene
	@FXML
	private TextField twitchName;
	@FXML
	private TextField oauth;
	@FXML
	private TextField channel;
	@FXML
	private TextField osuName;
	@FXML
	private TextField osuPass;
	@FXML
	private TextField adminToAdd;
	@FXML
	private TextField adminToRemove;
	@FXML
	private TextField userToBan;
	@FXML
	private TextField userToUnban;
	@FXML
	private ListView<String> adminListView;
	@FXML
    private CheckBox saveSettingsCheckBox; //use to determin if the program should save the user settings
	
	
	
	//Vars for local bot settings
	private static ChannelSettings twitchSettings;
	private static ChannelSettings osuSettings;
	private boolean useOsuChat=true;
	
	//List of users
	public ArrayList<String> admins = new ArrayList<String>();
	public ArrayList<String> banned = new ArrayList<String>();
	
	//Threads
	private Thread twitchThread;
	private Thread osuThread;
	
	@Override
	public void initialize(java.net.URL arg0, ResourceBundle arg1) {
		
	}
	
	public void start() throws IOException{
		
		if (!twitchName.getText().equals("") && !oauth.getText().equals("")){ //Make sure that the minimum has been entered
			
			if (osuName.getText().equals("") || osuPass.getText().equals("")){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Notice!");
				alert.setHeaderText(null);
				alert.setContentText("One of the main features brought with osu!Bot 3.0 was the use of in game chat channels. You currently"
						+ " have one or more of the required feilds for it blank thus in game chat will not work. It is recomended that "
						+ "you relaunch and put in the feilds, however, the program will run fine without the added features of in game chat.");
				alert.showAndWait();
				
				useOsuChat=false; //Used to make sure nothing for the osu irc is ran if not being used.
			}
			setupPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("running.fxml")));  //Sets screen to the running screen
		
			String twitchChannel; //Initialize var
			if (channel.getText().equals("")){ //Check if channel was left blank which indicates to use same channel for the user streaming
				twitchChannel="#"+twitchName.getText().toLowerCase(); //Set the channel to join to be # followed by the twitch name.
			} else {
				twitchChannel=channel.getText().toLowerCase(); //Set channel to channel provided.
			} //Twitch irc channels are lowercase only
			
			//Construct twitch settings var with given info
			twitchSettings=new ChannelSettings(true, twitchName.getText(), oauth.getText(), twitchChannel);
            if (saveSettingsCheckBox.isSelected()){
			    FileHandleing.writeOutChannelSettings(twitchSettings, "C://osuBot/twitchSettings.bin");  //Save the configuration of the bot for easy future use
            }
			//Construct Osu! settings var with given info
			if (useOsuChat){ //Only run if using osu irc
				osuSettings=new ChannelSettings(true, osuName.getText(), osuPass.getText(), "#OSU");
                if (saveSettingsCheckBox.isSelected()){
				    FileHandleing.writeOutChannelSettings(osuSettings, "C://osuBot/osuSettings.bin");  //Save the configuration of the bot for easy future use
                }
			}
			
			//Must make two threads, one for each irc so we can have it running in both at the same time.
			
			//Make new thread for twitch
			twitchThread=new Thread(){
				@Override
				public void run(){
					PircBotX twitchBot=null; //Initialize twitchBot so we can acess it outside of the while loop
					
					try { //Try catch so we can handle the interrupted exception we will throw
						while (!Thread.interrupted()){ //while loop so we can throw interrupted exception in order to close twitchBot
							//Configure Twitch bot with given info
							Configuration twitchConfiguration = new Configuration.Builder()
									.setAutoNickChange(false) //Twitch doesn't support multiple users
									.setOnJoinWhoEnabled(false) //Twitch doesn't support WHO command
									.setCapEnabled(true)
									.addServer("irc.twitch.tv")
									.setName(twitchSettings.getName()) //Your twitch.tv username
									.setServerPassword(twitchSettings.getOauth()) //Your oauth password from http://twitchapps.com/tmi
									.addAutoJoinChannel(twitchSettings.getChannel()) //Some twitch channel
									.addListener(new TwitchListener())
									.buildConfiguration();
						
							//Create bot
							twitchBot = new PircBotX(twitchConfiguration);
							
							//Start with try catch
							try {
								twitchBot.startBot();
							} catch (IOException e) {
								System.out.println("ERROR: IOException in twitchBot");
								e.printStackTrace();
							} catch (IrcException e) {
								System.out.println("ERROR: IrcException in twitchBot");
								e.printStackTrace();
							}
						}
					} catch (Exception e){
						//We may ignore it since we only wanted it so we can break the loop
					} finally { //Finally statement to make sure we dispose of the resources properly
						twitchBot.close(); //Close the twitchBot so the thread my close properly.
					}
				}
			};
			
			if (useOsuChat){ //Only run if using osu irc
				//Make new thread for Osu!
				osuThread=new Thread(){
					//Initialize osuBot so we can close it in a later method when closing the thread.
					@Override
					public void run(){
						//Initialize osuBot so we can close it in a later method when closing the thread.
						PircBotX osuBot=null;
						
						try {  //Try catch so we can handle the interrupted exception we will throw
							while (!Thread.interrupted()){ //while loop so we can throw interrupted exception in order to close
								//Configure osu! bot with given info
								Configuration osuConfiguration = new Configuration.Builder()
										.setAutoNickChange(false) //Twitch doesn't support multiple users
										.setOnJoinWhoEnabled(false) //Twitch doesn't support WHO command
										.setCapEnabled(true)
										.addServer("irc.ppy.sh")
										.setName(osuSettings.getName()) //Osu! username
										.setServerPassword(osuSettings.getOauth()) //Server password from https://osu.ppy.sh/p/irc
										.addAutoJoinChannel("#OSU") //Some twitch channel
										.addListener(new OsuListener())
										.buildConfiguration();
						
								//Create bot
								osuBot = new PircBotX(osuConfiguration);
								
								//Start with try catch
								try {
									osuBot.startBot();
								} catch (IOException e) {
									System.out.println("ERROR: IOException in twitchBot");
									e.printStackTrace();
								} catch (IrcException e) {
									System.out.println("ERROR: IrcException in twitchBot");
									e.printStackTrace();
								}
							}
						} catch (Exception e){
							//We may ignore it since we only wanted it so we can break the loop
						} finally {
							osuBot.close(); //Close the osuBot so the thread my close properly.
						}
					}
					
				};
			}
					
					
			twitchThread.start(); //Starts the twitch irc chat bot thread that was made above
			if (useOsuChat){ //Only run if using osu irc
				osuThread.start();  //Starts the osu irc chat bot thread that was made above
			}
		} else { //If minimum has not been entered give alert.
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Missing Input");
			alert.setHeaderText(null);
			alert.setContentText("Please enter in Twitch Name and oauth at the very minimum.");
			alert.showAndWait();
		}
	}
	
////////////////////////////////////////////////////LOAD SETTINGS///////////////////////////////////////////////////////
	
public void startLoaded() throws IOException{
	
	setupPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("running.fxml"))); //Set to running screen
	
	//Must make two threads, one for each irc so we can have it running in both at the same time.
			
	//Make new thread for twitch
	twitchThread=new Thread(){
		@Override
		public void run(){
			PircBotX twitchBot=null; //Initialize twitchBot so we can acess it outside of the while loop
			
			try { //Try catch so we can handle the interrupted exception we will throw
				while (!Thread.interrupted()){ //while loop so we can throw interrupted exception in order to close twitchBot
					//Configure Twitch bot with given info
					Configuration twitchConfiguration = new Configuration.Builder()
							.setAutoNickChange(false) //Twitch doesn't support multiple users
							.setOnJoinWhoEnabled(false) //Twitch doesn't support WHO command
							.setCapEnabled(true)
							.addServer("irc.twitch.tv")
							.setName(twitchSettings.getName()) //Your twitch.tv username
							.setServerPassword(twitchSettings.getOauth()) //Your oauth password from http://twitchapps.com/tmi
							.addAutoJoinChannel(twitchSettings.getChannel()) //Some twitch channel
							.addListener(new TwitchListener())
							.buildConfiguration();
						
					//Create bot
					twitchBot = new PircBotX(twitchConfiguration);
							
					//Start with try catch
					try {
						twitchBot.startBot();
					} catch (IOException e) {
						System.out.println("ERROR: IOException in twitchBot");
						e.printStackTrace();
					} catch (IrcException e) {
						System.out.println("ERROR: IrcException in twitchBot");
						e.printStackTrace();
					}
				}
			} catch (Exception e){
				//We may ignore it since we only wanted it so we can break the loop
			} finally { //Finally statement to make sure we dispose of the resources properly
				twitchBot.close(); //Close the twitchBot so the thread my close properly.
			}
		}
	};
		
	if (useOsuChat){ //Only run if using osu irc
		//Make new thread for Osu!
		osuThread=new Thread(){
			//Initialize osuBot so we can close it in a later method when closing the thread.
			@Override
			public void run(){
				//Initialize osuBot so we can close it in a later method when closing the thread.
				PircBotX osuBot=null;
				
				try {  //Try catch so we can handle the interrupted exception we will throw
					while (!Thread.interrupted()){ //while loop so we can throw interrupted exception in order to close
						//Configure osu! bot with given info
						Configuration osuConfiguration = new Configuration.Builder()
								.setAutoNickChange(false) //Twitch doesn't support multiple users
								.setOnJoinWhoEnabled(false) //Twitch doesn't support WHO command
								.setCapEnabled(true)
								.addServer("irc.ppy.sh")
								.setName(osuSettings.getName()) //Osu! username
								.setServerPassword(osuSettings.getOauth()) //Server password from https://osu.ppy.sh/p/irc
								.addAutoJoinChannel("#OSU") //Some twitch channel
								.addListener(new OsuListener())
								.buildConfiguration();
						
						//Create bot
						osuBot = new PircBotX(osuConfiguration);
						
						//Start with try catch
						try {
							osuBot.startBot();
						} catch (IOException e) {
							System.out.println("ERROR: IOException in twitchBot");
							e.printStackTrace();
						} catch (IrcException e) {
							System.out.println("ERROR: IrcException in twitchBot");
							e.printStackTrace();
						}
					}
				} catch (Exception e){
					//We may ignore it since we only wanted it so we can break the loop
				} finally {
					osuBot.close(); //Close the osuBot so the thread my close properly.
				}
			}
			
		};
	}
					
					
	twitchThread.start(); //Starts the twitch irc chat bot thread that was made above
	if (useOsuChat){ //Only run if using osu irc
		osuThread.start();  //Starts the osu irc chat bot thread that was made above
	}
}
	


//////////////////////////////////////////////////////////////END OF LOAD SETTINGS/////////////////////////////////////////////////////
	
	public void about() throws IOException{
		String url_open ="https://dragonheart.ninja/"; //Set URL to open
		java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));  //opens the URL in the default browser
	}
	
	public void gitHub() throws IOException{
		String url_open ="https://github.com/DragonHeart000/OsuBot"; //Set URL to open
		java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));  //opens the URL in the default browser
	}
	
	@SuppressWarnings("deprecation")
	public void exit(){
		try {
			if (twitchThread.isAlive()){
				System.out.println("twitch thread stoped");
				twitchThread.interrupt(); //stops the threads with the bot running to prevent resource leaks.\
				if (twitchThread.isAlive()){ //Checks if the thread is still alive
					twitchThread.stop(); //Force closes thread the unsafe way if the thread was not closing as it should have before.
				}
			}
		} catch (Exception e){
			//Try catch in order to prevent errors in event that the program is terminated via this method before start() is called
		}
		try {
			if (osuThread.isAlive()){
				System.out.println("osu thread stoped");
				osuThread.interrupt(); //stops the threads with the bot running to prevent resource leaks.
				if (osuThread.isAlive()){ //Checks if the thread is still alive
					osuThread.stop(); //Force closes thread the unsafe way if the thread was not closing as it should have before.
				}
			}
		} catch (Exception e){
			//Try catch in order to prevent errors in event that the program is terminated via this method before start() is called
		}
		Platform.exit();  //terminates the session
	}
	
	@SuppressWarnings("deprecation")
	public void killThreads(){
		try {
			if (twitchThread.isAlive()){
				System.out.println("twitch thread stoped");
				twitchThread.interrupt(); //stops the threads with the bot running to prevent resource leaks.\
				if (twitchThread.isAlive()){ //Checks if the thread is still alive
					twitchThread.stop(); //Force closes thread the unsafe way if the thread was not closing as it should have before.
				}
			}
		} catch (Exception e){
			//Try catch in order to prevent errors in event that the program is terminated via this method before start() is called
		}
		try {
			if (osuThread.isAlive()){
				System.out.println("osu thread stoped");
				osuThread.interrupt(); //stops the threads with the bot running to prevent resource leaks.
				if (osuThread.isAlive()){ //Checks if the thread is still alive
					osuThread.stop(); //Force closes thread the unsafe way if the thread was not closing as it should have before.
				}
			}
		} catch (Exception e){
			//Try catch in order to prevent errors in event that the program is terminated via this method before start() is called
		}
	}
	
	public void loadSaved(){
		useOsuChat=false;
		boolean runable=false;  //Var to keep track of if C://osuBot/twitchSettings.bin is found
		if (FileHandleing.exists("C://osuBot/twitchSettings.bin")){ //check if settings are saved in the default spot
			killThreads();
			twitchSettings=FileHandleing.readInChannelSettings("C://osuBot/twitchSettings.bin"); //Reads in the settings
			runable=true;  //Set to true since we found the file so we can try running it with these settings
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Missing Settings");
			alert.setHeaderText(null);
			alert.setContentText("Osu!Bot was unable to locate C://osuBot/twitchSettings.bin! This is likely do to the program not having"
					+ " been configured yet. Please run Osu!Bot at least once with correct inputs. If this error persists please go to "
					+ "Osu!Bot's GitHub page and report what happened there.");
			alert.showAndWait();
		}
		if (FileHandleing.exists("C://osuBot/osuSettings.bin")){ //check if settings are saved in the default spot
			osuSettings=FileHandleing.readInChannelSettings("C://osuBot/twitchSettings.bin"); //Reads in the settings
			useOsuChat=true;
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Missing Settings");
			alert.setHeaderText(null);
			alert.setContentText("Osu!Bot was unable to locate C://osuBot/osuSettings.bin! That may sound scary but is actually okay "
					+ "using the osu chat is not needed for Osu!Bot to work but is recomended. You may feel free to continue as is or "
					+ "you can go to File>New File and reconfigure Osu!Bot.");
			alert.showAndWait();
		}
		if (FileHandleing.exists("C://osuBot/lists.bin")){ //check if settings are saved in the default spot
			osuBotMain.loadNewLists(); //Reads in the settings
			admins=osuBotMain.getAdmins();
			banned=osuBotMain.getBanned();
			/*
			 * The following code will not work here as I believe adminListView is null since the screen
			 * that has it in it has not come up yet. If I put it in the startLoaded method it seems to
			 * still act the same. If I put it in a method called when a button on that screen is hit it
			 * will run without any errors but will not add the correct items.
			 */
			/*
			for (int i=0; i<admins.size(); i++){
				adminListView.getItems().addAll(admins.get(i));
			}
			*/
			
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Missing Lists");
			alert.setHeaderText(null);
			alert.setContentText("Osu!Bot was unable to locate C://osuBot/lists.bin! This means that either you have never"
					+ "banned a person or gave someone admin. If you know that you have but it just did not save right please"
					+ "report this error on the GitHub page found under the Help menu bar if it continues.");
			alert.showAndWait();
		}
		
		if (runable){ //Check if we found C://osuBot/twitchSettings.bin
			try {
				startLoaded(); //Start with the given settings
			} catch (IOException e) {
				System.out.println("ERROR: IOException");
				e.printStackTrace();
			}
		}
	}
	
	public void newFile(){
		killThreads(); //Stops threads so we can load new ones
		try {
			setupPane.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("osubotresetup.fxml"))); //Switches back to setup page
			/*
			 * Currently osubotresetup.fxml is an exact copy of osubotsetup.fxml just without a wrapper
			 * and without a nav bar. There likely is a more proper way to do this so that I do not need
			 * to have the extra file by just grabbing the anchor pane by ID, however, I have tried a
			 * few different way of doing that all of which do not work. Since it works as is I will put
			 * this off till later when doing optimization or if someone wants to contribute a simple
			 * solution on GitHub. Either way this HUGE comment is here to remind me and make it easy
			 * to find again later.
			 */
		} catch (IOException e) {
			System.out.println("ERROR: IOException when setting pane to setup pane on new file creation!");
			e.printStackTrace();
		}
		//This should kill the threads with the irc bots that are running (if they currently are) and reload back to osubotsetup.fxml
	}
	
	public void addAdmin(){
		admins.add(adminToAdd.getText().toLowerCase()); //Get the text field saying who to add as an admin
		osuBotMain.addAdmins(admins);
		adminListView.getItems().addAll(adminToAdd.getText().toLowerCase());
	}
	
	public void removeAdmin(){
		admins.remove(adminToRemove.getText().toLowerCase());
		osuBotMain.removeAdmin(adminToRemove.getText().toLowerCase());
		adminListView.getItems().remove(adminToRemove.getText().toLowerCase());
	}
	
	public void banUser(){
		banned.add(userToBan.getText().toLowerCase());
		osuBotMain.ban(banned);
		
		//Use alert box for now till I pick a better way to show confirmation
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText(null);
		alert.setContentText(userToBan.getText() + " has been banned.");
		alert.showAndWait();
	}
	
	public void unBanUser(){
		banned.remove(userToBan.getText().toLowerCase());
		osuBotMain.unban(userToBan.getText().toLowerCase());
		
		//Use alert box for now till I pick a better way to show confirmation
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText(null);
		alert.setContentText(userToBan.getText() + " has been removed from the banned list.");
		alert.showAndWait();
	}
	
	public void clearLists(){
		clearAdmins();
		clearBanned();
	}
	
	public void clearAdmins(){
		//adminListView.getItems().clear();
		admins=new ArrayList<String>();
		osuBotMain.clearAdmins();
	}
	
	public void clearBanned(){
		banned=new ArrayList<String>();
		osuBotMain.clearBanned();
	}
	
}
