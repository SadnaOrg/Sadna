package ORM.Statistics;
import com.sun.istack.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

@Entity
@Table(name = "StatisticMap")
@IdClass(StatisticMap.StatisticMapPKID.class)
public class StatisticMap{

    public StatisticMap(Statistics parent) {
        this.statistics = parent;
    }

    @ElementCollection
    @CollectionTable(name = "dayMap")
    @MapKeyColumn(name = "date")
    @Column(name = "value")
    Map<String,Integer> map;
    @Id
    int indexOfMapper;
    @Nullable
    Integer lastValue;
    @Id
    @JoinColumn(name = "Statistics")
    @ManyToOne(cascade = CascadeType.ALL)
    Statistics statistics;
    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }

    public int getLastValue() {
        return lastValue;
    }

    public void setLastValue(int lastValue) {
        this.lastValue = lastValue;
    }

    public Statistics getParent() {
        return statistics;
    }

    public void setParent(Statistics parent) {
        this.statistics = parent;
    }

    public int getIndexOfMapper() {
        return indexOfMapper;
    }

    public void setIndexOfMapper(int indexOfMapper) {
        this.indexOfMapper = indexOfMapper;
    }

    public StatisticMap(Map<String, Integer> map, int index, Integer lastValue, Statistics parent) {
        this.map = map;
        this.indexOfMapper = index;
        this.lastValue = lastValue;
        this.statistics = parent;
    }

    public StatisticMap() {
    }

    public static class StatisticMapPKID implements Serializable{
        private Statistics statistics;
        private int indexOfMapper;

        public StatisticMapPKID(Statistics statistics, int indexOfMapper) {
            this.statistics = statistics;
            this.indexOfMapper = indexOfMapper;
        }

        public StatisticMapPKID() {
        }
    }
}
