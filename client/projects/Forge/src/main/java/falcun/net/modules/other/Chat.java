package falcun.net.modules.other;

import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.modules.ModuleCategory;

@FalcunModuleInfo(fileName = "chat", name = "Chat", description = "Chat Settings", version = "1.0.0", category = ModuleCategory.MISC)
public class Chat extends FalcunModule {


//      @ConfigValue.Boolean(name = "Infinite Chat")
//  public boolean infiniteChat = true;

    @FalcunSetting("Infinite Chat")
    public FalcunValue<Boolean> infiniteChat = new FalcunValue<>(true);


//  @ConfigValue.Boolean(name = "Compact Chat")
//  private boolean compactChat = true;

    @FalcunSetting("Compact Chat")
    public FalcunValue<Boolean> compactChat = new FalcunValue<>(false);


//
//  @ConfigValue.Boolean(name = "Custom Chat")
//  private boolean fontChat = true;
//

    @FalcunSetting("Custom Chat")
    public FalcunValue<Boolean> fontChat= new FalcunValue<>(true);



//  @ConfigValue.Boolean(name = "Chat Timestamps")
//  private boolean chatChatTimestamps = true;

    @FalcunSetting("Mention Ping")
    public FalcunValue<Boolean> chatPingname = new FalcunValue<>(false);

//
//  @ConfigValue.Boolean(name = "Mention Ping")
//  private boolean chatPingname = false;

    @FalcunSetting("Ping PMs")
    public FalcunValue<Boolean> chatPingPms = new FalcunValue<>(false);

//
//  @ConfigValue.Boolean(name = "Ping PMs")
//  private boolean chatPingPms = false;
//

    enum MentionFormat {
        COLON,
        MISC
    }

    @FalcunSetting("Mention Format")
    public FalcunValue<MentionFormat> chatmsgFormat = new FalcunValue<>(MentionFormat.COLON);


//  @ConfigValue.List(name = "Mention Format", values = {
//    ":",
//    "�"
//  })
//  private String chatmsgFormat = ":";


    @FalcunSetting("Ping Cooldown")
    public FalcunValue<Boolean> pingCooldown = new FalcunValue<>(false);


//
//  @ConfigValue.Boolean(name = "Ping Cooldown")
//  private boolean chatPingCooldown = false;
//

    enum TimeStampFormat {
        HOUR_MINUTE,
        DAY_MONTH_HOUR_MINUTE,
        HOUR_MINUTE_SECONDS
    }

    @FalcunSetting("Time Stamps Format")
    public FalcunValue<TimeStampFormat> chatFormat = new FalcunValue<>(TimeStampFormat.DAY_MONTH_HOUR_MINUTE);


//  @ConfigValue.List(name = "Time Stamps Format", values = {
//    "hh:mm",
//    "dd:mm:hh:mm",
//    "hh:mm:ss"
//  })
//  private String chatFormat = "dd:mm:hh:mm";
}
