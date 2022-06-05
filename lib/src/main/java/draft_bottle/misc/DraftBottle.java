package draft_bottle.misc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class DraftBottle {

    private final UUID throwerUUID;
    private final UUID itemUUID;
    private final String content;
    private final ItemStack itemInside;

}
