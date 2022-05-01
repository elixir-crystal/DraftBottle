package DraftBottle.listeners;

import DraftBottle.PlugGividado;
import DraftBottle.configs.ConfigBus;
import DraftBottle.misc.DraftBottleItem;
import DraftBottle.misc.Utils;
import DraftBottle.misc.storage.BottleStorageUtils;
import DraftBottle.misc.storage.DraftStorageUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.misc.EventListener;
import redempt.redlib.misc.FormatUtils;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
public class ListenerBus {

    private final PlugGividado plug;

    public void register() {
        new EventListener<>(plug, PlayerInteractEvent.class, evt -> {
            if (evt.getAction().equals(Action.RIGHT_CLICK_AIR) || evt.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                Player p = evt.getPlayer();
                ItemStack it = Utils.getItemInHand(p);
                if (Utils.isDraftBottle(it)) {
                    evt.setCancelled(true);

                    try {
                        if (p.isSneaking()) {
                            openBottle(p, new DraftBottleItem(it));
                        } else {
                            if (Objects.requireNonNull(p.getTargetBlockExact(4, FluidCollisionMode.SOURCE_ONLY)).getType().equals(Material.WATER)) {
                                throwBottle(p, new DraftBottleItem(it));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void openBottle(Player p, DraftBottleItem it) {
        if (!p.hasPermission(ConfigBus.bottleOpenPerm)) {
            return;
        }

        InventoryGUI gui = new InventoryGUI(ConfigBus.guiSize, FormatUtils.color(ConfigBus.guiTitle));
        gui.getInventory().setContents(BottleStorageUtils.readConf(it.getId()).getContents());
        gui.setOnDestroy(() -> BottleStorageUtils.writeConf(it.getId(), gui.getInventory().getContents()));
        gui.open(p);
    }

    private void throwBottle(Player p, DraftBottleItem it) throws IOException {
        if (!p.hasPermission(ConfigBus.bottleThrowPerm)) {
            return;
        }

        DraftStorageUtils.addBottle(it.getId());
    }

}
