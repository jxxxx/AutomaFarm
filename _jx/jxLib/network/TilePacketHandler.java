
package _jx.jxLib.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class TilePacketHandler implements IPacketHandler
{
	//server - receive packet
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		if(!packet.channel.equals("xTile")) return;

		ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
		int x, y, z;

		try
		{
			x = data.readInt();
			y = data.readInt();
			z = data.readInt();

			World world = FMLClientHandler.instance().getClient().theWorld;
			TileEntity tile = world.getBlockTileEntity(x, y, z);

			if(tile == null || !(tile instanceof IPacketTile)) return;

			IPacketTile user = (IPacketTile)tile;

			user.readPackets(data);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	//client - sent packet
	public static Packet getPackets(TileEntity tile)
	{

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		int x = tile.xCoord;
		int y = tile.yCoord;
		int z = tile.zCoord;

		try
		{
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);

			if(!(tile instanceof IPacketTile)) return null;

			IPacketTile user = (IPacketTile)tile;

			user.writePackets(dos);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "xTile";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();
		packet.isChunkDataPacket = true;

		return packet;
	}

	public static void sentPackets(TileEntity tile)
	{
		PacketDispatcher.sendPacketToServer(TilePacketHandler.getPackets(tile));
	}
}
