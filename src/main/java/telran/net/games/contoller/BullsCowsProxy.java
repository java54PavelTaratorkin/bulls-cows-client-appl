package telran.net.games.contoller;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

import org.json.JSONObject;

import telran.net.*;
import telran.net.games.model.*;
import telran.net.games.service.BullsCowsService;

public class BullsCowsProxy implements BullsCowsService{
	TcpClient tcpClient;
	
	public BullsCowsProxy(TcpClient tcpClient) {
		this.tcpClient = tcpClient;
	}
	
	@Override
	public long createGame(int sequenceLength) {
		String result = tcpClient.sendAndReceive(new Request("createGame", 
				String.valueOf(sequenceLength)));
		return Long.parseLong(result);
	}
	
	@Override
	public List<String> startGame(long gameId) {
		String result = tcpClient.sendAndReceive(new Request("startGame", String.valueOf(gameId)));
		return List.of(result.split(";"));
	}
	
	@Override
	public void registerGamer(String username, LocalDate birthDate) {
	    GamerData gamerData = new GamerData(username, birthDate);
	    tcpClient.sendAndReceive(new Request("registerGamer", gamerData.toString()));		
	}
	
	@Override
	public void gamerJoinGame(long gameId, String username) {
		GameGamerDto gameGamer = new GameGamerDto(gameId, username);
		tcpClient.sendAndReceive(new Request("gamerJoinGame", gameGamer.toString()));		
	}
	
	@Override
	public List<Long> getNotStartedGames() {
		String result = tcpClient.sendAndReceive(new Request("getNotStartedGames", ""));
		return resultsFromJSON(result, Long::valueOf);
	}
	
	@Override
	public List<MoveData> moveProcessing(String sequence, long gameId, String username) {
		SequenceGameGamerDto sggd = new SequenceGameGamerDto(sequence, gameId, username);
		String result = tcpClient.sendAndReceive(new Request("moveProcessing", sggd.toString()));
		return resultsFromJSON(result, str -> new MoveData(new JSONObject(str))); 
	}
	
	@Override
	public boolean gameOver(long gameId) {
		String result = tcpClient.sendAndReceive(new Request("gameOver", 
				String.valueOf(gameId)));		
		return Boolean.valueOf(result);
	}
	
	@Override
	public List<String> getGameGamers(long gameId) {
		String result = tcpClient.sendAndReceive(new Request("getGameGamers", 
				String.valueOf(gameId)));
		return List.of(result.split(";"));
	}

	@Override
	public List<Long> getNotStartedGamesWithGamer(String username) {
		String result = tcpClient.sendAndReceive(new Request("getNotStartedGamesWithGamer", 
				username));
		return resultsFromJSON(result, Long::valueOf);
	}

	@Override
	public List<Long> getNotStartedGamesWithNoGamer(String username) {
		String result = tcpClient.sendAndReceive(new Request("getNotStartedGamesWithNoGamer", 
				username));
		return resultsFromJSON(result, Long::valueOf);
	}

	@Override
	public List<Long> getStartedGamesWithGamer(String username) {
		String result = tcpClient.sendAndReceive(new Request("getStartedGamesWithGamer", 
				username));
		return resultsFromJSON(result, Long::valueOf);
	}

	@Override
	public String loginGamer(String username) {
		String result = tcpClient.sendAndReceive(new Request("loginGamer", username));
		return result;
	}

	@Override
	public List<String> getAllGamers() {
		String result = tcpClient.sendAndReceive(new Request("getAllGamers", ""));
        return List.of(result.split(";"));
	}

	@Override
	public int getGameSequenceLength(long gameId) {
		String result = tcpClient.sendAndReceive(new Request("getGameSequenceLength", 
				String.valueOf(gameId)));
		return Integer.parseInt(result);
	}
	
	private <T> List<T> resultsFromJSON(String input, Function<String, T> constructor) {
        return Arrays.stream(input.split(";"))
                .map(constructor)
                .toList();
	}

	@Override
	public GameDefaultData getGameDefaults() {
		String result = tcpClient.sendAndReceive(new Request("getGameDefaults", ""));
		return new GameDefaultData(new JSONObject(result));
	}

}
