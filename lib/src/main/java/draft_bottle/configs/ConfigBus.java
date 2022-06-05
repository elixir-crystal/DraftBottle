package draft_bottle.configs;

import redempt.redlib.config.annotations.Comment;
import redempt.redlib.config.annotations.ConfigMappable;
import redempt.redlib.config.annotations.ConfigName;
import redempt.redlib.misc.FormatUtils;

@ConfigMappable
public class ConfigBus {

    @ConfigName("emptyCustomModelData")
    @Comment("The custom model data of an empty draft bottle")
    public static int emptyCmd = -255;

    @ConfigName("nonEmptyCustomModelData")
    @Comment("The custom model data of a non-empty draft bottle")
    public static int nonEmptyCmd = -256;

    @ConfigName("bottleName")
    @Comment("The display name of a draft bottle")
    public static String bottleName = FormatUtils.color("&c&l&b漂流瓶&r");

    @ConfigName("bottleOpenPermission")
    @Comment("The permission required to open a draft bottle")
    public static String bottleOpenPerm = "draftbottle.use.open";

    @ConfigName("bottleCreatePermission")
    @Comment("The permission required to throw a draft bottle")
    public static String bottleThrowPerm = "draftbottle.use.throw";

    @ConfigName("bottleCatchChance")
    @Comment("The chance of catching a draft bottle of each bite")
    public static String bottleCatchChance = ".05";

    @ConfigName("prefix")
    public static String prefix = FormatUtils.color("&f[&b漂流瓶&f] &f");

}
