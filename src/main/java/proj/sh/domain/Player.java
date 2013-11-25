package proj.sh.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name = "player.all", query = "SELECT pl FROM Player pl"),
	@NamedQuery(name = "player.byId", query = "SELECT pl FROM Player pl "
			 								 + "WHERE pl.id = :id"),
	@NamedQuery(name = "player.byName", query = "SELECT pl FROM Player pl "
											  + "WHERE pl.name = :name"),
	@NamedQuery(name = "player.banned", query = "SELECT pl FROM Player pl "
											  + "WHERE pl.banned = true"),
	@NamedQuery(name = "player.countPlayersWithLevel", query = "SELECT COUNT(pl) FROM Player pl "
															 + "WHERE pl.level = :level")
})
public class Player {
	private int id;
	
	private String name;
	private int level;
	private int gold;
	private boolean banned = false;
	
	public Player() {}
	
	public Player(String name, int level, int gold) {
		super();
		this.name = name;
		this.level = level;
		this.gold = gold;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(unique = true, nullable = false)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getGold() {
		return gold;
	}
	
	public void setGold(int gold) {
		this.gold = gold;
	}

	public boolean isBanned() {
		return banned;
	}

	public void setBanned(boolean banned) {
		this.banned = banned;
	}
	
}
