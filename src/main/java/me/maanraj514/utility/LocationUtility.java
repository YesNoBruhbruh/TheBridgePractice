package me.maanraj514.utility;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class LocationUtility {

    public void write(Location loc, ConfigurationSection section){
        section.set("worldName", loc.getWorld().getName());
        section.set("x", loc.getX());
        section.set("y", loc.getY());
        section.set("z", loc.getZ());
        section.set("yaw", loc.getYaw());
        section.set("pitch", loc.getPitch());
    }

    public Location read(ConfigurationSection section) {
        World world = Bukkit.createWorld(new WorldCreator(section.getString("worldName")));
        return new Location(
                world,
                section.getDouble("x"),
                section.getDouble("y"),
                section.getDouble("z"),
                (float) section.getDouble("yaw"),
                (float) section.getDouble("pitch"));
    }

    public List<Location> locationsFromTwoPoints(Location loc1, Location loc2) {
        List<Location> locations = new ArrayList<>();

        int topBlockX = (Math.max(loc1.getBlockX(), loc2.getBlockX()));
        int bottomBlockX = (Math.min(loc1.getBlockX(), loc2.getBlockX()));

        int topBlockY = (Math.max(loc1.getBlockY(), loc2.getBlockY()));
        int bottomBlockY = (Math.min(loc1.getBlockY(), loc2.getBlockY()));

        int topBlockZ = (Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
        int bottomBlockZ = (Math.min(loc1.getBlockZ(), loc2.getBlockZ()));

        for (int x = bottomBlockX; x <= topBlockX; x++)
        {
            for (int z = bottomBlockZ; z <= topBlockZ; z++)
            {
                for (int y = bottomBlockY; y <= topBlockY; y++)
                {

                    locations.add(loc1.getWorld().getBlockAt(x,y,z).getLocation());
                }
            }
        }

        return locations;
    }

    public boolean coordinatesMatch(Location one, Location two){
        if (one == null || two == null) return false;

        return one.getBlockX() == two.getBlockX() &&
                one.getBlockY() == two.getBlockY() &&
                one.getBlockZ() == two.getBlockZ();
    }

    public boolean coordinatesMatchWithoutY(Location one, Location two) {
        if (one == null || two == null) return false;

        return one.getBlockX() == two.getBlockX() &&
                one.getBlockZ() == two.getBlockZ();
    }
}
