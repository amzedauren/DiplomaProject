package com.vaadin.starter.beveragebuddy.ui.views.charts;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.starter.beveragebuddy.backend.ReviewService;
import com.vaadin.starter.beveragebuddy.backend.util.GeoIpUtil;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ChartTemplates {

  public static Chart getCountToIpChart( Map<String,Integer> map) {

    Map<String, Integer> newMapSortedByValue = map.entrySet().stream()
      .sorted(Entry.<String,Integer>comparingByValue().reversed()).limit(50)
      .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

    Chart chart = new Chart(ChartType.COLUMN);
    Configuration conf = chart.getConfiguration();
    conf.setTitle(new Title("Column chart count of attacks with same IPv4"));
    PlotOptionsColumn column = new PlotOptionsColumn();
    column.setMinPointLength(3);
    conf.setPlotOptions(column);
    XAxis xAxis = new XAxis();
    String[] ips = newMapSortedByValue.keySet().toArray(new String[newMapSortedByValue.size()]);
    xAxis.setCategories(ips);
    conf.addxAxis(xAxis);

    Tooltip tooltip = new Tooltip();
    tooltip.setFormatter(
      "function() { return ''+ this.series.name +': '+ this.y +'';}");
    conf.setTooltip(tooltip);

    conf.setCredits(new Credits(false));

    YAxis y = new YAxis();
    y.setMin(0);
    y.setTitle("Count");
    conf.addyAxis(y);

    List<Number> numbers = new ArrayList<>();
    for (int i = 0; i < newMapSortedByValue.size(); i++)
      numbers.add(newMapSortedByValue.get(ips[i]));

    conf.addSeries(new ListSeries("ip", numbers));

    return chart;
  }

  public static Chart pieChart(String title, Map<String, Integer> portCount) {

    Map<String, Integer> newMapSortedByValue = portCount.entrySet().stream()
      .sorted(Entry.<String,Integer>comparingByValue().reversed())
      .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


    Chart chart = new Chart(ChartType.PIE);
    Configuration conf = chart.getConfiguration();

    conf.setTitle(title);

    Tooltip tooltip = new Tooltip();
    tooltip.setValueDecimals(1);
    tooltip.setPointFormat("{series.name}: <b>{point.percentage}%</b>");
    conf.setTooltip(tooltip);

    PlotOptionsPie plotOptions = new PlotOptionsPie();
    plotOptions.setAllowPointSelect(true);
    plotOptions.setCursor(Cursor.POINTER);
    plotOptions.setShowInLegend(true);
    conf.setPlotOptions(plotOptions);

    DataSeries series = new DataSeries();
    int count = 0;
    int others = 0;
    for(String port: newMapSortedByValue.keySet()) {
      if(count > 100)
        break;
      if(count > 25) {
        others += portCount.get(port);
      } else {
        series.add(new DataSeriesItem(port, portCount.get(port)));
      }
      count++;
    }
    if(others > 0) {
      series.add(new DataSeriesItem("others", others));
    }
    conf.setSeries(series);
    chart.setVisibilityTogglingDisabled(true);

    return chart;
  }


  public static Chart visualizationOfActivity(Map<ReviewService.LogType, Map<Integer, Integer>> map) {

    Chart chart = new Chart();
    chart.setHeight("300px");
    Configuration configuration = chart.getConfiguration();
    configuration.getChart().setType(ChartType.LINE);
    configuration.getChart().setMarginRight(130);
    configuration.getChart().setMarginBottom(25);

    configuration.getTitle().setText("Monthly Average Activity");
    configuration.getSubTitle().setText("Source: Data from IDS/IPS programs");

    configuration.getxAxis().setCategories("Jan", "Feb", "Mar", "Apr",
      "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");

    YAxis yAxis = configuration.getyAxis();
    yAxis.setMin(0d);
    yAxis.setMax(100000d);
    yAxis.setTitle(new AxisTitle("Count of Activity"));
    yAxis.getTitle().setAlign(VerticalAlign.HIGH);

    configuration.getTooltip().setFormatter(
      "return '<b>'+ this.series.name +'</b><br/>'+this.x +': '+ this.y");

    PlotOptionsLine plotOptions = new PlotOptionsLine();
    plotOptions.setDataLabels(new DataLabels(true));
    configuration.setPlotOptions(plotOptions);

    Legend legend = configuration.getLegend();
    legend.setLayout(LayoutDirection.VERTICAL);
    legend.setAlign(HorizontalAlign.RIGHT);
    legend.setVerticalAlign(VerticalAlign.TOP);
    legend.setX(10d);

    legend.setY(100d);


    for(ReviewService.LogType logType: map.keySet()) {
      ListSeries ls = new ListSeries();
      ls.setName(logType.name());
      System.out.println(logType.name());
      Map<Integer, Integer> tmp = map.get(logType);

      ls.setData(tmp.get(0) == null ? 0 : tmp.get(0),
        tmp.get(1) == null ? 0 : tmp.get(1),
        tmp.get(2) == null ? 0 : tmp.get(2),
        tmp.get(3) == null ? 0 : tmp.get(3),
        tmp.get(4) == null ? 0 : tmp.get(4),
        tmp.get(5) == null ? 0 : tmp.get(5),
        tmp.get(6) == null ? 0 : tmp.get(6),
        tmp.get(7) == null ? 0 : tmp.get(7),
        tmp.get(8) == null ? 0 : tmp.get(8),
        tmp.get(9) == null ? 0 : tmp.get(9),
        tmp.get(10) == null ? 0 : tmp.get(10),
        tmp.get(11) == null ? 0 : tmp.get(11));

      configuration.addSeries(ls);
    }

    return chart;
  }

  public static void main(String[] args) {
    Map<Integer, Integer> map = new HashMap<>();
    System.out.println(map.get(0));

  }
}