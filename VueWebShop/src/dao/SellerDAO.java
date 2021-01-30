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
import java.util.Map;
import java.util.StringTokenizer;

import beans.Manifestation;
import beans.Seller;

public class SellerDAO {
	private Map<String, Seller> sellers = new HashMap<>();
	private String contextPath;
	private ManifestationDAO manifestationDAO;
	
	public SellerDAO(String contextPath, ManifestationDAO manifestationDAO) {
		this.contextPath = contextPath;
		this.manifestationDAO = manifestationDAO;
		loadSellers();
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
							Manifestation manifestation = manifestationDAO.getOneManifestation(manifestationId);
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

	public Map<String, Seller> getSellers() {
		return sellers;
	}

	public void setSellers(Map<String, Seller> sellers) {
		this.sellers = sellers;
	}
	
}
