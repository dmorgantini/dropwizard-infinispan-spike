package spike.dropwizard.infinispan;

import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class InfinispanBundle implements ConfiguredBundle<InfinispanServiceConfiguration> {

    private EmbeddedCacheManager defaultCacheManager;

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

    @Override
    public void run(InfinispanServiceConfiguration configuration, Environment environment) {
        InfinispanConfiguration infinispanConfiguration = configuration.getInfinispanConfiguration();
        if (infinispanConfiguration.getType() == InfinispanConfiguration.CacheType.clustered) {
            configureClusteredCache(environment, infinispanConfiguration);
        } else {
            configureStandaloneCache();
        }
    }

    private void configureStandaloneCache() {
        defaultCacheManager = new DefaultCacheManager();
    }

    private void configureClusteredCache(Environment environment, InfinispanConfiguration infinispanConfiguration) {
        // check for null first?
        System.setProperty("jgroups.tcp.bind_addr", infinispanConfiguration.getBindAddress());
        System.setProperty("jgroups.tcp.port", String.valueOf(infinispanConfiguration.getPort()));
        System.setProperty("jgroups.tcpping.initial_hosts", infinispanConfiguration.getInitialHosts());

        defaultCacheManager = new DefaultCacheManager(
                GlobalConfigurationBuilder.defaultClusteredBuilder()
                        .transport()
                        .defaultTransport()
                        .clusterName(infinispanConfiguration.getClusterName())
                        .addProperty("configurationFile", "jgroups.xml")
                        .build(),
                new ConfigurationBuilder()
                        .clustering().cacheMode(CacheMode.REPL_SYNC)
                        .build()
        );

        ManagedCacheManager managed = new ManagedCacheManager(defaultCacheManager);
        environment.manage(managed);
    }

    public EmbeddedCacheManager getCacheManager() {
        return defaultCacheManager;
    }
}
