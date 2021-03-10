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

import beans.Gender;
import beans.Role;
import beans.User;
import dao.interfaces.IUserDAO;

public class UserDAO implements IUserDAO {
	private Map<String, User> users = new HashMap<>();
	private String contextPath;
	
	public UserDAO() {}
	
	public UserDAO(String contextPath) {
		this.contextPath = contextPath;
		loadUsers();
	}

	@Override
	public User create(User user) {
		if(read(user.getUsername()) != null) {
            return null;
		}
		users.put(user.getUsername(), user);
		appendToFile(userCSVRepresentation(user));
		return user;
	}

	@Override
	public User read(String username) {
		return users.get(username);
	}

	@Override
	public User update(User user) {
		users.put(user.getUsername(), user);
		writeToFile();
		return user;
	}

	@Override
	public User delete(String username) {
		User user = users.get(username);
		user.setIsDeleted("1");
		writeToFile();
		return user;
	}
	
	@Override
	public User retrieve(String username) {
		User user = users.get(username);
		user.setIsDeleted("0");
		writeToFile();
		return user;
	}
	
	@Override
	public List<User> getAll() {
		return new ArrayList<User>(users.values());
	}
	
	private String userCSVRepresentation(User user) {
		StringBuilder userString = new StringBuilder();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String deleted = user.getIsDeleted() ? "1" : "0";
		userString.append(user.getRole() + ";" + user.getUsername() + ";" + user.getPassword() + ";" 
             + user.getFirstName() + ";" + user.getLastName() + ";"
             + user.getGender() + ";" + user.getBirthDate().format(formatter) + ";" + deleted);

        return userString.toString();
	}
	
	private void appendToFile(String line) {
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
	
	private void writeToFile() {
		File file = new File(contextPath + "/repositories/users.txt");

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            for(User user : users.values()) {
            	pw.println(userCSVRepresentation(user));
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

	
}
