package ORM.Statistics;

import com.sun.istack.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "Statistics")
public class Statistics {

    @Id
    private String day;
    @OneToMany
    @JoinTable(name = "statsmap",
            joinColumns = @JoinColumn(name = "day"),
            inverseJoinColumns = {
                    @JoinColumn(name = "indexOfMapper", referencedColumnName = "indexOfMapper"),
                    @JoinColumn(name = "Statistics", referencedColumnName = "Statistics")
            })
    @MapKey(name = "indexOfMapper")
    private Map<Integer,StatisticMap> maps;
    @Nullable
    private String lastTick;


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public StatisticMap getRegisteredUser() {
        return maps.get(0);
    }

    public void setRegisteredUser(StatisticMap registeredUser) {
        maps.put(0,registeredUser);
    }

    public StatisticMap getLoginUser() {
        return maps.get(1);
    }

    public void setLoginUser(StatisticMap loginUser) {
        maps.put(1,loginUser);
    }

    public StatisticMap getPurchase() {
        return maps.get(2);
    }

    public void setPurchase(StatisticMap purchase) {
        maps.put(2,purchase);
    }

    public String getLastTick() {
        return lastTick;
    }

    public void setLastTick(String lastTick) {
        this.lastTick = lastTick;
    }

    public Statistics(String day, Map<Integer, StatisticMap> maps, String lastTick) {
        this.day = day;
        this.maps = maps;
        this.lastTick = lastTick;
    }

    public Statistics() {
    }
}
