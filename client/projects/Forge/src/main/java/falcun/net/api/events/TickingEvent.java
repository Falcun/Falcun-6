package falcun.net.api.events;
import net.minecraftforge.fml.common.eventhandler.Event;
public final class TickingEvent extends Event {
	public static TickingEvent make(){return new TickingEvent();}
}
