package me.maanraj514.state;

import me.maanraj514.BridgePlugin;
import me.maanraj514.objects.GameMap;
import me.maanraj514.state.listener.PregameListenerProvider;
import me.maanraj514.state.listener.StateListenerProvider;
import me.maanraj514.utility.FileUtility;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class MapSelectionState extends GameState{

    @Override
    public void onEnable(BridgePlugin plugin) {
        super.onEnable(plugin);

        Set<String> keys = plugin.getMapConfigManager().getMapsFile().getConfiguration().getKeys(false);
        if (keys.size() == 0) {
            Bukkit.getLogger().severe("NO MAPS CONFIGURED!");
            return;
        }

        Bukkit.getLogger().info(Arrays.toString(keys.toArray()));

        String selected;
        if (keys.size() == 1){
            selected = (String) keys.toArray()[0];
        }else {
            selected = (String) keys.toArray()[ThreadLocalRandom.current().nextInt(keys.size() - 1)];
        }
        ConfigurationSection section = plugin.getMapConfigManager().getMapsFile().getConfiguration().getConfigurationSection(selected);

        String sourceWorldFolderName = section.getString("sourceWorldFolder");
        String destinationWorldFolderName = section.getString("destinationWorldFolder");

        File sourceWorldFolder = null;
        File destinationWorldFolder = null;
        try{
            sourceWorldFolder = new File(
                    getPlugin().getDataFolder().getCanonicalPath() + File.separator + ".." + File.separator + ".." + File.separator + sourceWorldFolderName
            );

            destinationWorldFolder = new File(getPlugin().getDataFolder().getCanonicalPath() + File.separator + ".." + File.separator + ".." + File.separator + destinationWorldFolderName);
        } catch (IOException e){
            e.printStackTrace();
        }

        try {
            assert sourceWorldFolder != null;
            FileUtility.copy(sourceWorldFolder, destinationWorldFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert destinationWorldFolderName != null;
        WorldCreator creator = new WorldCreator(destinationWorldFolderName);
        World world = creator.createWorld();
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setAutoSave(false);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.DISABLE_RAIDS, true);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        world.setDifficulty(Difficulty.NORMAL);
        world.setWeatherDuration(0);

        for (Entity entity : world.getEntities()){
            entity.remove();
        }

        GameMap map = new GameMap(section);
        getGameManager().setLoadedMap(map);
        getGameManager().setState(new WaitingState());
    }

    @Override
    public StateListenerProvider getListenerProvider() {
        return new PregameListenerProvider();
    }
}
