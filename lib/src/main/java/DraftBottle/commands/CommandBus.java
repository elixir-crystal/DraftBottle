package DraftBottle.commands;

import DraftBottle.configs.ConfigBus;
import DraftBottle.misc.DraftBottleItem;
import DraftBottle.misc.Utils;
import DraftBottle.misc.storage.BottleStorageUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.commandmanager.CommandHook;

@RequiredArgsConstructor
public class CommandBus {

    @CommandHook("get")
    public void get(CommandSender sender, String id) {
        Player p = (Player) sender;
        p.getInventory().addItem(new DraftBottleItem(id));
    }

    @CommandHook("show")
    public void show(CommandSender sender) {
        Player p = (Player) sender;
        ItemStack hd = Utils.getItemInHand(p);

        if (Utils.isDraftBottle(hd)) {
            p.sendMessage(ConfigBus.prefix + "此漂流瓶 ID 为: " + new DraftBottleItem(hd).getId());
        } else {
            p.sendMessage(ConfigBus.prefix + "这不是一个漂流瓶");
        }

    }

}
