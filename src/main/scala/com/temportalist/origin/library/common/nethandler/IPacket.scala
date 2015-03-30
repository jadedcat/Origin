package com.temportalist.origin.library.common.nethandler

import java.io._
import java.util.UUID

import scala.reflect.runtime.universe._

import com.temportalist.origin.api.INBTSaver
import com.temportalist.origin.library.common.lib.NameParser
import com.temportalist.origin.library.common.lib.vec.{BlockPos, V3O}
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt._
import net.minecraft.tileentity.TileEntity

/**
 *
 *
 * @author TheTemportalist
 */
trait IPacket {

	val writeStream: ByteArrayOutputStream = new ByteArrayOutputStream()
	val writeData: DataOutputStream = new DataOutputStream(this.writeStream)
	var readData: DataInputStream = null

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
					case bool: Boolean =>
						this.writeData.writeBoolean(bool)
					case byte: Byte =>
						this.writeData.writeByte(byte)
					case short: Short =>
						this.writeData.writeShort(short)
					case int: Int =>
						this.writeData.writeInt(int)
					case char: Char =>
						this.writeData.writeChar(char)
					case float: Float =>
						this.writeData.writeFloat(float)
					case double: Double =>
						this.writeData.writeDouble(double)
					case long: Long =>
						this.writeData.writeLong(long)
					case str: String =>
						this.writeData.writeUTF(str)
					case array: Array[Double] =>
						this.add(array.length)
						for (d: Double <- array)
							this.add(d)
					case uuid: UUID =>
						this.add(uuid.getMostSignificantBits)
						this.add(uuid.getLeastSignificantBits)
					case nbt: NBTTagCompound =>
						CompressedStreamTools.writeCompressed(nbt, this.writeData)
					case stack: ItemStack =>
						this.add(NameParser.getName(stack))
						this.add(stack.hasTagCompound)
						if (stack.hasTagCompound) this.add(stack.getTagCompound)
					case pos: BlockPos =>
						this.add(pos.getX)
						this.add(pos.getY)
						this.add(pos.getZ)
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
					case _ =>
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
					CompressedStreamTools.read(this.readData)
				case t if t =:= typeOf[ItemStack] =>
					val stack: ItemStack = NameParser.getItemStack(this.get[String])
					if (this.get[Boolean]) stack.setTagCompound(this.get[NBTTagCompound])
					stack
				case t if t =:= typeOf[BlockPos] =>
					new BlockPos(this.get[Int], this.get[Int], this.get[Int])
				case t if t =:= typeOf[V3O] =>
					new V3O(this.get[Double], this.get[Double], this.get[Double])
				case _ =>
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
