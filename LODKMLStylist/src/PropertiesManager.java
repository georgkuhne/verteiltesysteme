import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {
	private final static PropertiesManager instance = new PropertiesManager();
	Properties properties;

	public Properties getProperties() {
		return properties;
	}

	public boolean readProperties() {
		boolean ret = false;
		properties = new Properties();
		try {
			// load a properties file from class path, inside static method
			InputStream stream = new FileInputStream(new File(
					"config.properties"));
			properties.load(stream);
			ret = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static PropertiesManager getInstance() {
		return instance;
	}

	public boolean mapPropertiesToModel() {
		boolean ret = false;
		try {

			Model.getInstance().setDachfarbeAlle(
					properties.getProperty(KeyContainer.farbedach_Alle));

			Model.getInstance().setWandFarbeAlle(
					properties.getProperty(KeyContainer.farbewand_Alle));

			Model.getInstance().setInputAlle(
					properties.getProperty(KeyContainer.input_Alle));
			Model.getInstance().setOutputAlle(
					properties.getProperty(KeyContainer.output_Alle));

			/*************************************************************************/

			Model.getInstance().setDachfarbeSchulen(
					properties.getProperty(KeyContainer.farbedach_Schule));

			Model.getInstance().setWandfarbeSchulen(
					properties.getProperty(KeyContainer.farbewand_Schule));

			Model.getInstance().setInputSchulen(
					properties.getProperty(KeyContainer.input_Schule));
			Model.getInstance().setOutputSchulen(
					properties.getProperty(KeyContainer.output_Schule));
			/*************************************************************************/

			Model.getInstance().setDachfarbeAltlasten(
					properties.getProperty(KeyContainer.farbedach_Altlasten));

			Model.getInstance().setWandfarbeAltlasten(
					properties.getProperty(KeyContainer.farbewand_Altlasten));

			Model.getInstance().setInputAltlasten(
					properties.getProperty(KeyContainer.input_Altlasten));
			Model.getInstance().setOutputAltlasten(
					properties.getProperty(KeyContainer.output_Altlasten));

			/*************************************************************************/

			Model.getInstance()
					.setDachfarbeBundestrassen(
							properties
									.getProperty(KeyContainer.farbedach_Bundestraﬂen));

			Model.getInstance()
					.setWandfarbeBundesstrassen(
							properties
									.getProperty(KeyContainer.farbewand_Bundestraﬂen));

			Model.getInstance().setInputBundesstrassen(
					properties.getProperty(KeyContainer.input_Bundestraﬂen));
			Model.getInstance().setOutputBundestrassen(
					properties.getProperty(KeyContainer.output_Bundestraﬂen));
			ret = true;
		} catch (Exception e) {
		}
		return ret;
	}
}
