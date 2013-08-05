package spike.dropwizard.infinispan;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

public class ServiceConfiguration extends Configuration implements InfinispanServiceConfiguration {

    @JsonProperty
    private InfinispanConfiguration infinispanConfiguration;

    @Override
    public InfinispanConfiguration getInfinispanConfiguration() {
        return infinispanConfiguration;
    }
}
