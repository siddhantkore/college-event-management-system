package in.adcet.event_management;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import in.adcet.event_management.entity.Events;
import in.adcet.event_management.entity.User;
import in.adcet.event_management.notifications.MailNotification;
import in.adcet.event_management.notifications.Notification;
import in.adcet.event_management.service.EventService;
import in.adcet.event_management.service.RegisterService;
import in.adcet.event_management.service.ResultService;
import in.adcet.event_management.service.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * Take care of NullPointerException while making any next call
 */
@Slf4j
public class App {
//	static EventService eventService = new EventService();
//	static RegisterService registerService = new RegisterService();
//	static ResultService resultService = new ResultService();
//	static UserService userService = new UserService();
//
//   // public static void main( String[] args ) throws Exception {
//    	//dashboard();
//    	/*
//    	SessionFactory sFactory = SessionFactoryUtils.getSessionFactory();
//
//    	Session session = sFactory.openSession();
//
//    	Transaction transaction = session.beginTransaction();
//
//    	User user = new User();
//
//    	user.setRole("student");
//    	user.setUsername("siddhant");
//    	user.setPassword("1234567");
//
//    	session.persist(user);
//    	session.flush();
//    	transaction.commit();
//
//    	session.close();
//    	*/
////    	Notification notification = new MailNotification();
////    	notification.sendNotification("mr.pruthviraj07@gmail.com");
//    	//notification.sendNotificationToWinner("kore7447@@gmail.com", "eventName");
//    //}
//
//
//    public static void options() {
//    	System.out.println("1) Log in");
//    	System.out.println("2) Sign up");
//    	System.out.println("3) Add new event");
//    	System.out.println("4) Delete an event");
//    	System.out.println("5) Get all events");
//    	System.out.println("6) Get event by name");
//    	System.out.println("7) Get all Registrations");
//    	System.out.println("8) Get Events by a Student");
//    	System.out.println("9) Get Students of a Event");
//    	System.out.println("10) Register a Event");
//    	System.out.println("11) Add a Event result");
//    	System.out.println("12) Get Results");
//    	System.out.println("13) Delete a User");
//    	System.out.println("14) Get all user");
//    	System.out.println("15) Get user by username");
//    	System.out.println("-1) Exit");
//
//    }
//
//
//    public static void dashboard() {
//    	int choice = 0;
//    	Scanner scanner = new Scanner(System.in);
//
//    	User newUser = new User();
//		newUser.setEmail("megast7447@gmail.com");
//		newUser.setName("Tim Fine");
//		newUser.setUsername("timfine");
//		newUser.setPassword("12345");
//		newUser.setRole("student");
//
//
//		Events event = new Events();
//		event.setCategory("Cultural");
//		event.setDescription("Dance Compitition held in our college!");
//		event.setDate("2025, 8, 15");
//		event.setName("ADCET Dance Compitition");
//		event.setLastDate(LocalDate.of(2025, 8, 8));
//		event.setMaxParticipant(15);
//		event.setTime(LocalTime.of(10, 30));
//
//		Events event0 = new Events();
//		event0.setCategory("Technical");
//		event0.setDescription("Coding Compitition");
//		event0.setDate("2025, 8, 15");
//		event0.setName("Dragon Developers");
//		event0.setLastDate(LocalDate.of(2025, 8, 8));
//		event0.setMaxParticipant(50);
//		event0.setTime(LocalTime.of(10, 30));
//
//		User user = null;
//		Events events2 = null;
//
//    	do{
//    		options();
//    		System.out.println("Choose an option");
//    		choice = scanner.nextInt();
//
//    		switch(choice) {
//
//    		case 1: // Log in
//    			user = userService.loginUser("siddhantkore","12345");
//    			System.out.println(user.getUsername());
//    			System.out.println(user.getName());
//    			User user3 = userService.loginUser("timfine","12345");
//    			System.out.println(user3.getUsername());
//    			System.out.println(user3.getName());
//    			break;
//    		case 2: // Sign Up
//    			User user2 = userService.signupUser(newUser);
//    			System.out.println(user2);
//    			break;
//    		case 3: // Add new event
//
//    			eventService.addANewEvent(event0);
//    			break;
//    		case 4: // Delete an event
//    			eventService.deleteEvent("Dragon Developers");
//    			break;
//    		case 5: // Get all events
//    			eventService.getAllEvents().forEach(n-> System.out.println(n.getName()));
//    			break;
//    		case 6: // Get event by name
//    			events2 = eventService.getEventByEventName("ADCET Dance Compitition");
//    			System.out.println(events2.getName());
//    			break;
//    		case 7: // Get all Registrations
//    			registerService.getAllRegistrations().forEach(n->System.out.println(n.getUsername()));
//
//    			break;
//    		case 8: // Get Events by a Student
//    			registerService.getEventsOfAStudent("siddhantkore").forEach(n->System.out.println(n.getEventName()));
//    			break;
//    		case 9: // Get Students of a Event
//    			registerService.getAllStudentsOfaEvent("ADCET Dance Compitition").forEach(n->System.out.println(n.getUsername()));
//
//    			break;
//    		case 10: // Register a Event
//
////    			eventService.addANewEvent(event0);
//
//    			System.out.println(registerService.registerAEvent(events2, user));
//    			break;
//    		case 11: // Add a Event result
//    			resultService.addResult("timfine", "siddhantkore", "Dragon Developers");
//    			break;
//    		case 12: // Get Results
//    			resultService.getAllResults().forEach(n->System.out.println(n.getWinner()));
//    			break;
//    		case 13: // Delete a User
//    			userService.deleteUser("timfine");
//    			break;
//    		case 14: // Get all user
//    			userService.getAllUsers().forEach(n->System.out.println(n.getName()));
//    			break;
//    		case 15: // Get user by username
//    			User user4 = userService.getUserByUsername("siddhantkore");
//    			System.out.println(user4.getName());
//    			break;
//    		case -1:
//    			System.out.println("Exiting...");
//    			break;
//    		default:
//    			System.out.println("Choose a valid option");
//    		}
//
//    	} while(choice != -1);
//    }
    
}
