package proj.sh.service;

import java.util.Date;
import java.util.List;

import proj.sh.domain.Account;
import proj.sh.domain.Player;

public interface AccManager {
	
	abstract void addAccount(Account account); // addingCheck, gettingCheck
	abstract void deleteAccount(int id); // deletingCheck
	abstract List<Account> getAllAccounts(); // addingCheck
	abstract Account getAccount(int id); // gettingCheck
	abstract Account getAccount(String login); // addingCheck
	abstract List<Account> findAccounts(Date from, Date to); // gettingCheck
	abstract List<Player> getAccountPlayers(Account account); // addingCheck, gettingCheck
	
	abstract int addPlayer(Player player); // addingCheck, gettingCheck
	abstract void pinPlayerToAccount(int accountId, int playerId); // addingCheck, gettingCheck
	abstract void deletePlayer(int id);
	abstract void deletePlayer(Account account, int id); // deletingCheck
	abstract List<Player> getAllPlayers(); // addingCheck
	abstract Player getPlayer(int id); // gettingCheck
	abstract Player getPlayer(String name); // addingCheck
	abstract List<Player> findBannedPlayers(); // updatingCheck
	abstract long countPlayersWithLevel(int level); // gettingCheck
	abstract void banPlayer(int id); // updatingCheck

}
