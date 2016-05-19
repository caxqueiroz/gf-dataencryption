package io.pivotal.gemfire.encryption;

import com.gemstone.gemfire.cache.*;
import com.gemstone.gemfire.distributed.ServerLauncher;
import io.pivotal.gemfire.encryption.shared.CryptoUtils;
import io.pivotal.gemfire.encryption.shared.Dummy;
import io.pivotal.gemfire.encryption.shared.EncryptionC;
import io.pivotal.gemfire.encryption.shared.EncryptionWriter;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Properties;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * Created by cq on 14/4/16.
 */

public class DataEncryptionTest {


    static Cache cache;

    @BeforeClass
    public static void setUp() throws Exception {

        ServerLauncher serverLauncher  = new ServerLauncher.Builder()
                .setMemberName("server1")
                .set("log-level", "info")
                .build();

        System.out.println("Attempting to start cache server");

        serverLauncher.start();

        System.out.println("Cache server successfully started");

        cache = new CacheFactory().create();



    }

    @Test
    public void testDataWrite(){
        String regionName = "dummyRegion01";
        RegionFactory<Long,Dummy> regionFactory = cache.createRegionFactory();
        regionFactory.setDataPolicy(DataPolicy.PARTITION).create(regionName);

        Region region = cache.getRegion(regionName);


        Dummy dummy0 = new Dummy();
        dummy0.setId(1L);
        dummy0.setData1("Hello World!");
        dummy0.setData2("555-555-555-555");

        Dummy dummy1 = new Dummy();
        dummy1.setId(2L);
        dummy1.setData1("My data");
        dummy1.setData2("777-777-777-777");

        Dummy dummy2 = new Dummy();
        dummy2.setId(3L);
        dummy2.setData1("other data");
        dummy2.setData2("888-888-888-888");

        region.put(dummy0.getId(),dummy0);
        region.put(dummy1.getId(),dummy1);
        region.put(dummy2.getId(),dummy2);

        assertThat(region.isEmpty(),equalTo(false));

    }

    @Test
    public void testDataService() throws Exception {

        String regionName = "dummyRegion02";
        RegionFactory<Long,Dummy> regionFactory = cache.createRegionFactory();
        regionFactory.setDataPolicy(DataPolicy.PARTITION).create(regionName);

        Region region = cache.getRegion(regionName);

        Dummy dummy0 = new Dummy();
        dummy0.setId(1L);
        dummy0.setData1("Hello World!");
        dummy0.setData2("555-555-555-555");

        Dummy dummy1 = new Dummy();
        dummy1.setId(2L);
        dummy1.setData1("My data");
        dummy1.setData2("777-777-777-777");

        Dummy dummy2 = new Dummy();
        dummy2.setId(3L);
        dummy2.setData1("other data");
        dummy2.setData2("888-888-888-888");

        DummyRepository repository = new DummyRepository();

        repository.setRegion(region);

        DataEncryptionService service = new DataEncryptionService();
        service.setRepository(repository);


        service.saveData(dummy0);

        assertThat(dummy0.getData2(),equalTo(service.getData(1L).getData2()));



    }

    @Test
    public void testEncryption() throws Exception {

        String regionName = "dummyRegion03";
        RegionFactory<Long,Dummy> regionFactory = cache.createRegionFactory();
        regionFactory.setDataPolicy(DataPolicy.PARTITION).create(regionName);

        Region region = cache.getRegion(regionName);

        Dummy dummy0 = new Dummy();
        dummy0.setId(1L);
        dummy0.setData1("Hello World!");
        dummy0.setData2("555-555-555-555");


        DummyRepository repository = new DummyRepository();

        repository.setRegion(region);

        DataEncryptionService service = new DataEncryptionService();

        CryptoUtils cryptoUtils = new CryptoUtils();
        ReflectionTestUtils.setField(cryptoUtils,"passphrase","password");
        cryptoUtils.init();

        service.setCryptoUtils(cryptoUtils);
        service.setRepository(repository);


        service.saveDataAndEncrypt(dummy0);

        assertThat(service.getData(1L).getData2(),not("555-555-555-555"));

        assertThat(service.getDataAndDecrypt(1L).getData2(),equalTo("555-555-555-555"));

    }

    @Test
    public void testCacheWriter() throws Exception {
        CryptoUtils cryptoUtils = new CryptoUtils();
        ReflectionTestUtils.setField(cryptoUtils,"passphrase","password");
        cryptoUtils.init();

        EncryptionWriter writer = new EncryptionWriter();
        writer.setUtils(cryptoUtils);

        Properties properties = new Properties();
        properties.setProperty("encryptionEnabled","True");
        writer.init(properties);

        String regionName = "dummyRegion00";
        RegionFactory<Long,Dummy> regionFactory = cache.createRegionFactory();
        regionFactory.setCacheWriter(writer).setDataPolicy(DataPolicy.PARTITION).create(regionName);

        Dummy dummy0 = new Dummy();
        dummy0.setId(1L);
        dummy0.setData1("Hello World!");
        dummy0.setData2("555-555-555-555");

        DummyRepository repository = new DummyRepository();

        Region region = cache.getRegion(regionName);

        repository.setRegion(region);

        DataEncryptionService service = new DataEncryptionService();


        service.setCryptoUtils(cryptoUtils);
        service.setRepository(repository);

        service.saveData(dummy0);

        assertThat(service.getData(1L).getData2(),not("555-555-555-555"));

    }

    @Test
    public void testEncryptionC() throws Exception {

        System.setProperty("encryption.passphrase","password");

        EncryptionC encryptionC = new EncryptionC();


        String regionName = "dummyRegion100";
        RegionFactory<Long,Dummy> regionFactory = cache.createRegionFactory();
        regionFactory.setCompressor(encryptionC).setDataPolicy(DataPolicy.PARTITION).create(regionName);


        Dummy dummy0 = new Dummy();
        dummy0.setId(1L);
        dummy0.setData1("Hello World!");
        dummy0.setData2("555-555-555-555");

        DummyRepository repository = new DummyRepository();

        Region region = cache.getRegion(regionName);

        repository.setRegion(region);

        DataEncryptionService service = new DataEncryptionService();

        service.setRepository(repository);

        service.saveData(dummy0);

        assertThat(service.getData(1L).getData2(),equalTo("555-555-555-555"));


    }
}
