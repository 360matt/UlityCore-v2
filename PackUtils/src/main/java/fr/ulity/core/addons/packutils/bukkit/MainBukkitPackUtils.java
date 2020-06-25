package fr.ulity.core.addons.packutils.bukkit;

import fr.ulity.core.addons.packutils.bukkit.commands.economy.BalanceCommand;
import fr.ulity.core.addons.packutils.bukkit.commands.economy.EcoCommand;
import fr.ulity.core.addons.packutils.bukkit.commands.economy.PayCommand;
import fr.ulity.core.addons.packutils.bukkit.commands.gamemode.*;
import fr.ulity.core.addons.packutils.bukkit.commands.players.FlyCommand;
import fr.ulity.core.addons.packutils.bukkit.commands.teleports.*;
import fr.ulity.core.addons.packutils.bukkit.methods.EconomyMethods;
import fr.ulity.core.api.Api;
import fr.ulity.core.api.Config;
import fr.ulity.core.api.Initializer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class MainBukkitPackUtils extends JavaPlugin {
    public static MainBukkitPackUtils plugin;
    public static Config config;

    public static CommandEnabler enabler;

    @SuppressWarnings("deprecation")
    public static boolean setupEconomy () {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null)
            return false;


        plugin.getServer().getServicesManager().register(Economy.class, new EconomyMethods(), plugin, ServicePriority.Highest);
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;

        return true;
    }

    @Override
    public void onEnable() {
        plugin = this;

        if (setupEconomy()) {
            Initializer init = new Initializer(this);
            init.requireVersion("2.2");
            init.reloadLang();

            if (init.ok) {
                config = new Config("config", "addons/PackUtils");
                DefaultConfig.applique();

                enabler = new CommandEnabler();

                new GamemodeCommand(Api.Bukkit.commandMap, this);
                new GmcCommand(Api.Bukkit.commandMap, this);
                new GmsCommand(Api.Bukkit.commandMap, this);
                new GmaCommand(Api.Bukkit.commandMap, this);
                new GmpCommand(Api.Bukkit.commandMap, this);

                new FlyCommand(Api.Bukkit.commandMap, this);
                new TpCommand(Api.Bukkit.commandMap, this);
                new TpaCommand(Api.Bukkit.commandMap, this);
                new TpyesCommand(Api.Bukkit.commandMap, this);
                new TpnoCommand(Api.Bukkit.commandMap, this);
                new TopCommand(Api.Bukkit.commandMap, this);
                new BackCommand(Api.Bukkit.commandMap, this);
                new SetspawnCommand(Api.Bukkit.commandMap, this);
                new SpawnCommand(Api.Bukkit.commandMap, this);
                new SethomeCommand(Api.Bukkit.commandMap, this);
                new HomeCommand(Api.Bukkit.commandMap, this);
                new DelhomeCommand(Api.Bukkit.commandMap, this);

                new EcoCommand(Api.Bukkit.commandMap, this);
                new BalanceCommand(Api.Bukkit.commandMap, this);
                new PayCommand(Api.Bukkit.commandMap, this);

            }
        } else
            getLogger().severe("Can't hook with Vault !");


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}