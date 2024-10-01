package telran.net.games;

import java.util.*;

import telran.net.TcpClient;
import telran.net.games.service.*;
import telran.view.*;
public class BullsCowsClientAppl {

	private static final int N_DIGITS = 4;

	public static void main(String[] args) {
		InputOutput inputOutput = new SystemInputOutput();
		TcpClient tcpClient = new TcpClient("localhost", 5000);
		BullsCowsTcpProxy proxy = new BullsCowsTcpProxy(tcpClient);
		List<Item> items = BullsCowsApplicationItems.getItems(proxy, N_DIGITS);
		items.add(Item.of("Exit",
				io -> tcpClient.close(), true));
		Menu menu = new Menu("Bulls Cows Logical Network Game",
				items.toArray(Item[]::new));
		
		menu.perform(inputOutput);
	}

//	private static List<Item> getItems(BullsCowsService proxy) {
//		ArrayList<Item> res= new ArrayList<>();
//		res.add(Item.of("Login", io -> loginMenu(proxy, io, false)));
//		res.add(Item.of("Sign up", io -> loginMenu(proxy, io, true) ));
//		return res;
//	}
//
//	private static void loginMenu(BullsCowsService proxy, InputOutput io,
//			boolean register)
//	{
//		String username = io.readString("Enter username");
//		if(register) {
//			LocalDate birthDate = io.readIsoDate("Enter birthDate", "Wrong date yyyy-MM-dd");
//			proxy.registerGamer(username, birthDate);
//		}else {
//			proxy.loginGamer(username);
//		}
//		
//		
//		Item[] items = getGamerMenuItems(username, proxy);
//		Menu menu = new Menu("Welcome " + username, items);
//		menu.perform(io);
//	}
//
//	private static Item[] getGamerMenuItems(String username, BullsCowsService proxy) {
//		Item[] items = {
//			Item.of("create new game", io -> createNewGame(io,proxy)),
//			Item.of("start game", io -> startGame(io, username, proxy)),
//			Item.of("continue game", io -> continueGame(io, username, proxy)),
//			Item.of("join game", io -> joinGame(io, username, proxy)),
//			Item.ofExit()
//			
//		};
//		return items;
//	}
//
//	private static void continueGame(InputOutput io, String username,
//			BullsCowsService proxy) {
//		List<Long> ids = proxy.getStartedGamesWithGamer(username);
//		if(ids.isEmpty()) {
//			io.writeLine("No games you might have continued");
//		} else {
//		io.writeLine("Below are started game ID's, you are a gamer in");
//		displayLines(ids, io);
//		long gameId = io.readLong("Enter any id from the above list", "Wrong ID");
//		runGameMenu(gameId, username, io, proxy);
//		}
//	}
//
//	private static void startGame(InputOutput io, String username,
//			BullsCowsService proxy) {
//		List<Long> ids = proxy.getNotStartedGamesWithGamer(username);
//		if(ids.isEmpty()) {
//			io.writeLine("No games you might have started");
//		} else {
//		io.writeLine("Below are not started game ID's, you are a gamer in");
//		displayLines(ids, io);
//		long gameId = io.readLong("Enter any id from the above list", "Wrong ID");
//		proxy.startGame(gameId);
//		runGameMenu(gameId, username, io, proxy);
//		}
//		
//	}
//
//	private static void runGameMenu(long gameId, String username, InputOutput iop,
//			BullsCowsService proxy) {
//		Item[] items = {
//				Item.of("Your Move", io -> move(io, gameId, username, proxy)),
//				Item.ofExit()
//		};
//		Menu menu = new Menu(String.format("game: %d; gamer: %s", gameId, username), items);
//		menu.perform(iop);
//		
//	}
//
//	private static void move(InputOutput io, long gameId, String username, BullsCowsService proxy) {
//		String sequence = io.
//				readString(String.format("Enter %d non-repeated digits", N_DIGITS));
//		List<MoveData> history = proxy.moveProcessing(sequence, gameId, username);
//		displayLines(history, io);
//		if (proxy.gameOver(gameId)) {
//			
//			io.writeLine("Congratulations: you are winner");
//			io.writeLine("For starting / continuing game you should exit from menu");
//		}
//		
//	}
//
//	private static void joinGame(InputOutput io, String username, BullsCowsService proxy) {
//		List<Long> ids = proxy.getNotStartedGamesWithOutGamer(username);
//		if(ids.isEmpty()) {
//			io.writeLine("No games you might have joined");
//		} else {
//			io.writeLine("Below are not started game ID's, you are not a gamer in");
//			displayLines(ids, io);
//			long gameId = io.readLong("Enter any id from the above list", "Wrong ID");
//			proxy.gamerJoinGame(gameId, username);
//			io.writeLine("You have joined the game " + gameId);
//		}
//		
//	}
//
//	private static <T> void displayLines(List<T> lines, InputOutput io) {
//		lines.forEach(io::writeLine);
//		
//	}
//
//	private static void createNewGame(InputOutput io, BullsCowsService proxy) {
//		long gameId = proxy.createGame();
//		io.writeLine(String.format("Game with id %d has been created", gameId));
//	}

}
