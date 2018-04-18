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

  public static Chart pieChart() {

    Chart chart = new Chart(ChartType.PIE);
    Configuration conf = chart.getConfiguration();

    conf.setTitle("Visualiztion of victims' ports");

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
    series.add(new DataSeriesItem("135", 45.0));
    series.add(new DataSeriesItem("80", 26.8));
    DataSeriesItem chrome = new DataSeriesItem("110", 12.8);
    chrome.setSliced(true);
    chrome.setSelected(true);
    series.add(chrome);
    series.add(new DataSeriesItem("105", 8.5));
    series.add(new DataSeriesItem("21", 6.2));
    series.add(new DataSeriesItem("143", 0.7));
    conf.setSeries(series);
    chart.setVisibilityTogglingDisabled(true);

    return chart;
  }

  public static Chart visualizationOfActivity() {

    Chart chart = new Chart();
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
    yAxis.setMax(30d);
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

    ListSeries ls = new ListSeries();
    ls.setName("Snort");
    ls.setData(7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3,
      13.9, 9.6);
    configuration.addSeries(ls);
    ls = new ListSeries();
    ls.setName("Amun");
    ls.setData(0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1,
      8.6, 2.5);
    configuration.addSeries(ls);
    ls = new ListSeries();
    ls.setName("Glastopf");
    ls.setData(0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9,
      1.0);
    configuration.addSeries(ls);
    ls = new ListSeries();
    ls.setName("Dionaea");
    ls.setData(3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6,
      4.8);
    configuration.addSeries(ls);

    return chart;
  }

}