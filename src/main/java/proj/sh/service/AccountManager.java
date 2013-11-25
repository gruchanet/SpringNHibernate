package proj.sh.service;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import proj.sh.domain.Account;
import proj.sh.domain.Player;

@Component
@Transactional
public class AccountManager implements AccManager {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void addAccount(Account account) {
		account.setId(0);
		sessionFactory.getCurrentSession().persist(account);
	}

	@Override
	public void deleteAccount(int id) {
		Account account = (Account) sessionFactory.getCurrentSession().get(Account.class, id);
		
		// 'orphanRemoval' attribute
//		for (Player player : account.getPlayers())
//			sessionFactory.getCurrentSession().delete(player);
		
		sessionFactory.getCurrentSession().delete(account);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Account> getAllAccounts() {
		return sessionFactory.getCurrentSession().getNamedQuery("account.all").list();
	}
	
	@Override
	public Account getAccount(int id) {
		return (Account) sessionFactory.getCurrentSession().getNamedQuery("account.byId").setInteger("id", id).uniqueResult();
	}
	
	@Override
	public Account getAccount(String login) {
		return (Account) sessionFactory.getCurrentSession().getNamedQuery("account.byLogin").setString("login", login).uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Account> findAccounts(Date from, Date to) {		
		return sessionFactory.getCurrentSession().getNamedQuery("account.fromToDate").setDate("from", from).setDate("to", to).list();
	}

	@Override
	public List<Player> getAccountPlayers(Account account) {
		account = (Account) sessionFactory.getCurrentSession().get(Account.class, account.getId());
		
		return account.getPlayers();
	}
	
	@Override
	public int addPlayer(Player player) {
		player.setId(0);
		return (Integer) sessionFactory.getCurrentSession().save(player);
	}

	@Override
	public void pinPlayerToAccount(int accountId, int playerId) {
		Account account = (Account) sessionFactory.getCurrentSession().get(Account.class, accountId);
		Player player = (Player) sessionFactory.getCurrentSession().get(Player.class, playerId);
		
		account.getPlayers().add(player);
	}
	
	@Override
	public void deletePlayer(int id) {
		Player player = (Player) sessionFactory.getCurrentSession().get(Player.class, id);
		
		sessionFactory.getCurrentSession().delete(player);
	}

	@Override
	public void deletePlayer(Account account, int id) {
		account = (Account) sessionFactory.getCurrentSession().get(Account.class, account.getId());
		Player player = (Player) sessionFactory.getCurrentSession().get(Player.class, id);
		
		for (Player playerToRemove : account.getPlayers())
			if (playerToRemove.getId() == player.getId()) {
				account.getPlayers().remove(playerToRemove);
				sessionFactory.getCurrentSession().delete(player);
				return;
			}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Player> getAllPlayers() {
		return sessionFactory.getCurrentSession().getNamedQuery("player.all").list();
	}
	
	@Override
	public Player getPlayer(int id) {
		return (Player) sessionFactory.getCurrentSession().getNamedQuery("player.byId").setInteger("id", id).uniqueResult();
	}
	
	@Override
	public Player getPlayer(String name) {
		return (Player) sessionFactory.getCurrentSession().getNamedQuery("player.byName").setString("name", name).uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Player> findBannedPlayers() {
		return sessionFactory.getCurrentSession().getNamedQuery("player.banned").list();
	}

	@Override
	public long countPlayersWithLevel(int level) {
		return (Long) sessionFactory.getCurrentSession().getNamedQuery("player.countPlayersWithLevel").setInteger("level", level).list().get(0);
	}

	@Override
	public void banPlayer(int id) {
		Player player = (Player) sessionFactory.getCurrentSession().get(Player.class, id);
		
		player.setBanned(true);
	}

}
