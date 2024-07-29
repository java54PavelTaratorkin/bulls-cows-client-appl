package telran.games;

import telran.net.TcpClient;
import telran.view.*;
import java.util.*;
public class BullsCowsClientAppl {

	private static final int PORT = 5000;

	public static void main(String[] args) {
		TcpClient tcpClient = new TcpClient("localhost", PORT);
		BullsCowsService bullsCows = new BullsCowsProxy(tcpClient);
		List<Item> items = BullsCowsApplItems.getItems(bullsCows);
		items.add(Item.of("Exit & Close connection", io -> tcpClient.close(), true));
		Menu menu = new Menu("Bulls and Cows Network Game",
				items.toArray(Item[]::new));
		menu.perform(new SystemInputOutput());

	}

}
