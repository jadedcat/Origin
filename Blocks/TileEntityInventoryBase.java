package CountryGamer_Core.Blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityInventoryBase extends TileEntity implements IInventory {

	private ItemStack[] inv;
	private String name;
	private int maxStackSize;

	public TileEntityInventoryBase(String name, int inventorySize,
			int maxStackSize) {
		this.inv = new ItemStack[inventorySize];
		this.name = name;
		this.maxStackSize = maxStackSize;
	}

	@Override
	public int getSizeInventory() {
		return this.inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return this.inv[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return this.getStackInSlot(i);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemStack) {
		this.inv[i] = itemStack;
		this.onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return this.name;
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return this.maxStackSize;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void openChest() {

	}

	@Override
	public void closeChest() {

	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}

	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		NBTTagList tagList = tagCom.getTagList("Items");
		this.inv = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound tagCom1 = (NBTTagCompound) tagList.tagAt(i);
			byte b0 = tagCom1.getByte("Slot");

			if (b0 >= 0 && b0 < this.inv.length) {
				this.inv[b0] = ItemStack.loadItemStackFromNBT(tagCom1);
			}
		}

	}

	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		NBTTagList tagList = new NBTTagList();
		for (int i = 0; i < this.inv.length; i++) {
			if (this.inv[i] != null) {
				NBTTagCompound tagCom1 = new NBTTagCompound();
				tagCom1.setByte("Slot", (byte) i);
				this.inv[i].writeToNBT(tagCom1);
				tagList.appendTag(tagCom1);
			}
		}
		tagCom.setTag("Items", tagList);

	}

}
