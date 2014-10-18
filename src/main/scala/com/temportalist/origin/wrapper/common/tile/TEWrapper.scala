package com.temportalist.origin.wrapper.common.tile

import java.util

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{IInventory, ISidedInventory}
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.network.{NetworkManager, Packet}
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids._

/**
 * A wrapper class for Minecraft's TileEntity
 *
 * @param name
 * The name of this tile entity, this is superficial, used normally only for inventories
 *
 * @author CountryGamer
 */
class TEWrapper(var name: String)
		extends TileEntity() with IInventory with ISidedInventory with IFluidHandler with
		IPowerable with ICustomDrops {

	private val timers: util.HashMap[String, Array[Int]] = new util.HashMap[String, Array[Int]]()
	// Default Constructor
	this.timers.clear()

	// End Constructor

	// Other Constructors
	def this() {
		this("")
	}

	// End Constructors

	/**
	 * Returns the name of this tile entity
	 * @return
	 */
	def getName: String = {
		this.name
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ IInventory~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * The actual array of item stacks
	 */
	private var inventory: Array[ItemStack] = null
	/**
	 * The size of the inventory, same thing as the length of the inventory itemstack array
	 */
	private var inventorySize: Int = 0
	/**
	 * The maximum number of items in any stack in this inventory
	 */
	private var maxStackSize: Int = 0

	/**
	 * Sets self's inventory
	 * @param size The size of the inventory
	 * @param maxStackSize The maximum number of items per stack
	 */
	def setInventory(size: Int, maxStackSize: Int): Unit = {
		this.inventorySize = size
		this.maxStackSize = maxStackSize

		this.inventory = new Array[ItemStack](this.inventorySize)

	}

	def hasInventory: Boolean = {
		this.inventory != null
	}

	/**
	 * Completely deletes any itemstack in the inventory
	 */
	def clearInventory(): Unit = {
		if (this.hasInventory) {
			this.inventory = new Array[ItemStack](this.inventorySize)
		}
		else {
			this.inventory = null
		}

	}

	override def getSizeInventory: Int = {
		if (this.hasInventory) {
			this.inventorySize
		}
		else {
			0
		}

	}

	override def getStackInSlot(slotID: Int): ItemStack = {
		if (this.hasInventory) {
			if (slotID < this.inventorySize) {
				return this.inventory(slotID)
			}
		}
		null
	}

	override def decrStackSize(slotID: Int, decrement: Int): ItemStack = {
		// Check to see if self has a inventory
		if (this.hasInventory) {
			// check if it is a valid slot
			if (slotID < this.inventorySize) {
				// check to see of stack in slot is occupied
				if (this.inventory(slotID) != null) {
					// get a copy of stack in slot
					var itemStack: ItemStack = this.inventory(slotID).copy()
					// if extracting more than exists
					if (itemStack.stackSize <= decrement) {
						// remove internal stack
						this.inventory(slotID) = null
						// mark for resync
						this.markDirty()
						// return the extracted stack
						return itemStack
					}
					else {
						// split the stack in slot by the decrement,
						// leaving (stack in slot's stack size - decrement)
						// of the internal stack behind
						itemStack = this.inventory(slotID).splitStack(decrement)

						// if the stack in slot is non existant
						if (this.inventory(slotID).stackSize <= 0) {
							// remove the dead stack
							this.inventory(slotID) = null
						}

						// mark for resync
						this.markDirty()
						// return the extracted stack
						return itemStack
					}
				}
			}
		}
		// return nothing, since nothing was taken
		null
	}

	override def getStackInSlotOnClosing(slotID: Int): ItemStack = {
		this.getStackInSlot(slotID)
	}

	override def setInventorySlotContents(slotID: Int, itemStack: ItemStack): Unit = {
		if (this.hasInventory) {
			if (slotID < this.inventorySize) {
				if (itemStack eq null)
					this.inventory(slotID) = null
				else
					this.inventory(slotID) = itemStack.copy()
				this.markDirty()
			}
		}
	}

	override def getInventoryName: String = {
		if (this.hasInventory) {
			return this.getName
		}
		""
	}

	override def hasCustomInventoryName: Boolean = {
		false
	}

	override def getInventoryStackLimit: Int = {
		if (this.hasInventory) {
			return this.maxStackSize
		}
		0
	}

	override def isUseableByPlayer(p1: EntityPlayer): Boolean = {
		this.hasInventory
	}

	override def openInventory(): Unit = {}

	override def closeInventory(): Unit = {}

	override def isItemValidForSlot(slotID: Int, itemStack: ItemStack): Boolean = {
		if (this.hasInventory) {
			return slotID < this.inventorySize
		}
		false
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ISidedInventory ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	override def getAccessibleSlotsFromSide(side: Int): Array[Int] = {
		if (this.hasInventory) {
			val slotsFromSide: Array[Int] = new Array[Int](this.inventorySize)

			var i = 0
			for (i <- 0 to this.inventorySize) {
				slotsFromSide(i) = i
			}

			return slotsFromSide
		}
		null
	}

	override def canInsertItem(slotID: Int, itemStack: ItemStack, side: Int): Boolean = {
		this.hasInventory
	}

	override def canExtractItem(slotID: Int, itemStack: ItemStack, side: Int): Boolean = {
		this.hasInventory
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ IFluidHandler ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * The size of the tank
	 */
	private var tankSize: Int = 0
	/**
	 * The tank in this tile entity
	 */
	private var tank: FluidTank = null

	/**
	 * Sets self's tank
	 * @param sizeInMB The size of the tank in millibuckets (mB)
	 */
	def setTank(sizeInMB: Int) {
		this.tankSize = sizeInMB
		this.tank = new FluidTank(this.tankSize)
	}

	def hasTank: Boolean = {
		this.tank != null
	}

	/**
	 * Completely deletes any contents in the tank
	 */
	def clearTank(): Unit = {
		this.tank = new FluidTank(this.tankSize)
	}

	def getFluidStack: FluidStack = {
		if (this.hasTank) {
			if (this.tank.getFluid != null)
				return this.tank.getFluid.copy()
		}
		null
	}

	def getTankCapacity: Int = {
		if (this.hasTank) {
			return this.tankSize
		}
		0
	}

	def canHoldMoreFluid: Boolean = {
		if (this.hasTank) {
			return this.tank.getFluidAmount < this.tank.getCapacity
		}
		false
	}

	/**
	 * Get the ratio of self tank's current fluid to its capacity
	 * @return
	 */
	def getFluidRatio: Float = {
		if (this.hasTank) {
			return this.tank.getFluidAmount.asInstanceOf[Float] /
					this.tank.getCapacity.asInstanceOf[Float]
		}
		0.0F
	}

	override def fill(direction: ForgeDirection, fluidStack: FluidStack, doAction: Boolean): Int = {
		if (this.hasTank) {
			val amount: Int = this.tank.fill(fluidStack, doAction)

			if (amount > 0 && doAction) {
				this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord)
			}

			return amount
		}
		0
	}

	override def drain(direction: ForgeDirection, fluidStack: FluidStack,
			doAction: Boolean): FluidStack = {
		if (this.hasTank) {
			val internalStack: FluidStack = this.tank.getFluid
			if (internalStack.isFluidEqual(fluidStack)) {
				return this.drain(direction, internalStack.amount, doAction)
			}
		}
		null
	}

	override def drain(direction: ForgeDirection, amount: Int, doAction: Boolean): FluidStack = {
		if (this.hasTank) {
			val internalStack: FluidStack = this.tank.drain(amount, doAction)

			if (internalStack != null && doAction) {
				this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord)
			}

			return internalStack
		}
		null
	}

	override def canFill(direction: ForgeDirection, fluid: Fluid): Boolean = {
		if (this.hasTank) {
			return this.canHoldMoreFluid
		}
		false
	}

	override def canDrain(direction: ForgeDirection, fluid: Fluid): Boolean = {
		if (this.hasTank) {
			return this.tank.getFluidAmount > 0
		}
		false
	}

	override def getTankInfo(direction: ForgeDirection): Array[FluidTankInfo] = {
		if (this.hasTank) {

			var internalStack: FluidStack = null
			if (this.tank.getFluid != null) {
				internalStack = this.tank.getFluid.copy()
			}

			return Array[FluidTankInfo](
				new FluidTankInfo(internalStack, this.tank.getCapacity)
			)
		}
		null
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Timer ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Create a timer, which will be activated everytime the delay has run out
	 * @param name The name in which will be passed to the @see onTimerTriggered function
	 * @param delay The maximum delay in TICKS that this timer should run
	 */
	def createTimer(name: String, delay: Int): Unit = {
		this.timers.put(name, Array[Int](delay, delay))
	}

	/**
	 * Removes a timer from self
	 * @param name The name of the timer
	 */
	def killTimer(name: String): Unit = {
		if (this.timers.containsKey(name)) {
			this.timers.remove(name)
		}
	}

	override def updateEntity(): Unit = {
		super.updateEntity()

		// TODO Broken
		/*
		val iterator: util.Iterator[String] = this.timers.keySet().iterator()
		while (iterator.hasNext) {
			val timerName: String = iterator.next()
			val timerData: Array[Int] = this.timers.get(timerName)

			timerData(0) = timerData(0) - 1
			if (timerData(0) <= 0) {
				this.onTimerTriggered(timerName)
				timerData(0) = timerData(1)
			}

			if (this.timers.containsKey(timerName)) {
				this.timers.put(timerName, timerData)
			}
		}
		*/

	}

	/**
	 * Called when a timer has finished, and is about to refresh the delay
	 * @param timerName The name of the timer
	 */
	def onTimerTriggered(timerName: String): Unit = {}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Write/Read NBT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	override def writeToNBT(tagCom: NBTTagCompound): Unit = {
		super.writeToNBT(tagCom)

		tagCom.setString("TEWrapper_teName", this.name)

		tagCom.setBoolean("TEWrapper_hasInventory", this.hasInventory)
		if (this.hasInventory) {
			tagCom.setInteger("TEWrapper_Inventory_size", this.inventorySize)

			val tagList: NBTTagList = new NBTTagList()
			var slotID = 0
			for (slotID <- 0 to this.inventorySize) {
				if (this.getStackInSlot(slotID) != null) {
					val stackTagCom: NBTTagCompound = new NBTTagCompound()
					stackTagCom.setInteger("slot", slotID.asInstanceOf[Byte])
					this.getStackInSlot(slotID).writeToNBT(stackTagCom)
					tagList.appendTag(stackTagCom)
				}
			}
			tagCom.setTag("TEWrapper_Inventory_Stacks", tagList)

			tagCom.setInteger("TEWrapper_Inventory_maxStackSize", this.maxStackSize)
		}

		tagCom.setBoolean("TEWrapper_hasTank", this.hasTank)
		if (this.hasTank) {
			tagCom.setInteger("TEWrapper_Tank_capacity", this.tankSize)
			tagCom.setTag("TEWrapper_Tank_tankNBT", this.tank.writeToNBT(new NBTTagCompound()))
		}

		this.savePowerableNBT(tagCom)

		val timersList: NBTTagList = new NBTTagList()
		val iterator: util.Iterator[String] = this.timers.keySet().iterator()
		while (iterator.hasNext) {
			val timerName: String = iterator.next()

			val timer: NBTTagCompound = new NBTTagCompound()
			timer.setString("timerName", timerName)
			timer.setIntArray("timerData", this.timers.get(timerName))

			timersList.appendTag(timer)
		}
		tagCom.setTag("TEWrapper_timers", timersList)

	}

	override def readFromNBT(tagCom: NBTTagCompound): Unit = {
		super.readFromNBT(tagCom)

		this.name = tagCom.getString("TEWrapper_teName")

		if (tagCom.getBoolean("TEWrapper_hasInventory")) {
			this.inventorySize = tagCom.getInteger("TEWrapper_Inventory_size")
			this.inventory = new Array[ItemStack](this.inventorySize)

			val tagList: NBTTagList = tagCom.getTagList("TEWrapper_Inventory_Stacks", 10)
			for (tagIndex <- 0 to tagList.tagCount()) {
				val stackTagCom: NBTTagCompound = tagList.getCompoundTagAt(tagIndex)
				val slotID: Int = stackTagCom.getInteger("slot") & 255
				if (slotID >= 0 && slotID < this.inventorySize) {
					this.inventory(slotID) = ItemStack.loadItemStackFromNBT(stackTagCom)
				}
			}

			this.maxStackSize = tagCom.getInteger("TEWrapper_Inventory_maxStackSize")
		}
		else {
			this.inventorySize = 0
			this.maxStackSize = 0
			this.inventory = null
		}

		if (tagCom.getBoolean("TEWrapper_hasTank")) {
			this.tankSize = tagCom.getInteger("TEWrapper_Tank_capacity")
			this.tank = new FluidTank(this.tankSize)
			this.tank.readFromNBT(tagCom.getCompoundTag("TEWrapper_Tank_tankNBT"))
		}
		else {
			this.tankSize = 0
			this.tank = null
		}

		this.readPowerableNBT(tagCom)

		this.timers.clear()
		val timersList: NBTTagList = tagCom.getTagList("TEWrapper_timers", 10)
		var listIndex = 0
		for (listIndex <- 0 to timersList.tagCount()) {
			val timer: NBTTagCompound = timersList.getCompoundTagAt(listIndex)
			this.timers.put(timer.getString("timerName"), timer.getIntArray("timerData"))
		}

	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Other ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	override def markDirty: Unit = {
		super.markDirty
		this.worldObj.scheduleBlockUpdate(this.xCoord, this.yCoord, this.zCoord,
			this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord), 10)
		this.getWorldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord)

	}

	override def onDataPacket(net: NetworkManager, pkt: S35PacketUpdateTileEntity) {
		this.readFromNBT(pkt.func_148857_g)
	}

	override def getDescriptionPacket: Packet = {
		val tagCom: NBTTagCompound = new NBTTagCompound
		this.writeToNBT(tagCom)
		new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord,
			0, tagCom)
	}

}
