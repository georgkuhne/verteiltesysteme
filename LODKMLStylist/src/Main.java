public class Main {

	public static void main(String[] args) {
		if (!PropertiesManager.getInstance().readProperties()) {
			System.err.println("cant read config.properties");
			System.exit(1);
		}
		if (!PropertiesManager.getInstance().mapPropertiesToModel()) {
			System.err.println("config.properties is corrupt");
			System.exit(1);
		}

		ProgressTransformation.getInstance().excecute();
	}

}
