package io.pivotal.gemfire.encryption.shared;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.Security;

/**
 * Created by cq on 19/5/16.
 */
@Component
public class CryptoUtils {

    private final SecureRandom random;

    private final int iterations = 65536;

    private String salt = "SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-";

    private SecretKey key;
    private byte [] ivBytes;

    @Value("${encryption.passphrase}")
    private String passphrase;

    public CryptoUtils() {
        Security.addProvider(new BouncyCastleProvider());
        this.random = new SecureRandom();

    }

    @PostConstruct
    public void init() throws Exception{
        if(passphrase == null)
            passphrase = System.getProperty("encryption.passphrase");
        if(key == null){
            key = generateKey(passphrase);
            ivBytes = new byte[16];
            random.nextBytes(ivBytes);
        }

    }

    public byte [] encrypt(String data) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CTR/NOPADDING");
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec(), random);
        return cipher.doFinal(data.getBytes());
    }

    public byte [] encrypt(byte[] data) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CTR/NOPADDING");
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec(), random);
        return cipher.doFinal(data);
    }

    public String decrypt(byte [] ciphertext) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CTR/NOPADDING");
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec(), random);
        return new String(cipher.doFinal(ciphertext));
    }

    public byte[] decrypt2(byte [] ciphertext) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CTR/NOPADDING");
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec(), random);
        return cipher.doFinal(ciphertext);
    }


    private IvParameterSpec ivParameterSpec() throws Exception {

        return new IvParameterSpec(ivBytes);
    }

    private SecretKey generateKey(String passphrase) throws Exception {
        PBEKeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), salt.getBytes(), iterations, 256);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return new SecretKeySpec(keyFactory.generateSecret(keySpec).getEncoded(), "AES");

    }


}
