package DraftBottle.configs;

import redempt.redlib.config.annotations.Comment;
import redempt.redlib.config.annotations.ConfigMappable;
import redempt.redlib.config.annotations.ConfigName;

@ConfigMappable
public class ConfigBus {

    @ConfigName("emptyCustomModelData")
    @Comment("The custom model data of an empty draft bottle")
    public static int emptyCmd = -255;

    @ConfigName("nonEmptyCustomModelData")
    @Comment("The custom model data of a non-empty draft bottle")
    public static int nonEmptyCmd = -256;

    @ConfigName("emptyName")
    @Comment("The display name of an empty draft bottle")
    public static String emptyName = "§c§l§f空漂流瓶§r";

    @ConfigName("nonEmptyName")
    @Comment("The display name of a non-empty draft bottle")
    public static String nonEmptyName = "§c§l§b漂流瓶§r";

    @ConfigName("bottleOpenPermission")
    @Comment("The permission required to open a draft bottle")
    public static String bottleOpenPerm = "draftbottle.use.open";

    @ConfigName("bottleCreatePermission")
    @Comment("The permission required to throw a draft bottle")
    public static String bottleThrowPerm = "draftbottle.use.throw";

    @ConfigName("guiSize")
    @Comment("The size of a draft bottle")
    public static int guiSize = 9;

    @ConfigName("guiTitle")
    @Comment("The title of a draft bottle")
    public static String guiTitle = "&8漂流瓶";

    @ConfigName("prefix")
    public static String prefix = "§f[§b漂流瓶§f] ";

}
