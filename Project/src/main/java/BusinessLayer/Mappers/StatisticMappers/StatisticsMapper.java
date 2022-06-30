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
import java.util.Collection;

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
        if(statistics==null)
        {
            if(entity.getLastTick()!= null) {
                statistics = new Statistics(entity.getDay().toString(),
                        null,
                        entity.getLastTick().toString());
            }
            else
            {
                statistics = new Statistics(entity.getDay().toString(),
                        null,
                        null);
            }
            statistics.setRegisteredUser(statisticsMapMapper.run().toEntity(entity.getRegisteredUser(),statistics,0));
            statistics.setLoginUser(statisticsMapMapper.run().toEntity(entity.getLoginUser(),statistics,1));
            statistics.setPurchase(statisticsMapMapper.run().toEntity(entity.getPurchase(),statistics,2));
        }
        else
        {
            statistics.setRegisteredUser(statisticsMapMapper.run().toEntity(entity.getRegisteredUser(),statistics,0));
            statistics.setLoginUser(statisticsMapMapper.run().toEntity(entity.getLoginUser(),statistics,1));
            statistics.setPurchase(statisticsMapMapper.run().toEntity(entity.getPurchase(),statistics,2));
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
                statisticsMapMapper.run().fromEntity(entity.getRegisteredUser()),
                statisticsMapMapper.run().fromEntity(entity.getLoginUser()),
                statisticsMapMapper.run().fromEntity(entity.getPurchase()),
                LocalTime.parse(entity.getLastTick()));
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
