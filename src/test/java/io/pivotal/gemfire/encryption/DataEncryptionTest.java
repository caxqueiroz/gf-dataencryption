package io.pivotal.gemfire.encryption;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.distributed.ServerLauncher;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by cq on 14/4/16.
 */

public class DataEncryptionTest {


    @Test
    public void testDataWrite(){

        ServerLauncher serverLauncher  = new ServerLauncher.Builder()
                .setMemberName("server1")
                .set("cache-xml-file", "server-cache.xml")
                .set("log-level", "info")
                .build();

        System.out.println("Attempting to start cache server");

        serverLauncher.start();

        System.out.println("Cache server successfully started");

        Cache cache = new CacheFactory().create();


        Region region = cache.getRegion("dummyRegion");

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
}
