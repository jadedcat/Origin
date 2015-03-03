package com.temportalist.origin.library.common.lib.vec

import com.google.common.io.ByteArrayDataInput
import com.temportalist.origin.api.INBTSaver
import com.temportalist.origin.library.client.utility.TessRenderer
import com.temportalist.origin.library.common.utility.MathFuncs
import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.init.Blocks
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util._
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.{ChunkCoordIntPair, World}
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

/**
 *
 *
 * @author TheTemportalist
 */
class V3O(var x: Double, var y: Double, var z: Double) extends INBTSaver {

	def this(array: Array[Double]) {
		this(array(0), array(1), array(2))
	}

	def this(vec: Vec3) {
		this(vec.xCoord, vec.yCoord, vec.zCoord)
	}

	def this(vec: Vec3i) {
		this(vec.getX, vec.getY, vec.getZ)
	}

	def this(amount: Double) {
		this(amount, amount, amount)
	}

	def this(ent: Entity) {
		this(ent.posX, ent.posY, ent.posZ)
	}

	def this(tile: TileEntity) {
		this(tile.getPos)
	}

	def this(mop: MovingObjectPosition, isBlock: Boolean) {
		this(mop.hitVec)
	}

	def this(chunk: ChunkCoordIntPair) {
		this(chunk.chunkXPos, 0, chunk.chunkZPos)
	}

	def this(dir: EnumFacing) {
		this(dir.getFrontOffsetX, dir.getFrontOffsetY, dir.getFrontOffsetZ)
	}

	def this(nbt: NBTTagCompound) {
		this(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"))
	}

	def this(data: ByteArrayDataInput) {
		this(data.readDouble(), data.readDouble(), data.readDouble())
	}

	def this(data: ByteBuf) {
		this(data.readDouble(), data.readDouble(), data.readDouble())
	}

	def this(u: Int, v: Int) {
		this(u, v, 0)
	}

	def u(): Int = this.x_i()

	def v(): Int = this.y_i()

	def x_i(): Int = this.x.asInstanceOf[Int]

	def y_i(): Int = this.y.asInstanceOf[Int]

	def z_i(): Int = this.z.asInstanceOf[Int]

	def x_f(): Float = this.x.asInstanceOf[Float]

	def y_f(): Float = this.y.asInstanceOf[Float]

	def z_f(): Float = this.z.asInstanceOf[Float]

	def toData(data: ByteBuf): Unit = {
		data.writeDouble(this.x)
		data.writeDouble(this.y)
		data.writeDouble(this.z)
	}

	override def writeTo(tag: NBTTagCompound): Unit = {
		tag.setDouble("x", this.x)
		tag.setDouble("y", this.y)
		tag.setDouble("z", this.z)
	}

	override def readFrom(tag: NBTTagCompound): Unit = {
		this.x = tag.getInteger("x")
		this.y = tag.getInteger("y")
		this.z = tag.getInteger("z")
	}

	def toBlockPos(): BlockPos = {
		new BlockPos(
			MathHelper.floor_double(this.x),
			MathHelper.floor_double(this.y),
			MathHelper.floor_double(this.z)
		)
	}

	def toBlockCoord(world: World): BlockCoord = new BlockCoord(this, world.provider.getDimensionId)

	def markForUpdate(world: World): Unit = this.toBlockCoord(world).markForUpdate()

	def toVec3(): Vec3 = new Vec3(this.x, this.y, this.z)

	def toVec3i(): Vec3i = new Vec3i(this.x_i(), this.y_i(), this.z_i())

	def getChunk(world: World): Chunk = this.toBlockCoord(world).getChunk()

	def getBlockState(world: World): IBlockState = this.toBlockCoord(world).getBlockState()

	def getBlock(world: World): Block = this.getBlockState(world).getBlock

	def getTile(world: World): TileEntity = this.toBlockCoord(world).getTile()

	def setBlock(world: World, block: Block, meta: Int, notify: Int): Unit =
		this.toBlockCoord(world).setBlock(block, meta, notify)

	def setBlock(world: World, block: Block, meta: Int): Unit =
		this.setBlock(world, block, meta, 3)

	def setBlock(world: World, block: Block): Unit = {
		this.setBlock(world, block, 0)
	}

	def setBlockToAir(world: World): Unit = {
		this.setBlock(world, Blocks.air)
	}

	def toNBT(nbt: NBTTagCompound) {
		nbt.setDouble("x", this.x)
		nbt.setDouble("y", this.y)
		nbt.setDouble("z", this.z)
	}

	@SideOnly(Side.CLIENT)
	def addVecUV(u: Double, v: Double): Unit = {
		TessRenderer.addVertex(this.x, this.y, this.z, u, v)
	}

	def set(x1: Double, y1: Double, z1: Double): Unit = {
		this.x = x1
		this.y = y
		this.z = z
	}

	def set(vec: V3O): Unit = this.set(vec.x, vec.y, vec.z)

	// Additive things. Plus(+) & Add(+=) http://stackoverflow.com/questions/16644988/why-is-the-unary-prefix-needed-in-scala

	// Additive +, does not modify this vector

	def plus(x1: Double, y1: Double, z1: Double): V3O = new V3O(
		this.x + x1, this.y + y1, this.z + z1
	)

	def +(v: V3O): V3O = this.plus(v.x, v.y, v.z)

	def +(d: Double): V3O = this.plus(d, d, d)

	def +(dir: EnumFacing): V3O = this + new V3O(dir)

	// Additive +=

	def add(x1: Double, y1: Double, z1: Double): Unit = {
		this.set(this.plus(x1, y1, z1))
	}

	def +=(vec: V3O): Unit = this.add(vec.x, vec.y, vec.z)

	def +=(d: Double): Unit = this.add(d, d, d)

	def add(dir: EnumFacing, amount: Double): Unit =
		this += new V3O(dir) * amount

	def +=(dir: EnumFacing): Unit = this.add(dir, 1)

	def down(amount: Double): Unit = this.add(EnumFacing.DOWN, amount)

	def down(): Unit = this.down(1)

	def up(amount: Double): Unit = this.add(EnumFacing.UP, amount)

	def up(): Unit = this.up(1)

	def north(amount: Double): Unit = this.add(EnumFacing.NORTH, amount)

	def north(): Unit = this.north(1)

	def south(amount: Double): Unit = this.add(EnumFacing.SOUTH, amount)

	def south(): Unit = this.south(1)

	def east(amount: Double): Unit = this.add(EnumFacing.EAST, amount)

	def east(): Unit = this.east(1)

	def west(amount: Double): Unit = this.add(EnumFacing.WEST, amount)

	def west(): Unit = this.west(1)

	// Subtractive +, does not modify

	def minus(x1: Double, y1: Double, z1: Double): V3O = new V3O(
		this.x - x1, this.y - y1, this.z - z1
	)

	def -(vec: V3O): V3O = this.minus(vec.x, vec.y, vec.z)

	def -(d: Double): V3O = this.minus(d, d, d)

	// Subtractive +=

	def subtract(x1: Double, y1: Double, z1: Double): Unit = this.set(this.minus(x1, y1, z1))

	def -=(vec: V3O): Unit = this.subtract(vec.x, vec.y, vec.z)

	def -=(d: Double): Unit = this.subtract(d, d, d)

	// Multiplicitive *

	def times(x1: Double, y1: Double, z1: Double): V3O = new V3O(
		this.x * x1, this.y * y1, this.z * z1
	)

	def *(vec: V3O): V3O = this.times(vec.x, vec.y, vec.z)

	def *(d: Double): V3O = this.times(d, d, d)

	// Multiplicitive *=

	def multiply(x1: Double, y1: Double, z1: Double): Unit = this.set(this.times(x1, y1, z1))

	def *=(vec: V3O): Unit = this.multiply(vec.x, vec.y, vec.z)

	def *=(d: Double): Unit = this.multiply(d, d, d)

	def invert(): Unit = this.+=(-1)

	// 3D Functions

	def magSquared(): Double = {
		this.x * this.x + this.y * this.y + this.z * this.z
	}

	def magnitude(): Double = {
		Math.sqrt(this.magSquared())
	}

	def distance(vec: V3O): Double = {
		(this - vec).magnitude()
	}

	def distance(x: Double, y: Double, z: Double): Double = {
		this.distance(new V3O(x, y, z))
	}

	def normalize(): V3O = {
		val mag: Double = this.magnitude()
		this * (if (mag != 0.0D) 1.0d / mag else 1d)
	}

	def dotProduct(vec: V3O): Double = {
		var d: Double = vec.x * this.x + vec.y * this.y + vec.z * this.z
		d = MathFuncs.bind(1, d, 1.00001, 1)
		d = MathFuncs.bind(-1.00001, d, -1, -1)
		d
	}

	def dotProduct(x1: Double, y1: Double, z1: Double): Double = {
		x1 * this.x + y1 * this.y + z1 * this.z
	}

	def crossProduct(vec: V3O): V3O = {
		val (x1, y1, z1) = (
				this.y * vec.z - this.z * vec.y,
				this.z * vec.x - this.x * vec.z,
				this.x * vec.y - this.y * vec.x
				)
		x = x1
		y = y1
		z = z1
		this
	}

	def crossProduct(axis: EnumFacing.Axis): V3O = {
		val (x1, y1, z1) = (this.x, this.y, this.z)
		this.x = 0
		this.y = 0
		this.z = 0
		axis match {
			case EnumFacing.Axis.X =>
				this.y = z1
				this.z = -y1
			case EnumFacing.Axis.Y =>
				this.x = -z1
				this.z = x1
			case EnumFacing.Axis.Z =>
				this.x = y1
				this.y = -x1
			case _ =>
		}
		this
	}

	def xCrossProduct(): V3O = this.crossProduct(EnumFacing.Axis.X)

	def zCrossProduct(): V3O = this.crossProduct(EnumFacing.Axis.Z)

	def yCrossProduct(): V3O = this.crossProduct(EnumFacing.Axis.Y)

	def perpendicular(): V3O =
		if (this.z == 0.0D) this.zCrossProduct() else this.xCrossProduct()

	//def rotate(angle: Double, axis: Vector3O): Vector3O = new Vector3O(super.rotate(angle, axis))

	//def rotate(rotator: Quat): Vector3O = new Vector3O(super.rotate(rotator))

	def intercept(axis: EnumFacing.Axis, end: V3O, p: Double): V3O = {
		val (dx, dy, dz) = (end.x - this.x, end.y - this.y, end.z - this.z)
		var d: Double = 0.0D
		axis match {
			case EnumFacing.Axis.X =>
				if (dx == 0.0D) return null
				d = (p - this.x) / dx

				if (MathFuncs.between_eq(-1E-5, d, 1E-5)) return this
				if (!MathFuncs.between_eq(0, d, 1)) return null

				x = p
				y += d * dy
				z += d * dz
			case EnumFacing.Axis.Y =>
				if (dy == 0.0D) return null
				d = (p - this.y) / dy

				if (MathFuncs.between_eq(-1E-5, d, 1E-5)) return this
				if (!MathFuncs.between_eq(0, d, 1)) return null

				x += d * dx
				y = p
				z += d * dz
			case EnumFacing.Axis.Z =>
				if (dz == 0.0D) return null
				d = (p - this.z) / dz

				if (MathFuncs.between_eq(-1E-5, d, 1E-5)) return this
				if (!MathFuncs.between_eq(0, d, 1)) return null

				x += d * dx
				y += d * dy
				z = p
			case _ =>
		}
		this
	}

	def YZintercept(end: V3O, px: Double): V3O =
		this.intercept(EnumFacing.Axis.X, end, px)

	def XZintercept(end: V3O, py: Double): V3O =
		this.intercept(EnumFacing.Axis.Y, end, py)

	def XYintercept(end: V3O, pz: Double): V3O =
		this.intercept(EnumFacing.Axis.Z, end, pz)

	def unary_$tilde(): V3O = this.normalize()

	def $tilde(): V3O = this.normalize()

	def /(d: Double): V3O = this * (1 / d)

	def copy(): V3O = new V3O(this.x, this.y, this.z)

	override def equals(o: scala.Any): Boolean = {
		o match {
			case vec: V3O =>
				return vec.x == this.x && vec.y == this.y && vec.z == this.z
			case _ =>
		}
		false
	}

	override def hashCode(): Int = {
		var hash: Int = 1
		hash = hash * 31 + this.x.hashCode()
		hash = hash * 31 + this.y.hashCode()
		hash = hash * 31 + this.z.hashCode()
		hash
	}

}

object V3O {

	def from(x: Double, y: Double, z: Double, dir: EnumFacing): V3O = {
		new V3O(x, y, z) + new V3O(dir)
	}

	def from(aabb: AxisAlignedBB): V3O = {
		new V3O(aabb.maxX, aabb.maxY, aabb.maxZ) - new V3O(aabb.minX, aabb.minY, aabb.minZ)
	}

	def readFrom(tag: NBTTagCompound, key: String): V3O = {
		val vec: V3O = V3O.ZERO
		vec.readFrom(tag, key)
		vec
	}

	def UP: V3O = new V3O(EnumFacing.UP)

	def DOWN: V3O = new V3O(EnumFacing.DOWN)

	def NORTH: V3O = new V3O(EnumFacing.NORTH)

	def SOUTH: V3O = new V3O(EnumFacing.SOUTH)

	def EAST: V3O = new V3O(EnumFacing.EAST)

	def WEST: V3O = new V3O(EnumFacing.WEST)

	def ZERO: V3O = new V3O(0, 0, 0)

	def CENTER: V3O = new V3O(0.5, 0.5, 0.5)

	def SINGLE: V3O = new V3O(1, 1, 1)

}
