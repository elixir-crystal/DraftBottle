package DraftBottle.misc.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Objects;

public class BottleStorageUtils {

    private static final String SEP = File.separator;
    private static final Plugin PLUG = Bukkit.getServer().getPluginManager().getPlugin("DraftBottle");
    private static final String ROOT = Objects.requireNonNull(PLUG).getDataFolder().getPath();

    public static YamlConfiguration getBottleConf(String id) {
        File t = new File(ROOT + SEP + "storage" + SEP + "bottle" + SEP + "id.yml");
        return YamlConfiguration.loadConfiguration(t);
    }

    public static void writeConf(String id, ItemStack... contents) {
        YamlConfiguration y = getBottleConf(id);
        y.set("conf", new BottleConf(id, contents));
    }

    public static BottleConf readConf(String id) {
        YamlConfiguration y = getBottleConf(id);
        return (BottleConf) y.get("conf", new BottleConf("", new ItemStack[]{}));
    }

    @RequiredArgsConstructor
    @Getter
    public static class BottleConf {

        private final String id;
        private final ItemStack[] contents;

    }

}
