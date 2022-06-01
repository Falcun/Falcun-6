package mapwriter.config;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;

public class FactionConfig {
	private String configCategory;
	
	public boolean multiverseSupport = false;
	public boolean switchOnServerCommand = false;
	
	public FactionConfig(String configCategory) {
		this.configCategory = configCategory;
	}
	
	public void loadConfig() {
		// get options from config file
		//this.multiverseSupport = ConfigurationHandler.configuration.getBoolean("multiverseSupport", this.configCategory, false, "", "mw.config.factions.multiverse");
		//this.switchOnServerCommand = ConfigurationHandler.configuration.getBoolean("switchOnServerCommand", this.configCategory, false, "", "mw.config.factions.servercommand");
	}
	
	public void setDefaults() {}
	
	public IConfigElement categoryElement(String name, String tooltip_key) {
		return new DummyCategoryElement(name, tooltip_key, new ConfigElement(ConfigurationHandler.configuration.getCategory(this.configCategory)).getChildElements());
	}
}