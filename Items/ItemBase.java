package CountryGamer_Core.Items;

import CountryGamer_XPMod.lib.Reference;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBase extends Item {
	
	public String modid;
	
	public ItemBase(int id, String modid, String name) {
		super(id);
		this.modid = modid.toLowerCase();
		this.setUnlocalizedName(name);
		GameRegistry.registerItem(this, this.getUnlocalizedName());
		LanguageRegistry.addName(this,	name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconReg) {
		this.itemIcon = iconReg.registerIcon(this.modid + 
				":" + this.getUnlocalizedName().substring(5));
	}
	
	
	
}
