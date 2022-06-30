package BusinessLayer.Mappers.StatisticMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.Func;
import BusinessLayer.Statistics.Statistic;
import BusinessLayer.Statistics.StatisticMap;
import ORM.DAOs.DBImpl;
import ORM.DAOs.Statistics.StatisticsDAO;
import ORM.Statistics.Statistics;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsMapper implements DBImpl<Statistic, String> , CastEntity<ORM.Statistics.Statistics, Statistic> {

    StatisticsDAO dao = new StatisticsDAO();
    private Func<StatisticsMapMapper> statisticsMapMapper = StatisticsMapMapper::getInstance;



    static private class StatisticsMapperHolder {
        static final StatisticsMapper mapper = new StatisticsMapper();
    }

    public static StatisticsMapper getInstance(){
        return StatisticsMapper.StatisticsMapperHolder.mapper;
    }

    private StatisticsMapper() {

    }


    @Override
    public ORM.Statistics.Statistics toEntity(Statistic entity) {
        ORM.Statistics.Statistics statistics = dao.findById(entity.getDay().toString());
        Collection<ORM.Statistics.StatisticMap> collection = new ArrayList<>();
        collection.add(statisticsMapMapper.run().toEntity(entity.getRegisteredUser(),0));
        collection.add(statisticsMapMapper.run().toEntity(entity.getLoginUser(),1));
        collection.add(statisticsMapMapper.run().toEntity(entity.getRegisteredUser(),2));
        if(statistics==null)
        {
            if(entity.getLastTick()!= null) {
                statistics = new Statistics(entity.getDay().toString(),collection,
                        entity.getLastTick().toString());
            }
            else
            {
                statistics = new Statistics(entity.getDay().toString(),
                        collection,
                        null);
            }
//            statistics.getMaps().put(0,null);
//            statistics.getMaps().put(1,null);
//            statistics.getMaps().put(2,null);
            statistics.getMaps().clear();
//            statistics.setRegisteredUser(statistics.getMaps().put(0,statisticsMapMapper.run().toEntity(entity.getRegisteredUser())));
//            statistics.setLoginUser(statistics.getMaps().put(1,statisticsMapMapper.run().toEntity(entity.getLoginUser())));
//            statistics.setPurchase(statistics.getMaps().put(2,statisticsMapMapper.run().toEntity(entity.getPurchase())));
//            statistics.setRegisteredUser(statisticsMapMapper.run().toEntity(entity.getRegisteredUser(),0));
//            statistics.setLoginUser(statisticsMapMapper.run().toEntity(entity.getLoginUser(),1));
//            statistics.setPurchase(statisticsMapMapper.run().toEntity(entity.getPurchase(),2));
//            statistics.getMaps().get(0).setParent(statistics);
//            statistics.getMaps().get(1).setParent(statistics);
//            statistics.getMaps().get(2).setParent(statistics);
        }
        else
        {
            statistics.getMaps().clear();
            statistics.getMaps().addAll(collection);
//            statistics.getMaps().clear();
//            statistics.getMaps().put(0,null);
//            statistics.getMaps().put(1,null);
//            statistics.getMaps().put(2,null);
//            statistics.getMaps().clear();
//            statistics.setRegisteredUser(statisticsMapMapper.run().toEntity(entity.getRegisteredUser(),0));
//            statistics.setLoginUser(statisticsMapMapper.run().toEntity(entity.getLoginUser(),1));
//            statistics.setPurchase(statisticsMapMapper.run().toEntity(entity.getPurchase(),2));
//            Statistics finalStatistics = statistics;
//            statistics.s statistics.getMaps().values().stream().peek(statisticMap -> statisticMap.setParent(finalStatistics));
//            statistics.getMaps().get(0).setParent(statistics);
//            statistics.getMaps().get(1).setParent(statistics);
//            statistics.getMaps().get(2).setParent(statistics);
            if(entity.getLastTick()!= null) {
                statistics.setLastTick(entity.getLastTick().toString());
            }
            else
            {
                statistics.setLastTick(null);
            }
        }
        return statistics;
    }

    @Override
    public Statistic fromEntity(ORM.Statistics.Statistics entity) {
        return new Statistic(LocalDate.parse(entity.getDay()),
                statisticsMapMapper.run().fromEntity(entity.getMaps().stream().map(statisticMap -> {
                    if (statisticMap.getIndexOfMapper() == 0) return statisticMap;
                    return statisticMap;
                }).toList().get(0)),
                statisticsMapMapper.run().fromEntity(entity.getMaps().stream().map(statisticMap -> {
                    if (statisticMap.getIndexOfMapper() == 1) return statisticMap;
                    return statisticMap;
                }).toList().get(0)),
                statisticsMapMapper.run().fromEntity(entity.getMaps().stream().map(statisticMap -> {
                    if (statisticMap.getIndexOfMapper() == 2) return statisticMap;
                    return statisticMap;
                }).toList().get(0)),
                LocalTime.parse(entity.getLastTick()));
    }
//
//
//    public ORM.Statistics.Statistics secondBuild(Statistic entity ,ORM.Statistics.Statistics statistics) {
//        statistics.getMaps().clear();
//        statistics.getMaps().put(0,statisticsMapMapper.run().toEntity(entity.getRegisteredUser(),0));
//        statistics.getMaps().put(1,statisticsMapMapper.run().toEntity(entity.getLoginUser(),1));
//        statistics.getMaps().put(2,statisticsMapMapper.run().toEntity(entity.getPurchase(),2));
//        return statistics;
//    }
    @Override
    public int save(Statistic statistic) {
        ORM.Statistics.Statistics statistics=toEntity(statistic);
        dao.save(statistics);
//        dao.update(secondBuild(statistic,statistics));
        return 0;
    }

    @Override
    public void update(Statistic statistic) {
        ORM.Statistics.Statistics statistics=toEntity(statistic);
//        dao.update(secondBuild(statistic,statistics));
        dao.update(toEntity(statistic));
    }

    @Override
    public void delete(String statistics) {
//        dao.delete(toEntity(statistics));
    }

    @Override
    public Statistic findById(String localDate) {
        return fromEntity(dao.findById(localDate));
    }

    @Override
    public Collection<Statistic> findAll() {
        return dao.findAll().stream().map(this::fromEntity).toList();
    }

}
