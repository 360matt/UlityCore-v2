package fr.ulity.superjails.bukkit.api;

import de.leonhard.storage.sections.FlatFileSection;
import fr.ulity.core.api.Config;
import fr.ulity.core.api.Data;
import fr.ulity.core.utils.EnumUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public final class JailsSystem {
    public static Config jails = new Config("jails", "jails");
    public static Data players = new Data("jails", "players");

    public static Status createJail (String name, Location loc) {
        Status response = new Status();

        if (jails.isSet(name))
            response.setStatus(false, "already exist");
        else {
            FlatFileSection section = jails.getSection("jails." + name);
            section.set("type", "MODERATION");
            section.set("x", loc.getX());
            section.set("y", loc.getY());
            section.set("z", loc.getZ());
            section.set("world", Objects.requireNonNull(loc.getWorld()).getName());
            section.set("permission", "ulity.superjails.jails.default");
        }

        return response;
    }

    public static Status removeJail (String name) {
        Status response = new Status();

        if (!jails.isSet("jails." + name))
            response.setStatus(false, "no exist");
        else
            jails.remove("jails." + name);

        return response;
    }

    public static boolean exist (String name) {
        return jails.isSet("jails." + name);
    }

    public static Status isValid (String name) {
        Status response = new Status();

        FlatFileSection section = jails.getSection("jails." + name);
        String worldname = jails.getString(name + ".world");
        String type = section.getString("type");

        if (!exist(name))
            response.setStatus(false, "no exist");
        else if (Bukkit.getWorld(worldname) == null)
            response.setStatus(false, "world no exist", worldname);
        else if (type == null || !EnumUtil.contains(Arrays.asList(JailsSystem.JailType.values()), type.toUpperCase()))
            response.setStatus(false, "type undefined");

        return response;
    }

    public static Status getLocation (String name) {
        Status response = new Status();

        Status checkValid = isValid(name);
        if (!checkValid.success)
            response.setStatus(false, checkValid.code, checkValid.data);
        else {
            FlatFileSection section = jails.getSection("jails." + name);
            World world = Bukkit.getWorld(section.getString("world"));
            Location loc = new Location(world, section.getDouble("x"), section.getDouble("y"), section.getDouble("z"));

            response.data = loc;
        }

        return response;
    }

    public static Status setCustomMessage (String name, String message) {
        Status response = new Status();

        if (!exist(name))
            response.setStatus(false, "no exist");
        else {
            FlatFileSection section = jails.getSection("jails." + name);
            section.set("message", message);
        }

        return response;
    }


    public static Status getCustomMessage (String name) {
        Status response = new Status();

        if (!exist(name))
            response.setStatus(false, "no exist");
        else {
            FlatFileSection section = jails.getSection("jails." + name);
            String message = section.getString("message");

            if (message == null)
                response.setStatus(false, "message undefined");
            else
                response.data = ChatColor.translateAlternateColorCodes('&', message);
        }

        return response;
    }

    public static String[] getAllJails () {
        return jails.singleLayerKeySet("jails").toArray(new String[]{});
    }

    public static Status setType (String name, JailType type) {
        Status response = new Status();

        if (!exist(name))
            response.setStatus(false, "no exist");
        else {
            FlatFileSection section = jails.getSection("jails." + name);
            section.set("type", type);
        }

        return response;
    }


    public static Status getType (String name) {
        Status response = new Status();

        if (!exist(name))
            response.setStatus(false, "no exist");
        else {
            FlatFileSection section = jails.getSection("jails." + name);
            String type = section.getString("type");

            if (type == null || !EnumUtil.contains(Arrays.asList(JailsSystem.JailType.values()), type.toUpperCase()))
                response.setStatus(false, "type undefined");
            else
                response.data = JailsSystem.JailType.valueOf(type.toUpperCase());
        }

        return response;
    }

    public static Status getPermission (String name) {
        Status response = new Status();

        if (!exist(name))
            response.setStatus(false, "no exist");
        else {
            FlatFileSection section = jails.getSection("jails." + name);
            String permission = section.getString("permission");

            if (permission == null)
                response.setStatus(false, "type undefined");
            else
                response.data = permission;
        }

        return response;
    }

    public enum JailType {
        MODERATION,
        RP;
    }



    public static Status jailPlayer (String playername, String jailname, JailData data) {
        Status response = new Status();
        Status valid = isValid(jailname);

        if (!valid.success)
            response.setStatus(false, valid.code, valid.data);
        else {
            FlatFileSection sectionJail = jails.getSection("jails." + jailname);
            FlatFileSection sectionPlayer = players.getSection("players." + playername);
            FlatFileSection sectionPlayerNewJail = players.getSection("players." + playername + ".jails." + getType(jailname).data.toString());
            sectionPlayerNewJail.set("", data);
        }

        return response;
    }


    public static class JailData {
        public String reason;
        public long timestamp = new Date().getTime();
        public String staff;
        public long expire;
        public String jailname;
    }

    public static class Status {
        public Boolean success = true;
        public String code = "success";
        public Object data = null;

        private void setStatus(Boolean success, String err) {
            this.success = success;
            this.code = err;
        }

        private void setStatus(Boolean success, String err, Object data) {
            this.success = success;
            this.code = err;
            this.data = data;
        }
    }
}
