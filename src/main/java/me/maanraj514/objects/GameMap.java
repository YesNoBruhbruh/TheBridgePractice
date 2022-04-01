package me.maanraj514.objects;

import lombok.Data;
import lombok.Setter;
import me.maanraj514.utility.LocationUtility;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class GameMap {

    private String name;
    @Setter private List<MapTeam> teams;
    private GameMapMode gameMapMode;

    private Location buildableCornerOne;
    private Location buildableCornerTwo;

    public GameMap(ConfigurationSection section) {
        this.name = section.getString("name");

        ConfigurationSection teamSection = section.getConfigurationSection("teams");
        this.teams = teamSection.getKeys(false).stream()
                .map(key -> {
                    return new MapTeam(teamSection.getConfigurationSection(key));
                }).collect(Collectors.toList());

        this.gameMapMode = GameMapMode.valueOf(section.getString("gameMapMode"));
        this.buildableCornerOne = LocationUtility.read(section.getConfigurationSection("buildableCornerOne"));
        this.buildableCornerTwo = LocationUtility.read(section.getConfigurationSection("buildableCornerTwo"));
    }

    public void write(ConfigurationSection section) {
        section.set("name", name);
        section.set("teams", null);
        ConfigurationSection teamSection = section.createSection("teams");
        teams.forEach(team -> {
            ConfigurationSection sec = teamSection.createSection(UUID.randomUUID().toString());
            team.write(sec);
        });
        section.set("gameMapMode", gameMapMode.name());
        LocationUtility.write(buildableCornerOne, section.createSection("buildableCornerOne"));
        LocationUtility.write(buildableCornerTwo, section.createSection("buildableCornerTwo"));
    }

}
