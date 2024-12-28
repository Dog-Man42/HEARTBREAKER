package Heartbreaker.engine.resources;

/**
 * A Wrapper class to be used to store data in one place that is used by multiple objects/classes
 * @param <T>
 */
public class LoadableResource <T> {

    private final T resource;
    private final String resourceName;


    public LoadableResource(String resourceName, T object){
        this.resourceName = resourceName;
        this.resource = object;
    }


    public String getResourceName(){
        return resourceName;
    }

    /**
     * Returns the stored resource.
     * @return
     */
    public T getSharedResource() {
        return resource;
    }

    /**
     * Returns a new LoadableResource with the same data. To be used to avoid mutation of original data.
     * @return
     */
    public LoadableResource<T> getNewResource(){
        return new LoadableResource<>(resourceName, resource);
    }


}
