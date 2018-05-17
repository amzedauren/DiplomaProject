
package com.vaadin.starter.beveragebuddy.ui.views.logfileview;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.starter.beveragebuddy.backend.Review;
import com.vaadin.starter.beveragebuddy.backend.ReviewService;
import com.vaadin.starter.beveragebuddy.ui.MainLayout;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.vaadin.starter.beveragebuddy.ui.views.charts.ChartTemplates.getCountToIpChart;
import static com.vaadin.starter.beveragebuddy.ui.views.charts.ChartTemplates.pieChart;



@Route(value = "log", layout = MainLayout.class)
@PageTitle("")
public class LogVisualView extends VerticalLayout{


  private VerticalLayout details = new VerticalLayout();

  public LogVisualView() throws IOException {
    init();
  }

  private void init() throws IOException {
    setSizeFull();
    VerticalLayout container = new VerticalLayout();
    container.setSizeFull();
    initContent(container);
    add(container);
  }

  private void initContent(VerticalLayout wall) throws IOException {
    HorizontalLayout main = new HorizontalLayout();
    main.setSizeFull();
    wall.add(main);

    ListBox<String> listBox = new ListBox<>();
    List<Review> all = ReviewService.getInstance().getAll();
    listBox.setItems(all.stream().map(Review::getName).collect(Collectors.toList()));

    VerticalLayout right = new VerticalLayout();
    right.setSizeFull();
//    right.add(getRow(true, visualizationOfActivity(ReviewService.getInstance().getActivityByMonth())));
    right.add(details);

    main.add(listBox);
    main.add(right);
    listBox.addValueChangeListener((HasValue.ValueChangeListener<ListBox<String>, String>) valueChangeEvent -> {
      String value = valueChangeEvent.getValue();
      updateDetails(value);
    });
  }

  private void updateDetails(String fileName){
    details.removeAll();

    if(fileName.contains("snort")) {
      details.add(getRow(true, pieChart("Attacker Country",ReviewService.getInstance().getCountryCount(fileName))));
      details.add(getRow(true,getCountToIpChart(ReviewService.getInstance().getCountIp(fileName))));
    } else {
      details.add(getRow( false,
        pieChart("Victim ports", ReviewService.getInstance().getPortCount(fileName)),
        pieChart("Attacker Country", ReviewService.getInstance().getCountryCount(fileName))));
      details.add(getRow(true,getCountToIpChart(ReviewService.getInstance().getCountIp(fileName))));
    }

    Anchor link = new Anchor();
    link.setTarget("_blank");
    String filePAth = ReviewService.getInstance().nameToFileMap.get(fileName).getPath();
    filePAth = filePAth.replace("\\", "\\\\");

    link.setHref(String.format("http://localhost:5000/graph?filename=%s&type=ip", filePAth));
    link.setText("attacker Ip to Victim ip graph");

    details.add(link);
  }

  private HorizontalLayout getRow(boolean fullSize, Component... components) {
    HorizontalLayout horizontalLayout = new HorizontalLayout(components);
    if(fullSize)
      horizontalLayout.setSizeFull();
    return horizontalLayout;
  }


}