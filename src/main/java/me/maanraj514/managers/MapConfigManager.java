package me.maanraj514.managers;

import lombok.SneakyThrows;
import me.maanraj514.BridgePlugin;
import me.maanraj514.objects.GameMap;
import me.maanraj514.utility.ConfigurationFile;

import java.io.File;

public class MapConfigManager {

    private final ConfigurationFile mapsFile;

    @SneakyThrows
    public MapConfigManager(BridgePlugin plugin) {
        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();

        File mapsFile = new File(plugin.getDataFolder(), "maps.yml");
        if (!mapsFile.exists()) mapsFile.createNewFile();

        this.mapsFile = new ConfigurationFile(plugin, "maps");
    }

    public ConfigurationFile getMapsFile() {
        return mapsFile;
    }

    public void saveMapsFile() {
        mapsFile.save();
    }

    public void save(GameMap gameMap) {
        gameMap.write(mapsFile.getConfiguration().createSection(gameMap.getName()));
    }
}
