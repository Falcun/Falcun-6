package falcun.net.api.fps;

import falcun.net.api.fps.config.FalcunFPSField;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.Pair;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class FalcunFPS {

	private final Map<String, FalcunFPSField<?>> configData = new LinkedHashMap<>();
	private final Map<String, List<Pair<String, FalcunFPSField<?>>>> categories = new LinkedHashMap<>();




}
