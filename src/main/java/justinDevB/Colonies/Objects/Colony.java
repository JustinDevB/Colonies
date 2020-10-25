package justinDevB.Colonies.Objects;

import justinDevB.Colonies.XPlayer;

public class Colony {

	private String townName;
	private XPlayer mayor;

	public Colony(String name, XPlayer mayor) {
		this.townName = name;
		this.mayor = mayor;
		this.loadTown();
	}

	public Colony(String name) {
		this.townName = name;
	}

	public String getName() {
		return this.townName;
	}
	
	public XPlayer getMayor() {
		return this.mayor;
	}
	
	private void loadTown() {
		
	}

}
