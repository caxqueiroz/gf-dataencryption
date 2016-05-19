package io.pivotal.gemfire.encryption.shared;

import com.gemstone.gemfire.cache.*;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created by cq on 19/5/16.
 */
@Component
public class EncryptionWriter implements CacheWriter<Long,Dummy>, Declarable {

    private CryptoUtils utils;

    @Autowired
    public void setUtils(CryptoUtils utils) {
        this.utils = utils;
    }

    private Boolean encryption = true;

    @Override
    public void beforeUpdate(EntryEvent<Long, Dummy> entryEvent) throws CacheWriterException {
        updateData(entryEvent);
    }

    @Override
    public void beforeCreate(EntryEvent<Long, Dummy> entryEvent) throws CacheWriterException {

        updateData(entryEvent);

    }

    @Override
    public void beforeDestroy(EntryEvent<Long, Dummy> entryEvent) throws CacheWriterException {

    }

    @Override
    public void beforeRegionDestroy(RegionEvent<Long, Dummy> regionEvent) throws CacheWriterException {

    }

    @Override
    public void beforeRegionClear(RegionEvent<Long, Dummy> regionEvent) throws CacheWriterException {

    }

    @Override
    public void close() {

    }

    @Override
    public void init(Properties properties) {

        if(properties!=null)
            encryption = Boolean.valueOf(properties.getProperty("encryptionEnabled","True"));

        setUtils();

    }

    private void updateData(EntryEvent<Long, Dummy> entryEvent) {

        if(encryption){

            setUtils();

            try {

                Dummy dummy = entryEvent.getNewValue();
                byte[] encryptedData = utils.encrypt(dummy.getData2());
                String encodedData = new String(Base64.encode(encryptedData), "UTF8");
                dummy.setData2(encodedData);

            } catch (Exception e) {
                throw new CacheWriterException(e);
            }
        }
    }

    private void setUtils() {
        if(utils == null) {
            utils = new CryptoUtils();
            try {
                utils.init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
