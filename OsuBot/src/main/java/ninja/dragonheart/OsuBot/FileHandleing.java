package ninja.dragonheart.OsuBot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHandleing {
	
	public static boolean exists(String name){
		checkDir();
		File temp = new File(name);
		System.out.println(name);
		System.out.println(temp.getAbsoluteFile().exists());
		return temp.getAbsoluteFile().exists();
	}
	
	public static void checkDir(){
		if(!new File("C://osuBot").exists()){
			try {
				Files.createDirectories(Paths.get("C://osuBot"));
			} catch (IOException e) {
				System.out.println("ERROR: IOException when creating path C://osuBot");
				e.printStackTrace();
			}
		}
	}
	
	public static void writeOutChannelSettings(ChannelSettings toWrite, String filePath){
		checkDir();
		try {
			ObjectOutputStream os=new ObjectOutputStream(new FileOutputStream(filePath)); //File path should look as such C://osuBot/NAMEOFFILE
			os.writeObject(toWrite); //Writes the given object to file
			os.close();
		} catch (IOException e) {
			System.out.println("ERROR: IOException when writing out channel settings!");
			e.printStackTrace();
		} 
	}
	
	public static ChannelSettings readInChannelSettings(String fileName){
		checkDir();
		ChannelSettings settingsToReturn=null;
		try {
			ObjectInputStream is=new ObjectInputStream(new FileInputStream(fileName));
			try {
				settingsToReturn=(ChannelSettings) is.readObject(); //Reads the object
			} catch (ClassNotFoundException e) {
				System.out.println("ERROR: ClassNotFoundException when reading in channel settings!");
				e.printStackTrace();
			}
			is.close();
		} catch (IOException e) {
			System.out.println("ERROR: IOException when reading in channel settings!");
			e.printStackTrace();
		}
		
		return new ChannelSettings(settingsToReturn.getServerSimple(), settingsToReturn.getName(), settingsToReturn.getOauth(), settingsToReturn.getChannel());
	}
	
	public static void saveLists(Lists toSave, String filePath){
		checkDir();
		try {
			ObjectOutputStream os=new ObjectOutputStream(new FileOutputStream(filePath)); //File path should look as such C://osuBot/NAMEOFFILE
			os.writeObject(toSave); //Writes the given object to file
			os.close();
		} catch (IOException e) {
			System.out.println("ERROR: IOException when saving Lists!");
			e.printStackTrace();
		} 
	}
	
	public static Lists readLists(String filePath){
		checkDir();
		Lists listsToReturn=null;
		try {
			ObjectInputStream is=new ObjectInputStream(new FileInputStream(filePath));
			try {
				listsToReturn=(Lists) is.readObject(); //Reads the object
			} catch (ClassNotFoundException e) {
				System.out.println("ERROR: ClassNotFoundException when reading in lists!");
				e.printStackTrace();
			}
			is.close();
		} catch (IOException e) {
			System.out.println("ERROR: IOException when reading in lists!");
			e.printStackTrace();
		}
		
		return new Lists(listsToReturn.getAdmins(), listsToReturn.getBanned());
	}
}
