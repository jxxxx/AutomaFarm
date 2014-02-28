
package _jx.jxLib.network;

import java.io.DataOutputStream;

import com.google.common.io.ByteArrayDataInput;

public interface IPacketTile
{
    abstract void writePackets(DataOutputStream dos);

    abstract void readPackets(ByteArrayDataInput input);

}
