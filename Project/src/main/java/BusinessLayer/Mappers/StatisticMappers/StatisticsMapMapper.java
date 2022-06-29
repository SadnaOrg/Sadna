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

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

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


    public ORM.Statistics.StatisticMap toEntity(StatisticMap entity, ORM.Statistics.Statistics statistics) {
        ORM.Statistics.StatisticMap statisticMap = dao.findById(statistics);
        if(statisticMap == null)
        {
            statisticMap = new ORM.Statistics.StatisticMap((Map<LocalDateTime, Integer>) entity.getMap(),(int)entity.getLastValue(),statistics);
        }
        else
        {
            statisticMap.getMap().clear();
            statisticMap.getMap().putAll(entity.getMap());
            statisticMap.setLastValue((int)entity.getLastValue());
        }
        return statisticMap;
    }

    public StatisticMap fromEntity(ORM.Statistics.StatisticMap entity) {
        return new StatisticMap<Integer>(entity.getMap(),entity.getLastValue());
    }

    public int save(StatisticMap statisticMap, Statistic statistic) {
        return dao.save(toEntity(statisticMap,statisticsMapper.run().toEntity(statistic)));
    }

    public void update(StatisticMap statisticMap, Statistic statistic) {
        dao.update(toEntity(statisticMap,statisticsMapper.run().toEntity(statistic)));

    }

    public void delete(Statistic statistic) {
        dao.delete(statisticsMapper.run().toEntity(statistic));
    }

    public StatisticMap findById(Statistic statistic) {
        return fromEntity(dao.findById(statisticsMapper.run().toEntity(statistic)));
    }

    public Collection<StatisticMap> findAll() {
        return dao.findAll().stream().map(this::fromEntity).toList();
    }
}
