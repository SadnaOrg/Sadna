package BusinessLayer.Statistics;

import BusinessLayer.Mappers.StatisticMappers.StatisticsMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NotifyStatistic {

    private static class StatisticHolder{
        private static final NotifyStatistic instance= new NotifyStatistic();
    }

    private Map<LocalDate,Statistic> statisticMap;
    private Statistic statistic;

    public NotifyStatistic() {
        this.statistic =new Statistic(LocalDate.now());
        StatisticsMapper.getInstance().save(this.statistic);
        statisticMap=new ConcurrentHashMap<>();
        statisticMap.put(statistic.getDay(),statistic);
        statistic.start(5);
        StatisticsMapper.getInstance().update(this.statistic);

    }


    public static void login(){
        getInstance().statistic.login();
    }

    public static void logout(){
        getInstance().statistic.logout();
    }

    public static void register(){
        getInstance().statistic.register();
    }

    public static void purchase(){
        getInstance().statistic.purchase();
    }

    public static Statistic getStatistic(LocalDate day){
        return getInstance().statisticMap.getOrDefault(day,null);
    }



    private synchronized void  makeNewDay() {
        if (!LocalDate.now().equals(statistic.getDay())) {
            this.statistic = new Statistic(LocalDate.now(), statistic);
            StatisticsMapper.getInstance().save(this.statistic);
            statisticMap.put(statistic.getDay(), statistic);
            statistic.start(5);
            StatisticsMapper.getInstance().update(this.statistic);
        }
    }

    public static NotifyStatistic getInstance(){
        var stat =StatisticHolder.instance;
        if(!LocalDate.now().equals(stat.statistic.getDay()))
            stat.makeNewDay();
        return stat;
    }

}
