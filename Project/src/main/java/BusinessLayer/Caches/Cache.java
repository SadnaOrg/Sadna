package BusinessLayer.Caches;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public abstract class Cache<K,T> {
    private ConcurrentLinkedDeque<Cacheable<K,T>> cache;
    private ConcurrentHashMap<K,Cacheable<K,T>> quickLookUp;
    private int maxSize;
    private int size;

    public Cache(int maxSize){
        cache = new ConcurrentLinkedDeque<>();
        quickLookUp = new ConcurrentHashMap<>();
        this.maxSize = maxSize;
        this.size = 0;
    }

    public T findElement(K id){
        Cacheable<K,T> cacheable = quickLookUp.getOrDefault(id, null); // search in cache.
        if(cacheable == null){ // not found in cache, get from DB and put in cache.
            T element = remoteLookUp(id);
            if(element != null){
                insert(id, element, false); // we found something in DB, cache it.
            }
            return element;
        }
        // found in cache, update position in queue.
        reInsert(cacheable);
        return cacheable.getElement();
    }

    public void updateElement(K id, T newElement){
        Cacheable<K,T> cacheable = quickLookUp.getOrDefault(id, null); // search in cache.
        if(cacheable == null){ // the element has to be in the cache in order to update it.
            throw new IllegalArgumentException("can't update something that isn't in the cache!");
        }
        cacheable.setElement(newElement);
        reInsert(cacheable);
        remoteUpdate(newElement);
    }

    public void insert(K id, T element, boolean write){
        if(!(quickLookUp.containsKey(id))){
            Cacheable<K,T> cacheable = new Cacheable<>(id,element);
            Cacheable <K,T> toRemove = null;
            if(size == maxSize){
                // we simply write the head to the DB.
                toRemove = cache.removeFirst();
                quickLookUp.remove(toRemove.getId());
                remoteUpdate(toRemove.getElement()); // write to DB.
                cache.add(cacheable);
                quickLookUp.put(id,cacheable);
            }
            else {
                cache.add(cacheable);
                quickLookUp.put(id, cacheable);
                size++;
            }
            if(write)
                remoteUpdate(element);
        }
    }

    public void reInsert(Cacheable<K,T> cacheable){
        // this moves the element to the end of the queue. The head of the queue is the element to be removed
        // and because we just used this element we probably will use it again.
        cache.remove(cacheable);
        cache.add(cacheable);
    }

    public boolean remove(K id, boolean write){
        Cacheable<K,T> cacheable = quickLookUp.getOrDefault(id,null);
        if(cacheable != null){
            quickLookUp.remove(id);
            cache.remove(cacheable);
            size--;
            if(write)
                remoteUpdate(cacheable.getElement());
            return true;
        }
        return false;
    }

    public abstract Collection<T> findAll();

    public abstract T remoteLookUp(K id);

    public abstract void remoteUpdate(T element);
}
