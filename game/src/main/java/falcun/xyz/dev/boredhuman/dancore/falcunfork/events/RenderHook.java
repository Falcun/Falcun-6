package falcun.xyz.dev.boredhuman.dancore.falcunfork.events;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;

@Credits("https://github.com/boredhuman/Dancore/blob/master/src/main/java/dev/boredhuman/api/events/RenderHook.java")
public interface RenderHook {

	void onRender(RenderHook.Phase phase);

	enum Phase {
		SETUP,CLEAR;
	}
}
