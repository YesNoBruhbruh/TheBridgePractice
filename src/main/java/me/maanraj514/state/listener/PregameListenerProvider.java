package me.maanraj514.state.listener;

import me.maanraj514.objects.GameMap;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class PregameListenerProvider extends StateListenerProvider {

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        GameMap map = getPlugin().getGameManager().getLoadedMap();
        if (map == null) return;
        Player player = event.getPlayer();
        player.teleport(map.getTeams().get(0).getSpawnLocation());
        player.setGameMode(GameMode.SURVIVAL);
        player.setLevel(0);
        player.setExp(0);
        player.setWalkSpeed(0.2f);
        player.getInventory().clear();
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    private void onItemDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onArrowShoot(ProjectileLaunchEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onItemPickup(PlayerPickupItemEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    private void onFoodChange(FoodLevelChangeEvent event){
        event.setFoodLevel(20);
    }
}
