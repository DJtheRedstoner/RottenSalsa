package me.djtheredstoner.badchacha20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Block {

    private static final int CHACHA_CONSTANT_0 = 0x61707865;
    private static final int CHACHA_CONSTANT_1 = 0x3320646e;
    private static final int CHACHA_CONSTANT_2 = 0x79622d32;
    private static final int CHACHA_CONSTANT_3 = 0x6b206574;

    public static void main(String[] args) {
        int[] state = new int[]{0x11111111, 0x01020304, 0x9b8d6f43, 0x01234567};
        quarterRound(state, 0, 1, 2, 3);
        System.out.println(Arrays.stream(state).mapToObj(Integer::toHexString).collect(Collectors.joining(" ")));
    }

    public static void rot(int[] v, int i, int d) {
        v[i] = Integer.rotateLeft(v[i], d);
    }

    public static void quarterRound(int[] v, int w, int x, int y, int z) {
        //   1.  a += b; d ^= a; d <<<= 16;
        //   2.  c += d; b ^= c; b <<<= 12;
        //   3.  a += b; d ^= a; d <<<= 8;
        //   4.  c += d; b ^= c; b <<<= 7;

        v[w] += v[x]; v[z] ^= v[w]; rot(v, z, 16);
        v[y] += v[z]; v[x] ^= v[y]; rot(v, x, 12);
        v[w] += v[x]; v[z] ^= v[w]; rot(v, z, 8);
        v[y] += v[z]; v[x] ^= v[y]; rot(v, x, 7);
    }

    public static int[] createState(int[] key, int[] nonce, int blockCount) {
        int[] state = new int[16];
        state[0] = CHACHA_CONSTANT_0; state[1] = CHACHA_CONSTANT_1;
        state[2] = CHACHA_CONSTANT_2; state[3] = CHACHA_CONSTANT_3;
        System.arraycopy(key, 0, state, 4, 8);
        state[12] = blockCount;
        System.arraycopy(nonce, 0, state, 13, 3);

        return state;
    }

    public static void blockRounds(int[] state) {
        for (int i = 0; i < 10; i++) {
            quarterRound(state, 0, 4, 8, 12);
            quarterRound(state, 1, 5, 9, 13);
            quarterRound(state, 2, 6, 10, 14);
            quarterRound(state, 3, 7, 11, 15);
            quarterRound(state, 0, 5, 10, 15);
            quarterRound(state, 1, 6, 11, 12);
            quarterRound(state, 2, 7, 8, 13);
            quarterRound(state, 3, 4, 9, 14);
        }
    }

    public static int[] blockInner(int[] key, int blockCount, int[] nonce) {
        int[] state = createState(key, nonce, blockCount);
        int[] working_state = new int[16];
        System.arraycopy(state, 0, working_state, 0, 16);
        blockRounds(working_state);

        for (int i = 0; i < 16; i++) {
            state[i] += working_state[i];
        }

        return state;
    }

    public static byte[] block(int[] key, int blockCount, int[] nonce) {
        return serialize(blockInner(key, blockCount, nonce));
    }

    public static byte[] serialize(int[] data) {
        var buffer = ByteBuffer.allocate(data.length * 4).order(ByteOrder.LITTLE_ENDIAN);
        buffer.asIntBuffer().put(data);
        byte[] bytes = new byte[data.length * 4];
        buffer.get(bytes);
        return bytes;
    }

}
