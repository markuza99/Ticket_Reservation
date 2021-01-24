package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import beans.Comment;
import beans.Status;


public class CommentDAO {
	private Map<String, Comment> comments = new HashMap<>();
	private String contextPath;
	private ManifestationDAO manifestationDAO;
	private UserDAO userDAO;
	
	public CommentDAO(String contextPath, ManifestationDAO manifestationDAO, UserDAO userDAO) {
		this.contextPath = contextPath;
		this.manifestationDAO = manifestationDAO;
		this.userDAO = userDAO;
		loadComments();
	}
	
	public List<Comment> getCommentsForManifestation(String id, Status status) {
		List<Comment> manifestationComments = new ArrayList<Comment>();
		
		for(Comment c : comments.values()) {
			if(c.getManifestation().equals(id) && c.getCommentStatus() == status) {
				manifestationComments.add(c);
			}
		}
		return manifestationComments;
	}
	
	private void loadComments() {
		BufferedReader reader = null;
		try {
			File file = new File(contextPath + "/repositories/comments.txt");
			reader = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while((line = reader.readLine()) != null) {
				line = line.trim();
				if(line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while(st.hasMoreTokens()) {
					String user = st.nextToken().trim();
					String manifestation = st.nextToken().trim();
					String description = st.nextToken().trim();
					int review = Integer.parseInt(st.nextToken().trim());
					Status status = Status.valueOf(st.nextToken().trim());
					String id = user + manifestation;
					comments.put(id, new Comment(user, manifestation, description, review, status));
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

	public List<Comment> postComment(Comment comment) {
		// TODO Auto-generated method stub
		String id = comment.getUser() + comment.getManifestation();
		comments.put(id, comment);
		String commentLine = comment.getUser() + ";"
				+ comment.getManifestation() + ";"
				+ comment.getDescription() + ";" + comment.getRating()
				+ ";" + Status.NONACTIVE;
		write(commentLine);
		return new ArrayList<Comment> (comments.values());
	}
	
	private void write(String comment) {
        File fileUsers = new File(contextPath + "/repositories/comments.txt");

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(fileUsers, true)));
            pw.println(comment);
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
}
