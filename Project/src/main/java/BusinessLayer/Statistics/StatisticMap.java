package BusinessLayer.Statistics;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class StatisticMap<T> {
    Map<LocalDateTime, T> map;
    T lastValue;

    public StatisticMap(T lastValue){
        map =new ConcurrentHashMap<>();
        this.lastValue=lastValue;
    }

    public StatisticMap(Map<LocalDateTime, T> map, T lastValue) {
        this.map = map;
        this.lastValue = lastValue;
    }

    public synchronized void  Add(Function<T,T> fromLastValue){
        lastValue=fromLastValue.apply(lastValue);

    }

    public synchronized void tick() {
        map.put(LocalDateTime.now(),lastValue);
    }

    public synchronized void tick(T newLast) {
        map.put(LocalDateTime.now(),lastValue);
        lastValue = newLast;
    }

    public Map<LocalDateTime, T> getMap() {
        return map;
    }

    public T getLastValue() {
        return lastValue;
    }
}
