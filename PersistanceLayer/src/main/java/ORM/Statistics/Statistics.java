package ORM.Statistics;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
@Entity
@Table(name = "Statistics")
public class Statistics {

    @Id
    private LocalDate day;
    @OneToOne
    private StatisticMap registeredUser;
    @OneToOne
    private StatisticMap loginUser;

    @OneToOne
    private StatisticMap purchase;
    private LocalTime lastTick;


    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public StatisticMap getRegisteredUser() {
        return registeredUser;
    }

    public void setRegisteredUser(StatisticMap registeredUser) {
        this.registeredUser = registeredUser;
    }

    public StatisticMap getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(StatisticMap loginUser) {
        this.loginUser = loginUser;
    }

    public StatisticMap getPurchase() {
        return purchase;
    }

    public void setPurchase(StatisticMap purchase) {
        this.purchase = purchase;
    }

    public LocalTime getLastTick() {
        return lastTick;
    }

    public void setLastTick(LocalTime lastTick) {
        this.lastTick = lastTick;
    }

    public Statistics(LocalDate day, StatisticMap registeredUser, StatisticMap loginUser, StatisticMap purchase, LocalTime lastTick) {
        this.day = day;
        this.registeredUser = registeredUser;
        this.loginUser = loginUser;
        this.purchase = purchase;
        this.lastTick = lastTick;
    }

}
