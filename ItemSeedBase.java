package CountryGamer_Core;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

public class ItemSeedBase extends ItemBase implements IPlantable {
	/**
	 * The type of block this seed turns into (wheat or pumpkin stems for
	 * instance)
	 */
	private int plantBlockiD;

	/** BlockID of the block the seeds can be planted on. */
	private int soilBlockID;

	public ItemSeedBase(int id, String modid, String name, int plantBlockiD,
			int soilID) {
		super(id, modid, name);
		this.plantBlockiD = plantBlockiD;
		this.soilBlockID = soilID;
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, World par3World, int par4, int par5,
			int par6, int par7, float par8, float par9, float par10) {
		if (par7 != 1) {
			return false;
		} else if (par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7,
				par1ItemStack)
				&& par2EntityPlayer.canPlayerEdit(par4, par5 + 1, par6, par7,
						par1ItemStack)) {
			int i1 = par3World.getBlockId(par4, par5, par6);
			Block soil = Block.blocksList[i1];

			if (soil != null
					&& soil.canSustainPlant(par3World, par4, par5, par6,
							ForgeDirection.UP, this)
					&& par3World.isAirBlock(par4, par5 + 1, par6)) {
				par3World.setBlock(par4, par5 + 1, par6, this.plantBlockiD);
				--par1ItemStack.stackSize;
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public EnumPlantType getPlantType(World world, int x, int y, int z) {
		return (plantBlockiD == Block.netherStalk.blockID ? EnumPlantType.Nether
				: EnumPlantType.Crop);
	}

	@Override
	public int getPlantID(World world, int x, int y, int z) {
		return plantBlockiD;
	}

	@Override
	public int getPlantMetadata(World world, int x, int y, int z) {
		return 0;
	}
}
