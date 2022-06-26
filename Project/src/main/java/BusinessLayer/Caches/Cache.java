package BusinessLayer.Caches;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public abstract class Cache<K,T> {
    protected final ConcurrentLinkedDeque<Cacheable<K,T>> cache;
    protected final ConcurrentHashMap<K,Cacheable<K,T>> quickLookUp;
    protected final int maxSize;
    protected int size;

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
        cacheable.unMark();
    }

    public void insert(K id, T element, boolean write){
        if(!(quickLookUp.containsKey(id))){
            Cacheable<K,T> cacheable = new Cacheable<>(id,element);
            Cacheable <K,T> toRemove;
            if(size == maxSize){
                // we simply write the head to the DB.
                toRemove = cache.removeFirst();
                quickLookUp.remove(toRemove.getId());
                if(toRemove.isDirty())
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

    public boolean remove(K id){
        Cacheable<K,T> cacheable = quickLookUp.getOrDefault(id,null);
        if(cacheable != null){
            quickLookUp.remove(id);
            cache.remove(cacheable);
            size--;
            if(cacheable.isDirty()){
                remoteUpdate(cacheable.getElement());
                cacheable.unMark(); // if it's cached elsewhere
            }
            return true;
        }
        return false;
    }

    // some functions change the state of cached objects without writing (calling remoteUpdate).
    // For example: removing the admin of the shop is done by calling the function of SubscribedUser.
    // the shop was updated but through the user.
    public void updateByID(K id){
        Cacheable<K,T> cacheable = quickLookUp.getOrDefault(id,null); // look up
        if(cacheable != null){
            reInsert(cacheable);
            remoteUpdate(cacheable.getElement());
        }
    }

    public void mark(K id){
        Cacheable<K,T> element = quickLookUp.get(id);
        element.mark();
    }

    public boolean canPut(int numElements){
        return maxSize - size >= numElements;
    }

    public void insertAll(Collection<Cacheable<K,T>> cacheables){
        for (Cacheable<K,T> cacheable:
             cacheables) {
            insert(cacheable.getId(), cacheable.getElement(), false);
        }
    }

    public Cacheable<K,T> quickSearch(K id){
        return quickLookUp.getOrDefault(id, null);
    }

    public abstract Collection<T> findAll();

    public abstract T remoteLookUp(K id);

    public abstract void remoteUpdate(T element);

    public abstract void remoteRemove(K id);
}
