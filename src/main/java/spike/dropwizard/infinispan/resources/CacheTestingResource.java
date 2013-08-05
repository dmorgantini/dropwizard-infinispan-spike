package spike.dropwizard.infinispan.resources;


import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.sun.jersey.api.NotFoundException;
import org.infinispan.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Path("/cache")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CacheTestingResource {

    private static final Logger LOG = LoggerFactory.getLogger(CacheTestingResource.class);
    private Cache<String, String> cache;

    public CacheTestingResource(Cache<String, String> cache) {
        this.cache = cache;
    }

    @POST
    public Response setCache(CacheEntry cacheEntry) throws NoSuchMethodException {
        cache.put(cacheEntry.getKey(), cacheEntry.getValue());
        return Response.created(UriBuilder.fromPath("/{key}").build(cacheEntry.getKey())).build();
    }

    @GET
    @Path("/{key}")
    public CacheEntry getCacheValue(@PathParam("key") String key){
        if (!cache.containsKey(key)){
            String errorMessage = "Key " + key + " not found in the cache!";
            LOG.error(errorMessage);
            LOG.debug(getCacheValues());
            throw new NotFoundException(errorMessage);
        }
        String value = cache.get(key);
        return new CacheEntry(key, value);
    }

    @GET
    public Collection<CacheEntry> getCache(){
        return Collections2.transform(cache.entrySet(), new Function<Map.Entry<String, String>, CacheEntry>() {
            @Override
            public CacheEntry apply(Map.Entry<String, String> input) {
                return new CacheEntry(input.getKey(), input.getValue());
            }
        });
    }

    private String getCacheValues() {
        Set<Map.Entry<String,String>> entries = cache.entrySet();

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : entries) {
            sb.append(MessageFormat.format("{0}:{1}\n", entry.getKey(), entry.getValue()));
        }
        return sb.toString();
    }

}
