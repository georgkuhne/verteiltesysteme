import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Geometry;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.micromata.opengis.kml.v_2_2_0.PolyStyle;
import de.micromata.opengis.kml.v_2_2_0.Polygon;
import de.micromata.opengis.kml.v_2_2_0.Style;

public class ProgressTransformation {
	private ArrayList<Point> schulen = new ArrayList<Point>();
	ArrayList<Folder> schulenexport = new ArrayList<Folder>();

	ArrayList<Folder> bundesStrassenexport = new ArrayList<Folder>();
	ArrayList<LineString> bundesStrassen = new ArrayList<LineString>();

	ArrayList<Polygon> altlasten = new ArrayList<Polygon>();
	ArrayList<Folder> altlastenexport = new ArrayList<Folder>();

	private Kml kmlimportall;
	private Kml kmlexport;
	private Style styledachSchulen;
	private Style stylewandSchulen;
	private Style styledachBundesStrassen;
	private Style stylewandBundesStrassen;
	private Style styledachAltlasten;
	private Style stylewandAltlasten;

	private ProgressTransformation() {
	}

	private static ProgressTransformation instance = new ProgressTransformation();

	public static ProgressTransformation getInstance() {
		return instance;
	}

	public void excecute() {
		umnmrshallSchulen();
		umnmrshallBundesstrassen();
		umarshallAltlasten();
		kmlexport = KmlFactory.createKml();
		Document exportdoc = new Document();
		File input = new File(Model.getInstance().getInputAlle());
		kmlimportall = Kml.unmarshal(input);
		Document doc = (Document) kmlimportall.getFeature();
		exportdoc.setId(doc.getId());
		List<Feature> listfeatures = doc.getFeature();
		Style styledach = exportdoc.createAndAddStyle();

		styledach.setId("dachstyle");
		PolyStyle polystyledach = styledach.createAndSetPolyStyle();

		polystyledach.setColor(Model.getInstance().getDachfarbeAlle());
		styledach.createAndSetLineStyle().setColor(
				Model.getInstance().getDachfarbeAlle());
		styledach.setPolyStyle(polystyledach);
		Style stylewand = exportdoc.createAndAddStyle();
		stylewand.setId("wandstyle");
		PolyStyle polystylewand = stylewand.createAndSetPolyStyle();
		polystylewand.setColor(Model.getInstance().getWandFarbeAlle());
		polystylewand.setFill(true);
		stylewand.setPolyStyle(polystylewand);
		stylewand.createAndSetLineStyle().setColor(
				Model.getInstance().getWandFarbeAlle());
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
				if (isSchule(dachgeometry) != -1) {
					addSchule(gebaeudefolder.clone());
				}
				if (isHouseNeareBundesStrasse(dachgeometry) != -1) {
					addHouseBundestrasse(gebaeudefolder.clone());
				}
				if (isHouseonAltlasten(dachgeometry) != -1) {
					addHouseToAltlasten(gebaeudefolder.clone());

				}

			}
		}

		kmlexport.setFeature(exportdoc);
		File exportfile = new File(Model.getInstance().getOutputAlle()
				+ File.separator + KeyContainer.filenameAlle);
		try {
			exportfile.createNewFile();
			kmlexport.marshal(exportfile);
			saveSchulen();
			saveBundesStrassen();
			saveAltlasten();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void addHouseToAltlasten(Folder gebaeudefolder) {
		System.err.println("ALTLAST added");
		Placemark dach = (Placemark) gebaeudefolder.getFeature().get(0);
		dach.setStyleUrl(styledachAltlasten.getId());
		Placemark wand = (Placemark) gebaeudefolder.getFeature().get(1);
		wand.setStyleUrl(styledachAltlasten.getId());
		altlastenexport.add(gebaeudefolder);
	}

	private int isHouseonAltlasten(MultiGeometry dachgeometry) {
		int ret = -1;

		List<Geometry> list = dachgeometry.getGeometry();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Polygon geometry = (Polygon) iterator.next();
			List<Coordinate> coords = geometry.getOuterBoundaryIs()
					.getLinearRing().getCoordinates();

			for (Iterator iterator2 = coords.iterator(); iterator2.hasNext();) {
				Coordinate coordinate = (Coordinate) iterator2.next();
				for (int i = 0; i < altlasten.size(); i++) {

					if (contains(altlasten.get(i).getOuterBoundaryIs()
							.getLinearRing().getCoordinates(), coordinate))
						return i;

				}

			}

		}
		return ret;

	}

	private void addHouseBundestrasse(Folder gebaeudefolder) {
		System.err.println("Bundesstrasse added");
		Placemark dach = (Placemark) gebaeudefolder.getFeature().get(0);
		dach.setStyleUrl(styledachBundesStrassen.getId());
		Placemark wand = (Placemark) gebaeudefolder.getFeature().get(1);
		wand.setStyleUrl(stylewandBundesStrassen.getId());
		bundesStrassenexport.add(gebaeudefolder);
	}

	private void umarshallAltlasten() {
		styledachAltlasten = new Style();
		styledachAltlasten.setId("dachstyle");
		PolyStyle polystyledach = styledachAltlasten.createAndSetPolyStyle();

		polystyledach.setColor(Model.getInstance().getDachfarbeAltlasten());
		styledachAltlasten.createAndSetLineStyle().setColor(
				Model.getInstance().getDachfarbeAltlasten());
		styledachAltlasten.setPolyStyle(polystyledach);

		stylewandAltlasten = new Style();
		stylewandAltlasten.setId("wandstyle");
		PolyStyle polystylewand = stylewandAltlasten.createAndSetPolyStyle();
		polystylewand.setColor(Model.getInstance().getWandfarbeAltlasten());
		polystylewand.setFill(true);
		stylewandAltlasten.setPolyStyle(polystylewand);
		stylewandAltlasten.createAndSetLineStyle().setColor(
				Model.getInstance().getWandfarbeAltlasten());

		Kml importBundes = KmlFactory.createKml();
		File input = new File(Model.getInstance().getInputAltlasten());
		importBundes = Kml.unmarshal(input);
		Document doc = (Document) importBundes.getFeature();
		List<Feature> listfeatures = doc.getFeature();
		if (listfeatures.get(0) instanceof Folder) {
			Folder fold = (Folder) listfeatures.get(0);
			listfeatures = fold.getFeature();
		}
		for (Iterator iterator = listfeatures.iterator(); iterator.hasNext();) {
			Feature feature = (Feature) iterator.next();

			if (feature instanceof Placemark) {
				Placemark placemark = (Placemark) feature;
				altlasten.add((Polygon) placemark.getGeometry());
			}
		}

	}

	private void umnmrshallBundesstrassen() {
		styledachBundesStrassen = new Style();
		styledachBundesStrassen.setId("dachstyle");
		PolyStyle polystyledach = styledachBundesStrassen
				.createAndSetPolyStyle();

		polystyledach.setColor(Model.getInstance().getDachfarbeBundestrassen());
		styledachBundesStrassen.createAndSetLineStyle().setColor(
				Model.getInstance().getDachfarbeBundestrassen());
		styledachBundesStrassen.setPolyStyle(polystyledach);
		stylewandBundesStrassen = new Style();
		stylewandBundesStrassen.setId("wandstyle");
		PolyStyle polystylewand = stylewandBundesStrassen
				.createAndSetPolyStyle();
		polystylewand
				.setColor(Model.getInstance().getWandfarbeBundesstrassen());
		polystylewand.setFill(true);
		stylewandBundesStrassen.setPolyStyle(polystylewand);
		stylewandBundesStrassen.createAndSetLineStyle().setColor(
				Model.getInstance().getWandfarbeBundesstrassen());

		Kml importBundes = KmlFactory.createKml();
		File input = new File(Model.getInstance().getInputBundesstrassen());
		importBundes = Kml.unmarshal(input);
		Document doc = (Document) importBundes.getFeature();
		List<Feature> listfeatures = doc.getFeature();
		if (listfeatures.get(0) instanceof Folder) {
			Folder fold = (Folder) listfeatures.get(0);
			listfeatures = fold.getFeature();
		}
		for (Iterator iterator = listfeatures.iterator(); iterator.hasNext();) {
			Feature feature = (Feature) iterator.next();

			if (feature instanceof Placemark) {
				Placemark placemark = (Placemark) feature;
				LineString g = (LineString) placemark.getGeometry();
				bundesStrassen.add(g);
				System.err.println();
			}
		}
	}

	private void saveAltlasten() {
		Kml kmlexport = KmlFactory.createKml();
		Document exportdoc = new Document();
		exportdoc.getStyleSelector().add(styledachAltlasten);
		exportdoc.getStyleSelector().add(stylewandAltlasten);
		Iterator<Folder> it = altlastenexport.iterator();
		while (it.hasNext())
			exportdoc.addToFeature(it.next());
		kmlexport.setFeature(exportdoc);
		File exportfile = new File(Model.getInstance().getOutputAltlasten()
				+ File.separator + KeyContainer.fileNameAltlasten);
		try {
			exportfile.createNewFile();
			kmlexport.marshal(exportfile);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void saveSchulen() {
		Kml kmlexport = KmlFactory.createKml();
		Document exportdoc = new Document();
		exportdoc.getStyleSelector().add(styledachSchulen);
		exportdoc.getStyleSelector().add(stylewandSchulen);
		Iterator<Folder> it = schulenexport.iterator();
		while (it.hasNext())
			exportdoc.addToFeature(it.next());
		kmlexport.setFeature(exportdoc);
		File exportfile = new File(Model.getInstance().getOutputSchulen()
				+ File.separator + KeyContainer.fileNameSchulen);
		try {
			exportfile.createNewFile();
			kmlexport.marshal(exportfile);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addSchule(Folder gebaeudefolder) {
		System.err.println("SCHULEADDED");
		Placemark dach = (Placemark) gebaeudefolder.getFeature().get(0);
		dach.setStyleUrl(styledachSchulen.getId());
		Placemark wand = (Placemark) gebaeudefolder.getFeature().get(1);
		wand.setStyleUrl(stylewandSchulen.getId());
		schulenexport.add(gebaeudefolder);
	}

	private int isHouseNeareBundesStrasse(MultiGeometry dachgeometry) {
		int ret = -1;

		List<Geometry> list = dachgeometry.getGeometry();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Polygon geometry = (Polygon) iterator.next();
			List<Coordinate> coords = geometry.getOuterBoundaryIs()
					.getLinearRing().getCoordinates();

			for (int i = 0; i < bundesStrassen.size(); i++) {
				LineString line = bundesStrassen.get(i);
				if (isPolygoneNearBundesstrasse(coords, line))
					return i;
			}

		}
		return ret;
	}

	private boolean isPolygoneNearBundesstrasse(List<Coordinate> coords,
			LineString line) {
		List<Coordinate> linecoords = line.getCoordinates();
		Iterator iterator = linecoords.iterator();
		Coordinate coordinate0 = (Coordinate) iterator.next();
		Coordinate coordinate1;
		for (; iterator.hasNext();) {
			coordinate1 = (Coordinate) iterator.next();

			Point2D.Double r1 = new Point2D.Double();
			r1.x = coordinate0.getLongitude();
			r1.y = coordinate0.getLatitude();
			Point2D.Double r2 = new Point2D.Double();
			r2.x = coordinate1.getLongitude();
			r2.y = coordinate1.getLatitude();
			Line2D.Double line2d = new Line2D.Double(r1, r2);
			for (Iterator iterator2 = coords.iterator(); iterator2.hasNext();) {
				Coordinate coordinate = (Coordinate) iterator2.next();
				Point2D.Double point = new Point2D.Double();
				point.x = coordinate.getLongitude();
				point.y = coordinate.getLatitude();

				double distance = line2d.ptLineDist(point);
				if (distance < 0.0001)
					return (distance <= 0.0001);
			}

			coordinate0 = coordinate1;
		}
		return false;
	}

	private int isSchule(MultiGeometry dachgeometry) {
		int ret = -1;

		List<Geometry> list = dachgeometry.getGeometry();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Polygon geometry = (Polygon) iterator.next();
			List<Coordinate> coords = geometry.getOuterBoundaryIs()
					.getLinearRing().getCoordinates();

			for (int i = 0; i < schulen.size(); i++) {
				Point point = schulen.get(i);
				if (contains(coords, point.getCoordinates().get(0)))
					return i;
			}

		}
		return ret;
	}

	public class PointModel {

		public PointModel(double x, double y) {
			super();
			this.x = x;
			this.y = y;
		}

		public double x;
		public double y;

	}

	public boolean contains(List<Coordinate> coords, Coordinate point) {
		PointModel[] points = new PointModel[coords.size()];

		for (int i = 0; i < coords.size(); i++) {
			points[i] = new PointModel(coords.get(i).getLongitude(), coords
					.get(i).getLatitude());

		}
		PointModel test = new PointModel(point.getLongitude(),
				point.getLatitude());

		int i;
		int j;
		boolean result = false;
		for (i = 0, j = points.length - 1; i < points.length; j = i++) {
			if ((points[i].y > test.y) != (points[j].y > test.y)
					&& (test.x < (points[j].x - points[i].x)
							* (test.y - points[i].y)
							/ (points[j].y - points[i].y) + points[i].x)) {
				result = !result;
			}
		}
		return result;
	}

	private void umnmrshallSchulen() {

		styledachSchulen = new Style();

		styledachSchulen.setId("dachstyle");
		PolyStyle polystyledach = styledachSchulen.createAndSetPolyStyle();

		polystyledach.setColor(Model.getInstance().getDachfarbeSchulen());
		styledachSchulen.createAndSetLineStyle().setColor(
				Model.getInstance().getDachfarbeSchulen());
		styledachSchulen.setPolyStyle(polystyledach);
		stylewandSchulen = new Style();
		stylewandSchulen.setId("wandstyle");
		PolyStyle polystylewand = stylewandSchulen.createAndSetPolyStyle();
		polystylewand.setColor(Model.getInstance().getWandfarbeSchulen());
		polystylewand.setFill(true);
		stylewandSchulen.setPolyStyle(polystylewand);
		stylewandSchulen.createAndSetLineStyle().setColor(
				Model.getInstance().getWandfarbeSchulen());

		Kml importSchulen = KmlFactory.createKml();
		File input = new File(Model.getInstance().getInputSchulen());
		importSchulen = Kml.unmarshal(input);
		Document doc = (Document) importSchulen.getFeature();
		List<Feature> listfeatures = doc.getFeature();
		if (listfeatures.get(0) instanceof Folder) {
			Folder fold = (Folder) listfeatures.get(0);
			listfeatures = fold.getFeature();
		}
		for (Iterator iterator = listfeatures.iterator(); iterator.hasNext();) {
			Feature feature = (Feature) iterator.next();

			if (feature instanceof Placemark) {
				Placemark placemark = (Placemark) feature;
				Point p = (Point) placemark.getGeometry();
				schulen.add(p);
			}
		}
	}

	private void saveBundesStrassen() {
		Kml kmlexport = KmlFactory.createKml();
		Document exportdoc = new Document();
		exportdoc.getStyleSelector().add(styledachBundesStrassen);
		exportdoc.getStyleSelector().add(stylewandBundesStrassen);
		Iterator<Folder> it = bundesStrassenexport.iterator();
		while (it.hasNext())
			exportdoc.addToFeature(it.next());
		kmlexport.setFeature(exportdoc);
		File exportfile = new File(Model.getInstance().getOutputBundestrassen()
				+ File.separator + KeyContainer.fileNameBundesStraﬂen);
		try {
			exportfile.createNewFile();
			kmlexport.marshal(exportfile);

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
		// System.err.println(winkel);
		if (winkel < 90)
			return true;
		else
			return false;
	}
}