import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Geometry;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.PolyStyle;
import de.micromata.opengis.kml.v_2_2_0.Polygon;
import de.micromata.opengis.kml.v_2_2_0.Style;

public class ProgressTransformation {
	private Kml kmlimport;
	private Kml kmlexport;

	private ProgressTransformation() {
	}

	private static ProgressTransformation instance = new ProgressTransformation();

	public static ProgressTransformation getInstance() {
		return instance;
	}

	public void excecute(Color colorDach, Color colorWand,
			File exportDirectory, String exportFilename, File importfile) {

		kmlexport = KmlFactory.createKml();
		Document exportdoc = new Document();
		String scolorDach = String.format("%02x%02x%02x%02x", 255,
				colorDach.getBlue(), colorDach.getGreen(), colorDach.getRed());
		String scolorWand = String.format("%02x%02x%02x%02x", 255,
				colorWand.getBlue(), colorWand.getGreen(), colorWand.getRed());
		kmlimport = Kml.unmarshal(importfile);
		Document doc = (Document) kmlimport.getFeature();
		exportdoc.setId(doc.getId());
		List<Feature> listfeatures = doc.getFeature();
		Style styledach = exportdoc.createAndAddStyle();

		styledach.setId("dachstyle");
		PolyStyle polystyledach = styledach.createAndSetPolyStyle();

		polystyledach.setColor(scolorDach);
		styledach.createAndSetLineStyle().setColor(scolorDach);
		styledach.setPolyStyle(polystyledach);
		Style stylewand = exportdoc.createAndAddStyle();
		stylewand.setId("wandstyle");
		PolyStyle polystylewand = stylewand.createAndSetPolyStyle();
		polystylewand.setColor(scolorWand);
		polystylewand.setFill(true);
		stylewand.setPolyStyle(polystylewand);
		stylewand.createAndSetLineStyle().setColor(scolorWand);
		if (listfeatures.get(0) instanceof Folder) {
			Folder fold = (Folder) listfeatures.get(0);
			listfeatures = fold.getFeature();
		}

		for (Iterator iterator = listfeatures.iterator(); iterator.hasNext();) {
			Feature feature = (Feature) iterator.next();

			if (feature instanceof Placemark) {
				Placemark placemark = (Placemark) feature;
				Folder gebaeudefolder = exportdoc.createAndAddFolder();
				gebaeudefolder.setId(placemark.getId());

				MultiGeometry geometry = (MultiGeometry) placemark
						.getGeometry();

				Placemark dach = gebaeudefolder.createAndAddPlacemark();
				dach.setStyleUrl(styledach.getId());
				Placemark wand = gebaeudefolder.createAndAddPlacemark();
				wand.setStyleUrl(stylewand.getId());
				MultiGeometry dachgeometry = dach.createAndSetMultiGeometry();
				MultiGeometry wandgeometry = wand.createAndSetMultiGeometry();
				List<Geometry> listgeometry = geometry.getGeometry();
				for (Iterator iterator2 = listgeometry.iterator(); iterator2
						.hasNext();) {
					Geometry geometrypoly = (Geometry) iterator2.next();
					if (geometrypoly instanceof Polygon) {
						Polygon polygon = (Polygon) geometrypoly;
						if (isDach(polygon)) {
							dachgeometry.addToGeometry(polygon);
						} else {
							wandgeometry.addToGeometry(polygon);
						}

					}
				}
			}
		}

		Style s = new Style();
		kmlexport.setFeature(exportdoc);
		File exportfile = new File(exportDirectory.getAbsolutePath()
				+ File.separator + exportFilename);
		try {
			exportfile.createNewFile();
			kmlexport.marshal(exportfile);
			MessageBox msg = new MessageBox(Display.getCurrent()
					.getActiveShell());
			msg.setMessage("OK");
			msg.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean isDach(Polygon polygon) {
		List<Coordinate> coordinates = polygon.getOuterBoundaryIs()
				.getLinearRing().getCoordinates();

		Iterator<Coordinate> iter = coordinates.iterator();
		Coordinate x0 = iter.next();
		Coordinate x1 = iter.next();
		Coordinate x2 = iter.next();
		Coordinate x01, x02, normalenvector;
		x01 = new Coordinate(x1.getLongitude() - x0.getLongitude(),
				x1.getLatitude() - x0.getLatitude(), x1.getAltitude()
						- x0.getAltitude());
		x02 = new Coordinate(x2.getLongitude() - x0.getLongitude(),
				x2.getLatitude() - x0.getLatitude(), x2.getAltitude()
						- x0.getAltitude());

		// kreuzproduct
		normalenvector = new Coordinate(x01.getLatitude() * x02.getAltitude()
				- x0.getAltitude() * x0.getLatitude(), x01.getAltitude()
				* x02.getLongitude() - x01.getLongitude() * x02.getAltitude(),
				x01.getLongitude() * x02.getLatitude() - x01.getLatitude()
						* x02.getLongitude());

		Double winkel = Math.acos(normalenvector.getAltitude()
				* 10
				/ (Math.sqrt(normalenvector.getAltitude()
						* normalenvector.getAltitude()
						+ normalenvector.getLongitude()
						* normalenvector.getLongitude()
						+ normalenvector.getLatitude()
						* normalenvector.getLatitude()) * 10));
		winkel = (360 / (2 * Math.PI)) * winkel;
		System.err.println(winkel);
		if (winkel < 90)
			return true;
		else
			return false;
	}
}