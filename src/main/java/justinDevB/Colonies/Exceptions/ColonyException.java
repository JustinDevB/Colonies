package justinDevB.Colonies.Exceptions;

public class ColonyException extends Exception {

	private static final long serialVersionUID = -6291780675475164282L;
	
	public ColonyException() {
		super("error occured");
	}
	
	public ColonyException(String message) {
		super(message);
	}
	
	

}
