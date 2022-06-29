package ServiceLayer.Objects;

import BusinessLayer.Statistics.StatisticMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public record Statistic(LocalDate day, Map<LocalTime,Integer> register, Map<LocalTime,Integer> numberOfLogin, Map<LocalTime,Integer> numberOfPurchase) {

    public Statistic(BusinessLayer.Statistics.Statistic s){
        this(s.getDay(),convertMap(s.getRegisteredUser()),convertMap(s.getLoginUser()),convertMap(s.getPurchase()));
    }

    private static Map<LocalTime, Integer> convertMap(StatisticMap<Integer> s) {
        var map = new ConcurrentHashMap<LocalTime,Integer>();
         s.getMap().forEach((key, value) -> map.put(LocalTime.from(key), value));
         return map;
    }



}
