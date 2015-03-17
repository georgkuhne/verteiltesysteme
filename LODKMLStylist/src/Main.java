import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

//Send questions, comments, bug reports, etc. to the authors:

//Rob Warner (rwarner@interspatial.com)
//Robert Harris (rbrt_harris@yahoo.com)

/**
 * This class demonstrates the ColorDialog class
 */
public class Main {
	private Color colorDach;
	private Color colorWand;

	private Label importkmllabel;
	private Label exportlabel;
	private Text outputfilename;
	private Shell shell;
	private File importfile;
	private File exportDirectory;

	/**
	 * Runs the application
	 */
	public void run() {
		Display display = new Display();
		shell = new Shell(display);
		shell.setText("Color Chooser");
		createContents(shell);
		shell.pack();

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		// Dispose the color we created for the Label
		if (colorDach != null)
			colorDach.dispose();

		if (colorWand != null)
			colorWand.dispose();

		display.dispose();
	}

	/**
	 * Creates the window contents
	 * 
	 * @param shell
	 *            the parent shell
	 */
	private void createContents(final Shell shell) {
		shell.setLayout(new GridLayout(3, true));

		// Start with Celtics green
		colorWand = new Color(shell.getDisplay(), new RGB(200, 200, 200));
		colorDach = new Color(shell.getDisplay(), new RGB(255, 0, 0));

		// CHOOSE KML
		GridData gdfolder = new GridData();
		gdfolder.widthHint = 200;

		new Label(shell, SWT.None).setText("KML import:");
		importkmllabel = new Label(shell, SWT.None);
		importkmllabel.setLayoutData(gdfolder);
		Button importButton = new Button(shell, SWT.None);
		importButton.setText("Choose");
		importButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				importbuttonklicked();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		new Label(shell, SWT.None).setText("outputfolder:");
		exportlabel = new Label(shell, SWT.None);
		exportlabel.setLayoutData(gdfolder);
		Button exportButton = new Button(shell, SWT.None);
		exportButton.setText("Choose");

		exportButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				exportButtonKlicked();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		new Label(shell, SWT.None).setText("outputfilename:");

		outputfilename = new Text(shell, SWT.DEFAULT);
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		gd.widthHint = 100;
		outputfilename.setLayoutData(gd);
		outputfilename.setEditable(true);
		// Dachfarbe:
		new Label(shell, SWT.None).setText("Dachfarbe:");
		final Label DachfarbeColorLabel = new Label(shell, SWT.NONE);
		DachfarbeColorLabel.setText("                              ");
		DachfarbeColorLabel.setBackground(colorDach);

		Button dachfarbe = new Button(shell, SWT.PUSH);
		dachfarbe.setText("Color...");
		dachfarbe.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				// Create the color-change dialog
				ColorDialog dlg = new ColorDialog(shell);

				// Set the selected color in the dialog from
				// user's selected color
				dlg.setRGB(DachfarbeColorLabel.getBackground().getRGB());

				// Change the title bar text
				dlg.setText("Choose a Color");

				// Open the dialog and retrieve the selected color
				RGB rgb = dlg.open();
				if (rgb != null) {
					// Dispose the old color, create the
					// new one, and set into the label
					colorDach.dispose();
					colorDach = new Color(shell.getDisplay(), rgb);
					DachfarbeColorLabel.setBackground(colorDach);
				}
			}
		});

		new Label(shell, SWT.None).setText("Wandfarbe:");
		final Label WandfarbeColorLabel = new Label(shell, SWT.NONE);
		WandfarbeColorLabel.setText("                              ");
		WandfarbeColorLabel.setBackground(colorWand);

		Button wandfarbe = new Button(shell, SWT.PUSH);
		wandfarbe.setText("Color...");
		wandfarbe.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				// Create the color-change dialog
				ColorDialog dlg = new ColorDialog(shell);

				// Set the selected color in the dialog from
				// user's selected color
				dlg.setRGB(DachfarbeColorLabel.getBackground().getRGB());

				// Change the title bar text
				dlg.setText("Choose a Color");

				// Open the dialog and retrieve the selected color
				RGB rgb = dlg.open();
				if (rgb != null) {
					// Dispose the old color, create the
					// new one, and set into the label
					colorWand.dispose();
					colorWand = new Color(shell.getDisplay(), rgb);
					WandfarbeColorLabel.setBackground(colorDach);
				}
			}
		});

		Button execute = new Button(shell, SWT.None);
		execute.setText("execute");
		execute.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				execute();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	protected void execute() {
		ProgressTransformation.getInstance().excecute(colorDach, colorWand,
				exportDirectory, outputfilename.getText(), importfile);
	}

	protected void exportButtonKlicked() {

		DirectoryDialog dd = new DirectoryDialog(shell);
		String path = dd.open();
		File f = new File(path);
		if (f.isDirectory()) {
			exportDirectory = f;
			exportlabel.setText(f.getAbsolutePath());
		}

	}

	protected void importbuttonklicked() {
		FileDialog fd = new FileDialog(shell);
		String[] ext = { "*.kml" };
		fd.setFilterExtensions(ext);
		String path = fd.open();
		File f = new File(path);
		if (f.exists()) {
			importfile = f;
			importkmllabel.setText(f.getAbsolutePath());

		} else {
			MessageBox msg = new MessageBox(shell);
			msg.setText("please select an kml-File");
		}
	}

	/**
	 * The application entry point
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		new Main().run();
	}
}
