package draft_bottle.misc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public record DraftBottle(UUID throwerUUID, UUID itemUUID, String content, ItemStack itemInside) {
}
