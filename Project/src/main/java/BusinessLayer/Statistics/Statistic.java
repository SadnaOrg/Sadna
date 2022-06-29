package BusinessLayer.Statistics;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class Statistic{
    private LocalDate day;
    private StatisticMap<Integer> registeredUser;
    private StatisticMap<Integer> loginUser;
    private StatisticMap<Integer> purchase;
    private LocalTime lastTick;
    private long tick;

    public Statistic(LocalDate day) {
        this.day=day;
        registeredUser= new StatisticMap<>(0);
        loginUser= new StatisticMap<>(0);
        purchase=new StatisticMap<>(0);
    }
    public Statistic(LocalDate day,Statistic yesterday){
        registeredUser=new StatisticMap<>(yesterday.registeredUser.lastValue);
        loginUser=new StatisticMap<>(yesterday.loginUser.lastValue);
        purchase=new StatisticMap<>(yesterday.purchase.lastValue);
    }



    public void start(long tickValueInMin){
       tick =tickValueInMin;
       if(lastTick == null) {
           lastTick = LocalTime.now();
           registeredUser.tick(0);
           loginUser.tick(0);
           purchase.tick(0);
       }
       else makeTick();

    }

    private void makeTick() {

        if (lastTick!=null &&LocalTime.now().isAfter(lastTick.plusMinutes(tick))) {
            synchronized (this) {
                if (lastTick!=null && LocalTime.now().isAfter(lastTick.plusMinutes(tick))) {
                    lastTick = LocalTime.now();
                    registeredUser.tick(0);
                    loginUser.tick();//for saving the last value the same
                    purchase.tick(0);
                }
            }
        }
    }

    public void register(){
        registeredUser.Add(i-> i+1);
        makeTick();
    }

    public void login(){
        loginUser.Add(i->i+1);
        makeTick();

    }
    public void logout(){
        loginUser.Add(i->i-1);
        makeTick();

    }

    public void purchase(){
        purchase.Add(i->i+1);
        makeTick();

    }

    public LocalDate getDay() {
        return day;
    }

    public StatisticMap<Integer> getRegisteredUser() {
        return registeredUser;
    }

    public StatisticMap<Integer> getLoginUser() {
        return loginUser;
    }

    public StatisticMap<Integer> getPurchase() {
        return purchase;
    }

    public long getTick() {
        return tick;
    }

    public LocalTime getLastTick() {
        return lastTick;
    }
}
