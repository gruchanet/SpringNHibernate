package proj.sh.service;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import proj.sh.domain.Account;
import proj.sh.domain.Player;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class AccountManagerTest {
	
	@Autowired
	AccManager accountManager;
	
	private final String LOGIN[] = {"franko24", "witcher665"};
	private final String PASSWORD[] = {"kochamagate123", "g3r41t"};
	
	private final String NAME[] = {"Franczyshan", "Geralt", "Triss"};
	private final int LEVEL[] = {23, 11, 11};
	private final int GOLD[] = {1234, 1337, 665};
	
	// check 'orphanRemoval'
	
	@Before
	public void setUp() {}
	
	@After
	public void tearDown() {
		List<Account> retrievedAccounts = accountManager.getAllAccounts();
		List<Player> retrievedPlayers = accountManager.getAllPlayers();
		
		for (Player player : retrievedPlayers)
			accountManager.deletePlayer(player.getId());
		
		for (Account account : retrievedAccounts)
			accountManager.deleteAccount(account.getId());
	}
	
	@Test
	public void addingCheck() {	
		Account account = new Account(LOGIN[0], PASSWORD[0]);
		Player player = new Player(NAME[0], LEVEL[0], GOLD[0]);
		
		accountManager.addAccount(account);
		int playerId = accountManager.addPlayer(player);
		accountManager.pinPlayerToAccount(account.getId(), playerId);
		
		Account retrievedAccount = accountManager.getAccount(LOGIN[0]);
		Player retrievedPlayer = accountManager.getPlayer(NAME[0]);
		List<Player> accountPlayers = accountManager.getAccountPlayers(retrievedAccount);
		
		assertEquals(account, retrievedAccount);
		assertEquals(player, retrievedPlayer);
		assertEquals(1, accountPlayers.size());
		assertEquals(player, accountPlayers.get(0));
	}
	
	@Test
	@SuppressWarnings("deprecation")
	public void gettingCheck() {
		Calendar yesterday = Calendar.getInstance();
		Calendar today = Calendar.getInstance();
		yesterday.add(Calendar.DATE, -1);
		
		Account account = new Account(LOGIN[1], PASSWORD[1]);
		Player players[] = {new Player(NAME[1], LEVEL[1], GOLD[1]),
				new Player(NAME[2], LEVEL[2], GOLD[2])};
		
		accountManager.addAccount(account);
		int playerId[] = {accountManager.addPlayer(players[0]),
				accountManager.addPlayer(players[1])};
		accountManager.pinPlayerToAccount(account.getId(), playerId[0]);
		accountManager.pinPlayerToAccount(account.getId(), playerId[1]);
		
		Account retrievedAccount = accountManager.getAccount(account.getId());
		Player retrievedPlayers[] = {accountManager.getPlayer(playerId[0]),
				accountManager.getPlayer(playerId[1])};
		List<Player> accountPlayers = accountManager.getAccountPlayers(retrievedAccount);
		List<Account> accounts = accountManager.findAccounts(yesterday.getTime(), today.getTime());
		long playersWithLevel = accountManager.countPlayersWithLevel(LEVEL[1]);
		
		assertEquals(account, retrievedAccount);
		assertEquals(players, retrievedPlayers);
		assertEquals(2, accountPlayers.size());
		assertEquals(players, accountPlayers.toArray());
		assertEquals(1, accounts.size());
		assertEquals(account, accounts.get(0));
		assertEquals(2, playersWithLevel);
	}
	
	@Test
	public void updatingCheck() {
		Account account = new Account(LOGIN[0], PASSWORD[0]);
		Player players[] = {new Player(NAME[0], LEVEL[0], GOLD[0]),
				new Player(NAME[1], LEVEL[1], GOLD[1])};
		
		accountManager.addAccount(account);
		int playerId[] = {accountManager.addPlayer(players[0]),
				accountManager.addPlayer(players[1])};
		accountManager.pinPlayerToAccount(account.getId(), playerId[0]);
		accountManager.pinPlayerToAccount(account.getId(), playerId[1]);
		
		accountManager.banPlayer(playerId[0]);
		
		Account retrievedAccount = accountManager.getAccount(account.getId());
		Player retrievedPlayers[] = {accountManager.getPlayer(playerId[0]),
				accountManager.getPlayer(playerId[1])};
		List<Player> bannedPlayers = accountManager.findBannedPlayers();
		List<Player> accountPlayers = accountManager.getAccountPlayers(retrievedAccount);
		
		assertEquals(players[0], retrievedPlayers[0]);
		assertEquals(1, bannedPlayers.size());
		assertEquals(players[0], bannedPlayers.get(0));
		assertEquals(players[0], accountPlayers.get(0));
		assertEquals(false, retrievedPlayers[1].isBanned());
	}
	
	@Test
	public void deletingCheck() {
		Account accounts[] = {new Account(LOGIN[0], PASSWORD[0]),
				new Account(LOGIN[1], PASSWORD[1])};
		Player players[] = {new Player(NAME[0], LEVEL[0], GOLD[0]),
				new Player(NAME[1], LEVEL[1], GOLD[1])};
		
		accountManager.addAccount(accounts[0]);
		accountManager.addAccount(accounts[1]);
		int playerId[] = {accountManager.addPlayer(players[0]),
				accountManager.addPlayer(players[1])};
		accountManager.pinPlayerToAccount(accounts[0].getId(), playerId[0]);
		accountManager.pinPlayerToAccount(accounts[0].getId(), playerId[1]);
		
		accountManager.deletePlayer(accounts[1], players[0].getId()); // won't delete, cuz player[0] doesn't belong to account[1]
		accountManager.deletePlayer(accounts[0], players[1].getId()); // will delete
		
		List<Account> retrievedAccounts = accountManager.getAllAccounts();
		List<Player> retrievedPlayers = accountManager.getAllPlayers();
		List<Player> accountPlayers = accountManager.getAccountPlayers(retrievedAccounts.get(0));
		
		assertEquals(1, retrievedPlayers.size());
		assertEquals(players[0], retrievedPlayers.get(0));
		assertEquals(1, accountPlayers.size());
		assertEquals(players[0], accountPlayers.get(0));
		assertEquals(null, accountManager.getPlayer(players[1].getId()));
		
		accountManager.deleteAccount(accounts[1].getId());
		
		retrievedAccounts = accountManager.getAllAccounts();
		retrievedPlayers = accountManager.getAllPlayers();
		
		assertEquals(1, retrievedAccounts.size());
		assertEquals(1, retrievedPlayers.size());
		
		accountManager.deleteAccount(accounts[0].getId());
		
		retrievedAccounts = accountManager.getAllAccounts();
		retrievedPlayers = accountManager.getAllPlayers();
		
		assertEquals(0, retrievedAccounts.size());
		assertEquals(0, retrievedPlayers.size());
	}
}
