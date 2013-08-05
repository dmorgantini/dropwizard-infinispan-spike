package spike.dropwizard.infinispan.resources;

public class CacheEntry {
    private String key;
    private String value;

    @SuppressWarnings("unused") // needed for JAXB
    CacheEntry() {
    }

    public CacheEntry(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
