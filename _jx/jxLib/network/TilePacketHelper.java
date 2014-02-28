
package _jx.jxLib.network;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "TilePacketHelper", name = "TilePacketHelper", version = "1.6.2")

@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = "xTile", packetHandler = TilePacketHandler.class)
public class TilePacketHelper
{
    @Instance("TilePacketHelper")
    public static TilePacketHelper instance;
}
