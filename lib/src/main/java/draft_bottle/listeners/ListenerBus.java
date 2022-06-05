package draft_bottle.listeners;

import draft_bottle.PlugGividado;
import draft_bottle.configs.ConfigBus;
import draft_bottle.misc.DraftBottle;
import draft_bottle.misc.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import redempt.redlib.misc.EventListener;
import redempt.redlib.misc.FormatUtils;

import java.io.IOException;
import java.util.Random;

@RequiredArgsConstructor
public class ListenerBus {

    private final PlugGividado plug;

    public void register() {
        new EventListener<>(plug, PlayerFishEvent.class, evt -> {
            if (evt.isCancelled()) {
                return;
            }

            if (evt.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
                int range = ConfigBus.bottleCatchChance.substring(1).length();

                String[] l = ConfigBus.bottleCatchChance.split("");
                StringBuilder c = new StringBuilder();
                boolean flag = false;
                for (String s : l) {
                    if (flag ||
                            !(s.equals("") || s.equals(".") || s.equals("0"))) {
                        flag = true;
                        c.append(s);
                    }
                }

                int n = Integer.parseInt(c.toString());

                if (new Random().nextInt((int) Math.pow(10, range)) <= n) {
                    DraftBottle bottle = Utils.pool.pick();
                    assert bottle != null;
                    openBottle(evt.getPlayer(), bottle);
                }

            }
        });
    }

    private void openBottle(Player p, DraftBottle bottle) {
        if (!p.hasPermission(ConfigBus.bottleOpenPerm)) {
            return;
        }

        p.sendMessage(ConfigBus.prefix + FormatUtils.color("&e" + Bukkit.getPlayer(bottle.throwerUUID()) + ": &f") + bottle.content());

        if (p.getInventory().firstEmpty() == -1) {
            p.getWorld().dropItemNaturally(p.getLocation(), bottle.itemInside());
        } else {
            p.getInventory().addItem(bottle.itemInside());
        }
        Utils.pool.disposeBottle(bottle);
    }

    @SuppressWarnings("unused")
    @Deprecated
    private void throwBottle(Player p, DraftBottle bottle) throws IOException {
        if (!p.hasPermission(ConfigBus.bottleThrowPerm)) {
            return;
        }

        Utils.getItemInHand(p).setAmount(Utils.getItemInHand(p).getAmount() - 1);
        Utils.pool.put(bottle);
    }

}
