package me.djtheredstoner.badchacha20.test;

import me.djtheredstoner.badchacha20.Block;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RFCBlockTests extends RFCTestBase {

    protected final int[] TEST_KEY = hexToIntArray("00:01:02:03:04:05:06:07:08:09:0a:0b:0c:0d:0e:0f:10:11:12:13:14:15:16:17:18:19:1a:1b:1c:1d:1e:1f");
    protected final int[] TEST_NONCE = hexToIntArray("00:00:00:09:00:00:00:4a:00:00:00:00");

    @Test
    public void arithmeticTest() {
        var a = 0x11111111;
        var b = 0x01020304;
        var c = 0x77777777;
        var d = 0x01234567;

        assertEquals(0x789abcde, c = c + d);
        assertEquals(0x7998bfda,  b = b ^ c);
        assertEquals(0xcc5fed3c, b = Integer.rotateLeft(b, 7));
    }

    @Test
    public void quarterRoundTest() {
        var vector = new int[]{0x11111111, 0x01020304, 0x9b8d6f43, 0x01234567};
        Block.quarterRound(vector, 0, 1, 2, 3);
        var expected = new int[]{0xea2a92f4, 0xcb1cf8ce, 0x4581472e, 0x5881c4bb};
        assertArrayEquals(vector, expected);
    }

    @Test
    public void quarterRoundStateTest() {
        var state = new int[]{
            0x879531e0, 0xc5ecf37d, 0x516461b1, 0xc9a62f8a,
            0x44c20ef3, 0x3390af7f, 0xd9fc690b, 0x2a5f714c,
            0x53372767, 0xb00a5631, 0x974c541a, 0x359e9963,
            0x5c971061, 0x3d631689, 0x2098d9d6, 0x91dbd320
        };
        Block.quarterRound(state, 2, 7, 8, 13);

        var expected = new int[]{
            0x879531e0, 0xc5ecf37d, 0xbdb886dc, 0xc9a62f8a,
            0x44c20ef3, 0x3390af7f, 0xd9fc690b, 0xcfacafd2,
            0xe46bea80, 0xb00a5631, 0x974c541a, 0x359e9963,
            0x5c971061, 0xccc07c79, 0x2098d9d6, 0x91dbd320
        };

        assertArrayEquals(expected, state);
    }

    @Test
    public void innerBlockTest() {
        var state = Block.createState(TEST_KEY, TEST_NONCE, 1);

        {
            var expected = new int[]{
                0x61707865, 0x3320646e, 0x79622d32, 0x6b206574,
                0x03020100, 0x07060504, 0x0b0a0908, 0x0f0e0d0c,
                0x13121110, 0x17161514, 0x1b1a1918, 0x1f1e1d1c,
                0x00000001, 0x09000000, 0x4a000000, 0x00000000
            };
            assertArrayEquals(expected, state);
        }

        Block.blockRounds(state);

        {
            var expected = new int[]{
                0x837778ab, 0xe238d763, 0xa67ae21e, 0x5950bb2f,
                0xc4f2d0c7, 0xfc62bb2f, 0x8fa018fc, 0x3f5ec7b7,
                0x335271c2, 0xf29489f3, 0xeabda8fc, 0x82e46ebd,
                0xd19c12b4, 0xb04e16de, 0x9e83d0cb, 0x4e3c50a2
            };
            assertArrayEquals(expected, state);
        }
    }

    @Test
    public void blockTest() {
        var state = Block.blockInner(TEST_KEY, 1, TEST_NONCE);

        var expected = new int[]{
            0xe4e7f110, 0x15593bd1, 0x1fdd0f50, 0xc47120a3,
            0xc7f4d1c7, 0x0368c033, 0x9aaa2204, 0x4e6cd4c3,
            0x466482d2, 0x09aa9f07, 0x05d7c214, 0xa2028bd9,
            0xd19c12b5, 0xb94e16de, 0xe883d0cb, 0x4e3c50a2
        };

        assertArrayEquals(expected, state);
    }

}
