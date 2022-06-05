package draft_bottle.misc;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@SuppressWarnings("ALL")
public class StorageUtils {

    private static final String SEP = File.separator;
    private static final String DATA = Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("DraftBottle")).getDataFolder().getPath();
    private static final String STORAGE = DATA + SEP + "storage";

    @RequiredArgsConstructor
    public static class DraftPool {

        public void create() {
            File f = new File(STORAGE);
            if (!f.exists()) {
                boolean __ = f.mkdirs();
            }
        }

        public void put(DraftBottle bottle) throws IOException {
            File d = new File(STORAGE + SEP + bottle.getThrowerUUID());
            if (!d.exists()) {
                boolean __ = d.mkdirs();
            }
            File f = new File(STORAGE + SEP + bottle.getThrowerUUID() + SEP + bottle.getItemUUID() + ".yml");
            boolean __ = f.createNewFile();

            YamlConfiguration y = YamlConfiguration.loadConfiguration(f);
            y.set("content", bottle.getContent());
            y.set("item", bottle.getItemInside());
            y.save(f);
        }

        public DraftBottle pick() {
            File d1 = new File(STORAGE);
            File[] l1 = d1.listFiles((dir, name) -> new File(dir.getPath() + SEP + name).isDirectory());
            if (l1.length == 0) {
                return null;
            }
            String s1 = l1[new Random().nextInt(l1.length)].getName();

            File d2 = new File(STORAGE + SEP + s1);
            File[] l2 = d2.listFiles((dir, name) -> name.endsWith(".yml"));
            if (l2.length == 0) {
                return null;
            }
            File s2 = l2[new Random().nextInt(l2.length)];
            YamlConfiguration y = YamlConfiguration.loadConfiguration(s2);

            return new DraftBottle(UUID.fromString(s1), UUID.fromString(s2.getName().substring(0, s2.getName().length() - 4)), y.getString("content"), y.getItemStack("item"));

        }

        public void disposeBottle(DraftBottle bottle) {
            File f = new File(STORAGE + SEP + bottle.getThrowerUUID() + SEP + bottle.getItemUUID() + ".yml");
            if (f.exists()) {
                boolean __ = f.delete();
            }
        }

        public YamlConfiguration seek(UUID ouuid, UUID iuuid) {
            File f = new File(STORAGE + SEP + ouuid + SEP + iuuid + ".yml");
            return YamlConfiguration.loadConfiguration(f);
        }

    }


}
