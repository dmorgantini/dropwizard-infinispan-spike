package spike.dropwizard.infinispan;

import com.yammer.dropwizard.lifecycle.Managed;
import org.infinispan.manager.EmbeddedCacheManager;

public class ManagedCacheManager implements Managed {

    private EmbeddedCacheManager defaultCacheManager;

    public ManagedCacheManager(EmbeddedCacheManager defaultCacheManager) {
        this.defaultCacheManager = defaultCacheManager;
    }

    @Override
    public void start() throws Exception {
        defaultCacheManager.start();
    }

    @Override
    public void stop() throws Exception {
        defaultCacheManager.stop();
    }
}
