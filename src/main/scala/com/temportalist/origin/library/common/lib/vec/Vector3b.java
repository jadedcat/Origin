package com.temportalist.origin.library.common.lib.vec;

import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Quat;
import codechicken.lib.vec.Vector3;
import com.google.common.io.ByteArrayDataInput;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @author TheTemportalist
 */
public class Vector3b extends Vector3 implements IVector3 {

	public Vector3b() {
	}

	public Vector3b(double x, double y, double z) {
		super(x, y, z);
	}

	public Vector3b(double[] da) {
		this(da[0], da[1], da[2]);
	}

	public Vector3b(Vec3 vec) {
		x = vec.xCoord;
		y = vec.yCoord;
		z = vec.zCoord;
	}

	public Vector3b(Vector3 vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
	}

	public Vector3b(IVector3 vec) {
		this(vec.x(), vec.y(), vec.z());
	}

	public Vector3b(Vector3b vec) {
		this(vec.x(), vec.y(), vec.z());
	}

	public Vector3b(BlockCoord coord) {
		x = coord.x;
		y = coord.y;
		z = coord.z;
	}

	public Vector3b(double amount) {
		super(amount, amount, amount);
	}

	public Vector3b(Entity ent) {
		Vector3.fromEntity(ent);
	}

	public Vector3b(TileEntity tile) {
		Vector3.fromTileEntity(tile);
	}

	public Vector3b(MovingObjectPosition mop) {
		super(mop.blockX, mop.blockY, mop.blockZ);
	}

	public Vector3b(ChunkCoordinates chunk) {
		super(chunk.posX, chunk.posY, chunk.posZ);
	}

	public Vector3b(ForgeDirection dir) {
		super(dir.offsetX, dir.offsetY, dir.offsetZ);
	}

	public Vector3b(NBTTagCompound nbt) {
		this(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"));
	}

	public Vector3b(ByteArrayDataInput data) {
		this(data.readDouble(), data.readDouble(), data.readDouble());
	}

	public static Vector3b from(int x, int y, int z, ForgeDirection dir) {
		return new Vector3b(x, y, z).add(new Vector3b(dir));
	}

	@Override
	public double x() {
		return this.x;
	}

	@Override
	public double y() {
		return this.y;
	}

	@Override
	public double z() {
		return this.z;
	}

	public int x_i() {
		return (int) this.x;
	}

	public int y_i() {
		return (int) this.y;
	}

	public int z_i() {
		return (int) this.z;
	}

	public float x_f() {
		return (float) this.x;
	}

	public float y_f() {
		return (float) this.y;
	}

	public float z_f() {
		return (float) this.z;
	}

	public Block getBlock(IBlockAccess world) {
		return world.getBlock(this.x_i(), this.y_i(), this.z_i());
	}

	public int getBlockMetadata(IBlockAccess world) {
		return world.getBlockMetadata(this.x_i(), this.y_i(), this.z_i());
	}

	public TileEntity getTileEntity(IBlockAccess world) {
		return world.getTileEntity(this.x_i(), this.y_i(), this.z_i());
	}

	public boolean setBlock(World world, Block block, int meta, int notify) {
		return world.setBlock(this.x_i(), this.y_i(), this.z_i(), block, meta, notify);
	}

	public boolean setBlock(World world, Block block, int meta) {
		return this.setBlock(world, block, meta, 3);
	}

	public boolean setBlock(World world, Block block) {
		return this.setBlock(world, block, 0);
	}

	public boolean setBlockToAir(World world) {
		return this.setBlock(world, Blocks.air);
	}

	/* Math funcs (not override) */

	public Vector3b scale(double x, double y, double z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		return this;
	}

	public Vector3b scale(double amount) {
		return this.scale(amount, amount, amount);
	}

	public Vector3b scale(Vector3 vec) {
		return this.scale(vec.x, vec.y, vec.z);
	}

	public Vector3b scale(IVector3 vec) {
		return this.scale(vec.x(), vec.y(), vec.z());
	}

	public Vector3b scale(Vec3 vec) {
		return this.scale(vec.xCoord, vec.yCoord, vec.zCoord);
	}

	public Vector3b invert() {
		return this.scale(-1);
	}

	public Vector3b translate(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public Vector3b translate(double amount) {
		return this.translate(amount, amount, amount);
	}

	public Vector3b translate(IVector3 vec) {
		return this.translate(vec.x(), vec.y(), vec.z());
	}

	public Vector3b translate(Vector3 vec) {
		return this.translate(vec.x, vec.y, vec.z);
	}

	public Vector3b translate(Vector3b vec) {
		return this.translate(vec.x(), vec.y(), vec.z());
	}

	public Vector3b translate(Vec3 vec) {
		return this.translate(new Vector3b(vec));
	}

	public Vector3b translate(ForgeDirection dir, double scale) {
		return new Vector3b(dir).scale(scale);
	}

	public Vector3b translate(ForgeDirection dir) {
		return this.translate(dir, 1);
	}

	public static Vector3b translate(Vector3b a, Vector3b b) {
		return a.copy().translate(b);
	}

	public static Vector3b translate(Vector3b vec, double amount) {
		return vec.copy().translate(amount);
	}

	public Vector3b subtract(double x, double y, double z) {
		return new Vector3b(this.x() - x, this.y() - y, this.z() - z);
	}

	public Vector3b subtract(double amount) {
		return this.subtract(amount, amount, amount);
	}

	public Vector3b subtract(Vec3 vec) {
		return this.subtract(vec.xCoord, vec.yCoord, vec.zCoord);
	}

	public Vector3b subtract(IVector3 vec) {
		return this.subtract(vec.x(), vec.y(), vec.z());
	}

	public Vector3b subtract(Vector3b vec) {
		return this.subtract(vec.x(), vec.y(), vec.z());
	}

	/* Override funcs */

	@Override
	public Vector3b copy() {
		return new Vector3b(this);
	}

	@Override
	public Vector3b crossProduct(Vector3 vec) {
		double d = y * vec.z - z * vec.y;
		double d1 = z * vec.x - x * vec.z;
		double d2 = x * vec.y - y * vec.x;
		return new Vector3b(d, d1, d2);
	}

	@Override
	public Vector3b add(double d, double d1, double d2) {
		return new Vector3b(this.x + d, this.y + d1, this.z + d2);
	}

	@Override
	public Vector3b add(Vector3 vec) {
		return new Vector3b(this.x + vec.x, this.y + vec.y, this.z + vec.z);
	}

	@Override
	public Vector3b subtract(Vector3 vec) {
		return this.subtract(vec.x, vec.y, vec.z);
	}

	@Override
	@Deprecated
	public Vector3b negate(Vector3 vec) {
		return new Vector3b(super.negate(vec));
	}

	@Override
	public Vector3b negate() {
		return new Vector3b(-this.x, -this.y, -this.z);
	}

	@Override
	public Vector3b multiply(double d) {
		return new Vector3b(this.x * d, this.y * d, this.z * d);
	}

	@Override
	public Vector3b multiply(Vector3 f) {
		return new Vector3b(this.x * f.x, this.y * f.y, this.z * f.z);
	}

	@Override
	public Vector3b multiply(double fx, double fy, double fz) {
		return new Vector3b(this.x * fx, this.y * fy, this.z * fz);
	}

	@Override
	public Vector3b xCrossProduct() {
		return new Vector3b(0, this.z, -this.y);
	}

	@Override
	public Vector3b zCrossProduct() {
		return new Vector3b(this.y, -this.x, 0);
	}

	@Override
	public Vector3b yCrossProduct() {
		return new Vector3b(-this.z, 0, this.x);
	}

	@Override
	public Vector3b rotate(double angle, Vector3 axis) {
		Vector3b v = this.copy();
		Quat.aroundAxis(axis.copy().normalize(), angle).rotate(v);
		return v;
	}

	@Override
	public Vector3b rotate(Quat rotator) {
		Vector3b v = this.copy();
		rotator.rotate(v);
		return v;
	}

	@Override
	public Vector3b YZintercept(Vector3 end, double px) {
		return this.copy().YZintercept(end, px);
	}

	@Override
	public Vector3b XZintercept(Vector3 end, double py) {
		return this.copy().XZintercept(end, py);
	}

	@Override
	public Vector3b XYintercept(Vector3 end, double pz) {
		return this.copy().XYintercept(end, pz);
	}

	/* Conversion Functions  */

	public EulerAngle toAngle() {
		return new EulerAngle(
				Math.toDegrees(Math.atan2(this.x, this.z)),
				Math.toDegrees(-Math.atan2(this.y, Math.hypot(this.z, this.x)))
		);
	}

	public EulerAngle toAngle(IVector3 vec) {
		return this.copy().subtract(vec).toAngle();
	}

	public void toNBT(NBTTagCompound nbt) {
		nbt.setDouble("x", this.x);
		nbt.setDouble("y", this.y);
		nbt.setDouble("z", this.z);
	}

	public ForgeDirection toForgeDirection() {
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
			if (this.x == direction.offsetX && this.y == direction.offsetY
					&& this.z == direction.offsetZ) {
				return direction;
			}
		}
		return ForgeDirection.UNKNOWN;
	}

	public double distance(double x, double y, double z) {
		return this.copy().subtract(x, y, z).mag();
	}

	public double distance(Vector3 vec) {
		return this.copy().distance(vec.x, vec.y, vec.z);
	}

	public double distance(IVector3 vec) {
		return this.copy().distance(vec.x(), vec.y(), vec.z());
	}

	public double distance(Vector3b vec) {
		return this.copy().distance(vec.x(), vec.y(), vec.z());
	}

	public static Vector3b UP() {
		return new Vector3b(ForgeDirection.UP);
	}

	public static Vector3b DOWN() {
		return new Vector3b(ForgeDirection.DOWN);
	}

	public static Vector3b NORTH() {
		return new Vector3b(ForgeDirection.NORTH);
	}

	public static Vector3b SOUTH() {
		return new Vector3b(ForgeDirection.SOUTH);
	}

	public static Vector3b WEST() {
		return new Vector3b(ForgeDirection.WEST);
	}

	public static Vector3b EAST() {
		return new Vector3b(ForgeDirection.EAST);
	}

	public static Vector3b ZERO() {
		return new Vector3b(0, 0, 0);
	}

	public static Vector3b CENTER() {
		return new Vector3b(0.5, 0.5, 0.5);
	}

}
