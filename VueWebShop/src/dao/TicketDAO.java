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
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import beans.Ticket;
import beans.TicketStatus;
import beans.TicketType;
import dao.interfaces.ITicketDAO;

public class TicketDAO implements ITicketDAO {
	private String contextPath;
	private Map<String, Ticket> tickets = new HashMap<>();
	
	public TicketDAO(String contextPath) {
		this.contextPath = contextPath;
		loadTickets();
	}

	@Override
	public Ticket create(Ticket ticket) {
		tickets.put(ticket.getId(), ticket);
		appendToFile(ticketCSVRepresentation(ticket));
		return ticket;
	}

	@Override
	public Ticket read(String id) {
		return tickets.get(id);
	}

	@Override
	public Ticket update(Ticket ticket) {
		tickets.put(ticket.getId(), ticket);
		writeToFile();
		return ticket;
	}

	@Override
	public Ticket delete(String id) {
		Ticket ticket = tickets.get(id);
		ticket.setIsDeleted("1");
		writeToFile();
		return ticket;
	}

	@Override
	public List<Ticket> getAll() {
		return new ArrayList<Ticket>(tickets.values());
	}

	@Override
	public Ticket retrieve(String id) {
		Ticket ticket = tickets.get(id);
		ticket.setIsDeleted("0");
		writeToFile();
		return ticket;
	}
	
	private String ticketCSVRepresentation(Ticket ticket) {
		StringBuilder ticketString = new StringBuilder();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String deleted = ticket.getIsDeleted() ? "1" : "0";
		ticketString.append(ticket.getId() + ";" + ticket.getManifestationId() + ";"
				+ ticket.getDateTime().format(formatter) + ";" + ticket.getPrice() + ";"
				+ ticket.getUser() + ";" + ticket.getTicketStatus() + ";"
				+ ticket.getTicketType() + ";" + ticket.getNumberOfTickets() + ";" + deleted);
        return ticketString.toString();
	}
	
	private void appendToFile(String line) {
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

	public void writeToFile() {
        File file = new File(contextPath + "/repositories/tickets.txt");

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            for(Ticket ticket : tickets.values()) {
            	pw.println(ticketCSVRepresentation(ticket));
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
					int numberOfTickets = Integer.parseInt(st.nextToken().trim());
					String deleted = st.nextToken().trim();
					Boolean isDeleted = false;
					if(deleted.equals("1")) {
						isDeleted = true;
					}
					tickets.put(id, new Ticket(id, manifestation, maintenance, price, user, status, type, numberOfTickets, isDeleted));
					
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

	
//	public void updateTicket(String oldUser, String newUser) {
//		ArrayList<Ticket> ticketsUser = new ArrayList<Ticket>();
//		for(Iterator<Map.Entry<String, Ticket>> it = tickets.entrySet().iterator(); it.hasNext(); ) {
//		    Map.Entry<String, Ticket> entry = it.next();
//		    if(entry.getValue().getUser().equals(oldUser)) {
//		    	Ticket t = entry.getValue();
//		        it.remove();
//		        ticketsUser.add(t);
//		    }
//		}
//		for (Ticket ticket : ticketsUser) {
//			ticket.setUser(newUser);
//			tickets.put(ticket.getId(), ticket);
//		}
//		writeToFile();
//		
//	}
}
