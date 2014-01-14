package CountryGamer_Core.Items;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMetadataBase extends ItemBase {

	public final String[] metaNames;

	@SideOnly(Side.CLIENT)
	private Icon[] metaIcons;

	public ItemMetadataBase(int id, String modid, String[] names) {
		super(id, modid, names[0]);
		this.metaNames = names;
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		for (String name : metaNames) {
			LanguageRegistry.instance().addStringLocalization(name + ".name",
					name);
		}
	}

	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1) {
		int j = MathHelper.clamp_int(par1, 0, this.metaNames.length - 1);
		return this.metaIcons[j];
	}

	public String getUnlocalizedName(ItemStack par1ItemStack) {
		int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0,
				this.metaNames.length - 1);
		return metaNames[i];
	}

	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs,
			List par3List) {
		for (int j = 0; j < this.metaNames.length; ++j) {
			par3List.add(new ItemStack(par1, 1, j));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconReg) {
		this.metaIcons = new Icon[this.metaNames.length];
		for (int i = 0; i < this.metaNames.length; ++i) {
			this.metaIcons[i] = iconReg.registerIcon(this.modid + ":"
					+ metaNames[i]);
		}
	}
	
	public static int getIndex(String[] names, String name) {
		return Arrays.asList(names).indexOf(name);
	}
	
}
