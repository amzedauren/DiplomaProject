package com.vaadin.starter.beveragebuddy.ui.views.charts;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;

import java.util.*;

public class DimplomaCharts {

  public static Chart getCountToIpChart() {

    Chart chart = new Chart(ChartType.COLUMN);

    Configuration conf = chart.getConfiguration();
    conf.setTitle(new Title("Column chart count of attacks with same IPv4"));
    PlotOptionsColumn column = new PlotOptionsColumn();
    column.setMinPointLength(3);
    conf.setPlotOptions(column);
    XAxis xAxis = new XAxis();
    String[] ips = new String[100];
    Random rnd = new Random();
    for (int i = 0; i < 100; i++)
      ips[i] = ("192.168.1." + rnd.nextInt(255));

    xAxis.setCategories(ips);
    conf.addxAxis(xAxis);

    Tooltip tooltip = new Tooltip();
    tooltip.setFormatter(
      "function() { return ''+ this.series.name +': '+ this.y +'';}");
    conf.setTooltip(tooltip);

    conf.setCredits(new Credits(false));

    List<Number> numbers = new ArrayList<>();
    for (int i = 0; i < 100; i++)
      numbers.add(new Random().nextInt(100));
    numbers.sort((o1, o2) -> Integer.compare(o2.intValue(), o1.intValue()));

    YAxis y = new YAxis();
    y.setMin(0);
    y.setTitle("Count");
    conf.addyAxis(y);

    conf.addSeries(new ListSeries("ip", numbers));

    return chart;
  }

}