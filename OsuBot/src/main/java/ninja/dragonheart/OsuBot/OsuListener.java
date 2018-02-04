package ninja.dragonheart.OsuBot;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;
//import org.pircbotx.hooks.events.PrivateMessageEvent;

public class OsuListener extends ListenerAdapter{
	@Override
    public void onGenericMessage(GenericMessageEvent event) {
		System.out.println(event.getMessage()); //Just testing to make sure that you are able to connect and see activity.
		
		
		//onPrivateMessage(event.respondPrivateMessage("asdf"));
	}
}
