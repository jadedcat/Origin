package CountryGamer_Core;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



public class BlockBase extends Block {
	
	public String modid;
	
	public BlockBase(int id, Material mat, String modid, String name) {
		super(id, mat);
		this.setUnlocalizedName(name);
		GameRegistry.registerBlock(this,name);
		LanguageRegistry.addName(this,	name);
		
		this.modid = modid;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(this.modid + ":" +
					this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1));
	}
	
}
