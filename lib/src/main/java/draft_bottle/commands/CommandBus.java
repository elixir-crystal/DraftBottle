package draft_bottle.commands;

import draft_bottle.PlugGividado;
import draft_bottle.configs.ConfigBus;
import draft_bottle.misc.DraftBottle;
import draft_bottle.misc.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.commandmanager.CommandHook;
import redempt.redlib.misc.FormatUtils;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class CommandBus {

    private final PlugGividado plug;

    @CommandHook("reload")
    public void reload(CommandSender sender) {
        plug.cman.reload();
    }

    @CommandHook("get")
    public void get(CommandSender sender) throws IOException {
        Player p = (Player) sender;
        p.getInventory().addItem(Utils.generateBottle(true, null, null, null));
    }

    // @CommandHook("throw")
    public void put(CommandSender sender, String content) {
        if (!sender.hasPermission(ConfigBus.bottleThrowPerm)) {
            return;
        }
        Player p = (Player) sender;
        try {


            if (Utils.getItemInHand(p).getType().equals(Material.AIR)) {
                Utils.pool.put(new DraftBottle(p.getUniqueId(), UUID.randomUUID(), content, new ItemStack(Material.AIR)));
            } else {
                Utils.pool.put(new DraftBottle(p.getUniqueId(), UUID.randomUUID(), content, Utils.getItemInHand(p)));
                Utils.getItemInHand(p).setAmount(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            p.sendMessage(FormatUtils.color(ConfigBus.prefix + "&c无法扔出漂流瓶"));
        }
    }

}
