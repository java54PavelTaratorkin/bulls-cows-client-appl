package telran.net.games.service;

import java.time.LocalDate;
import java.util.List;

import telran.net.games.model.*;

public interface BullsCowsService {
    long createGame(int sequenceLength);
    List<String> startGame(long gameId);
    void registerGamer(String username, LocalDate birthDate);
    void gamerJoinGame(long gameId, String username);
    List<Long> getNotStartedGames();
    List<MoveData> moveProcessing(String sequence, long gameId, String username);
    boolean gameOver(long gameId);
    List<String> getGameGamers(long gameId);
    
    /**
     * Returns a list of game IDs for games that have not yet started and where the specified gamer has joined.
     * 
     * @param username the username of the gamer
     * @return list of game IDs
     */
    List<Long> getNotStartedGamesWithGamer(String username);
    
    /**
     * Returns a list of game IDs for not-started games where the specified gamer has not yet joined.
     * 
     * @param username the username of the gamer
     * @return list of game IDs
     */
    List<Long> getNotStartedGamesWithNoGamer(String username);
    
    /**
     * Returns a list of game IDs for games that have already started and where the specified gamer has joined.
     * 
     * @param username the username of the gamer
     * @return list of game IDs
     */
    List<Long> getStartedGamesWithGamer(String username);
    
    /**
     * Logs in the specified gamer by username and returns the logged-in username.
     * 
     * @param username the username to log in
     * @return the logged-in username
     */
    String loginGamer(String username);
    
    /**
     * Returns a list of all registered gamers.
     * 
     * @return list of gamer usernames
     */
    List<String> getAllGamers();
    
    /**
     * Returns the sequence length for the specified game ID.
     * 
     * @param gameId the game ID
     * @return the sequence length
     */
    int getGameSequenceLength(long gameId);
    
    /**
     * Retrieves default game settings such as sequence size and minimum age.
     * 
     * @return the default game data
     */
    GameDefaultData getGameDefaults();
}