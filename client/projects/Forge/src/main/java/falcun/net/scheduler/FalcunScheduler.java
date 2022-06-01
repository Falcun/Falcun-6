package falcun.net.scheduler;

import falcun.net.Falcun;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SideOnly(Side.CLIENT)
public final class FalcunScheduler {
	private FalcunScheduler() {
	}
	private static final FalcunScheduler scheduler;

	static {
		MinecraftForge.EVENT_BUS.register(scheduler = new FalcunScheduler());
	}

	public static void addTask(Runnable task, int delay, boolean async) {
		tasks.add(Pair.of(false, Pair.of(new AtomicInteger(delay), task)));
	}

	public static void addSyncTask(Runnable task, int delay) {
		addTask(task, delay, false);
	}

	public static void addAsyncTask(Runnable task, int delay) {
		addTask(task, delay, true);
	}

	private static final List<Pair<Boolean, Pair<AtomicInteger, Runnable>>> tasks = new ObjectArrayList<>();

	@SubscribeEvent
	public void tick(TickEvent.ClientTickEvent e) {
		if (e.phase != TickEvent.Phase.START) return;
		try {
			tasks.removeIf(pair -> {
				if (pair.second.first.decrementAndGet() < 1) {
					try {
						if (!pair.first) pair.second.second.run();
						else Falcun.submitJob(() -> {
							pair.second.second.run();
							return null;
						}, null);
					} catch (Throwable err) {
						err.printStackTrace();
					}
					return true;
				}
				return false;
			});
		} catch (Throwable err) {
			err.printStackTrace();
		}
	}
}
