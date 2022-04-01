package me.maanraj514.utility;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;

@UtilityClass
public class ChatUtility {

    public String colorize(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
