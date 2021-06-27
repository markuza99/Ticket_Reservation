package dao.interfaces;

import java.awt.image.BufferedImage;
import java.util.List;

import beans.Manifestation;

public interface IManifestationDAO extends CRUD<Manifestation, String> {
	List<Manifestation> getAll();
	Manifestation retrieve(String id);
	void appendImageToFile(String string);
	void updateImage(Manifestation manifestation);
}
