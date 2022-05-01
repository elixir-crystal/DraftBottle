package DraftBottle.misc.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DraftStorageUtils {

    private static final String SEP = File.separator;
    private static final Plugin PLUG = Bukkit.getServer().getPluginManager().getPlugin("DraftBottle");
    private static final String ROOT = Objects.requireNonNull(PLUG).getDataFolder().getPath();
    private static final File T = new File(ROOT + SEP + "storage" + SEP + "draft.yml");

    public static YamlConfiguration getDraftConf() {
        return YamlConfiguration.loadConfiguration(T);
    }

    public static void addBottle(String id) throws IOException {
        YamlConfiguration y = getDraftConf();
        List<String> n = readConf().online;
        n.add(id);
        y.set("conf", new DraftConf(n));
        y.save(T);
    }

    public static void removeBottle(String id) throws IOException {
        YamlConfiguration y = getDraftConf();
        List<String> n = readConf().online;
        n.remove(id);
        y.set("conf", new DraftConf(n));
        y.save(T);
    }

    public static DraftConf readConf() {
        YamlConfiguration y = getDraftConf();
        return (DraftConf) y.get("conf", new DraftConf(new ArrayList<>()));
    }

    @RequiredArgsConstructor
    @Getter
    public static class DraftConf {

        private final List<String> online;

    }

}
