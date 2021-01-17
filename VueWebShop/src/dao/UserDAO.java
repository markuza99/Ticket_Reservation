package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import beans.Gender;
import beans.Role;
import beans.User;

public class UserDAO {
	private Map<String, User> users = new HashMap<>();
	private String contextPath;
	
	public UserDAO() {}
	
	public UserDAO(String contextPath) {
		this.contextPath = contextPath;
		loadUsers();
	}
	
	public User find(String username, String password) {
		User user = users.get(username);
		if (user==null || !user.getPassword().equals(password)) {
			return null;
		}
		return user;
	}
	
	public List<User> getAllUsers() {
		return new ArrayList<User>(users.values());
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
					String role = st.nextToken().trim();
					String username = st.nextToken().trim();
					String password = st.nextToken().trim();
					String firstName = st.nextToken().trim();
					String lastName = st.nextToken().trim();
					String gender = st.nextToken().trim();
					String birthDate = st.nextToken().trim();
					
					users.put(username, new User(username, firstName, lastName, 
							password, gender, birthDate, role));
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
        users.put(user.getUsername(), user); 
        String birthDate = user.getBirthDate();
        String userString = "CUSTOMER" + ";" + user.getUsername() + ";" + user.getPassword() + ";" 
                            + user.getFirstName() + ";" + user.getLastName() + ";"
                            + user.getGender() + ";" + birthDate;

        String customerString = user.getUsername() + ";;" + 0;
        write(userString, customerString);
        return user;
    }
	private Boolean usernameExists(String username) {
	        return users.containsKey(username);
	   }
	private void write(String user, String customer) {
	        File fileUsers = new File(contextPath + "/repositories/users.txt");
	        File fileCustomers = new File(contextPath + "/repositories/customers.txt");
	
	        PrintWriter pw = null;
	        PrintWriter pwCustomers = null;
	        try {
	            pw = new PrintWriter(new BufferedWriter(new FileWriter(fileUsers, true)));
	            pwCustomers = new PrintWriter(new BufferedWriter(new FileWriter(fileCustomers, true)));
	            pw.println(user);
	            pwCustomers.println(customer);
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if(pw != null) {
	                try {
	                    pw.close();
	                }
	                catch (Exception e) {}
	            }
	
	            if(pwCustomers != null) {
	                try {
	                    pwCustomers.close();
	                }
	                catch (Exception e) {}
	            }
	        }
	    }
	
}
