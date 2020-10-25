package justinDevB.Colonies.Objects;

import justinDevB.Colonies.XPlayer;

public class Colony {

	private String colonyName;
	private XPlayer mayor;

	public Colony(String name, XPlayer mayor) {
		this.colonyName = name;
		this.mayor = mayor;
		this.loadColony();
	}

	public Colony(String name) {
		this.colonyName = name;
	}

	public String getName() {
		return this.colonyName;
	}
	
	public XPlayer getMayor() {
		return this.mayor;
	}
	
	private void loadColony() {
		
	}

}
