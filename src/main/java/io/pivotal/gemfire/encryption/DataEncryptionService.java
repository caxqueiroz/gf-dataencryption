package io.pivotal.gemfire.encryption;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
@Service
public class DataEncryptionService {

    private DummyRepository repository;

    private final SecureRandom random;

    private final int iterations = 65536;

    private String salt = "SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-SP-";

    @Value("${passphrase}")
    private String passphrase;

    private SecretKey key;
    private byte [] ivBytes;


    @Autowired
    public void setRepository(DummyRepository repository) {
        this.repository = repository;
    }

    public DataEncryptionService() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        random = new SecureRandom();
    }

    public DataEncryptionService(String passphrase) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        random = new SecureRandom();
        key = generateKey(passphrase);
        ivBytes = new byte[16];
        random.nextBytes(ivBytes);
    }

    @PostConstruct
    private void init() throws Exception{

        key = generateKey(passphrase);
        ivBytes = new byte[16];
        random.nextBytes(ivBytes);
    }

    public void saveData(Dummy payload){
        repository.save(payload);

    }

    public Dummy getData(Long key){
        return repository.getDummy(key);
    }


    public void saveDataAndEncrypt(Dummy payload) throws Exception {

        byte[] encryptedData = encrypt(payload.getData2());

        String encodedData = new String(Base64.encode(encryptedData), "UTF8");
        payload.setData2(encodedData);

        repository.save(payload);

    }

    public Dummy getDataAndDecrypt(Long key) throws Exception {
        Dummy dummy = repository.getDummy(key);
        String encodedData = dummy.getData2();
        byte[] decodedData = Base64.decode(encodedData);

        String decrytedData = decrypt(decodedData);
        dummy.setData2(decrytedData);

        return dummy;

    }


    private byte [] encrypt(String data) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CTR/NOPADDING");
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec(), random);
        return cipher.doFinal(data.getBytes());
    }

    private String decrypt(byte [] ciphertext) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CTR/NOPADDING");
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec(), random);
        return new String(cipher.doFinal(ciphertext));
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
