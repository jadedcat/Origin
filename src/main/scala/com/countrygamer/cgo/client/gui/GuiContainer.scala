package com.countrygamer.cgo.client.gui

import java.util

import com.countrygamer.cgo.common.lib.util.UtilRender
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.{FontRenderer, Gui, GuiScreen}
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.{OpenGlHelper, RenderHelper}
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.{Container, Slot}
import net.minecraft.item.ItemStack
import net.minecraft.util.{EnumChatFormatting, IIcon, MathHelper}
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.{GL11, GL12}

/**
 *
 *
 * @author CountryGamer
 */
@SideOnly(Side.CLIENT)
class GuiContainer(xSize: Int, ySize: Int, val container: Container)
		extends GuiScreenWrapper(xSize, ySize) {

	// ~~~~~~~~~~ Default GuiContainer Variables, TODO refactor
	protected val itemRender: RenderItem = new RenderItem()
	private var theSlot: Slot = null
	/** Used when touchscreen is enabled. */
	private var clickedSlot: Slot = null
	/** Used when touchscreen is enabled. */
	private var isRightMouseClick: Boolean = false
	/** Used when touchscreen is enabled */
	private var draggedStack: ItemStack = null
	private var mouseXAndGuiLeftDif: Int = 0
	private var mouseYAndGuiTopDif: Int = 0
	private var returningStackDestSlot: Slot = null
	private var returningStackTime: Long = 0L
	/** Used when touchscreen is enabled */
	private var returningStack: ItemStack = null
	private var field_146985_D: Slot = null
	private var field_146986_E: Long = 0L
	/** Used to track the multiple slots on mouse drag */
	protected final val hoveredSlotSet: util.Set[Slot] = new util.HashSet[Slot]
	protected var field_147007_t: Boolean = false
	/** Used to decide the action that is called by a container function
	  * The function is called to split a stack, or just take one item
	  */
	private var slotTakingDecider: Int = 0
	private var field_146988_G: Int = 0
	private var field_146995_H: Boolean = false
	/** Saved stack size when dragging a stack with the mouse over multiple slots,
	  * the stack size all dragged slots should have
	  */
	private var mouseItemStackSize: Int = 0
	private var field_146997_J: Long = 0L
	private var field_146998_K: Slot = null
	private var field_146992_L: Int = 0
	private var field_146993_M: Boolean = false
	private var field_146994_N: ItemStack = null

	// Default Constructor
	{
		this.field_146995_H = true
	}

	// End Constructor

	// Other Constructors
	def this(container: Container) {
		this(176, 166, container)
	}

	// End Constructors

	// ~~~~~~~~~~~ Overridden Methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	override def initGui(): Unit = {
		super.initGui()

		this.mc.thePlayer.openContainer = this.container

	}

	override def drawScreen(mouseX: Int, mouseY: Int, renderPartialTicks: Float): Unit = {
		// Draw minecraft background (world render)
		this.drawDefaultBackground()
		// Get the gui's left
		val guiLeft: Int = this.getGuiLeft()
		// Get the gui's top
		val guiTop: Int = this.getGuiTop()

		// Draw the background layer
		this.drawGuiBackgroundLayer(mouseX, mouseY, renderPartialTicks)

		GL11.glDisable(GL12.GL_RESCALE_NORMAL)
		RenderHelper.disableStandardItemLighting()
		GL11.glDisable(GL11.GL_LIGHTING)
		GL11.glDisable(GL11.GL_DEPTH_TEST)

		// Call the GuiScreen's drawScreen method
		super.guiScreenDrawScreen(mouseX, mouseY, renderPartialTicks)

		RenderHelper.enableGUIStandardItemLighting()

		// New matrix
		GL11.glPushMatrix()

		// Translate to the gui
		GL11.glTranslatef(guiLeft.asInstanceOf[Float], guiTop.asInstanceOf[Float], 0.0F)
		// Full Color
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)

		GL11.glEnable(GL12.GL_RESCALE_NORMAL)

		this.theSlot = null

		val short1: Short = 240
		val short2: Short = 240

		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,
			short1.asInstanceOf[Float] / 1.0F, short2.asInstanceOf[Float] / 1.0F)
		// Reset back to Full Color
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F)

		var k1: Int = 0

		var slotID: Int = 0
		// Iterate through this container's slots
		for (slotID <- 0 until this.container.inventorySlots.size()) {
			val slot: Slot = this.container.inventorySlots.get(slotID).asInstanceOf[Slot]

			this.calculateAndDrawSlot(slot)

			if (this.isMouseOverSlot(slot, mouseX, mouseY) && slot.func_111238_b()) {

				this.theSlot = slot

				GL11.glDisable(GL11.GL_LIGHTING)
				GL11.glDisable(GL11.GL_DEPTH_TEST)

				val slotX: Int = slot.xDisplayPosition
				val slotY: Int = slot.yDisplayPosition

				GL11.glColorMask(true, true, true, true)
				// Draw highlight
				this.drawGradientRect(slotX, slotY, slotX + 16, slotY + 16, -2130706433,
					-2130706433)
				GL11.glColorMask(true, true, true, true)

				GL11.glEnable(GL11.GL_LIGHTING)
				GL11.glEnable(GL11.GL_DEPTH_TEST)

			}

		}

		// Draw foreground
		GL11.glDisable(GL11.GL_LIGHTING)
		this.drawGuiForegroundLayer(mouseX, mouseY, renderPartialTicks)
		GL11.glEnable(GL11.GL_LIGHTING)

		val inventoryPlayer: InventoryPlayer = this.mc.thePlayer.inventory

		var mouseItemStack: ItemStack = inventoryPlayer.getItemStack
		if (this.draggedStack != null) {
			mouseItemStack = this.draggedStack
		}

		if (mouseItemStack != null) {
			val b0: Byte = 8

			k1 = 8
			if (this.draggedStack != null) {
				k1 = 16
			}

			var s: String = null

			if (this.draggedStack != null && this.isRightMouseClick) {
				mouseItemStack = mouseItemStack.copy()
				mouseItemStack.stackSize = MathHelper
						.ceiling_float_int(mouseItemStack.stackSize.asInstanceOf[Float] / 2.0F)
			}
			else if (this.field_147007_t && this.hoveredSlotSet.size() > 1) {
				mouseItemStack = mouseItemStack.copy()
				mouseItemStack.stackSize = this.mouseItemStackSize

				if (mouseItemStack.stackSize == 0) {
					s = "" + EnumChatFormatting.YELLOW + "0"
				}

			}

			this.drawItemStack(mouseItemStack, mouseX - guiLeft - b0, mouseY - guiTop - k1, s)

		}

		if (this.returningStack != null) {

			var f1: Float =
				(Minecraft.getSystemTime - this.returningStackTime).asInstanceOf[Float] / 100.0F

			if (f1 >= 1.0F) {
				f1 = 1.0F
				this.returningStack = null
			}

			// slotX - (mouseX - guiLeft)
			val xVar: Int = this.returningStackDestSlot.xDisplayPosition - this.mouseXAndGuiLeftDif
			// slotY - (mouseY - guiTop)
			val yVar: Int = this.returningStackDestSlot.yDisplayPosition - this.mouseYAndGuiTopDif
			// (mouseX - guiLeft) + ((slotX - (mouseX - guiLeft)) * timeVar)
			val itemStackX: Int = this.mouseXAndGuiLeftDif + (xVar.asInstanceOf[Float] * f1).asInstanceOf[Int]
			// (mouseY - guiTop) + ((slotY - (mouseY - guiTop)) * timeVar)
			val itemStackY: Int = this.mouseYAndGuiTopDif + (yVar.asInstanceOf[Float] * f1).asInstanceOf[Int]

			// Draw itemstack with not tool tip?
			this.drawItemStack(this.returningStack, itemStackX, itemStackY, null.asInstanceOf[String])
		}

		// End Matrix
		GL11.glPopMatrix()

		if (inventoryPlayer.getItemStack == null && this.theSlot != null &&
				this.theSlot.getHasStack) {
			val itemStack1: ItemStack = this.theSlot.getStack
			this.renderToolTip(itemStack1, mouseX, mouseY)
		}

		GL11.glEnable(GL11.GL_LIGHTING)
		GL11.glEnable(GL11.GL_DEPTH_TEST)
		RenderHelper.enableStandardItemLighting()

	}

	private def calculateAndDrawSlot(slot: Slot): Unit = {

		// Store the x position of the slot
		val slotX: Int = slot.xDisplayPosition
		// Store the y position of the slot
		val slotY: Int = slot.yDisplayPosition
		// Store the itemstack in the slot
		var slotItemStack: ItemStack = slot.getStack

		var shouldDrawHighlightedSlot: Boolean = false

		// Store the itemstack in the player's mouse
		val mouseItemStack: ItemStack = this.mc.thePlayer.inventory.getItemStack

		var s: String = null

		if (slot == this.clickedSlot && this.draggedStack != null &&
				this.isRightMouseClick && slotItemStack != null) {
			// Make a copy
			slotItemStack = slotItemStack.copy()
			// Divide the slot's itemstack size by 2 (cut in half)
			slotItemStack.stackSize /= 2
		}
		else if (this.field_147007_t && this.hoveredSlotSet.contains(slot) &&
				mouseItemStack != null) {
			// if the total number of slots for drag is just 1 slot, then dont do anything else
			if (this.hoveredSlotSet.size() == 1) {
				return
			}

			// Some unknown function
			// Can the container drag into a slot
			if (Container.func_94527_a(slot, mouseItemStack, true) &&
					this.container.canDragIntoSlot(slot)) {
				// copy the itemstack from the mouse to the slot
				slotItemStack = mouseItemStack.copy()

				shouldDrawHighlightedSlot = true

				var slotStackSize: Int = 0
				if (slot.getStack != null) {
					slotStackSize = slot.getStack.stackSize
				}
				Container.func_94525_a(this.hoveredSlotSet, this.slotTakingDecider, slotItemStack,
					slotStackSize)

				// Cap stacksize to that of the max stack size
				if (slotItemStack.stackSize > slotItemStack.getMaxStackSize) {
					s = EnumChatFormatting.YELLOW + "" + slotItemStack.getMaxStackSize
					slotItemStack.stackSize = slotItemStack.getMaxStackSize
				}

				// Cap stacksize to taht of the limit of the slot
				if (slotItemStack.stackSize > slot.getSlotStackLimit) {
					s = EnumChatFormatting.YELLOW + "" + slot.getSlotStackLimit
					slotItemStack.stackSize = slot.getSlotStackLimit
				}

			}
			else {
				// The slot cannot be dragged upon, so remove from the dragged slot list
				this.hoveredSlotSet.remove(slot)

				this.calculateHoveredStackSize()

			}

		}

		this.zLevel = 100.0F
		this.itemRender.zLevel = 100.0F

		var should_not_DrawItem: Boolean = slot == this.clickedSlot && this.draggedStack != null &&
				!this.isRightMouseClick

		// If the slot was/is empty, draw background of the slot
		if (slotItemStack == null) {
			val iicon: IIcon = slot.getBackgroundIconIndex

			if (iicon != null) {
				GL11.glDisable(GL11.GL_LIGHTING)

				UtilRender.bindResource(TextureMap.locationItemsTexture)
				this.drawTexturedModelRectFromIcon(slotX, slotY, iicon, 16, 16)
				GL11.glEnable(GL11.GL_LIGHTING)

				should_not_DrawItem = true
			}

		}

		if (!should_not_DrawItem) {
			if (shouldDrawHighlightedSlot) {
				Gui.drawRect(slotX, slotY, slotX + 16, slotY + 16, -2130706433)
			}

			GL11.glEnable(GL11.GL_DEPTH_TEST)
			// draw item
			itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager,
				slotItemStack, slotX, slotY)
			// draw tool tip
			itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager,
				slotItemStack, slotX, slotY, s)

		}

		itemRender.zLevel = 0.0F
		this.zLevel = 0.0F

	}

	private def calculateHoveredStackSize(): Unit = {
		// Get the itemstack in the mouse
		val mouseItemStack: ItemStack = this.mc.thePlayer.inventory.getItemStack

		// Stack in mouse not null
		// TODO Par is true
		if (mouseItemStack != null && this.field_147007_t) {
			// Save the mouse's stack size
			this.mouseItemStackSize = mouseItemStack.stackSize

			// A variable to store the itemstack in each slot from the list of dragged slots
			var hoveredItemStack: ItemStack = null
			// A variable that is used to calculate the stacksize that each of the dragged slots should have
			var hoveredItemStackSize: Int = 0

			// The iterator for the dragged slots
			val iterator: util.Iterator[_] = this.hoveredSlotSet.iterator()

			// iterate
			while (iterator.hasNext) {
				val slot: Slot = iterator.next().asInstanceOf[Slot]

				// Set the variable to a copy of the mouse's stack
				hoveredItemStack = mouseItemStack.copy()

				// Set the stacksize of the to-be slot stack
				hoveredItemStackSize = 0
				// if the slot already has a stack, then the stack size should be that of the present stack
				if (slot.getStack != null) {
					hoveredItemStackSize = slot.getStack.stackSize
				}

				// Handles splitting the stack
				Container.func_94525_a(this.hoveredSlotSet, this.slotTakingDecider, hoveredItemStack,
					hoveredItemStackSize)

				// If the stacksize is over the max stack size, cap it
				if (hoveredItemStack.stackSize > hoveredItemStack.getMaxStackSize) {
					hoveredItemStack.stackSize = hoveredItemStack.getMaxStackSize
				}

				// If stack size over stack limit, cap it
				if (hoveredItemStack.stackSize > slot.getSlotStackLimit) {
					hoveredItemStack.stackSize = slot.getSlotStackLimit
				}

				// save the stack size to the stacksize - 1
				this.mouseItemStackSize -= hoveredItemStack.stackSize - 1

			}

		}

	}

	private def isMouseOverSlot(slot: Slot, mouseX: Int, mouseY: Int): Boolean = {
		val x: Int = slot.xDisplayPosition
		val y: Int = slot.yDisplayPosition
		val w: Int = 16
		val h: Int = 16
		val guiLeft: Int = this.getGuiLeft()
		val guiTop: Int = this.getGuiTop()
		val mouseX_1 = mouseX - guiLeft
		val mouseY_1 = mouseY - guiTop
		return (mouseX_1 >= x - 1) && (mouseX_1 < x + w + 1) && (mouseY_1 >= y - 1) &&
				(mouseY_1 < y + h + 1)
	}

	private def drawItemStack(itemStack: ItemStack, par2: Int, par3: Int, string: String) {
		GL11.glTranslatef(0.0F, 0.0F, 32.0F)

		this.zLevel = 200.0F
		this.itemRender.zLevel = 200.0F

		var font: FontRenderer = null

		if (itemStack != null) {
			font = itemStack.getItem.getFontRenderer(itemStack)
		}

		if (font == null) {
			font = this.fontRendererObj
		}

		this.itemRender
				.renderItemAndEffectIntoGUI(font, this.mc.getTextureManager, itemStack, par2, par3)
		var v: Int = 0
		if (this.draggedStack != null) {
			v = 8
		}
		this.itemRender.renderItemOverlayIntoGUI(font, this.mc.getTextureManager, itemStack, par2,
			par3 - v, string)

		this.zLevel = 0.0F
		this.itemRender.zLevel = 0.0F

	}

	override def mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int): Unit = {
		super.mouseClicked(mouseX, mouseY, mouseButton)

		val flag: Boolean = mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode + 100
		val slot: Slot = this.getSlotAtPosition(mouseX, mouseY)
		val l: Long = Minecraft.getSystemTime
		this.field_146993_M = this.field_146998_K == slot && l - this.field_146997_J < 250L &&
				this.field_146992_L == mouseButton
		this.field_146995_H = false

		if (mouseButton == 0 || mouseButton == 1 || flag) {
			val i1: Int = this.getGuiLeft()
			val j1: Int = this.getGuiTop()

			val flag1: Boolean = mouseX < i1 || mouseY < j1 || mouseX >= i1 + this.getXSize() ||
					mouseY >= j1 + this.getYSize()

			var k1: Int = -1
			if (slot != null) {
				k1 = slot.slotNumber
			}
			if (flag1) {
				k1 = -999
			}

			if (this.mc.gameSettings.touchscreen && flag1 &&
					this.mc.thePlayer.inventory.getItemStack == null) {
				this.mc.displayGuiScreen(null.asInstanceOf[GuiScreen])
			}

			if (k1 != -1) {
				if (this.mc.gameSettings.touchscreen) {
					if (slot != null && slot.getHasStack) {
						this.clickedSlot = slot
						this.draggedStack = null
						this.isRightMouseClick = mouseButton == 1

					}
					else {
						this.clickedSlot = null

					}

				}
				else if (!this.field_147007_t) {
					if (this.mc.thePlayer.inventory.getItemStack == null) {
						if (mouseButton == this.mc.gameSettings.keyBindPickBlock.getKeyCode + 100) {
							this.handleMouseClick(slot, k1, mouseButton, 3)

						}
						else {
							val flag2: Boolean = k1 != -999 &&
									(Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54))
							var b0: Byte = 0

							if (flag2) {
								this.field_146994_N = null
								if (slot != null && slot.getHasStack) {
									this.field_146994_N = slot.getStack
								}
								b0 = 1
							}
							else if (k1 == -999) {
								b0 = 4
							}

							this.handleMouseClick(slot, k1, mouseButton, b0)

						}

						this.field_146995_H = true

					}
					else {
						this.field_147007_t = true
						this.field_146988_G = mouseButton
						this.hoveredSlotSet.clear()

						if (mouseButton == 0) {
							this.slotTakingDecider = 0
						}
						else if (mouseButton == 1) {
							this.slotTakingDecider = 1
						}

					}

				}

			}

		}

		this.field_146998_K = slot
		this.field_146997_J = l
		this.field_146992_L = mouseButton

	}

	def getSlotAtPosition(x: Int, y: Int): Slot = {
		var slotID: Int = 0
		for (slotID <- 0 until this.container.inventorySlots.size()) {
			val slot: Slot = this.container.inventorySlots.get(slotID).asInstanceOf[Slot]
			if (this.isMouseOverSlot(slot, x, y)) {
				return slot
			}

		}

		return null
	}

	protected def handleMouseClick(slot: Slot, slotID: Int, par3: Int, par4: Int): Unit = {
		// Take the passed slotID
		var slotID_Copy: Int = slotID

		// If the slot passed is not null, use the slot's id instead of the one passed
		if (slot != null) {
			slotID_Copy = slot.slotNumber
		}

		// Tell MC that a certain slot was clicked
		this.mc.playerController
				.windowClick(this.container.windowId, slotID_Copy, par3, par4, this.mc.thePlayer)

	}

	override def mouseClickMove(mouseX: Int, mouseY: Int, lastButtonClicked: Int,
			timeSinceMouseClick: Long): Unit = {
		val slot: Slot = this.getSlotAtPosition(mouseX, mouseY)
		val itemStack: ItemStack = this.mc.thePlayer.inventory.getItemStack

		if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
			if (lastButtonClicked == 0 || lastButtonClicked == 1) {
				if (this.draggedStack == null) {
					if (slot != this.clickedSlot) {
						this.draggedStack = this.clickedSlot.getStack.copy()
					}

				}
				else if (this.draggedStack.stackSize > 1 && slot != null &&
						Container.func_94527_a(slot, this.draggedStack, false)) {
					val i1: Long = Minecraft.getSystemTime

					if (this.field_146985_D == slot) {
						if (i1 - this.field_146986_E > 500L) {
							this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0,
								0)
							this.handleMouseClick(slot, slot.slotNumber, 1, 0)
							this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0,
								0)
							this.field_146986_E = i1 + 750L
							this.draggedStack.stackSize = this.draggedStack.stackSize - 1

						}

					}
					else {
						this.field_146985_D = slot
						this.field_146986_E = i1

					}

				}

			}

		}
		else if (this.field_147007_t && slot != null && itemStack != null &&
				itemStack.stackSize > this.hoveredSlotSet.size() &&
				Container.func_94527_a(slot, itemStack, true) && slot.isItemValid(itemStack) &&
				this.container.canDragIntoSlot(slot)) {
			this.hoveredSlotSet.add(slot)
			this.calculateHoveredStackSize()

		}

	}

	/**
	 * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
	 * mouseMove, which==0 or which==1 is mouseUp
	 */
	override def mouseMovedOrUp(mouseX: Int, mouseY: Int, which: Int): Unit = {
		super.mouseMovedOrUp(mouseX, mouseY, which)

		// Get the slot at the mouse's position
		val slot: Slot = this.getSlotAtPosition(mouseX, mouseY)
		// Get the left of the gui
		val guiLeft: Int = this.getGuiLeft()
		// Get the top of the gui
		val guiRight: Int = this.getGuiTop()

		// Declare a variable for the slot's number
		var slotID: Int = -1

		if (slot != null) {
			// save the slot's number
			slotID = slot.slotNumber
		}

		// If the mouse is outside the screen
		if (mouseX < guiLeft || mouseY < guiRight || mouseX >= guiLeft + this.getXSize() ||
				mouseY >= guiRight + this.getYSize()) {
			slotID = -999
		}

		// TODO PAR is true
		// The current slot is not null
		// The mouse is moving in a direction, not just up
		// Some variable returning true
		if (this.field_146993_M && slot != null && which == 0 &&
				this.container.func_94530_a(null.asInstanceOf[ItemStack], slot)) {
			// If player is holding shift
			if (GuiScreen.isShiftKeyDown) {
				// Slot's inventory not null
				// TODO PAR not null
				if (slot.inventory != null && this.field_146994_N != null) {

					// set the iterator of slots (this container's inventory
					val iterator: util.Iterator[_] = this.container.inventorySlots.iterator()

					// Create a variable for the slot
					var containerSlot: Slot = null

					// iterate
					while (iterator.hasNext) {
						// Get the next slot in this container
						containerSlot = iterator.next().asInstanceOf[Slot]
						// the slot is not null
						// the slot can give a stack to the player
						// the slot has a stack
						// the inventory of the slot is the same as the slot where the mouse is (likely empty)
						// Some function of unknown usage
						if (containerSlot != null &&
								containerSlot.canTakeStack(this.mc.thePlayer) &&
								containerSlot.getHasStack &&
								containerSlot.inventory == slot.inventory &&
								Container.func_94527_a(containerSlot, this.field_146994_N, true)) {
							// Send the moused over slot to be clicked
							this.handleMouseClick(containerSlot, containerSlot.slotNumber, which, 1)

						}

					}

				}

			}
			else {
				this.handleMouseClick(slot, slotID, which, 0)

			}

			this.field_146993_M = false
			this.field_146997_J = 0L

		}
		else {
			if (this.field_147007_t && this.field_146988_G != which) {
				this.field_147007_t = false
				this.hoveredSlotSet.clear()
				this.field_146995_H = true
				return
			}

			if (this.field_146995_H) {
				this.field_146995_H = false
				return
			}

			var flag1: Boolean = false

			if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
				if (which == 0 || which == 1) {
					if (this.draggedStack == null && slot != this.clickedSlot) {
						this.draggedStack = this.clickedSlot.getStack
					}

					flag1 = Container.func_94527_a(slot, this.draggedStack, false)

					if (slotID != -1 && this.draggedStack != null && flag1) {
						this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, which,
							0)
						this.handleMouseClick(slot, slotID, 0, 0)

						if (this.mc.thePlayer.inventory.getItemStack != null) {
							this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber,
								which, 0)
							this.mouseXAndGuiLeftDif = mouseX - guiLeft
							this.mouseYAndGuiTopDif = mouseY - guiRight
							this.returningStackDestSlot = this.clickedSlot
							this.returningStack = this.draggedStack
							this.returningStackTime = Minecraft.getSystemTime

						}
						else {
							this.returningStack = null

						}

					}
					else if (this.draggedStack != null) {
						this.mouseXAndGuiLeftDif = mouseX - guiLeft
						this.mouseYAndGuiTopDif = mouseY - guiRight
						this.returningStackDestSlot = this.clickedSlot
						this.returningStack = this.draggedStack
						this.returningStackTime = Minecraft.getSystemTime

					}

					this.draggedStack = null
					this.clickedSlot = null

				}

			}
			else if (this.field_147007_t && !this.hoveredSlotSet.isEmpty) {
				this.handleMouseClick(null.asInstanceOf[Slot], -999,
					Container.func_94534_d(0, this.slotTakingDecider), 5)

				var slot1: Slot = null

				var iterator: util.Iterator[_] = this.hoveredSlotSet.iterator()

				while (iterator.hasNext) {
					slot1 = iterator.next().asInstanceOf[Slot]
					this.handleMouseClick(slot1, slot1.slotNumber,
						Container.func_94534_d(1, this.slotTakingDecider), 5)

				}

				this.handleMouseClick(null.asInstanceOf[Slot], -999,
					Container.func_94534_d(0, this.slotTakingDecider), 5)

			}
			else if (this.mc.thePlayer.inventory.getItemStack != null) {
				if (which == this.mc.gameSettings.keyBindPickBlock.getKeyCode + 100) {
					this.handleMouseClick(slot, slotID, which, 3)

				}
				else {
					flag1 = slotID != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54))

					if (flag1) {
						this.field_146994_N = null
						if (slot != null && slot.getHasStack) {
							this.field_146994_N = slot.getStack

						}

					}

					var i: Int = 0
					if (flag1) {
						i = 1
					}
					this.handleMouseClick(slot, slotID, which, i)

				}

			}

		}

		if (this.mc.thePlayer.inventory.getItemStack == null) {
			this.field_146997_J = 0L
		}

		this.field_147007_t = false

	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	override def keyTyped(letter: Char, key: Int): Unit = {
		if (key == 1 | key == this.mc.gameSettings.keyBindInventory.getKeyCode) {
			this.mc.thePlayer.closeScreen()
		}

		this.checkHotbarKeys(key)

		if (this.theSlot != null && this.theSlot.getHasStack) {
			if (key == this.mc.gameSettings.keyBindPickBlock.getKeyCode) {
				this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, 0, 3)

			}
			else if (key == this.mc.gameSettings.keyBindDrop.getKeyCode) {
				var i: Int = 0
				if (GuiScreen.isCtrlKeyDown) {
					i = 1
				}

				this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, i, 4)

			}
		}

		super.keyTyped(letter, key)

	}

	/**
	 * This function is what controls the hotbar shortcut check when you press a number key when hovering a stack.
	 */
	protected def checkHotbarKeys(par1: Int): Boolean = {
		if (this.mc.thePlayer.inventory.getItemStack == null && this.theSlot != null) {
			var j: Int = 0
			for (j <- 0 until 9) {
				if (par1 == this.mc.gameSettings.keyBindsHotbar(j).getKeyCode) {
					this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, j, 2)
					return true
				}

			}

		}

		return false
	}

	override def onGuiClosed(): Unit = {
		super.onGuiClosed()

		if (this.mc.thePlayer != null) {
			this.container.onContainerClosed(this.mc.thePlayer)

		}

	}

	override def updateScreen(): Unit = {
		super.updateScreen()

		if (!this.mc.thePlayer.isEntityAlive || this.mc.thePlayer.isDead) {
			this.mc.thePlayer.closeScreen()
		}

	}

}
