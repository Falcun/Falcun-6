package falcun.xyz.dev.boredhuman.dancore.falcunfork.tweakers;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;

@Credits("https://github.com/boredhuman/Dancore/blob/master/src/main/java/dev/boredhuman/api/tweakers/TweakerAPI.java")
public abstract class TweakerAPI {

	private static TweakerAPI API;

	static TweakerAPI getAPI() {
		return TweakerAPI.API;
	}

	public abstract void registerTweaker(CorePatchModule tweaker);
}
