
package _jx.AutomaFarm.client.gui.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import _jx.AutomaFarm.tileentity.TileEntityVector;

public class ContainerVector extends Container
{
    private TileEntityVector tile;

    public ContainerVector(IInventory inv, TileEntityVector tile)
    {
	this.tile = tile;
	int i, j;

	this.addSlotToContainer(new Slot(tile, 0, 25, 17));

	//3*3 tile inventory
	for (i = 0; i < 3; ++i)
	{
	    for (j = 0; j < 3; ++j)
	    {
		this.addSlotToContainer(new Slot(tile, j + i * 3 + 1, 62 + j * 18 + 34, 17 + i * 18));
	    }
	}

	//3*9 player inventory
	for (i = 0; i < 3; ++i)
	{
	    for (j = 0; j < 9; ++j)
	    {
		this.addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
	    }
	}

	//1*9 player quick inventory
	for (i = 0; i < 9; ++i)
	{
	    this.addSlotToContainer(new Slot(inv, i, 8 + i * 18, 142));
	}
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
	return this.tile.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int par2)
    {
	ItemStack itemstack = null;
	Slot slot = (Slot)this.inventorySlots.get(par2);

	if(slot == null || !slot.getHasStack()) return itemstack;

	ItemStack slotItem = slot.getStack();
	itemstack = slotItem.copy();

	if(par2 < 9 && !this.mergeItemStack(slotItem, 9, 45, true)) return null;

	if (par2 >= 9 && !this.mergeItemStack(slotItem, 0, 9, false)) return null;

	if (slotItem.stackSize == 0) slot.putStack((ItemStack)null);

	if (slotItem.stackSize != 0) slot.onSlotChanged();

	if (slotItem.stackSize == itemstack.stackSize) return null;

	slot.onPickupFromSlot(player, slotItem);

	return itemstack;
    }
}
