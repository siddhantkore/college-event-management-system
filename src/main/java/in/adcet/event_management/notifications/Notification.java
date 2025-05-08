package in.adcet.event_management.notifications;

import java.util.List;

import in.adcet.event_management.entity.Events;

public interface Notification {
	
	public void sendNotificationToWinner (String to, String eventName);
	public void sendNotificationToRunnerUp (String to, String eventName);
	public void sendNotificationToAll (List<String> emails, Events events);
}
