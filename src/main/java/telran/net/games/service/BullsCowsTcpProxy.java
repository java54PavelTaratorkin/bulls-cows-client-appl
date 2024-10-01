package telran.net.games.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.json.JSONObject;

import telran.net.Request;
import telran.net.TcpClient;
import telran.net.games.model.GameGamerDto;
import telran.net.games.model.MoveData;
import telran.net.games.model.SequenceGameGamerDto;
import telran.net.games.model.UsernameBirthdate;

public class BullsCowsTcpProxy implements BullsCowsService {
	private TcpClient tcpClient;
	
	public BullsCowsTcpProxy(TcpClient tcpClient) {
		this.tcpClient = tcpClient;
	}

	@Override
	public long createGame() {
		String gameIdStr = tcpClient.sendAndReceive(new Request("createGame", ""));
		return Long.parseLong(gameIdStr);
	}

	@Override
	public List<String> startGame(long gameId) {
		String gamersStr = tcpClient.sendAndReceive
				(new Request("startGame", Long.toString(gameId)));
		return getListObjects(gamersStr, Function.identity());
	}

	@Override
	public void registerGamer(String username, LocalDate birthDate) {
		Request request = new Request("registerGamer", new UsernameBirthdate(username, birthDate)
				.toString());
		tcpClient.sendAndReceive(request);

	}

	@Override
	public void gamerJoinGame(long gameId, String username) {
		GameGamerDto gameGamer = new GameGamerDto(gameId, username);
		Request request = new Request("gamerJoinGame",
				gameGamer.toString());
		tcpClient.sendAndReceive(request);

	}

	@Override
	public List<Long> getNotStartedGames() {
		String listOfData = tcpClient.sendAndReceive(
				new Request("getNotStartedGames", ""));
		return getListObjects(listOfData, Long::parseLong);
	}

	@Override
	public List<MoveData> moveProcessing
	(String sequence, long gameId, String username) {
		SequenceGameGamerDto sggd =
				new SequenceGameGamerDto(sequence, gameId, username);
		String listOfData = tcpClient.sendAndReceive(new Request("moveProcessing", sggd.toString()));
		return getListObjects(listOfData, s -> new MoveData(new JSONObject(s)) );
	}

	@Override
	public boolean gameOver(long gameId) {
		String resStr = tcpClient.sendAndReceive(new Request("gameOver", Long.toString(gameId)));
		return Boolean.parseBoolean(resStr);
	}

	@Override
	public List<String> getGameGamers(long gameId) {
		String listOfData = tcpClient.sendAndReceive(new Request("getGameGamers",
				Long.toString(gameId)));
		return getListObjects(listOfData, Function.identity());
	}

	@Override
	public List<Long> getNotStartedGamesWithGamer(String username) {
		String listOfData = tcpClient.sendAndReceive
				(new Request("getNotStartedGamesWithGamer", username));
		return getListObjects(listOfData, Long::parseLong);
	}

	@Override
	public List<Long> getNotStartedGamesWithOutGamer(String username) {
		String listOfData = tcpClient.sendAndReceive
				(new Request("getNotStartedGamesWithOutGamer", username));
		return getListObjects(listOfData, Long::parseLong);
	}

	@Override
	public List<Long> getStartedGamesWithGamer(String username) {
		String listOfData = tcpClient.sendAndReceive
				(new Request("getStartedGamesWithGamer", username));
		
		return getListObjects(listOfData, Long::parseLong);
	}

	@Override
	public String loginGamer(String username) {
		
		return tcpClient.sendAndReceive(new Request("loginGamer", username));
	}
	
	
	
	private<T> List<T> getListObjects(String listOfData,
			Function<String, T> mapper) {
		return listOfData.isEmpty() ? Collections.emptyList() : Arrays.stream(listOfData.split(";"))
				.map(mapper)
				.toList();
	}

}
