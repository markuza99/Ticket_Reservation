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

import beans.Seller;
import dao.interfaces.ISellerDAO;

public class SellerDAO implements ISellerDAO {
	private Map<String, Seller> sellers = new HashMap<>();
	private String contextPath;
	
	public SellerDAO(String contextPath) {
		this.contextPath = contextPath;
		loadSellers();
	}
	
	@Override
	public Seller create(Seller seller) {
		if(read(seller.getUsername()) != null) {
            return null;
		}
		sellers.put(seller.getUsername(), seller);
		appendToFile(sellerCSVRepresentation(seller));
		return seller;
	}

	@Override
	public Seller read(String id) {
		return sellers.get(id);
	}

	@Override
	public Seller update(Seller seller) {
		sellers.put(seller.getUsername(), seller);
		writeToFile();
		return seller;
	}

	@Override
	public Seller delete(String id) {
		return null;
	}

	@Override
	public List<Seller> getAll() {
		return new ArrayList<Seller>(sellers.values());
	}

	@Override
	public Seller retrieve(String username) {
		return null;
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
					ArrayList<String> manifestationArray = new ArrayList<String>();
					
					String manifestations = st.nextToken().trim();
					if(!manifestations.equals("")) {
						StringTokenizer st2 = new StringTokenizer(manifestations, ":");
						while(st2.hasMoreTokens()) {
							String manifestationId = st2.nextToken().trim();
							manifestationArray.add(manifestationId);
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

	public void appendToFile(String sellerLine) {
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
	
	public void writeToFile() {
		File file = new File(contextPath + "/repositories/sellers.txt");

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            for(Seller seller : sellers.values()) {
            	pw.println(sellerCSVRepresentation(seller));
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

	private String sellerCSVRepresentation(Seller seller) {
		StringBuilder sellerString = new StringBuilder(); 
		sellerString.append(seller.getUsername() + ";");
		if(seller.getManifestations().size() == 0) {
			sellerString.append(" ;");
		} else {
			for(String m : seller.getManifestations()) {
				sellerString.append(m + ":");
			}
			sellerString.append(";");
		}
        return sellerString.toString();
	}

	@Override
	public Seller getSellerForManifestation(String id) {
		// TODO Auto-generated method stub
		for(Seller s : sellers.values()) {
			if(s.getManifestations().contains(id)) return s;
		}
		return null;
	}
}
