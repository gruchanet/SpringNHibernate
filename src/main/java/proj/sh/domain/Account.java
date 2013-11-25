package proj.sh.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
	@NamedQuery(name = "account.all", query = "SELECT acc FROM Account acc"),
	@NamedQuery(name = "account.byId", query = "SELECT acc FROM Account acc "
											 + "WHERE acc.id = :id"),
	@NamedQuery(name = "account.byLogin", query = "SELECT acc FROM Account acc "
											 + "WHERE acc.login = :login"),
	@NamedQuery(name = "account.fromToDate", query = "SELECT acc FROM Account acc "
												   + "WHERE acc.creationDate BETWEEN :from AND :to")
})
public class Account {
	private int id;
	
	private List<Player> players = new ArrayList<Player>();
	
	private String login = "";
	private String password = "";
	private Date creationDate = new Date();
	
	public Account() {}
	
	public Account(String login, String password) {
		super();
		this.login = login;
		this.password = password;
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
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	@Column(nullable = false)
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	@Temporal(TemporalType.DATE)
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
}
