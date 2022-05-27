package falcun.xyz.dev.boredhuman.dancore.falcunfork.api;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.api.textures.TextureManager;

public abstract class DanCoreAPI {

	private static DanCoreAPI API;



	public static DanCoreAPI getAPI() {
		return DanCoreAPI.API;
	}

	public abstract TextureManager getTextureManager();
}