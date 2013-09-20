package CountryGamer_Core;

import CountryGamer_XPMod.ExperienceMod.lib.Reference;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBase extends Item {
	
	public String modid;
	
	public ItemBase(int id, String modid) {
		super(id);
		this.modid = modid.toLowerCase();
		if( this.getUnlocalizedName().equals(""))
			this.setUnlocalizedName("genericItem");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconReg) {
		this.itemIcon = iconReg.registerIcon(this.modid + 
				":" + this.getUnlocalizedName().substring(5));
	}
	
	
	
}
