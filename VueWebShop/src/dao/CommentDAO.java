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
import beans.CommentApproval;
import dao.interfaces.ICommentDAO;


public class CommentDAO implements ICommentDAO {
	private Map<String, Comment> comments = new HashMap<>();
	private String contextPath;
	
	public CommentDAO(String contextPath) {
		this.contextPath = contextPath;
		loadComments();
	}

	@Override
	public Comment create(Comment comment) {
		if(read(comment.getUser() + comment.getManifestation()) != null) {
            return null;
		}
		comments.put(comment.getUser() + comment.getManifestation(), comment);
		appendToFile(commentCSVRepresentation(comment));
		return comment;
	}

	@Override
	public Comment read(String id) {
		return comments.get(id);
	}

	@Override
	public Comment update(Comment comment) {
		comments.put(comment.getUser() + comment.getManifestation(), comment);
		writeToFile();
		return comment;
	}

	@Override
	public Comment delete(String id) {
		Comment comment = comments.get(id);
		comment.setIsDeleted("1");
		writeToFile();
		return comment;
	}

	@Override
	public List<Comment> getAll() {
		return new ArrayList<Comment>(comments.values());
	}

	@Override
	public Comment retrieve(String id) {
		Comment comment = comments.get(id);
		comment.setIsDeleted("0");
		writeToFile();
		return comment;
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
					String id = user + manifestation;
					CommentApproval approval = CommentApproval.valueOf(st.nextToken().trim());
					String deleted = st.nextToken().trim();
					Boolean isDeleted = false;
					if(deleted.equals("1")) {
						isDeleted = true;
					}
					comments.put(id, new Comment(user, manifestation, description, review, approval, isDeleted));
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

	public String commentCSVRepresentation(Comment comment) {
		StringBuilder commentString = new StringBuilder(); 
		String deleted = comment.getIsDeleted() ? "1" : "0";
		commentString.append(comment.getUser() + ";"
				+ comment.getManifestation() + ";"
				+ comment.getDescription() + ";"
				+ comment.getRating() + ";"
				+ comment.getApproval() + ";"
				+ deleted);
        return commentString.toString();
	}
	
	public void appendToFile(String line) {
		File file = new File(contextPath + "/repositories/comments.txt");

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
		File file = new File(contextPath + "/repositories/comments.txt");

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            for(Comment comment : comments.values()) {
            	pw.println(commentCSVRepresentation(comment));
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



}
