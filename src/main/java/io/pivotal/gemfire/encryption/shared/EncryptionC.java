package io.pivotal.gemfire.encryption.shared;

import com.gemstone.gemfire.compression.Compressor;

/**
 * Created by cq on 19/5/16.
 */
public class EncryptionC implements Compressor {

    private final CryptoUtils utils;

    public EncryptionC() throws Exception {
        this.utils = new CryptoUtils();
        utils.init();
    }

    @Override
    public byte[] compress(byte[] bytes) {

        try {
            return utils.encrypt(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return bytes;
        }


    }

    @Override
    public byte[] decompress(byte[] bytes) {
        try {
            return utils.decrypt2(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return bytes;
        }
    }
}
