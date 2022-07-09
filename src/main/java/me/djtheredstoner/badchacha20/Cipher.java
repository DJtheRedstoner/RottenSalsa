package me.djtheredstoner.badchacha20;

public class Cipher {

    public static byte[] encrypt(int[] key, int counter, int[] nonce, byte[] plaintext) {
        byte[] encryptedMessage = new byte[plaintext.length];

        for (int i = 0; i <= plaintext.length / 64 - 1; i++) {
            byte[] keyStream = Block.block(key, counter + i, nonce);
            int k = 0;
            for (int j = i * 64; j < (i + 1) * 64; j++) {
                encryptedMessage[j] = (byte) (plaintext[j] ^ keyStream[k++]);
            }
        }

        if (plaintext.length % 64 != 0) {
            int i = plaintext.length / 64;
            byte[] keyStream = Block.block(key, counter + i, nonce);
            int k = 0;
            for (int j = i * 64; j <= plaintext.length - 1; j++) {
                encryptedMessage[j] = (byte) (plaintext[j] ^ keyStream[k++]);
            }
        }

        return encryptedMessage;
    }

}
