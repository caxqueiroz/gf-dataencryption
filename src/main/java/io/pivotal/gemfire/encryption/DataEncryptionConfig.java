package io.pivotal.gemfire.encryption;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import io.pivotal.gemfire.encryption.shared.Dummy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by cq on 19/5/16.
 */
@Configuration
public class DataEncryptionConfig {

    @Value("${gemfire.locator.address}")
    private String locatorAddress;

    @Value("${gemfire.locator.port}")
    private int locatorPort;

    @Value("${gemfire.region.name}")
    private String regionName;

    @Bean
    ClientCache clientCache(){
        ClientCache cache = new ClientCacheFactory()
                .addPoolLocator(locatorAddress, locatorPort)
                .set("log-level","error")
                .create();

        return cache;
    }

    @Bean
    DummyRepository repository(ClientCache cache){
        Region<Long, Dummy> region = cache.<Long, Dummy>createClientRegionFactory(ClientRegionShortcut.PROXY).create(regionName);
        DummyRepository repository = new DummyRepository();
        repository.setRegion(region);
        return repository;
    }
}
