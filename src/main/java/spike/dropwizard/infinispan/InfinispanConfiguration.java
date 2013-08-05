package spike.dropwizard.infinispan;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import com.yammer.dropwizard.validation.ValidationMethod;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public class InfinispanConfiguration {

    public enum CacheType {
        standalone,
        clustered
    }

    @JsonProperty
    @Valid
    private Optional<String> bindAddress;

    @JsonProperty
    private int port = 7800;

    @JsonProperty
    @Valid
    private Optional<String> initialHosts;

    @JsonProperty
    @Valid
    private Optional<String> clusterName;

    @JsonProperty
    @NotNull
    private CacheType type;

    public String getBindAddress() {
        return bindAddress.get();
    }

    public int getPort() {
        return port;
    }

    public String getInitialHosts() {
        return initialHosts.get();
    }

    public String getClusterName() {
        return clusterName.get();
    }

    public CacheType getType() {
        return type;
    }

    // TODO: write a test for me :-)
    @ValidationMethod(message = "Infinispan is not valid - check documentation")
    public boolean isValid() { // must start with an 'is' due to a daft JavaBeans convention (http://dropwizard.codahale.com/manual/core/#man-core-representations)
        switch (getType()){
            case clustered:
                return clusterName.isPresent() && bindAddress.isPresent() && initialHosts.isPresent();
            case standalone:
                return true;
        }
        return false; // this shouldn't happen
    }
}
