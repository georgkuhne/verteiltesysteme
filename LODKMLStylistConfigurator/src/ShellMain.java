import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class ShellMain extends Shell {

	CompositeConfig cAlle;
	CompositeConfig cSchulen;
	CompositeConfig cAltlasten;
	CompositeConfig cBundestraﬂen;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			ShellMain shell = new ShellMain(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void open() {
		super.open();
		loadProperties();
	}

	/**
	 * Create the shell.
	 * 
	 * @param display
	 */

	public void loadProperties() {
		Properties prop = new Properties();
		try {
			// load a properties file from class path, inside static method
			InputStream stream = new FileInputStream(new File(
					"config.properties"));
			prop.load(stream);

			String colordach = prop.getProperty(KeyContainer.farbedach_Alle);
			String colorwand = prop.getProperty(KeyContainer.farbewand_Alle);
			String input = prop.getProperty(KeyContainer.input_Alle);
			String output = prop.getProperty(KeyContainer.output_Alle);
			cAlle.loadProperties(colordach, colorwand, input, output);

			colordach = prop.getProperty(KeyContainer.farbedach_Altlasten);
			colorwand = prop.getProperty(KeyContainer.farbewand_Altlasten);
			input = prop.getProperty(KeyContainer.input_Altlasten);
			output = prop.getProperty(KeyContainer.output_Altlasten);
			cAltlasten.loadProperties(colordach, colorwand, input, output);

			colordach = prop.getProperty(KeyContainer.farbedach_Bundestraﬂen);
			colorwand = prop.getProperty(KeyContainer.farbewand_Bundestraﬂen);
			input = prop.getProperty(KeyContainer.input_Bundestraﬂen);
			output = prop.getProperty(KeyContainer.output_Bundestraﬂen);
			cBundestraﬂen.loadProperties(colordach, colorwand, input, output);

			colordach = prop.getProperty(KeyContainer.farbedach_Schule);
			colorwand = prop.getProperty(KeyContainer.farbewand_Schule);
			input = prop.getProperty(KeyContainer.input_Schule);
			output = prop.getProperty(KeyContainer.output_Schule);
			cSchulen.loadProperties(colordach, colorwand, input, output);

		} catch (Exception ex) {
			String colordach = "ff8000ff";
			String colorwand = "ffcccccc";
			String input = "C:" + File.separator + "verteiltesystemeprojekt"
					+ File.separator + "kmlexport" + File.separator
					+ "lod1.kml";
			String output = "C:" + File.separator + "verteiltesystemeprojekt"
					+ File.separator + "ergebnisse";

			cAlle.loadProperties(colordach, colorwand, input, output);

			colordach = "ff8000ff";
			input = "C:" + File.separator + "verteiltesystemeprojekt"
					+ File.separator + "kmlexport" + File.separator
					+ "Altlasten.kml";

			cAltlasten.loadProperties(colordach, colorwand, input, output);

			colordach = "ff8000ff";
			input = "C:" + File.separator + "verteiltesystemeprojekt"
					+ File.separator + "kmlexport" + File.separator
					+ "Bundestrassen.kml";

			cBundestraﬂen.loadProperties(colordach, colorwand, input, output);

			colordach = "ff8000ff";
			input = "C:" + File.separator + "verteiltesystemeprojekt"
					+ File.separator + "kmlexport" + File.separator
					+ "Schulen.kml";

			cSchulen.loadProperties(colordach, colorwand, input, output);
			ex.printStackTrace();
		}
	}

	public ShellMain(Display display) {
		super(display, SWT.SHELL_TRIM);
		setLayout(new FormLayout());

		TabFolder tabFolder = new TabFolder(this, SWT.NONE);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.bottom = new FormAttachment(80);
		fd_tabFolder.right = new FormAttachment(0, 466);
		fd_tabFolder.top = new FormAttachment(0);
		fd_tabFolder.left = new FormAttachment(0);
		tabFolder.setLayoutData(fd_tabFolder);

		cAlle = new CompositeConfig(tabFolder, SWT.DEFAULT);
		cSchulen = new CompositeConfig(tabFolder, SWT.DEFAULT);
		cBundestraﬂen = new CompositeConfig(tabFolder, SWT.DEFAULT);
		cAltlasten = new CompositeConfig(tabFolder, SWT.DEFAULT);

		TabItem tbtmAlleGebude = new TabItem(tabFolder, SWT.NONE);
		tbtmAlleGebude.setText("Alle Geb\u00E4ude");
		tbtmAlleGebude.setControl(cAlle);

		TabItem tbtmAltlasten = new TabItem(tabFolder, SWT.NONE);
		tbtmAltlasten.setText("Altlasten");
		tbtmAltlasten.setControl(cAltlasten);
		TabItem tbtmSchulen = new TabItem(tabFolder, SWT.NONE);
		tbtmSchulen.setText("Schulen");
		tbtmSchulen.setControl(cSchulen);

		TabItem tbtmGebudeAnBundestraen = new TabItem(tabFolder, SWT.NONE);
		tbtmGebudeAnBundestraen.setText("Geb\u00E4ude an Bundestra\u00DFen");
		tbtmGebudeAnBundestraen.setControl(cBundestraﬂen);
		Button btnSpeichern = new Button(this, SWT.NONE);
		FormData fd_btnSpeichern = new FormData();
		fd_btnSpeichern.bottom = new FormAttachment(100, -10);
		fd_btnSpeichern.right = new FormAttachment(100, -24);

		btnSpeichern.setLayoutData(fd_btnSpeichern);
		btnSpeichern.setText("Speichern");
		btnSpeichern.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				saveButtonClicked();

			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		createContents();
	}

	protected void saveButtonClicked() {
		Properties prop = new Properties();
		OutputStream output = null;
		try {

			output = new FileOutputStream("config.properties");

			// DACHFARBE
			prop.setProperty(KeyContainer.farbedach_Alle, cAlle.getColorDach());
			prop.setProperty(KeyContainer.farbedach_Altlasten,
					cAltlasten.getColorDach());
			prop.setProperty(KeyContainer.farbedach_Bundestraﬂen,
					cBundestraﬂen.getColorDach());
			prop.setProperty(KeyContainer.farbedach_Schule,
					cSchulen.getColorDach());
			// WANDFARBE

			prop.setProperty(KeyContainer.farbewand_Alle, cAlle.getColorWand());
			prop.setProperty(KeyContainer.farbewand_Altlasten,
					cAltlasten.getColorWand());
			prop.setProperty(KeyContainer.farbewand_Bundestraﬂen,
					cBundestraﬂen.getColorWand());
			prop.setProperty(KeyContainer.farbewand_Schule,
					cSchulen.getColorWand());
			// save properties to project root folder

			// inputfile
			prop.setProperty(KeyContainer.input_Alle, cAlle.getInput());
			prop.setProperty(KeyContainer.input_Altlasten,
					cAltlasten.getInput());
			prop.setProperty(KeyContainer.input_Bundestraﬂen,
					cBundestraﬂen.getInput());
			prop.setProperty(KeyContainer.input_Schule, cSchulen.getInput());

			// outputfile
			prop.setProperty(KeyContainer.output_Alle, cAlle.getOutput());
			prop.setProperty(KeyContainer.output_Altlasten,
					cAltlasten.getOutput());
			prop.setProperty(KeyContainer.output_Bundestraﬂen,
					cBundestraﬂen.getOutput());
			prop.setProperty(KeyContainer.output_Schule, cSchulen.getOutput());

			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("LODKMLStylist Configurator");
		setSize(492, 361);

	}

	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
