package tfar.worldtooltips;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.item.ItemEntity;


import java.util.HashMap;
import java.util.Map;

public class ModUtils {

	public static final Map<String, String> modid_to_mod_name = new HashMap<>();

	public static void populateMap() {
		FabricLoader.getInstance().getAllMods().stream().map(ModContainer::getMetadata).forEach(modMetadata -> {
							String modid = modMetadata.getId();
							String name = modMetadata.getName();
							modid_to_mod_name.put(modid, name);
						}
		);
	}

	public static String getModName(ItemEntity entity) {
		return modid_to_mod_name.get(Registry.ITEM.getKey(entity.getItem().getItem()).getNamespace());
	}
}
