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

import beans.Customer;
import beans.Manifestation;
import beans.Seller;
import beans.Ticket;

public class SellerDAO {
	private Map<String, Seller> sellers = new HashMap<>();
	private String contextPath;
	private ManifestationDAO manifestationDAO;
	
	public SellerDAO(String contextPath, ManifestationDAO manifestationDAO) {
		this.contextPath = contextPath;
		this.manifestationDAO = manifestationDAO;
		loadSellers();
	}
	
	public Seller getSeller(String id) {
		return sellers.get(id);
	}
	
	public List<Manifestation> add(Manifestation manifestation, String user) {
		if(manifestationDAO.idExists(manifestation.getId())) {
			return null;
		}
		if(!manifestationDAO.checkManifestationMaintainance(manifestation.getDate(), manifestation.getLocation(), manifestation.getId())) {
			return null;
		}
		manifestationDAO.getManifestations().put(manifestation.getId(), manifestation);
		Seller seller = getSeller(user);
		seller.getManifestations().add(manifestation);
		write();

		manifestationDAO.append(manifestationDAO.getManifestationLine(manifestation));
		return manifestationDAO.getActiveManifestations();
	}
	
	private void loadSellers() {
		BufferedReader reader = null;
		try {
			File file = new File(contextPath + "/repositories/sellers.txt");
			reader = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while((line = reader.readLine()) != null) {
				line = line.trim();
				if(line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while(st.hasMoreTokens()) {
					String username = st.nextToken().trim();
					ArrayList<Manifestation> manifestationArray = new ArrayList<Manifestation>();
					
					String manifestations = st.nextToken().trim();
					if(!manifestations.equals("")) {
						StringTokenizer st2 = new StringTokenizer(manifestations, ":");
						while(st2.hasMoreTokens()) {
							String manifestationId = st2.nextToken().trim();
							Manifestation manifestation = manifestationDAO.getManifestation(manifestationId);
							manifestationArray.add(manifestation);
						}
					}
					
					sellers.put(username, new Seller(username, manifestationArray));
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

	public void append(String sellerLine) {
		File file = new File(contextPath + "/repositories/sellers.txt");

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            	pw.println(sellerLine);
            
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
	
	public void write() {
		File file = new File(contextPath + "/repositories/sellers.txt");

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            for(Seller seller : sellers.values()) {
            	pw.println(getSellerLine(seller));
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

	private String getSellerLine(Seller seller) {
		// TODO Auto-generated method stub
		StringBuilder sellerString = new StringBuilder(); 
		sellerString.append(seller.getUsername() + ";");
		if(seller.getManifestations().size() == 0) {
			sellerString.append(" ;");
		} else {
			for(Manifestation m : seller.getManifestations()) {
				sellerString.append(m.getId() + ":");
			}
			sellerString.append(";");
		}
        return sellerString.toString();
	}

	public Map<String, Seller> getSellers() {
		return sellers;
	}

	public void setSellers(Map<String, Seller> sellers) {
		this.sellers = sellers;
	}
	
}
