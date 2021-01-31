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
import beans.Manifestation;
import beans.Seller;
import beans.Status;
import dto.CommentDTO;


public class CommentDAO {
	private Map<String, Comment> comments = new HashMap<>();
	private String contextPath;
	private ManifestationDAO manifestationDAO;
	private SellerDAO sellerDAO;
	
	public CommentDAO(String contextPath, ManifestationDAO manifestationDAO, SellerDAO sellerDAO) {
		this.contextPath = contextPath;
		this.manifestationDAO = manifestationDAO;
		this.sellerDAO = sellerDAO;
		loadComments();
	}
	
	public List<Comment> getCommentsForManifestation(String id, Status status) {
		List<Comment> manifestationComments = new ArrayList<Comment>();
		
		for(Comment c : comments.values()) {
			if(c.getManifestation().equals(id) && c.getCommentStatus() == status && c.getApproval() == CommentApproval.ACCEPTED) {
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
					CommentApproval approval = CommentApproval.valueOf(st.nextToken().trim());
					comments.put(id, new Comment(user, manifestation, description, review, status, approval));
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
		String id = comment.getUser() + comment.getManifestation();
		comments.put(id, comment);
//		String commentLine = comment.getUser() + ";"
//				+ comment.getManifestation() + ";"
//				+ comment.getDescription() + ";" + comment.getRating()
//				+ ";" + Status.NONACTIVE;
		append(getCommentLine(comment));
		return new ArrayList<Comment> (comments.values());
	}
	
	public String getCommentLine(Comment comment) {
		StringBuilder commentString = new StringBuilder(); 
		commentString.append(comment.getUser() + ";"
				+ comment.getManifestation() + ";"
				+ comment.getDescription() + ";"
				+ comment.getRating() + ";"
				+ comment.getCommentStatus() + ";"
				+ comment.getApproval());

        return commentString.toString();
	}
	
	public void append(String line) {
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
	
	private void write() {
		File file = new File(contextPath + "/repositories/comments.txt");

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            for(Comment comment : comments.values()) {
            	pw.println(getCommentLine(comment));
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

	public Boolean userCommentedManifestation(String manifestationId, String username) {
		for(Comment c : comments.values()) {
			if(c.getManifestation().equals(manifestationId)
					&& c.getUser().equals(username)
					&& c.getCommentStatus() == Status.NONACTIVE) {
				return true;
			}
		}
		return false;
	}



	public int getManifestationRating(String manifestationId) {
		// TODO Auto-generated method stub
		int sumOfRatings = 0;
		int n = 0;
		for(Comment comment : comments.values()) {
			if(comment.getManifestation().equals(manifestationId)
					&& comment.getCommentStatus() == Status.ACTIVE) {
				sumOfRatings += comment.getRating();
				n++;
			}
		}
		if(n == 0) {
			return 0;
		}
		// TO DO -- srediti average rating
		return sumOfRatings / n;
	}

	public List<Comment> getCommentsForSeller(String username) {
		// TODO Auto-generated method stub
		List<Comment> manifestationComments = new ArrayList<Comment>();
		
		for(Comment comment : comments.values()) {
			Seller seller = sellerDAO.getSeller(username);
			for(Manifestation m : seller.getManifestations()) {
				if(comment.getManifestation().equals(m.getId())) {
					manifestationComments.add(comment);
				}
			}
		}

		return manifestationComments;
	}

	public List<Comment> approveComment(Comment comment, String username) {
		for(Comment c : comments.values()) {
			if(c.getUser().equals(comment.getUser()) && c.getManifestation().equals(comment.getManifestation())) {
				c.setCommentStatus("ACTIVE");
				c.setApproval("ACCEPTED");
			}
		}
		write();
		return getCommentsForSeller(username);
	}

	public List<Comment> declineComment(Comment comment, String username) {
		// TODO Auto-generated method stub
		for(Comment c : comments.values()) {
			if(c.getUser().equals(comment.getUser()) && c.getManifestation().equals(comment.getManifestation())) {
				c.setCommentStatus("ACTIVE");
				c.setApproval("DENIED");
			}
		}
		write();
		return getCommentsForSeller(username);
	}
}
