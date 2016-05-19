package io.pivotal.gemfire.encryption;

import com.gemstone.gemfire.cache.Region;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cq on 19/5/16.
 */
@Repository
public class DummyRepository {

    private Region<Long,Dummy> region;

    public void setRegion(Region<Long, Dummy> region) {
        this.region = region;
    }

    public void save(Dummy payload){

        region.put(payload.getId(),payload);

    }

    public void saveAll(List<Dummy> dummyList){

        Map map = dummyList.stream().map(payload -> {
            Map<Long, Dummy> m = new HashMap<>();
            m.put(payload.getId(),payload);
            return m;
        }).reduce((m0, m1) -> {
            m0.putAll(m1);
            return m0;
        }).orElse(null);

        if(map!=null) region.putAll(map);

    }

    public Dummy getDummy(Long id){
        return region.get(id);
    }

    public Region<Long, Dummy> getRegion() {
        return region;
    }
}
