package com.SadnaORM.Statistics;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@Entity
public class StatisticMap {
    public Map<LocalDateTime, Integer> getMap() {
        return map;
    }

    public void setMap(Map<LocalDateTime, Integer> map) {
        this.map = map;
    }

    public int getLastValue() {
        return lastValue;
    }

    public void setLastValue(int lastValue) {
        this.lastValue = lastValue;
    }

    public Statistics getParent() {
        return parent;
    }

    public void setParent(Statistics parent) {
        this.parent = parent;
    }

    public StatisticMap(Map<LocalDateTime, Integer> map, int lastValue, Statistics parent) {
        this.map = map;
        this.lastValue = lastValue;
        this.parent = parent;
    }

    @ElementCollection
            @CollectionTable(name = "dayMap")
            @MapKeyColumn(name = "date")
            @Column(name = "value")
    Map<LocalDateTime,Integer> map;
    int lastValue;
    @Id
    @OneToOne
    Statistics parent;
}
