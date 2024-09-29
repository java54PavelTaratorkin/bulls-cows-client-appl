package telran.games;

import telran.net.TcpClient;
import telran.net.games.contoller.BullsCowsProxy;
import telran.net.games.menu.BullsCowsApplItems;
import telran.net.games.service.*;
import telran.view.*;
import java.util.*;

public class BullsCowsClientAppl {

	private static final int PORT = 4000;

	public static void main(String[] args) {
		
		int port = args.length > 0 ? Integer.parseInt(args[0]) : PORT;
		TcpClient tcpClient = new TcpClient("localhost", port);
		BullsCowsService bullsCows = new BullsCowsProxy(tcpClient);

		List<Item> items = BullsCowsApplItems.getItems(bullsCows);
		items.add(Item.of("Exit & Close connection", io -> tcpClient.close(), true));
		Menu menu = new Menu("Bulls and Cows Network Game",
				items.toArray(Item[]::new));
		menu.perform(new SystemInputOutput());

	}

}
