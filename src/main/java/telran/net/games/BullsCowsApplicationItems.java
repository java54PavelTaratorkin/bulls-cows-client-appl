package telran.net.games;

import java.time.LocalDate;
import java.util.*;

import telran.net.games.model.MoveData;
import telran.net.games.service.BullsCowsService;
import telran.view.*;
public class BullsCowsApplicationItems {
	private static BullsCowsService bcService;
	private static String username;
	private static long gameId;
	private static int nDigits;
	public static List<Item> getItems(BullsCowsService service, int nDigits) {
		bcService = service;
		BullsCowsApplicationItems.nDigits = nDigits;
		ArrayList<Item> res= new ArrayList<>();
		res.add(Item.of("Login", io -> loginMenu(io, false)));
		res.add(Item.of("Sign up", io -> loginMenu(io, true) ));
		return res;
	}
	private static void loginMenu(InputOutput io,
			boolean register)
	{
		String username = io.readString("Enter username");
		if(register) {
			LocalDate birthDate = io.readIsoDate("Enter birthDate", "Wrong date yyyy-MM-dd");
			bcService.registerGamer(username, birthDate);
		}else {
			bcService.loginGamer(username);
		}
		
		
		Item[] items = getGamerMenuItems(username);
		Menu menu = new Menu("Welcome " + username, items);
		menu.perform(io);
	}
	private static Item[] getGamerMenuItems(String username) {
		BullsCowsApplicationItems.username = username;
		Item[] items = {
				Item.of("create new game",
						BullsCowsApplicationItems::createNewGame),
				Item.of("start game", BullsCowsApplicationItems::startGame),
				Item.of("continue game", BullsCowsApplicationItems::continueGame),
				Item.of("join game", BullsCowsApplicationItems::joinGame),
				Item.ofExit()
				
			};
			return items;
	}
	private static void continueGame(InputOutput io) {
		List<Long> ids = bcService.getStartedGamesWithGamer(username);
		if(ids.isEmpty()) {
			io.writeLine("No games you might have continued");
		} else {
		io.writeLine("Below are started game ID's, you are a gamer in");
		displayLines(ids, io);
		gameId = io.readLong("Enter any id from the above list", "Wrong ID");
		//FIX display all moves of the gamer in the game (no appropriate service method)
		runGameMenu(io);
		}
	}

	private static void startGame(InputOutput io) {
		List<Long> ids = bcService.getNotStartedGamesWithGamer(username);
		if(ids.isEmpty()) {
			io.writeLine("No games you might have started");
		} else {
		io.writeLine("Below are not started game ID's, you are a gamer in");
		displayLines(ids, io);
		gameId = io.readLong("Enter any id from the above list", "Wrong ID");
		List<String> gamers = bcService.startGame(gameId);
		io.writeLine("Game started with following gamers:");
		displayLines(gamers, io);
		runGameMenu(io);
		}
		
	}

	private static void runGameMenu(InputOutput iop) {
		Item[] items = {
				Item.of("Your Move", BullsCowsApplicationItems::move),
				Item.ofExit()
		};
		Menu menu = new Menu(String.format("game: %d; gamer: %s", gameId, username), items);
		menu.perform(iop);
		
	}

	private static void move(InputOutput io) {
		String sequence = io.
				readString(String.format("Enter %d non-repeated digits", nDigits));
		List<MoveData> history = bcService.moveProcessing(sequence, gameId, username);
		displayLines(history, io);
		if (bcService.gameOver(gameId)) {
			
			io.writeLine("Congratulations: you are winner");
			io.writeLine("For starting / continuing game you should exit from menu");
		}
		
	}

	private static void joinGame(InputOutput io) {
		List<Long> ids = bcService.getNotStartedGamesWithOutGamer(username);
		if(ids.isEmpty()) {
			io.writeLine("No games you might have joined");
		} else {
			io.writeLine("Below are not started game ID's, you are not a gamer in");
			displayLines(ids, io);
			gameId = io.readLong("Enter any id from the above list", "Wrong ID");
			bcService.gamerJoinGame(gameId, username);
			io.writeLine("You have joined the game " + gameId);
		}
		
	}

	private static <T> void displayLines(List<T> lines, InputOutput io) {
		lines.forEach(io::writeLine);
		
	}

	private static void createNewGame(InputOutput io) {
		gameId = bcService.createGame();
		io.writeLine(String.format("Game with id %d has been created", gameId));
	}

}
