package com.temportalist.origin.library.common.lib.vec

import com.google.common.io.ByteArrayDataInput
import com.temportalist.origin.library.common.utility.MathFuncs
import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.Tessellator
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
class Vector3O(var x: Double, var y: Double, var z: Double) {

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

	def scale(x: Double, y: Double, z: Double): Vector3O = {
		this.x *= x
		this.y *= y
		this.z *= z
		this
	}

	def scale(amount: Double): Vector3O = this.scale(amount, amount, amount)

	def scale(vec: Vector3O): Vector3O = this.scale(vec.x, vec.y, vec.z)

	def invert(): Vector3O = this.scale(-1)

	def copy(): Vector3O = new Vector3O(this.x, this.y, this.z)

	def toNBT(nbt: NBTTagCompound) {
		nbt.setDouble("x", this.x)
		nbt.setDouble("y", this.y)
		nbt.setDouble("z", this.z)
	}

	def distance(vec: Vector3O): Double = {
		this.copy().subtract(vec).mag()
	}

	def distance(x: Double, y: Double, z: Double): Double = {
		this.distance(new Vector3O(x, y, z))
	}

	@SideOnly(Side.CLIENT)
	def addVecUV(u: Double, v: Double): Unit = {
		Tessellator.getInstance().getWorldRenderer.addVertexWithUV(this.x, this.y, this.z, u, v)
	}

	def magSquared(): Double = {
		this.x * this.x + this.y * this.y + this.z * this.z
	}

	def mag(): Double = {
		Math.sqrt(this.magSquared())
	}

	def add(dir: EnumFacing): Vector3O = this.add(new Vector3O(dir))

	def set(x1: Double, y1: Double, z1: Double): Vector3O = {
		this.x = x1
		this.y = y
		this.z = z
		this
	}

	def set(vec: Vector3O): Vector3O = this.set(vec.x, vec.y, vec.z)

	def add(x1: Double, y1: Double, z1: Double): Vector3O = {
		this.x += x1
		this.y += y1
		this.z += z1
		this
	}

	def add(vec: Vector3O): Vector3O = this.add(vec.x, vec.y, vec.z)

	def add(d: Double): Vector3O = this.add(d, d, d)

	def subtract(x1: Double, y1: Double, z1: Double): Vector3O = {
		this.x -= x1
		this.y -= y1
		this.z -= z1
		this
	}

	def subtract(vec: Vector3O): Vector3O = this.subtract(vec.x, vec.y, vec.z)

	def subtract(d: Double): Vector3O = this.subtract(d, d, d)

	def negate(): Vector3O = {
		this.x = -this.x
		this.y = -this.y
		this.z = -this.z
		this
	}

	def multiply(x1: Double, y1: Double, z1: Double): Vector3O = {
		this.x *= x1
		this.y *= y1
		this.z *= z1
		this
	}

	def multiply(d: Double): Vector3O = this.multiply(d, d, d)

	def multiply(f: Vector3O): Vector3O = this.multiply(f.x, f.y, f.z)

	def normalize(): Vector3O = {
		val mag: Double = this.mag()
		if (mag != 0.0D) {
			this.multiply(1.0D / mag)
		}
		else this
	}

	def dotProduct(vec: Vector3O): Double = {
		var d: Double = vec.x * this.x + vec.y * this.y + vec.z * this.z
		d = MathFuncs.bind(1, d, 1.00001, 1)
		d = MathFuncs.bind(-1.00001, d, -1, -1)
		d
	}

	def dotProduct(x1: Double, y1: Double, z1: Double): Double = {
		x1 * this.x + y1 * this.y + z1 * this.z
	}

	def crossProduct(vec: Vector3O): Vector3O = {
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

	def crossProduct(axis: EnumFacing.Axis): Vector3O = {
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

	def xCrossProduct(): Vector3O = this.crossProduct(EnumFacing.Axis.X)

	def zCrossProduct(): Vector3O = this.crossProduct(EnumFacing.Axis.Z)

	def yCrossProduct(): Vector3O = this.crossProduct(EnumFacing.Axis.Y)

	def perpendicular(): Vector3O =
		if (this.z == 0.0D) this.zCrossProduct() else this.xCrossProduct()

	//def rotate(angle: Double, axis: Vector3O): Vector3O = new Vector3O(super.rotate(angle, axis))

	//def rotate(rotator: Quat): Vector3O = new Vector3O(super.rotate(rotator))

	def intercept(axis: EnumFacing.Axis, end: Vector3O, p: Double): Vector3O = {
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

	def YZintercept(end: Vector3O, px: Double): Vector3O =
		this.intercept(EnumFacing.Axis.X, end, px)

	def XZintercept(end: Vector3O, py: Double): Vector3O =
		this.intercept(EnumFacing.Axis.Y, end, py)

	def XYintercept(end: Vector3O, pz: Double): Vector3O =
		this.intercept(EnumFacing.Axis.Z, end, pz)

	def unary_$tilde(): Vector3O = this.normalize()

	def $tilde(): Vector3O = this.normalize()

	def $minus(v: Vector3O): Vector3O = this.subtract(v)

	def $plus(v: Vector3O): Vector3O = this.add(v)

	def $times(d: Double): Vector3O = this.multiply(d)

	def $div(d: Double): Vector3O = this.multiply(1 / d)

	def $times(v: Vector3O): Vector3O = this.crossProduct(v)

	def $dot$times(v: Vector3O): Double = this.dotProduct(v)

	override def equals(o: scala.Any): Boolean = {
		o match {
			case vec: Vector3O =>
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

object Vector3O {

	def from(x: Double, y: Double, z: Double, dir: EnumFacing): Vector3O = {
		new Vector3O(x, y, z).add(new Vector3O(dir))
	}

	def UP: Vector3O = new Vector3O(EnumFacing.UP)

	def DOWN: Vector3O = new Vector3O(EnumFacing.DOWN)

	def NORTH: Vector3O = new Vector3O(EnumFacing.NORTH)

	def SOUTH: Vector3O = new Vector3O(EnumFacing.SOUTH)

	def EAST: Vector3O = new Vector3O(EnumFacing.EAST)

	def WEST: Vector3O = new Vector3O(EnumFacing.WEST)

	def ZERO: Vector3O = new Vector3O(0, 0, 0)

	def CENTER: Vector3O = new Vector3O(0.5, 0.5, 0.5)

	def SINGLE: Vector3O = new Vector3O(1, 1, 1)

}
