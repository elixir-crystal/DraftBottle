package draft_bottle;

import draft_bottle.commands.CommandBus;
import draft_bottle.configs.ConfigBus;
import draft_bottle.listeners.ListenerBus;
import draft_bottle.misc.Utils;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.commandmanager.CommandParser;
import redempt.redlib.config.ConfigManager;

public class PlugGividado extends JavaPlugin {

    public ConfigManager cman;

    @Override
    public void onEnable() {

        Utils.init();

        cman = ConfigManager.create(this).target(ConfigBus.class).saveDefaults().load();
        new CommandParser(this.getResource("commands.rdcml")).parse().register("draftbottle", new CommandBus());
        new ListenerBus(this).register();

    }

}
