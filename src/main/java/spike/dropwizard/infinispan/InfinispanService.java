package spike.dropwizard.infinispan;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.views.ViewBundle;
import spike.dropwizard.infinispan.resources.CacheTestingResource;

public class InfinispanService extends Service<ServiceConfiguration> {

    private final InfinispanBundle bundle = new InfinispanBundle();

    public static void main(String[] args) throws Exception{
        new InfinispanService().run(args);
    }

    @Override
    public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {
        bootstrap.addBundle(bundle);

    }

    @Override
    public void run(ServiceConfiguration configuration, Environment environment) throws Exception {
        environment.addResource(new CacheTestingResource(bundle.getCacheManager().<String,String>getCache()));
    }
}
