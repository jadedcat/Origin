package CountryGamer_Core;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemFood;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFoodBase extends ItemFood {

	public String modid;

	public ItemFoodBase(int id, String modid, String name, int healAmount, float saturationModifier, boolean isWolfFavorite) {
		// id, healAmount, saturationModifier, isWolf'sFavorite
		super(id, healAmount, saturationModifier, isWolfFavorite);
		this.modid = modid.toLowerCase();
		this.setUnlocalizedName(name);
		GameRegistry.registerItem(this, this.getUnlocalizedName());
		LanguageRegistry.addName(this, name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconReg) {
		this.itemIcon = iconReg.registerIcon(this.modid + ":"
				+ this.getUnlocalizedName().substring(5));
	}

}
