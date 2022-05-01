package DraftBottle.misc;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class Utils {

    public static String getItemName(ItemStack item) {
        try {
            return Objects.requireNonNull(item.getItemMeta()).getDisplayName();
        } catch (NullPointerException ignored) {}
        return "";
    }

    public static boolean isDraftBottle(ItemStack item) {
        ItemMeta im = item.getItemMeta();
        return Objects.requireNonNull(im).getPersistentDataContainer().has(DraftBottleItem.CONTENTS_KEY, PersistentDataType.STRING);
    }

    public static ItemStack getItemInHand(HumanEntity ety) {
        return ety.getInventory().getItemInMainHand();
    }

}
