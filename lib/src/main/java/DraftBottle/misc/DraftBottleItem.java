package DraftBottle.misc;

import DraftBottle.configs.ConfigBus;
import DraftBottle.misc.storage.BottleStorageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import redempt.redlib.misc.FormatUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DraftBottleItem extends ItemStack {

    public static final NamespacedKey CONTENTS_KEY;

    static {
        CONTENTS_KEY = new NamespacedKey(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("DraftBottle")), "draftbottle_id");
    }

    public DraftBottleItem(ItemStack item) {
        super(item);
    }

    public DraftBottleItem(String id, ItemStack... contents) {
        super(Material.GLASS_BOTTLE);
        setId(id);
        if (contents.length != 0) {
            ItemMeta im = this.getItemMeta();
            Objects.requireNonNull(im).setDisplayName(ConfigBus.nonEmptyName);
            im.setCustomModelData(ConfigBus.nonEmptyCmd);
            List<String> lores = new ArrayList<>();
            lores.add(FormatUtils.color("&r"));
            lores.add(FormatUtils.color("&f内容物:"));
            ItemStack[] cont = BottleStorageUtils.readConf(getId()).getContents();
            for (int t = 0; t < 3; t++) {
                try {
                    lores.add(" - " + Utils.getItemName(cont[t]));
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
            if (cont.length > 3) {
                lores.add(FormatUtils.color("&8 ..."));
            }
            im.setLore(lores);
            this.setItemMeta(im);
        } else {
            ItemMeta im = this.getItemMeta();
            Objects.requireNonNull(im).setDisplayName(ConfigBus.emptyName);
            im.setCustomModelData(ConfigBus.emptyCmd);
            List<String> lores = new ArrayList<>();
            lores.add(FormatUtils.color("&r"));
            lores.add(FormatUtils.color("&e右键 &f向其中塞入物品"));
            im.setLore(lores);
            this.setItemMeta(im);
        }
    }

    public void setId(String id) {
        ItemMeta im = this.getItemMeta();
        PersistentDataContainer cont = Objects.requireNonNull(im).getPersistentDataContainer();
        cont.set(CONTENTS_KEY, PersistentDataType.STRING, id);
        this.setItemMeta(im);
    }

    public String getId() {
        ItemMeta im = this.getItemMeta();
        return Objects.requireNonNull(im).getPersistentDataContainer().get(CONTENTS_KEY, PersistentDataType.STRING);
    }

}
