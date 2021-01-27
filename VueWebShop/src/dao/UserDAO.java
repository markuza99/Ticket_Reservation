package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import beans.Customer;
import beans.Gender;
import beans.Role;
import beans.Ticket;
import beans.User;

public class UserDAO {
	private Map<String, User> users = new HashMap<>();
	private String contextPath;
	private CustomerDAO customerDAO;
	
	public UserDAO() {}
	
	public UserDAO(String contextPath, CustomerDAO customerDAO) {
		this.contextPath = contextPath;
		this.customerDAO = customerDAO;
		loadUsers();
	}
	
	public User find(String username, String password) {
		User user = users.get(username);
		if (!user.getPassword().equals(password)) {
			return null;
		}
		return user;
	}
	
	public List<User> getAllUsers() {
		return new ArrayList<User>(users.values());
	}
	
	public User getOneUser(String id) {
		return users.get(id);
	}
	
	private void loadUsers() {
		BufferedReader reader = null;
		try {
			File file = new File(contextPath + "/repositories/users.txt");
			reader = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while((line = reader.readLine()) != null) {
				line = line.trim();
				if(line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while(st.hasMoreTokens()) {
					Role role = Role.valueOf(st.nextToken().trim());
					String username = st.nextToken().trim();
					String password = st.nextToken().trim();
					String firstName = st.nextToken().trim();
					String lastName = st.nextToken().trim();
					Gender gender = Gender.valueOf(st.nextToken().trim());
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate birthDate = LocalDate.parse(st.nextToken().trim(), formatter);
					String deleted = st.nextToken().trim();
					Boolean isDeleted = false;
					if(deleted.equals("1")) {
						isDeleted = true;
					}
					users.put(username, new User(username, firstName, lastName, 
							password, gender, birthDate, role, isDeleted));
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(reader != null) {
				try {
					reader.close();
				}
				catch (Exception e) {}
			}
		}
		
	}

	
	public User registration(User user) {
        if(usernameExists(user.getUsername())) {
            return null;
        }
        String customerLine = user.getUsername() + "; ;" + 0 + ";" + "regularni";
        users.put(user.getUsername(), user);
        customerDAO.getCustomers().put(user.getUsername(),
        		new Customer(user.getUsername(), new ArrayList<Ticket>(),
        		0, customerDAO.getCustomerType("regularni")));
        append(getUserLine(user));
        if(user.getRole() == Role.CUSTOMER) {
        	customerDAO.append(customerLine);
        }
        return user;
    }
	
	private Boolean usernameExists(String username) {
	    return users.containsKey(username);
	}
	
	private void append(String line) {
		File file = new File(contextPath + "/repositories/users.txt");

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            pw.println(line);
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(pw != null) {
                try {
                    pw.close();
                }
                catch (Exception e) {}
            }
        }
	}
	
	public String getUserLine(User user) {
		StringBuilder userString = new StringBuilder();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String deleted = user.getIsDeleted() ? "1" : "0";
		userString.append(user.getRole() + ";" + user.getUsername() + ";" + user.getPassword() + ";" 
             + user.getFirstName() + ";" + user.getLastName() + ";"
             + user.getGender() + ";" + user.getBirthDate().format(formatter) + ";" + deleted);

        return userString.toString();
	}
	
	private void write() {
		File file = new File(contextPath + "/repositories/users.txt");

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            for(User user : users.values()) {
            	pw.println(getUserLine(user));
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(pw != null) {
                try {
                    pw.close();
                }
                catch (Exception e) {}
            }
        }
	}

	public List<User> deleteUser(String username) {
		// TODO Auto-generated method stub
		User user = users.get(username);
		user.setIsDeleted("1");
		write();
		return getAllUsers();
	}

	public List<User> retrieveUser(String username) {
		// TODO Auto-generated method stub
		User user = users.get(username);
		user.setIsDeleted("0");
		write();
		return getAllUsers();
	}
	
}
