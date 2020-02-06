package justinDevB.TownyX.Objects;

import justinDevB.TownyX.XPlayer;

public class Town {

	private String townName;
	private XPlayer mayor;

	public Town(String name, XPlayer mayor) {
		this.townName = name;
		this.mayor = mayor;
		this.loadTown();
	}

	public Town(String name) {
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
