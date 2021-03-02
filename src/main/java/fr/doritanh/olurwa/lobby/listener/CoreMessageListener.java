package fr.doritanh.olurwa.lobby.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import fr.doritanh.olurwa.lobby.Lobby;

public class CoreMessageListener implements PluginMessageListener {

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		System.out.println("Arrive de " + channel);
		if (!channel.equals("olurwa:core")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		System.out.println("Subchannel : " + subchannel);
		if (subchannel.equals("PlayerList")) {
			String server = in.readUTF();
			System.out.println("Server : " + server);
			String[] playerList = in.readUTF().split(", ");
			if (server.equalsIgnoreCase("creative")) {
				Lobby.get().getTabList().updateCreative(playerList);
			}
			for (Player p : Bukkit.getOnlinePlayers()) {
				Lobby.get().getTabList().send(p);
			}
		}
	}

}
