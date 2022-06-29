package BusinessLayer.Mappers.StatisticMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.Func;
import BusinessLayer.Statistics.Statistic;
import BusinessLayer.Statistics.StatisticMap;
import ORM.DAOs.DBImpl;
import ORM.DAOs.Statistics.StatisticsDAO;
import ORM.Statistics.Statistics;

import java.time.LocalDate;
import java.util.Collection;

public class StatisticsMapper implements DBImpl<Statistic, LocalDate> , CastEntity<ORM.Statistics.Statistics, Statistic> {

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
        ORM.Statistics.Statistics statistics = dao.findById(entity.getDay());
        if(statistics==null)
        {
            statistics = new Statistics(entity.getDay(),
                    null,
                    null,
                    null,
                    entity.getLastTick());
            statistics.setRegisteredUser(statisticsMapMapper.run().toEntity(entity.getRegisteredUser(),statistics));
            statistics.setLoginUser(statisticsMapMapper.run().toEntity(entity.getLoginUser(),statistics));
            statistics.setPurchase(statisticsMapMapper.run().toEntity(entity.getPurchase(),statistics));
        }
        else
        {
            statistics.setLoginUser(statisticsMapMapper.run().toEntity(entity.getLoginUser(),statistics));
            statistics.setRegisteredUser(statisticsMapMapper.run().toEntity(entity.getRegisteredUser(),statistics));
            statistics.setPurchase(statisticsMapMapper.run().toEntity(entity.getPurchase(),statistics));
            statistics.setLastTick(entity.getLastTick());
        }
        return statistics;
    }

    @Override
    public Statistic fromEntity(ORM.Statistics.Statistics entity) {
        return new Statistic(entity.getDay(),
                statisticsMapMapper.run().fromEntity(entity.getRegisteredUser()),
                statisticsMapMapper.run().fromEntity(entity.getLoginUser()),
                statisticsMapMapper.run().fromEntity(entity.getPurchase()),
                entity.getLastTick());
    }

    @Override
    public int save(Statistic statistic) {
        return dao.save(toEntity(statistic));
    }

    @Override
    public void update(Statistic statistic) {
        dao.update(toEntity(statistic));

    }

    @Override
    public void delete(LocalDate statistics) {
//        dao.delete(toEntity(statistics));
    }

    @Override
    public Statistic findById(LocalDate localDate) {
        return fromEntity(dao.findById(localDate));
    }

    @Override
    public Collection<Statistic> findAll() {
        return dao.findAll().stream().map(this::fromEntity).toList();
    }

}
