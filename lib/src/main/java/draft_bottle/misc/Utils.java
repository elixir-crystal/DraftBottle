package draft_bottle.misc;

import draft_bottle.configs.ConfigBus;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import redempt.redlib.itemutils.ItemBuilder;
import redempt.redlib.misc.FormatUtils;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class Utils {

    public static final StorageUtils.DraftPool pool = new StorageUtils.DraftPool();
    private static final ItemStack BOTTLE_TEMPLATE = new ItemBuilder(Material.GLASS_BOTTLE)
            .setName(ConfigBus.bottleName)
            .addLore(FormatUtils.color("&f"))
            .addLore(FormatUtils.color("&e留言: &f<>"))
            .addLore(FormatUtils.color("&f"))
            .setCustomModelData(ConfigBus.emptyCmd);

    public static void init() {
        pool.create();
    }

    @Deprecated
    public static ItemStack generateBottle(boolean isEmpty, String content, ItemStack itemInside, Player thrower) throws IOException {
        ItemBuilder i = new ItemBuilder(BOTTLE_TEMPLATE)
                .setCustomModelData(isEmpty ? ConfigBus.emptyCmd : ConfigBus.nonEmptyCmd)
                .setLore(
                        FormatUtils.color("&f"),
                        FormatUtils.color("&e留言: &f|" + content));
        if (isEmpty) {
            i.addLore(FormatUtils.color("&f"));
        } else {
            UUID ouuid = thrower.getUniqueId();
            UUID iuuid = UUID.randomUUID();
            i.addLore(FormatUtils.color("&e内容物: &f|" + describeItem(itemInside)));
            i.addLore(FormatUtils.color("&f"));
            i.addLore(FormatUtils.color("&7OUUID: &8|" + ouuid));
            i.addLore(FormatUtils.color("&7IUUID: &8|" + iuuid));
            i.addLore(FormatUtils.color("&f"));
            throwBottle(new DraftBottle(ouuid, iuuid, content, itemInside));
        }
        return i;
    }

    private static void throwBottle(DraftBottle bottle) throws IOException {
        pool.put(bottle);
    }

    public static String describeItem(ItemStack item) {
        ItemMeta im = item.getItemMeta();
        assert im != null;
        if (im.hasDisplayName()) {
            return im.getDisplayName();
        } else {
            return item.getType().name();
        }
    }

    @SuppressWarnings("unused")
    @Deprecated
    public static DraftBottle parseItem(ItemStack item) {
        if (!isDraftBottle(item)) {
            return null;
        }

        ItemMeta im = item.getItemMeta();
        String content = null;
        UUID ouuid = null;
        UUID iuuid = null;
        ItemStack inside;
        assert im != null;
        for (String s : Objects.requireNonNull(im.getLore())) {
            if (s.matches(".*留言.*\\|")) {
                content = s.replaceAll(".*留言.*\\|", "");
            } else if (s.matches(".*OUUID.*\\|")) {
                ouuid = UUID.fromString(s.replaceAll(".*OUUID.*\\|", ""));
            } else if (s.matches(".*IUUID.*\\|")) {
                iuuid = UUID.fromString(s.replaceAll(".*IUUID.*\\|", ""));
            }
        }

        if (content == null || ouuid == null || iuuid == null) {
            return null;
        }

        // get from pool
        inside = pool.seek(ouuid, iuuid).getItemStack("item");
        return new DraftBottle(ouuid, iuuid, content, inside);
    }

    public static boolean isDraftBottle(ItemStack item) {
        ItemMeta im = item.getItemMeta();
        assert im != null;
        return im.getDisplayName().equals(ConfigBus.bottleName);
    }

    public static ItemStack getItemInHand(HumanEntity ety) {
        return ety.getInventory().getItemInMainHand();
    }

}
