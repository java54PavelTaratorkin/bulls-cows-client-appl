package telran.games;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import telran.games.dto.Move;
import telran.games.dto.MoveResult;
import telran.net.Request;
import telran.net.TcpClient;

public class BullsCowsProxy implements BullsCowsService{
TcpClient tcpClient;


public BullsCowsProxy(TcpClient tcpClient) {
	this.tcpClient = tcpClient;
}

@Override
public long createNewGame() {
	String strRes = tcpClient.sendAndReceive(new Request("createNewGame", ""));
	
	return Long.parseLong(strRes);
}

@Override
public List<MoveResult> getResults(Move move) {
	String strRes = tcpClient.sendAndReceive(new Request("getResults", move.toString()));
	return Arrays.stream(strRes.split(";"))
			.map(s -> new MoveResult(new JSONObject(s))).toList();
}

@Override
public boolean isGameOver(long gameId) {
	String strRes = tcpClient.sendAndReceive(new Request("isGameOver",
			"" + gameId));
	return Boolean.parseBoolean(strRes);
}

}
