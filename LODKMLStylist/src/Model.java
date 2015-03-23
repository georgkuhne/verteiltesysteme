public class Model {
	private final static Model instance = new Model();

	private Model() {
	}

	private String dachfarbeAlle, wandFarbeAlle, inputAlle, outputAlle,
			dachfarbeSchulen, wandfarbeSchulen, inputSchulen, outputSchulen,
			dachfarbeAltlasten, wandfarbeAltlasten, inputAltlasten,
			outputAltlasten, wandfarbeBundesstrassen, dachfarbeBundestrassen,
			inputBundesstrassen, outputBundestrassen;

	public void setDachfarbeAlle(String dachfarbeAlle) {
		this.dachfarbeAlle = dachfarbeAlle;
	}

	public void setWandFarbeAlle(String wandFarbeAlle) {
		this.wandFarbeAlle = wandFarbeAlle;
	}

	public void setInputAlle(String inputAlle) {
		this.inputAlle = inputAlle;
	}

	public void setOutputAlle(String outputAlle) {
		this.outputAlle = outputAlle;
	}

	public void setDachfarbeSchulen(String dachfarbeSchulen) {
		this.dachfarbeSchulen = dachfarbeSchulen;
	}

	public void setWandfarbeSchulen(String wandfarbeSchulen) {
		this.wandfarbeSchulen = wandfarbeSchulen;
	}

	public void setInputSchulen(String inputSchulen) {
		this.inputSchulen = inputSchulen;
	}

	public void setOutputSchulen(String outputSchulen) {
		this.outputSchulen = outputSchulen;
	}

	public void setDachfarbeAltlasten(String dachfarbeAltlasten) {
		this.dachfarbeAltlasten = dachfarbeAltlasten;
	}

	public void setWandfarbeAltlasten(String wandfarbeAltlasten) {
		this.wandfarbeAltlasten = wandfarbeAltlasten;
	}

	public void setInputAltlasten(String inputAltlasten) {
		this.inputAltlasten = inputAltlasten;
	}

	public void setOutputAltlasten(String outputAltlasten) {
		this.outputAltlasten = outputAltlasten;
	}

	public void setWandfarbeBundesstrassen(String wandfarbeBundesstrassen) {
		this.wandfarbeBundesstrassen = wandfarbeBundesstrassen;
	}

	public void setDachfarbeBundestrassen(String dachfarbeBundestrassen) {
		this.dachfarbeBundestrassen = dachfarbeBundestrassen;
	}

	public void setInputBundesstrassen(String inputBundesstrassen) {
		this.inputBundesstrassen = inputBundesstrassen;
	}

	public void setOutputBundestrassen(String outputBundestrassen) {
		this.outputBundestrassen = outputBundestrassen;
	}

	public static Model getInstance() {
		return instance;
	}

	public String getDachfarbeAlle() {
		return dachfarbeAlle;
	}

	public String getWandFarbeAlle() {
		return wandFarbeAlle;
	}

	public String getInputAlle() {
		return inputAlle;
	}

	public String getOutputAlle() {
		return outputAlle;
	}

	public String getDachfarbeSchulen() {
		return dachfarbeSchulen;
	}

	public String getWandfarbeSchulen() {
		return wandfarbeSchulen;
	}

	public String getInputSchulen() {
		return inputSchulen;
	}

	public String getOutputSchulen() {
		return outputSchulen;
	}

	public String getDachfarbeAltlasten() {
		return dachfarbeAltlasten;
	}

	public String getWandfarbeAltlasten() {
		return wandfarbeAltlasten;
	}

	public String getInputAltlasten() {
		return inputAltlasten;
	}

	public String getOutputAltlasten() {
		return outputAltlasten;
	}

	public String getWandfarbeBundesstrassen() {
		return wandfarbeBundesstrassen;
	}

	public String getDachfarbeBundestrassen() {
		return dachfarbeBundestrassen;
	}

	public String getInputBundesstrassen() {
		return inputBundesstrassen;
	}

	public String getOutputBundestrassen() {
		return outputBundestrassen;
	}

}
