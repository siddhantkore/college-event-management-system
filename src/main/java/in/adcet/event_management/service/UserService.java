package in.adcet.event_management.service;

import java.util.List;

import in.adcet.event_management.dao.UserDAO;
import in.adcet.event_management.entity.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserService {
	
	// handle exceptions thrown by DAO
	private UserDAO userDAO = new UserDAO();
	
	public User loginUser(String username, String password) {
		User loggedInUser=null;
		try {
			loggedInUser = userDAO.login(username, password);
		} catch (Exception e) {
			log.error("Error while Log In",e);
		}
		
		return loggedInUser;
	}
	
	
	public User signupUser(User user) {
		User signedUpUser=null;
		try {
			signedUpUser = userDAO.signup(user);
		} catch (Exception e) {
			log.error(" Error While signup !",e);
		}
		return signedUpUser;
	}
	
	public User getUserByUsername(String username) {
		User user=null;
		try {
			user = userDAO.getUserByUsername(username);
			if(user==null) {
				throw new Exception("User not found!!");
			}
		} catch (Exception e) {
			log.error("No user found or something goes wrong",e);
		}
		return user;
	}
	
	
	public void deleteUser(String username) {
		try {
			userDAO.deleteUser(username);
		} catch (Exception e) {
			log.error("",e);
		}
	}
	
	public List<User> getAllUsers() {
		List<User> allUsers = null;
		try {
			allUsers = userDAO.getAllUser();
		} catch (Exception e) {
			log.error("",e);
		}
		return allUsers;
	}
}
