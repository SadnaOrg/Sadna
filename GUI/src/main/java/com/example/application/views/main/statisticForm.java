package com.example.application.views.main;

import ServiceLayer.Objects.Statistic;
import ServiceLayer.interfaces.SystemManagerService;
import com.example.application.Header.Header;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.LinkedList;

import static com.example.application.Utility.notifyError;

public class statisticForm  extends Header {

    SystemManagerService service;
    Collection<Statistic> stat;

    DatePicker departureDate;
    DatePicker returnDate;


    public statisticForm(SystemManagerService systemManagerService) {
        service = systemManagerService;
        try{
            setName(service.getUserInfo().getElement().username);
        } catch (Exception ignored) {}

        departureDate = new DatePicker("from ",LocalDate.now());
        returnDate = new DatePicker("to",LocalDate.now());
        departureDate.addValueChangeListener(e -> {
            returnDate.setMin(e.getValue());
            dateChanged();
        });
        returnDate.addValueChangeListener(e -> {
            departureDate.setMax(e.getValue());
            dateChanged();
        });
        dateChanged();
        registerToNotification(service);

    }

    private Component printStat() {
        var layout = new VerticalLayout();

        var statRes = service.getStatistic(departureDate.getValue(),returnDate.getValue());
        if(!statRes.isOk()) {
            notifyError(statRes.getMsg());
            return layout;
        }
        stat = statRes.getElement();

        layout.add(new H1("Statistic from  "+departureDate.getValue().toString() +" to " + returnDate.getValue().toString()));

        layout.add(makeLoginChart(stat));
        return layout;
    }

    private Component makeLoginChart(Collection<Statistic> stats) {
        Chart chart = new Chart();
        chart.setTimeline(true);

        Configuration configuration = chart.getConfiguration();
        configuration.getTitle().setText("statistic : ");

        YAxis yAxis = new YAxis();
        Labels label = new Labels();
        label.setFormatter("function() { return (this.value > 0 ? ' + ' : '') + this.value + '%'; }");
        yAxis.setLabels(label);

        PlotLine plotLine = new PlotLine();
        plotLine.setValue(2);
        yAxis.setPlotLines(plotLine);
        configuration.addyAxis(yAxis);

        Tooltip tooltip = new Tooltip();
        tooltip.setPointFormat("<span>{series.name}</span>: <b>{point.y}</b> ({point.change}%)<br/>");
        tooltip.setValueDecimals(2);
        configuration.setTooltip(tooltip);

        DataSeries loggedinSeries = new DataSeries();
        loggedinSeries.setName("number of login user");
        DataSeries registerSeries = new DataSeries();
        registerSeries.setName("number of registration to system");
        DataSeries purchaseSeries = new DataSeries();
        purchaseSeries.setName("number of purchase");
        for(var stat : stats) {
            for (var data : stat.numberOfLogin().entrySet()) {
                DataSeriesItem item = new DataSeriesItem();
                item.setX(LocalDateTime.of(stat.day(), data.getKey()).toInstant(ZoneOffset.UTC));
                item.setY(data.getValue());
                loggedinSeries.add(item);
            }


            for (var data : stat.register().entrySet()) {
                DataSeriesItem item = new DataSeriesItem();
                item.setX(LocalDateTime.of(stat.day(), data.getKey()).toInstant(ZoneOffset.UTC));
                item.setY(data.getValue());
                registerSeries.add(item);
            }

            for (var data : stat.numberOfPurchase().entrySet()) {
                DataSeriesItem item = new DataSeriesItem();
                item.setX(LocalDateTime.of(stat.day(), data.getKey()).toInstant(ZoneOffset.UTC));
                item.setY(data.getValue());
                purchaseSeries.add(item);
            }
        }
        configuration.setSeries(loggedinSeries,registerSeries,purchaseSeries);//, googSeries, msftSeries);

        PlotOptionsSeries plotOptionsSeries = new PlotOptionsSeries();
        plotOptionsSeries.setCompare(Compare.PERCENT);
        configuration.setPlotOptions(plotOptionsSeries);

        RangeSelector rangeSelector = new RangeSelector();
        rangeSelector.setSelected(4);
        configuration.setRangeSelector(rangeSelector);
        return chart;
    }

    private void dateChanged() {
        setContent(new VerticalLayout(new HorizontalLayout(departureDate,returnDate),printStat()));
    }
}
