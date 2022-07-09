package me.djtheredstoner.badchacha20.test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HexFormat;

public class RFCTestBase {

    protected int[] hexToIntArray(String hex) {
        byte[] bytes = HexFormat.ofDelimiter(":").parseHex(hex);
        var buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
        int[] data = new int[buffer.capacity()];
        buffer.get(data);
        return data;
    }

}
