package me.maanraj514.managers;

import lombok.RequiredArgsConstructor;
import me.maanraj514.BridgePlugin;
import me.maanraj514.objects.runtime.GameTeam;
import me.maanraj514.utility.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@RequiredArgsConstructor
public class PlayerManager {

    private final BridgePlugin plugin;

    public void giveItems(Player player) {
        player.getInventory().clear();

        Optional<GameTeam> gameTeamOptional = plugin.getGameManager().getPlayerTeam(player);
        if (!gameTeamOptional.isPresent()) return;

        player.getInventory()
                .setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE)
                        .setLeatherArmorColor(gameTeamOptional.get().getMapTeam().getColor()).build());
        player.getInventory()
                .setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS)
                        .setLeatherArmorColor(gameTeamOptional.get().getMapTeam().getColor()).build());
        player.getInventory()
                .setBoots(new ItemBuilder(Material.LEATHER_BOOTS)
                        .setLeatherArmorColor(gameTeamOptional.get().getMapTeam().getColor()).build());
        player.getInventory()
                .setItem(0, new ItemBuilder(Material.IRON_SWORD)
                        .addEnchant(Enchantment.DAMAGE_ALL, 1).build());
        player.getInventory()
                        .setItem(1, new ItemBuilder(Material.BOW).build());
        player.getInventory().setItem(2, new ItemBuilder(Material.DIAMOND_PICKAXE)
                .addEnchant(Enchantment.DIG_SPEED, 2).build());
        player.getInventory().addItem(new ItemBuilder(gameTeamOptional.get().getMapTeam().getBlockType(), 64).build());
        player.getInventory().addItem(new ItemBuilder(gameTeamOptional.get().getMapTeam().getBlockType(), 64).build());
        player.getInventory().addItem(new ItemBuilder(Material.GOLDEN_APPLE, 8).build());
        player.getInventory().addItem(new ItemBuilder(Material.ARROW,1).build());

        player.getInventory().setHeldItemSlot(0);
    }
}
