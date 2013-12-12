package CountryGamer_Core;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockContainerBase extends BlockContainer {
	
	public String modid;
	public TileEntity tileEnt;
	
	public BlockContainerBase(int id, Material mat,
			String modid, String name) {
		super(id, mat);
		this.setUnlocalizedName(name);
		GameRegistry.registerBlock(this,name);
		LanguageRegistry.addName(this,	name);
		
		this.modid = modid;
		this.tileEnt = null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(this.modid + ":" +
					this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1));
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return this.tileEnt;
	}
	
	
}
