import org.eclipse.swt.widgets.Display;

public class Configurator {
	ShellMain shell;

	public static void main(String[] args) {
		new Configurator().run();
	}

	public void run() {
		Display display = new Display();
		shell = new ShellMain(display);
		shell.pack();

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
