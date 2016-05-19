package io.pivotal.gemfire.encryption;

import io.pivotal.gemfire.encryption.shared.CryptoUtils;
import io.pivotal.gemfire.encryption.shared.Dummy;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by cq on 19/5/16.
 */
@Service
public class DataEncryptionService {

    private DummyRepository repository;

    private CryptoUtils cryptoUtils;

    @Autowired
    public void setRepository(DummyRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setCryptoUtils(CryptoUtils cryptoUtils) {
        this.cryptoUtils = cryptoUtils;
    }

    public void saveData(Dummy payload){
        repository.save(payload);

    }

    public Dummy getData(Long key){
        return repository.getDummy(key);
    }


    public void saveDataAndEncrypt(Dummy payload) throws Exception {

        byte[] encryptedData = cryptoUtils.encrypt(payload.getData2());

        String encodedData = new String(Base64.encode(encryptedData), "UTF8");
        payload.setData2(encodedData);

        repository.save(payload);

    }

    public Dummy getDataAndDecrypt(Long key) throws Exception {
        Dummy dummy = repository.getDummy(key);
        String encodedData = dummy.getData2();
        byte[] decodedData = Base64.decode(encodedData);

        String decrytedData = cryptoUtils.decrypt(decodedData);
        dummy.setData2(decrytedData);

        return dummy;

    }




}
