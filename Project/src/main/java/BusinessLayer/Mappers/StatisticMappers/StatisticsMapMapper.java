package BusinessLayer.Mappers.StatisticMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.Func;
import BusinessLayer.Mappers.ShopMappers.PurchaseHistoryMapper;
import BusinessLayer.Mappers.ShopMappers.PurchaseMapper;
import BusinessLayer.Shops.Purchase;
import BusinessLayer.Statistics.Statistic;
import BusinessLayer.Statistics.StatisticMap;
import ORM.DAOs.DBImpl;
import ORM.DAOs.Shops.PurchaseDAO;
import ORM.DAOs.Statistics.StatisticMapDAO;
import ORM.DAOs.Statistics.StatisticsDAO;
import ORM.Statistics.Statistics;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsMapMapper {
    private final StatisticMapDAO dao = new StatisticMapDAO();



    static private class StatisticsMapMapperHolder {
        static final StatisticsMapMapper mapper = new StatisticsMapMapper();
    }

    public static StatisticsMapMapper getInstance(){
        return StatisticsMapMapper.StatisticsMapMapperHolder.mapper;
    }

    private StatisticsMapMapper() {

    }
    private Func<StatisticsMapper> statisticsMapper = StatisticsMapper::getInstance;
//    @Override
    public ORM.Statistics.StatisticMap toEntity(StatisticMap<Integer> entity,int indexOfMapper) {
        Map<String,Integer> m = entity.getMap().entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue, (a, b) -> b));
        return new ORM.Statistics.StatisticMap(m,indexOfMapper,entity.getLastValue());
    }

//    @Override
    public StatisticMap<Integer> fromEntity(ORM.Statistics.StatisticMap entity) {
        Map<LocalDateTime,Integer> m = entity.getMap().entrySet().stream().collect(Collectors.toMap
                (e -> LocalDateTime.parse(e.getKey()), Map.Entry::getValue, (a, b) -> b));
        return new StatisticMap<Integer>(m, entity.getLastValue());
    }
//
//    public ORM.Statistics.StatisticMap toEntity(StatisticMap<Integer> entity, ORM.Statistics.Statistics statistics, int index) {
//        ORM.Statistics.StatisticMap statisticMap = dao.findById(new ORM.Statistics.StatisticMap.StatisticMapPKID(statistics,index));
//        if(statisticMap == null)
//        {
//            Map<String,Integer> m = entity.getMap().entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue, (a, b) -> b));
//            statisticMap = new ORM.Statistics.StatisticMap(m,index,
//                    entity.getLastValue(),statistics);
//        }
//        else
//        {
//            Map<String,Integer> m = entity.getMap().entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue, (a, b) -> b));
//
//            statisticMap.getMap().clear();
//            statisticMap.getMap().putAll(m);
//            statisticMap.setLastValue(entity.getLastValue());
//        }
//        return statisticMap;
//    }
//
//    public StatisticMap fromEntity(ORM.Statistics.StatisticMap entity) {
//        Map<LocalDateTime,Integer> m = entity.getMap().entrySet().stream().collect(Collectors.toMap
//                (e -> LocalDateTime.parse(e.getKey()), Map.Entry::getValue, (a, b) -> b));
//        return new StatisticMap<Integer>(m, entity.getLastValue());
//    }
//
//    public int save(StatisticMap statisticMap,ORM.Statistics.Statistics statistics,int index) {
//        return dao.save(toEntity(statisticMap,statistics,index));
//    }
//
//    public void update(StatisticMap statisticMap, ORM.Statistics.Statistics statistics,int index) {
//        dao.update(toEntity(statisticMap,statistics,index));
//
//    }
//
//
//    @Override
//    public int save(StatisticMap statisticMap) {
//        return 0;
//    }
//
//    @Override
//    public void update(StatisticMap statisticMap) {
//
//    }
//
//    @Override
//    public void delete(ORM.Statistics.StatisticMap.StatisticMapPKID statisticMapPKID) {
//        dao.delete(statisticMapPKID);
//    }
//
//    @Override
//    public StatisticMap findById(ORM.Statistics.StatisticMap.StatisticMapPKID statisticMapPKID) {
//        return fromEntity(dao.findById(statisticMapPKID));
//    }
//
//    @Override
//    public Collection<StatisticMap> findAll() {
//        return dao.findAll().stream().map(this::fromEntity).toList();
//    }
}
