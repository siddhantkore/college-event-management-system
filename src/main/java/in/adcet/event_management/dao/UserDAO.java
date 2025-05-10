package in.adcet.event_management.dao;

import java.util.List;

import in.adcet.event_management.notifications.MailNotification;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import in.adcet.event_management.entity.User;
import in.adcet.event_management.utils.SessionFactoryUtils;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class UserDAO {

	private final SessionFactory sFactory = SessionFactoryUtils.getSessionFactory();
	
	
	public User login(String username, String password) throws Exception {
		
		String findUser = "FROM User WHERE username=:username AND password=:password";
		try (Session session = sFactory.openSession()) {
			User user = session.createQuery(findUser,User.class)
					.setParameter("username", username)
					.setParameter("password", password)
					.uniqueResult();
			if(user==null)
				log.warn("No User found.");
			
			return user;
		} catch (Exception e) {
			log.error("Error While log in");
			throw e;
		}
	}
	
	
	public User signup(User user) throws Exception {
		
		Transaction transaction = null;
		
		try (Session session = sFactory.openSession()) {
			
			log.info("",user);
			User alreadyExistUser = session.createQuery("FROM User u WHERE u.username = :username",User.class)
					.setParameter("username", user.getUsername())
					.uniqueResult();
			
			if(alreadyExistUser!=null) {
				throw new Exception("User already Exist");
			}
			transaction = session.beginTransaction();
			
			session.persist(user);
			new MailNotification().signupMail(user.getEmail());
			transaction.commit();
			return user;
		} catch (Exception e) {
			log.error("User already exist");
			throw e;
		}	
	}
	
	
	public User getUserByUsername(String username) throws Exception {
		String hqlQuery = "FROM User u WHERE u.username = :username";
	
		try (Session session = sFactory.openSession()) {
			
			log.info(username);
			
			User queryResult =  session.createQuery(hqlQuery,User.class)
					.setParameter("username", username)
					.uniqueResult();
			
			if(queryResult==null) {
				log.warn("No user found with this usernme");
			}
			
			return queryResult;
			
		} catch (Exception e) {
			throw new Exception();
		}
		
	}


	public String deleteUser(String username) throws Exception {
		
		
		Transaction transaction = null;
		
		try (Session session = sFactory.openSession()) {
			
			transaction = session.beginTransaction();
			
			String deleteUserQuery = "DELETE FROM User u WHERE u.username = :username";
			
			int deletedUser = session.createQuery(deleteUserQuery,User.class)
					.setParameter("username", username)
					.executeUpdate();
			transaction.commit();
			session.close();
			
			if(deletedUser<1) {
				log.warn("No user Found");
				throw new Exception();
			}
			
			return "Deleted successful!!";
			
		} catch (Exception e) {
			if(transaction!=null)
				transaction.rollback();
			log.error("Error While deleting a user");
			throw e;
		}
		
	}
	
	public List<User> getAllUser () throws Exception {
		
		try (Session session = sFactory.openSession()) {
			List<User> allUsers = session.createQuery("FROM User u", User.class)
					.getResultList();
			
			if (allUsers.isEmpty()) {
				log.warn("No user found");
			}
			
			return allUsers;
		} catch (Exception e) {
			log.error("Error While fetching users");
			throw e;
		}
		
	}
	
}
