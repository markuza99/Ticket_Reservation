package dao;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import beans.Manifestation;
import beans.ManifestationType;
import beans.Status;
import dao.interfaces.IManifestationDAO;

public class ManifestationDAO implements IManifestationDAO {
	private Map<String, Manifestation> manifestations = new HashMap<>();
	private Map<String, String> images = new HashMap<>();
	private String contextPath;
	
	public ManifestationDAO(String contextPath) {
		this.contextPath = contextPath;
		loadImages();
		loadManifestations();
	}

	@Override
	public Manifestation create(Manifestation manifestation) {
		manifestations.put(manifestation.getId(), manifestation);
		images.put(manifestation.getId(), manifestation.getImage());
		appendToFile(manifestationCSVRepresentation(manifestation));
		appendImageToFile(imageCSVRepresentation(manifestation.getId(), manifestation.getImage()));
		return manifestation;
	}

	@Override
	public Manifestation read(String id) {
		return manifestations.get(id);
	}

	@Override
	public Manifestation update(Manifestation manifestation) {
		manifestations.put(manifestation.getId(), manifestation);
		writeToFile();
		return manifestation;
	}

	@Override
	public Manifestation delete(String id) {
		Manifestation manifestation = manifestations.get(id);
		manifestation.setIsDeleted("1");
		writeToFile();
		return manifestation;
	}

	@Override
	public List<Manifestation> getAll() {
		return new ArrayList<Manifestation>(manifestations.values());
	}

	@Override
	public Manifestation retrieve(String id) {
		Manifestation manifestation = manifestations.get(id);
		manifestation.setIsDeleted("0");
		writeToFile();
		return manifestation;
	}


	public String manifestationCSVRepresentation(Manifestation manifestation) {
		StringBuilder manifestationString = new StringBuilder();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		manifestationString.append(manifestation.getId() + ";" + manifestation.getName() + ";"
				+ manifestation.getType() + ";" + manifestation.getNumberOfSeats() + ";"
				+ manifestation.getRemainingNumberOfSeats() + ";" + manifestation.getStartTime().format(formatter) + ";"
				+ manifestation.getEndTime().format(formatter) + ";"
				+ manifestation.getTicketPrice() + ";");
		if(manifestation.getStatus() == Status.ACTIVE) {
			manifestationString.append("1;");
		} else {
			manifestationString.append("0;");
		}
		manifestationString.append(manifestation.getLocation() + ";");
		if(manifestation.isChecked()) {
			manifestationString.append("1;");
		} else {
			manifestationString.append("0;");
		}
		if(manifestation.getIsDeleted()) {
			manifestationString.append("1;");
		} else {
			manifestationString.append("0;");
		}
        return manifestationString.toString();
	}
	
	public void writeToFile() {
		File file = new File(contextPath + "/repositories/manifestations.txt");

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            for(Manifestation manifestation : manifestations.values()) {
            	pw.println(manifestationCSVRepresentation(manifestation));
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
	
	private void loadManifestations() {
		BufferedReader reader = null;
		try {
			File file = new File(contextPath + "/repositories/manifestations.txt");
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
					String name = st.nextToken().trim();
					ManifestationType type = ManifestationType.valueOf(st.nextToken().trim());
					int numberOfSeats = Integer.parseInt(st.nextToken().trim());
					int remainingNumberOfSeats = Integer.parseInt(st.nextToken().trim());
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
					LocalDateTime startTime = LocalDateTime.parse(st.nextToken().trim(), formatter);
					LocalDateTime endTime = LocalDateTime.parse(st.nextToken().trim(), formatter);
					int ticketPrice = Integer.parseInt(st.nextToken().trim());
					Status status = (Integer.parseInt(st.nextToken().trim())) == 1 ? 
							Status.ACTIVE : Status.INACTIVE;
					String location = st.nextToken().trim();
					String imagePath = images.get(id);
					String checked = st.nextToken().trim();
					String deleted = st.nextToken().trim();
					Boolean isDeleted = false;
					boolean isChecked = false;
					if(deleted.equals("1")) {
						isDeleted = true;
					}
					if(checked.equals("1")) {
						isChecked = true;
					}
					manifestations.put(id, new Manifestation(
							id, name, type, numberOfSeats,
							remainingNumberOfSeats, startTime, endTime, ticketPrice,
							status, location, imagePath, isChecked, isDeleted));
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
	
	private void loadImages() {
		BufferedReader reader = null;
		try {
			File file = new File(contextPath + "/repositories/images.txt");
			reader = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while((line = reader.readLine()) != null) {
				line = line.trim();
				if(line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, "|");
				while(st.hasMoreTokens()) {
					String id = st.nextToken().trim();
					String image = st.nextToken().trim();
					images.put(id, image);
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

	public void appendToFile(String line) {
		File file = new File(contextPath + "/repositories/manifestations.txt");

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
	
	public void appendImageToFile(String line) {
		File file = new File(contextPath + "/repositories/images.txt");

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
	
	public void writeImagesToFile() {
		File file = new File(contextPath + "/repositories/images.txt");

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            for (Map.Entry<String,String> entry : images.entrySet())
            	pw.println(imageCSVRepresentation(entry.getKey(), entry.getValue()));
            
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

	private String imageCSVRepresentation(String manifestationId, String image64base) {
        return manifestationId + "|" + image64base;
	}

	@Override
	public void updateImage(Manifestation manifestation) {
		images.put(manifestation.getId(), manifestation.getImage());
		writeImagesToFile();
	}

}
