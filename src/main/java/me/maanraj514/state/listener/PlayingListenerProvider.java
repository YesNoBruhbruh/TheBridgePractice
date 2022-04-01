package me.maanraj514.state.listener;

import me.maanraj514.objects.GameMap;
import me.maanraj514.objects.runtime.GameTeam;
import me.maanraj514.state.RoundStartingTask;
import me.maanraj514.utility.ChatUtility;
import me.maanraj514.utility.LocationUtility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;

public class PlayingListenerProvider extends StateListenerProvider{

    @EventHandler
    private void onItemPickup(PlayerPickupItemEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Material type = event.getBlock().getType();
        if (!type.name().equalsIgnoreCase("CLAY")) {
            event.setCancelled(true);
        }
        event.getBlock().getDrops().clear();
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        Location loc = event.getBlock().getLocation();

        GameMap map = getPlugin().getGameManager().getLoadedMap();
        List<Location> allowedLocations = LocationUtility.locationsFromTwoPoints(map.getBuildableCornerOne(), map.getBuildableCornerTwo());

        boolean canPlace = false;
        for (Location allowed : allowedLocations) {
            if (LocationUtility.coordinatesMatch(loc, allowed)) {
                canPlace = true;
                break;
            }
        }

        event.setCancelled(!canPlace);
    }

    @EventHandler
    private void onItemDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onTP(PlayerTeleportEvent event) {
        event.setCancelled(true);
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) return;

        Location loc = event.getFrom();

        for (GameTeam team : getPlugin().getGameManager().getGameTeams()) {
            List<Location> portalBlocks = LocationUtility.locationsFromTwoPoints(team.getMapTeam().getPortalCornerOne(), team.getMapTeam().getPortalCornerTwo());

            for (Location portalBlockLocation : portalBlocks) {
                if (LocationUtility.coordinatesMatch(loc, portalBlockLocation)){
                    Optional<GameTeam> playerTeam = getPlugin().getGameManager().getPlayerTeam(event.getPlayer());
                    if (!playerTeam.isPresent()) break;

                    if (team.getPlayers().contains(event.getPlayer().getUniqueId())){
                        event.getPlayer().damage(event.getPlayer().getHealth());
                    } else {
                        playerTeam.ifPresent(t -> t.setScore(team.getScore() + 1));
                        getPlugin().getGameManager().setState(new RoundStartingTask(playerTeam.get()));
                    }

                    event.getPlayer().teleport(playerTeam.get().getMapTeam().getSpawnLocation());
                    break;
                }
            }
        }
    }

    private final Map<UUID, EntityDamageEvent.DamageCause> lastDamageCauseMap = new HashMap<>();
    private final Map<UUID, String> lastDamageMap = new HashMap<>();

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)){
            return;
        }

        Player player = (Player)event.getEntity();
        if (event.getFinalDamage() < player.getHealth()){
            return;
        }

        player.getInventory().clear();
        player.playSound(player.getLocation(), Sound.ENTITY_BAT_DEATH, 1, 1);

        EntityDamageEvent.DamageCause lastDamageCause = lastDamageCauseMap.getOrDefault(player.getUniqueId(), EntityDamageEvent.DamageCause.VOID);
        switch (lastDamageCause){
            case FALL:
                Bukkit.broadcastMessage(ChatUtility.colorize("&a" + player.getDisplayName() + " &7hit the ground too hard."));
                break;
            case VOID:
            case SUICIDE:
                Bukkit.broadcastMessage(ChatUtility.colorize("&a" + player.getDisplayName() + " &7fell into the void."));
                break;
            case ENTITY_ATTACK:
                Bukkit.broadcastMessage(ChatUtility.colorize("&a" + player.getDisplayName() + " &7was kill by " + lastDamageMap.getOrDefault(player.getUniqueId(), "&can act of God!")));
                break;
            default:
                Bukkit.broadcastMessage(ChatUtility.colorize("&a" + player.getDisplayName() + " 7&died suddenly."));
                break;
        }
        Optional<GameTeam> playerTeam = getPlugin().getGameManager().getPlayerTeam(player);
        if (!playerTeam.isPresent()) return;
        player.teleport(playerTeam.get().getMapTeam().getSpawnLocation());
        getPlugin().getPlayerManager().giveItems(player);
        player.setHealth(player.getMaxHealth());
    }

    @EventHandler
    public void onDamageByOther(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)){
            return;
        }
        Player player = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        Optional<GameTeam> damagerTeam = getPlugin().getGameManager().getPlayerTeam(damager);
        Optional<GameTeam> playerTeam = getPlugin().getGameManager().getPlayerTeam(player);

        if (damagerTeam.equals(playerTeam)){
            event.setCancelled(true);
            return;
        }

        lastDamageCauseMap.put(player.getUniqueId(), event.getCause());

        if (damagerTeam.isPresent()){
            lastDamageMap.put(player.getUniqueId(), damager.getDisplayName());
        }

        if (event.getFinalDamage() >= player.getHealth()){
            damager.playSound(damager.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        }
    }

    @EventHandler
    private void onFoodChange(FoodLevelChangeEvent event){
        event.setFoodLevel(20);
    }
}