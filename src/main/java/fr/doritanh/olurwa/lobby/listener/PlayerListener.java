package fr.doritanh.olurwa.lobby.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import fr.doritanh.olurwa.lobby.Lobby;
import fr.doritanh.olurwa.lobby.inventory.MenuInventory;

public class PlayerListener implements Listener {

	private void sendMenu(Player p) {
		final ItemStack itemMenu = new ItemStack(Material.CLOCK, 1);
		final ItemMeta meta = itemMenu.getItemMeta();
		meta.setDisplayName("Lobby menu");
		itemMenu.setItemMeta(meta);
		p.getInventory().addItem(itemMenu);
	}

	/**
	 * When player login
	 * 
	 * @param e
	 */
	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent e) {
		e.getPlayer().teleport(Lobby.get().getSpawn());
		e.setJoinMessage(null);
		e.getPlayer().getInventory().clear();

		Lobby.get().getTabList().sendHeaderFooter(e.getPlayer());
		Lobby.get().getTabList().updateLobby();

		for (Player p : Bukkit.getOnlinePlayers()) {
			Lobby.get().getTabList().send(p);
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				Lobby.get().getTabList().requestUpdateServers();

			}
		}.runTaskLater(Lobby.get(), 20);

		this.sendMenu(e.getPlayer());
	}

	/**
	 * When player disconnect
	 * 
	 * @param e
	 */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		e.setQuitMessage(null);
		Lobby.get().getTabList().updateLobby(e.getPlayer());

		for (Player p : Bukkit.getOnlinePlayers()) {
			Lobby.get().getTabList().send(p);
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				Lobby.get().getTabList().requestUpdateServers();

			}
		}.runTaskLater(Lobby.get(), 20);
	}

	/**
	 * When player interact
	 * 
	 * @param e
	 */
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getItem() != null && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("Lobby menu")) {
				MenuInventory m = new MenuInventory();
				m.openInventory(e.getPlayer());
			}
		}
	}

	/**
	 * Disable player drop
	 * 
	 * @param e
	 */
	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}

	/**
	 * Disable player food
	 * 
	 * @param e
	 */
	@EventHandler
	public void onPlayerLooseFood(FoodLevelChangeEvent e) {
		if (e.getEntityType() == EntityType.PLAYER) {
			e.setFoodLevel(20);
		}
	}

	/**
	 * Disable player damage
	 * 
	 * @param e
	 */
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.getCause() == DamageCause.DROWNING || e.getCause() == DamageCause.FALL
				|| e.getCause() == DamageCause.FIRE) {
			e.setCancelled(true);
		}
	}

	/**
	 * Player respawn
	 * 
	 * @param e
	 */
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		e.setRespawnLocation(Lobby.get().getSpawn());
	}

	/**
	 * Delete menu when player die
	 * 
	 * @param e
	 */
	@EventHandler
	public void onPlayerDie(PlayerDeathEvent e) {
		e.setKeepInventory(true);
	}

	/**
	 * TP player when falling in the void
	 * 
	 * @param e
	 */
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (e.getPlayer().getLocation().getY() <= 1) {
			e.getPlayer().teleport(Lobby.get().getSpawn());
		}
	}

	/**
	 * Change the command suggestion list
	 * 
	 * @param e
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerCommandPreprocess(PlayerCommandSendEvent e) {
		e.getPlayer().sendMessage("Hello ");
		e.getCommands().clear();
		e.getCommands().add("help");
	}
}
