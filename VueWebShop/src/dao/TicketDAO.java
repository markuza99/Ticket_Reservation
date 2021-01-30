package dao;

import java.io.BufferedReader; 
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import beans.Customer;
import beans.Ticket;
import beans.TicketStatus;
import beans.TicketType;
import dto.ReservationDTO;

public class TicketDAO {
	private String contextPath;
	private Map<String, Ticket> tickets = new HashMap<>();
	
	public TicketDAO(String contextPath) {
		this.contextPath = contextPath;
		loadTickets();
	}
	
	public Ticket getOneTicket(String id) {
		return tickets.get(id);
	}
	
	public Ticket addTicket(ReservationDTO reservationDTO) {
		String ticketId = generateRandomId();
		while(getOneTicket(ticketId) != null) {
			ticketId = generateRandomId();
		}
		Ticket ticket = new Ticket(ticketId,reservationDTO.manifestation,LocalDateTime.now(),reservationDTO.ticketPrice,
									reservationDTO.user,TicketStatus.RESERVED,TicketType.FAN_PIT);
		
		tickets.put(ticketId, ticket);
		append(getTicketLine(ticket));
		return ticket;
	}
	
	public String generateRandomId() {
	    
	    String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz"; 
	    StringBuilder sb = new StringBuilder(10);
	    for (int i = 0; i < 10; i++) {  
            int index = (int)(AlphaNumericString.length() * Math.random()); 
            sb.append(AlphaNumericString.charAt(index)); 
        } 
  
        return sb.toString(); 
	}
	
	public String getTicketLine(Ticket ticket) {
		StringBuilder ticketString = new StringBuilder();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		ticketString.append(ticket.getId() + ";" + ticket.getManifestationId() + ";"
				+ ticket.getDateTime().format(formatter) + ";" + ticket.getPrice() + ";"
				+ ticket.getUser() + ";" + ticket.getTicketStatus() + ";"
				+ ticket.getTicketType());
        return ticketString.toString();
	}
	
	public Boolean userAttended(String manifestationId, String username) {
		// TODO Auto-generated method stub
		for(Ticket ticket : tickets.values()) {
			if(ticket.getManifestationId().equals(manifestationId)
				&& ticket.getUser().equals(username)) {
				return true;
			}
		}
		return false;
	}
	
	public void append(String line) {
		File file = new File(contextPath + "/repositories/tickets.txt");

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
        File file = new File(contextPath + "/repositories/tickets.txt");

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            for(Ticket ticket : tickets.values()) {
            	pw.println(getTicketLine(ticket));
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
	
	private void loadTickets() {
		// TODO Auto-generated method stub
		BufferedReader reader = null;
		try {
			File file = new File(contextPath + "/repositories/tickets.txt");
			reader = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while((line = reader.readLine()) != null) {
				line = line.trim();
				if(line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while(st.hasMoreTokens()) {
					
					String id = st.nextToken().trim();
					String manifestation = st.nextToken().trim();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
					LocalDateTime maintenance = LocalDateTime.parse(st.nextToken().trim(), formatter);
					double price = Double.parseDouble(st.nextToken().trim());
					String user = st.nextToken().trim();
					TicketStatus status = TicketStatus.valueOf(st.nextToken().trim());
					TicketType type = TicketType.valueOf(st.nextToken().trim());
					tickets.put(id, new Ticket(id, manifestation, maintenance, price, user, status, type));
					
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
	public void updateTicket(String oldUser, String newUser) {
		ArrayList<Ticket> ticketsUser = new ArrayList<Ticket>();
		for(Iterator<Map.Entry<String, Ticket>> it = tickets.entrySet().iterator(); it.hasNext(); ) {
		    Map.Entry<String, Ticket> entry = it.next();
		    if(entry.getValue().getUser().equals(oldUser)) {
		    	Ticket t = entry.getValue();
		        it.remove();
		        ticketsUser.add(t);
		    }
		}
		for (Ticket ticket : ticketsUser) {
			ticket.setUser(newUser);
			tickets.put(ticket.getId(), ticket);
		}
		write();
		
	}
}
