package me.maanraj514.objects;

import lombok.Data;
import me.maanraj514.utility.LocationUtility;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

@Data
public class MapTeam {

    private String name;
    private Location spawnLocation;
    private Location portalCornerOne;
    private Location portalCornerTwo;
    private Color color;
    private Material blockType;

    public MapTeam(ConfigurationSection section){
        spawnLocation = LocationUtility.read(section.getConfigurationSection("spawnLocation"));
        portalCornerOne = LocationUtility.read(section.getConfigurationSection("portalCornerOne"));
        portalCornerTwo = LocationUtility.read(section.getConfigurationSection("portalCornerTwo"));
        color = section.getColor("color");
        blockType = Material.valueOf(section.getString("blockType"));
        name = section.getString("name");
    }

    public void write(ConfigurationSection section) {
        LocationUtility.write(spawnLocation, section.createSection("spawnLocation"));
        LocationUtility.write(portalCornerOne, section.createSection("portalCornerOne"));
        LocationUtility.write(portalCornerTwo, section.createSection("portalCornerTwo"));
        section.set("color", color);
        section.set("blockType", blockType.name());
        section.set("name", name);
    }
}