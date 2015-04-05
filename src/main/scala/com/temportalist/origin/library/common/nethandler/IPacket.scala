package com.temportalist.origin.library.common.nethandler

import java.io._
import java.util.UUID

import com.temportalist.origin.api.INBTSaver
import com.temportalist.origin.library.common.Origin
import com.temportalist.origin.library.common.lib.vec.V3O
import com.temportalist.origin.library.common.lib.{LogHelper, NameParser}
import cpw.mods.fml.common.network.NetworkRegistry
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt._
import net.minecraft.tileentity.TileEntity

import scala.reflect.runtime.universe._

/**
 *
 *
 * @author TheTemportalist
 */
trait IPacket {

	val writeStream: ByteArrayOutputStream = new ByteArrayOutputStream()
	val writeData: DataOutputStream = new DataOutputStream(this.writeStream)
	var readData: DataInputStream = null

	def getChannel(): String

	def sendTo(dest: EnumPacketDestination, args: Any*): Unit = {
		dest match {
			case EnumPacketDestination.CLIENTS =>
				PacketHandler.sendToClients(this.getChannel(), this)
			case EnumPacketDestination.PLAYER =>
				PacketHandler.sendToPlayer(this.getChannel(), this,
					args(0).asInstanceOf[EntityPlayer])
			case EnumPacketDestination.ALL_AROUND =>
				PacketHandler.sendToAllAround(this.getChannel(), this,
					args(0).asInstanceOf[NetworkRegistry.TargetPoint])
			case EnumPacketDestination.DIMENSION =>
				PacketHandler.sendToDimension(this.getChannel(), this, args(0).asInstanceOf[Int])
			case EnumPacketDestination.SERVER =>
				PacketHandler.sendToServer(this.getChannel(), this)
			case EnumPacketDestination.BOTH =>
				PacketHandler.sendToServerAndClients(this.getChannel(), this)
			case _ =>
		}
	}

	def sendToClients(): Unit = this.sendTo(EnumPacketDestination.CLIENTS)

	def sendToPlayer(player: EntityPlayer): Unit = this.sendTo(EnumPacketDestination.PLAYER, player)

	def sendToAll(p: NetworkRegistry.TargetPoint): Unit =
		this.sendTo(EnumPacketDestination.ALL_AROUND, p)

	def sendToDimension(d: Int): Unit = this.sendTo(EnumPacketDestination.DIMENSION, d)

	def sendToServer(): Unit = this.sendTo(EnumPacketDestination.SERVER)

	def sendToBoth(): Unit = this.sendTo(EnumPacketDestination.BOTH)

	final def writeTo(buffer: ByteBuf): Unit = {
		buffer.writeBytes(this.writeStream.toByteArray)
	}

	final def readFrom(buffer: ByteBuf): Unit = {
		this.readData = new DataInputStream(new ByteArrayInputStream(buffer.array()))
		try {
			this.readData.skipBytes(1)
		}
		catch {
			case e: Exception =>
		}
	}

	final def add(all: Any*): IPacket = {
		if (all == null) return this
		for (any: Any <- all) if (any != null) {
			try {
				any match {
					case bool: Boolean => this.writeData.writeBoolean(bool)
					case byte: Byte => this.writeData.writeByte(byte)
					case short: Short => this.writeData.writeShort(short)
					case int: Int => this.writeData.writeInt(int)
					case char: Char => this.writeData.writeChar(char)
					case float: Float => this.writeData.writeFloat(float)
					case double: Double => this.writeData.writeDouble(double)
					case long: Long => this.writeData.writeLong(long)
					case str: String => this.writeData.writeUTF(str)
					case array: Array[Double] =>
						this.add(array.length)
						for (d: Double <- array)
							this.add(d)
					case uuid: UUID =>
						this.add(uuid.getMostSignificantBits)
						this.add(uuid.getLeastSignificantBits)
					case nbt: NBTTagCompound =>
						//CompressedStreamTools.writeCompressed(nbt, this.writeData)
						if (nbt == null) this.add((-1).toShort)
						else this.add(CompressedStreamTools.compress(nbt))
					case stack: ItemStack =>
						this.add(NameParser.getName(stack))
						this.add(stack.hasTagCompound)
						if (stack.hasTagCompound) this.add(stack.getTagCompound)
					case v: V3O =>
						this.add(v.x)
						this.add(v.y)
						this.add(v.z)
					case tile: TileEntity =>
						this.add(new V3O(tile))
					case saver: INBTSaver =>
						val tag: NBTTagCompound = new NBTTagCompound
						saver.writeTo(tag)
						this.add(tag)
					case array: Array[Byte] =>
						this.add(array.length.toShort)
						this.writeData.write(array)
					case _ =>
						LogHelper.error(Origin.MODID,
							"Packets cannot add " + any.getClass.getCanonicalName + " objects")
				}
			}
			catch {
				case e: Exception =>
					e.printStackTrace()
			}
		}
		this
	}

	final def get[T: TypeTag]: T = {
		(try {
			typeOf[T] match {
				case t if t =:= typeOf[Boolean] =>
					this.readData.readBoolean()
				case t if t =:= typeOf[Byte] =>
					this.readData.readByte()
				case t if t =:= typeOf[Short] =>
					this.readData.readShort()
				case t if t =:= typeOf[Int] =>
					this.readData.readInt()
				case t if t =:= typeOf[Char] =>
					this.readData.readChar()
				case t if t =:= typeOf[Float] =>
					this.readData.readFloat()
				case t if t =:= typeOf[Double] =>
					this.readData.readDouble()
				case t if t =:= typeOf[Long] =>
					this.readData.readLong()
				case t if t =:= typeOf[String] =>
					this.readData.readUTF()
				case t if t =:= typeOf[Array[Double]] =>
					val array: Array[Double] = new Array[Double](this.get[Int])
					for (i <- 0 until array.length)
						array(i) = this.get[Double]
				case t if t =:= typeOf[UUID] =>
					new UUID(this.get[Long], this.get[Long])
				case t if t =:= typeOf[NBTTagCompound] =>
					//CompressedStreamTools.read(this.readData)
					val array: Array[Byte] = this.get[Array[Byte]]
					if (array != null) {
						CompressedStreamTools.func_152457_a(array,
							new NBTSizeTracker(2097152L)
						)
					}
					else null
				case t if t =:= typeOf[ItemStack] =>
					val stack: ItemStack = NameParser.getItemStack(this.get[String])
					if (this.get[Boolean]) stack.setTagCompound(this.get[NBTTagCompound])
					stack
				case t if t =:= typeOf[V3O] =>
					new V3O(this.get[Double], this.get[Double], this.get[Double])
				case t if t =:= typeOf[Array[Byte]] =>
					val length: Short = this.get[Short]
					if (length < 0) null
					else {
						val array: Array[Byte] = new Array[Byte](length)
						this.readData.read(array)
						array
					}
				case _ =>
					LogHelper.error(Origin.MODID, "Packets cannot get type: " + typeOf[T])
					null
			}
		}
		catch {
			case e: Exception =>
				e.printStackTrace()
				null
		}).asInstanceOf[T] // wrap what ever returns to make compielr ahppy
	}

	def handleOnClient(player: EntityPlayer): Unit = {
		this.handle(player, isServer = false)
	}

	def handleOnServer(player: EntityPlayer): Unit = {
		this.handle(player, isServer = true)
	}

	def handle(player: EntityPlayer, isServer: Boolean): Unit = {}

}
