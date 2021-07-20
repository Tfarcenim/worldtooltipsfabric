package tfar.worldtooltips;

import net.fabricmc.api.ClientModInitializer;

public class WorldTooltips implements ClientModInitializer {

	public static final String MODID = "worldtooltips";
	@Override
	public void onInitializeClient() {
		//WorldTooltipsRenderType.getType();
		ModUtils.populateMap();
	}
}
