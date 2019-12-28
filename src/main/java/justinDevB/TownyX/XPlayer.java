package justinDevB.TownyX;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import justinDevB.TownyX.Events.PlayerRegisterEvent;

public class XPlayer {

	private final TownyX townyx;
	private final Player player;
	private Rank rank = Rank.PLAYER;

	public XPlayer(TownyX tx, Player p) {
		this.townyx = tx;
		this.player = p;

		PlayerRegisterEvent registerEvent = new PlayerRegisterEvent(p, tx.getXPlayer(p.getUniqueId()));
		Bukkit.getPluginManager().callEvent(registerEvent);
	}

	public Player getPlayer() {
		return this.player;
	}

	public String getName() {
		return getPlayer().getName();
	}

	public enum Rank {
		// TODO: Change these out for Town specific ranks ex: Mayor, Assitant, etc
		ADMIN(100), MOD(90), HELPER(1), PLAYER(0);
		public final int value;

		private Rank(int value) {
			this.value = value;
		}
	}

	public Rank getRank() {
		return rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}

	public boolean hasRank(Rank rank) {
		if (getRank().value >= rank.value)
			return true;
		return false;
	}

}
