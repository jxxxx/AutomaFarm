
package _jx.AutomaFarm.tileentity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import _jx.jxLib.network.IPacketTile;
import _jx.jxLib.network.TilePacketHandler;

import com.google.common.io.ByteArrayDataInput;

public class TileEntityVector extends TileEntity implements IInventory, IPacketTile
{
    private int targetmeta;

    private ItemStack[] stack = new ItemStack[10];

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
	super.readFromNBT(nbt);

	NBTTagList nbtlist = nbt.getTagList("Items");

	this.stack = new ItemStack[this.getSizeInventory()];

	for(int i = 0; i < nbtlist.tagCount(); ++i)
	{
	    NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbtlist.tagAt(i);
	    int j = nbttagcompound1.getByte("Slot") & 255;

	    if(j >= 0 && j < this.stack.length) this.stack[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
	}

	this.setTargetmeta(nbt.getInteger("Meta"));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
	super.writeToNBT(nbt);

	NBTTagList nbtlist = new NBTTagList();

	for(int i = 0; i < this.stack.length; ++i)
	{
	    if(this.stack[i] == null) continue;

	    NBTTagCompound nbttagcompound1 = new NBTTagCompound();
	    nbttagcompound1.setByte("Slot", (byte)i);

	    this.stack[i].writeToNBT(nbttagcompound1);
	    nbtlist.appendTag(nbttagcompound1);
	}

	nbt.setInteger("Meta", this.getTargetmeta());
    }

    public int getTargetmeta()
    {
	return targetmeta;
    }

    public void setTargetmeta(int targetmeta)
    {
	if(targetmeta < 0) return;

	this.targetmeta = targetmeta;
    }

    public int getSeedID()
    {
	ItemStack seed = this.stack[0];

	if(seed == null) return 0;

	return seed.itemID;
    }

    @Override
    public void updateEntity()
    {
	TileEntityVector tile = (TileEntityVector) this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord);

	int x,y,z;

	for(int l = 0; l <= 9; l++)
	{
	    for(int m = 0; m <= 9; m++)
	    {
		x = this.xCoord - 4 + l;
		y = this.yCoord;
		z = this.zCoord - 4 + m;

		Block b = Block.blocksList[this.worldObj.getBlockId(x, y, z)];

		if(!this.isTargetPlant(b, tile.getSeedID(), this.worldObj.getBlockMetadata(x, y, z))) continue;

		//if(this.worldObj.isRemote) return;

		//this.addItemStack(new ItemStack(tile.getSeedID(), 1, 0));

		this.worldObj.setBlock(x, y, z, this.worldObj.getBlockId(x, y, z));
	    }
	}
    }

    private boolean isTargetPlant(Block block, int seedID, int meta)
    {
	if(!(block instanceof BlockCrops)) return false;

	BlockCrops crop = (BlockCrops)block;

	int id = this.getSeedItemID(crop);

	if(id == 0 || seedID == 0 || id != seedID || meta != this.getTargetmeta()) return false;

	return true;
    }

    private int getSeedItemID(BlockCrops c)
    {
	int id = 0;

	try
	{
	    Method m = c.getClass().getDeclaredMethod("getSeedItem");

	    m.setAccessible(true);

	    id = (Integer)m.invoke(c);
	}

	catch (Exception ex){}

	return id;
    }

    public int addItemStack(ItemStack itemstack)
    {
	for (int i = 1; i < this.stack.length; ++i)
	{
	    if (this.stack[i] != null && this.stack[i].itemID != 0) continue;

	    this.setInventorySlotContents(i, itemstack);
	    return i;
	}

	return -1;
    }
    //

    @Override
    public int getSizeInventory()
    {
	return 10;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
	return this.stack[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
	if(this.stack[i] == null) return null;

	ItemStack itemstack;

	if(this.stack[i].stackSize <= j)
	{
	    itemstack = this.stack[i];
	    this.stack[i] = null;
	    this.onInventoryChanged();
	    return itemstack;
	}

	itemstack = this.stack[i].splitStack(j);

	if(this.stack[i].stackSize == 0) this.stack[i] = null;

	this.onInventoryChanged();

	return itemstack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
	if(this.stack[i] == null) return null;

	ItemStack itemstack = this.stack[i];
	this.stack[i] = null;
	return itemstack;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack item)
    {
	this.stack[i] = item;

	if (item != null && item.stackSize > this.getInventoryStackLimit()) item.stackSize = this.getInventoryStackLimit();

	this.onInventoryChanged();
    }

    @Override
    public String getInvName()
    {
	return "Vector";
    }

    @Override
    public boolean isInvNameLocalized()
    {
	return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
	return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
	return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : entityplayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openChest(){}

    @Override
    public void closeChest(){}

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
	return true;
    }

    @Override
    public void writePackets(DataOutputStream dos)
    {
	try
	{
	    dos.writeInt(this.getTargetmeta());

	    for(int i = 0; i < this.stack.length; ++i)
	    {
		ItemStack item = this.getStackInSlot(i);

		Packet.writeItemStack(item, dos);
	    }
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }

    @Override
    public void readPackets(ByteArrayDataInput input)
    {
	int x = input.readInt();

	this.setTargetmeta(x);

	try
	{
	    for (int i = 0; i < this.stack.length; ++i) this.setInventorySlotContents(i, Packet.readItemStack(input));
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }

    @Override
    public Packet getDescriptionPacket()
    {
	return TilePacketHandler.getPackets(this);
    }
}
