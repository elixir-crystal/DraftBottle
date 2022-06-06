package draft_bottle.listeners;

import draft_bottle.PlugGividado;
import draft_bottle.configs.ConfigBus;
import draft_bottle.misc.DraftBottle;
import draft_bottle.misc.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;
import redempt.redlib.itemutils.ItemBuilder;
import redempt.redlib.misc.EventListener;
import redempt.redlib.misc.FormatUtils;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

@RequiredArgsConstructor
public class ListenerBus {

    private final PlugGividado plug;

    public void register() {
        // listener for picking & opening a bottle
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

        new EventListener<>(plug, EntityPickupItemEvent.class, evt -> {
            if (evt.isCancelled()) {
                return;
            }

            ItemStack i = evt.getItem().getItemStack();
            ItemMeta im = i.getItemMeta();
            assert im != null;
            if (im.hasLore()) {
                //noinspection deprecation
                if (Objects.requireNonNull(im.getLore()).get(0).equals(FormatUtils.color("&c&l&a&l&c&lDRAFT BOTTLE ITEM ENTITY&r"))) {
                    evt.setCancelled(true);
                    evt.getItem().remove();
                }
            }
        });

        // listener for creating or tagging and throwing a bottle
        new EventListener<>(plug, PlayerInteractEvent.class, evt -> {
            //noinspection deprecation
            if (evt.isCancelled() || !evt.hasItem()) {
                return;
            }

            System.out.println("outer");
            if (Utils.isDraftBottle(evt.getItem()) && evt.getAction().isRightClick()) {
                try {
                    // test if it is an empty draft bottle
                    DraftBottle b = Utils.parseItem(Objects.requireNonNull(evt.getItem()));

                    // throwing
                    System.out.println("throw");
                    if (evt.getPlayer().hasPermission(ConfigBus.bottleThrowPerm)) {
                        if (Objects.requireNonNull(evt.getPlayer().getTargetBlockExact(4)).getType().equals(Material.WATER)) {
                            Item it = evt.getPlayer().getWorld().dropItem(evt.getPlayer().getLocation(), new ItemBuilder(Material.GLASS_BOTTLE)
                                    .setLore(FormatUtils.color("&c&l&a&l&c&lDRAFT BOTTLE ITEM ENTITY&r")));
                            it.setPickupDelay(100000);
                            it.getLocation().setDirection(evt.getPlayer().getLocation().getDirection());
                            it.getLocation().toVector().add(evt.getPlayer().getLocation().toVector());
                            it.getLocation().toVector().multiply(1.1);
                            try {
                                ItemStack i = evt.getItem();
                                assert i != null;
                                i.setAmount(i.getAmount() - 1);
                                throwBottle(evt.getPlayer(), b);
                                evt.getPlayer().playSound(evt.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            evt.getPlayer().sendMessage(ConfigBus.prefix + "你只能将漂流瓶扔在水里");
                        }
                    }
                } catch (IllegalArgumentException ex) { // empty draft bottle
                    System.out.println("tag");
                    tagBottle(evt.getPlayer(), Objects.requireNonNull(evt.getItem()));
                }
            }

        });
    }

    private void openBottle(Player p, DraftBottle bottle) {
        if (!p.hasPermission(ConfigBus.bottleOpenPerm)) {
            return;
        }

        p.sendMessage(ConfigBus.prefix + FormatUtils.color("&e" + Objects.requireNonNull(Bukkit.getPlayer(bottle.getThrowerUUID())).getName() + ": &f") + bottle.getContent());

        if (p.getInventory().firstEmpty() == -1) {
            p.getWorld().dropItemNaturally(p.getLocation(), bottle.getItemInside());
        } else {
            p.getInventory().addItem(bottle.getItemInside());
        }
        Utils.pool.disposeBottle(bottle);
    }

    private void tagBottle(Player p, ItemStack item) {
        //noinspection deprecation
        InventoryGUI gui = new InventoryGUI(Bukkit.createInventory(null, InventoryType.ANVIL, FormatUtils.color("&8漂流瓶")));
        gui.openSlot(0);
        gui.addButton(1, new ItemButton(new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                .setName(FormatUtils.color("&c"))) {
            @Override
            public void onClick(InventoryClickEvent e) {
            }
        });
        gui.addButton(2, new ItemButton(new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                .setName(FormatUtils.color("&c"))) {
            @Override
            public void onClick(InventoryClickEvent e) {
            }
        });
        gui.setOnDestroy(() -> {
            AnvilInventory ai = (AnvilInventory) gui.getInventory();
            ItemStack bottle;
            try {
                bottle = Utils.generateBottle(false, FormatUtils.color(Objects.requireNonNull(ai.getRenameText())), ai.getFirstItem(), p);
                if (p.getInventory().firstEmpty() == -1) {
                    p.getWorld().dropItem(p.getLocation(), bottle);
                } else {
                    p.getInventory().addItem(bottle);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        item.setAmount(item.getAmount() - 1);
        gui.open(p);
    }

    private void throwBottle(Player p, DraftBottle bottle) throws IOException {
        if (!p.hasPermission(ConfigBus.bottleThrowPerm)) {
            return;
        }

        // Utils.getItemInHand(p).setAmount(Utils.getItemInHand(p).getAmount() - 1);
        Utils.pool.put(bottle);
    }

}
