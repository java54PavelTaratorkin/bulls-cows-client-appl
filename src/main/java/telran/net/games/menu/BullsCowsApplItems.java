package telran.net.games.menu;

import java.time.*;
import java.util.*;
import java.util.function.Supplier;

import telran.net.games.model.*;
import telran.net.games.service.BullsCowsService;
import telran.view.*;

public class BullsCowsApplItems {

	private static BullsCowsService bullsCows;
	private static String username = "";
	private static long gameId;
	private static int gameSeqLength;
	private static int minSeqSize;
	private static int maxSeqSize;
	private static int defSeqSize;
	private static int minAge;
	
	public static List<Item> getItems(BullsCowsService bullsCows) {
		BullsCowsApplItems.bullsCows = bullsCows;	
		InputOutput io = new SystemInputOutput();
		getGameDefaultData(io, bullsCows);		
		Item[] items = {
				Item.of("Login", BullsCowsApplItems::login),
				Item.of("Register", BullsCowsApplItems::register)
		};
		return new ArrayList<>(List.of(items));
	}
	
	/**
	 * Retrieves game default data (such as sequence size and minimum age) from the server 
	 * and assigns it to the appropriate fields (minSeqSize, maxSeqSize, defSeqSize, minAge).
	 * Any exceptions during retrieval are caught, and an error message is displayed.
	 */
	private static void getGameDefaultData(InputOutput io, BullsCowsService bullsCows) {
		GameDefaultData gameDefaultData = bullsCows.getGameDefaults();		
		minSeqSize = gameDefaultData.minSeqSize();
		maxSeqSize = gameDefaultData.maxSeqSize();
		defSeqSize = gameDefaultData.defSeqSize();
		minAge = gameDefaultData.minAge();
	}
	
	/**
	 * Displays a menu for creating or continuing a game after checking if the user 
	 * is logged in. If the user is not logged in, it prompts for a new or existing gamer.
	 */
	private static void login(InputOutput io) {
		try {
			bullsCows.loginGamer(username);			
		} catch (Exception e) {
			io.writeLine(e.getMessage());
			getGamerOrRegister(io);
		}			
		
		Menu menu = new Menu(String.format("Game menu. Gamer: %s", username), new Item[] {
				Item.of("Create game", BullsCowsApplItems::createGame),
				Item.of("Start game", BullsCowsApplItems::startGame),
				Item.of("Continue game", BullsCowsApplItems::continueGame),
				Item.of("Join game", BullsCowsApplItems::joinGame),
				Item.of("Show all not started games", BullsCowsApplItems::getNotStartedGames),
				Item.ofExit()
		});
		menu.perform(io);

	}
	
	/**
	 * Fetches a list of all not-started games from the server and displays their IDs.
	 * If an exception occurs during retrieval, it is caught and displayed to the user.
	 */
	private static void getNotStartedGames(InputOutput io) {
    	io.writeLine("List of all not started games IDs:");
		List<Long> games = bullsCows.getNotStartedGames();
		games.forEach(io::writeLine);
	}
	
	/**
	 * Displays a list of all registered gamers and prompts the user to select an existing
	 * gamer or register a new one. If the selected gamer is not valid, the user is prompted 
	 * to register.
	 */
	private static void getGamerOrRegister(InputOutput io) {		
		try {
			io.writeLine("List of all registered gamers:");
			List<String> gamers = bullsCows.getAllGamers();
			gamers.forEach(io::writeLine);
			username = io.readString("Select existing gamer username from the list or "
					+ "enter a new one:");
			if (!gamers.contains(username) || username.isEmpty()) {
				register(io);
			}
		} catch (Exception e) {
			io.writeLine(e.getMessage());
			register(io);
		}
		
	}
	
	/**
	 * Registers a new gamer by prompting the user for a username and birthdate.
	 * If the registration is successful, a confirmation message is displayed. 
	 * Otherwise, the exception is caught, and an error message is shown.
	 */
	private static void register(InputOutput io) {
	    username = getGamerUsername(io, username);
	    LocalDate birthDate = getGamerBirthDate(io);
        bullsCows.registerGamer(username, birthDate);
        io.writeLine(String.format("Gamer \"%s\" registered successfully.", username));
	}
	
	/**
	 * Prompts the user to input a valid username. If the provided username is empty,
	 * the method keeps prompting the user until a valid (non-empty) username is entered.
	 */
	private static String getGamerUsername(InputOutput io, String username) {
	    if (username.isEmpty()) {
	        return io.readStringPredicate("Enter new gamer username:", 
	                "Username can't be empty. Please try again.", 
	                input -> !input.isEmpty());
	    }
	    return username;
	}

	/**
	 * Prompts the user to enter their birthdate and ensures that the user meets the 
	 * minimum age requirement. If the entered date is invalid, the user is prompted to try again.
	 */
	private static LocalDate getGamerBirthDate(InputOutput io) {
	    return io.readObject(String.format("Enter birthdate in format yyyy-MM-dd " +
	            "(!!!gamer must be older than %d years old!!!):", minAge), 
	            "Incorrect date or format entered. Please try again.", 
	            input -> {
	                LocalDate date = LocalDate.parse(input);
	                if (ageTest(date, minAge)) {
	                    throw new IllegalArgumentException("You are too young to play this game");
	                }
	                return date;
	            });
	}
		
	/**
	 * Checks if the given birthdate meets the minimum age requirement.
	 * Returns true if the player is too young, otherwise returns false.
	 */
	private static boolean ageTest(LocalDate birthDate, int minAge) {
	    LocalDate today = LocalDate.now();
	    return birthDate.isAfter(today.minusYears(minAge));
	}
	
	/**
	 * Allows the user to create a new game by specifying the sequence length (or default).
	 * If the game is created successfully, the game ID is displayed. Otherwise, an error message is shown.
	 */
	private static void createGame(InputOutput io) {
		int sequenceLength  = io.readInt(String.format("Enter sequence length (%d - %d) "
				+ "or 0 for default value of %d digits:", minSeqSize, maxSeqSize, defSeqSize), 
				"Incorrect data enterred.");
		gameId =  bullsCows.createGame(sequenceLength);
		io.writeLine(String.format("Game with id \"%d\" successfully created.", gameId));
	}
	
	/**
	 * Handles the selection of a game by displaying a list of available games and 
	 * allowing the user to choose one. If a valid game is selected, the provided 
	 * gameAction is executed.
	 */
	private static void handleGameSelection(InputOutput io, String title, String prompt, 
			String errorMessage, Supplier<List<Long>> gameSupplier, Runnable gameAction) {
    	io.writeLine(title);
        List<Long> games = gameSupplier.get();
        games.forEach(io::writeLine);	        
        gameId = Long.parseLong(io.readStringPredicate(
            prompt,
            errorMessage,
            input -> {
                try {
                    long gameId = Long.parseLong(input);
                    return games.contains(gameId);
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        ));	        
        gameAction.run();
	}
	
	/**
	 * Prompts the user to start one of the not-started games they have joined. 
	 * If a game is successfully selected, it shows the list of gamers in the game and starts the game.
	 */
	private static void startGame(InputOutput io) {
	    handleGameSelection(	    	
	        io,
	        String.format("List of all not started games where gamer \"%s\" joined:", username),
	        "Select game ID:",
	        "Invalid game ID. Please select game ID from the list again.",
	        () -> bullsCows.getNotStartedGamesWithGamer(username),
	        () -> {
				List<String> gameGamers = bullsCows.startGame(gameId);
				io.writeLine(String.format("The list of gamers in game \"%d\":", gameId));
				gameGamers.forEach(io::writeLine);
				playGame(io);
	        }
	    );
	}
	
	/**
	 * Allows the user to continue a game that has already started but is not yet finished.
	 * It displays the list of gamers in the selected game and resumes the game.
	 */
	private static void continueGame(InputOutput io) {
	    handleGameSelection(
	        io,
	        String.format("List of all started games where gamer \"%s\" joined:", username),
	        "Select game ID:",
	        "Invalid game ID. Please select game ID from the list again.",
	        () -> bullsCows.getStartedGamesWithGamer(username),
	        () -> {
				List<String> gameGamers = bullsCows.getGameGamers(gameId);
				io.writeLine(String.format("The list of gamers in game \"%d\":", gameId));
				gameGamers.forEach(io::writeLine);
				playGame(io);
	        }
	    );
	}
	
	/**
	 * Allows the user to join one of the not-started games where they have not yet joined.
	 * If the user successfully joins the game, a confirmation message is displayed.
	 */
	private static void joinGame(InputOutput io) {
	    handleGameSelection(
	        io,
	        String.format("List of all not started games where gamer \"%s\" not joined:", username),
	        "Select game ID:",
	        "Invalid game ID. Please select game ID from the list again.",
	        () -> bullsCows.getNotStartedGamesWithNoGamer(username),
	        () -> {
                bullsCows.gamerJoinGame(gameId, username);
                io.writeLine(String.format("Gamer \"%s\" joined game \"%d\"", username, gameId));
	        }
	    );
	}
	
	/**
	 * Starts the guessing phase of the game, prompting the user to guess the sequence 
	 * and displaying feedback (bulls and cows). If the game is over, it congratulates the player.
	 */
	private static void playGame(InputOutput io) {
		gameSeqLength = bullsCows.getGameSequenceLength(gameId);		
		Menu menu = new Menu(String.format("Game %d: guess sequence of %d digits", 
				gameId, gameSeqLength), new Item[] {
				Item.of("guess sequence", BullsCowsApplItems::guessItem),
				Item.ofExit()
		});
		menu.perform(io);		
	}
	
	/**
	 * Processes the user's guess by sending the guess to the server and receiving 
	 * feedback (bulls and cows). If the game is over, the user is notified of their win.
	 */
	private static void guessItem(InputOutput io) {
		String guess = io.readString(String.format("Enter %d non-repeated digits:", gameSeqLength));
		List<MoveData> history = bullsCows.moveProcessing(guess, gameId, username);
		history.forEach(io::writeLine);
		if (bullsCows.gameOver(gameId)) {
			io.writeLine("Congratulations! You are the winner!");
		}
	}	
}
