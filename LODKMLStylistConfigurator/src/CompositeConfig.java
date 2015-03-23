import java.io.File;
import java.lang.reflect.Array;
import java.util.Formatter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

public class CompositeConfig extends Composite {

	private Label label_inputfile;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	String input, output, scolorDach, scolorWand;

	private Label label_outputpath;

	private Label label_dachfarbe;

	private Label label_wandfarbe;

	private Color colorDach, colorWand;

	public String getInput() {
		return input;
	}

	public String getOutput() {
		return output;
	}

	public String getColorDach() {
		return scolorDach;
	}

	public String getColorWand() {
		return scolorWand;
	}

	public void loadProperties(String colordach1, String scolorwand1,
			String input1, String output1) {
		scolorDach = colordach1;
		scolorWand = scolorwand1;
		input = input1;
		label_inputfile.setText(input1);
		output = output1;
		label_outputpath.setText(output1);
		String s = scolorwand1.substring(6, 8);
		int r = Integer.parseInt(s, 16);
		s = scolorwand1.substring(4, 6);
		System.err.println(s);
		int g = Integer.parseInt(s, 16);

		s = scolorwand1.substring(2, 4);
		System.err.println(s);

		int b = Integer.parseInt(s, 16);
		System.err.println(s);

		RGB rgb = new RGB(r, g, b);
		if (rgb != null) {
			if (colorWand != null) {
				colorWand.dispose();
				colorWand = null;
			}
			colorWand = new Color(this.getDisplay(), rgb);
		}

		s = colordach1.substring(6, 8);
		r = Integer.parseInt(s, 16);
		s = colordach1.substring(4, 6);

		g = Integer.parseInt(s, 16);

		s = colordach1.substring(2, 4);

		b = Integer.parseInt(s, 16);
		rgb = new RGB(r, g, b);
		if (rgb != null) {
			if (colorDach != null) {
				colorDach.dispose();
				colorDach = null;
			}
			colorDach = new Color(this.getDisplay(), rgb);
		}

		label_dachfarbe.setText(colordach1);
		label_dachfarbe.setBackground(colorDach);
		label_wandfarbe.setText(scolorwand1);
		label_wandfarbe.setBackground(colorWand);
	}

	public CompositeConfig(Composite parent, int style) {
		super(parent, SWT.BORDER);
		setLayout(new GridLayout(3, false));

		Label lblInputFile = new Label(this, SWT.NONE);
		lblInputFile.setText("Input File:");

		label_inputfile = new Label(this, SWT.BORDER);
		GridData gd_label_inputfile = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_label_inputfile.widthHint = 300;

		label_inputfile.setLayoutData(gd_label_inputfile);

		Button btnChooseInputFile = new Button(this, SWT.NONE);
		btnChooseInputFile.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				chooseInputFile();
			}
		});
		btnChooseInputFile.setText("choose");

		Label lblOutputpath = new Label(this, SWT.NONE);
		lblOutputpath.setText("Output Path:");

		label_outputpath = new Label(this, SWT.BORDER);
		GridData gd_label_outputpath = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_label_outputpath.widthHint = 300;
		label_outputpath.setLayoutData(gd_label_outputpath);

		Button btnChoose_1 = new Button(this, SWT.NONE);
		btnChoose_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				chooseOutputPath();
			}
		});
		btnChoose_1.setText("choose");

		Label lblFarbeDach = new Label(this, SWT.NONE);
		lblFarbeDach.setText("Farbe Dach:");

		label_dachfarbe = new Label(this, SWT.BORDER);
		GridData gd_label_dachfarbe = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_label_dachfarbe.widthHint = 100;
		label_dachfarbe.setLayoutData(gd_label_dachfarbe);

		Button btnChoose_2 = new Button(this, SWT.NONE);
		btnChoose_2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				chooseDach();
			}
		});
		btnChoose_2.setText("choose");

		Label lblFarbeWand = new Label(this, SWT.NONE);
		lblFarbeWand.setText("Farbe Wand:");
		label_wandfarbe = new Label(this, SWT.BORDER);
		GridData gd_label_wandfarbe = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_label_wandfarbe.widthHint = 100;
		label_wandfarbe.setLayoutData(gd_label_wandfarbe);

		Button btnChoose_3 = new Button(this, SWT.NONE);
		btnChoose_3.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				chooseWand();
			}
		});
		btnChoose_3.setText("choose");

	}

	protected void chooseWand() {
		ColorDialog dlg = new ColorDialog(this.getShell());
		// Set the selected color in the dialog from
		// user's selected color
		dlg.setRGB(label_wandfarbe.getBackground().getRGB());
		// Change the title bar text
		dlg.setText("Choose a Color");
		// Open the dialog and retrieve the selected color
		RGB rgb = dlg.open();
		if (rgb != null) {
			// Dispose the old color, create the
			// new one, and set into the label
			if (colorWand != null) {
				colorWand.dispose();
				colorWand = null;
			}
			colorWand = new Color(this.getDisplay(), rgb);
			label_wandfarbe.setBackground(colorWand);
			Formatter formatter = new Formatter();
			int[] argumets = { 255, colorWand.getBlue(), colorWand.getGreen(),
					colorWand.getRed() };

			scolorWand = String.format("%02x%02x%02x%02x", getArray(argumets));
			label_wandfarbe.setText(scolorWand);
		}
	}

	protected void chooseDach() {
		ColorDialog dlg = new ColorDialog(this.getShell());

		// Set the selected color in the dialog from
		// user's selected color
		dlg.setRGB(label_dachfarbe.getBackground().getRGB());

		// Change the title bar text
		dlg.setText("Choose a Color");

		// Open the dialog and retrieve the selected color
		RGB rgb = dlg.open();
		if (rgb != null) {
			// Dispose the old color, create the
			if (colorDach != null) {
				colorDach.dispose();
				colorDach = null;
				colorDach = new Color(this.getDisplay(), rgb);
			}
			// new one, and set into the labe
			colorDach = new Color(this.getDisplay(), rgb);

			label_dachfarbe.setBackground(colorDach);
			Formatter formatter = new Formatter();
			int[] argumets = { 255, colorDach.getBlue(), colorDach.getGreen(),
					colorDach.getRed() };

			scolorDach = String.format("%02x%02x%02x%02x", getArray(argumets));
			label_dachfarbe.setText(scolorDach);
			;
		}
	}

	private Object[] getArray(Object val) {
		if (val instanceof Object[])
			return (Object[]) val;
		int arrlength = Array.getLength(val);
		Object[] outputArray = new Object[arrlength];
		for (int i = 0; i < arrlength; ++i) {
			outputArray[i] = Array.get(val, i);
		}
		return outputArray;
	}

	protected void chooseOutputPath() {
		DirectoryDialog dd = new DirectoryDialog(this.getShell());
		String path = dd.open();
		if (path == null)
			return;
		File f = new File(path);
		if (f.isDirectory()) {
			output = f.getAbsolutePath();
			label_outputpath.setText(f.getAbsolutePath());
		}
	}

	protected void chooseInputFile() {
		FileDialog fd = new FileDialog(this.getShell());
		String[] ext = { "*.kml" };
		fd.setFilterExtensions(ext);
		String path = fd.open();
		if (path == null)
			return;
		File f = new File(path);
		if (f.exists()) {
			input = f.getAbsolutePath();
			label_inputfile.setText(f.getAbsolutePath());

		} else {
			MessageBox msg = new MessageBox(this.getShell());
			msg.setText("please select an kml-File");
		}
	}

	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
