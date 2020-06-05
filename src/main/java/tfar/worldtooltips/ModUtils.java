package tfar.worldtooltips;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.registry.Registry;

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
		return modid_to_mod_name.get(Registry.ITEM.getId(entity.getStack().getItem()).getNamespace());
	}
}
