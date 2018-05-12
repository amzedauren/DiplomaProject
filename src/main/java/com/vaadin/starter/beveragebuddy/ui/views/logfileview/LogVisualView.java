/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.starter.beveragebuddy.ui.views.logfileview;

import com.maxmind.geoip.Country;
import com.maxmind.geoip.LookupService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.starter.beveragebuddy.backend.Review;
import com.vaadin.starter.beveragebuddy.backend.ReviewService;
import com.vaadin.starter.beveragebuddy.ui.MainLayout;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.stream.Collectors;

import static com.vaadin.starter.beveragebuddy.ui.views.charts.ChartTemplates.getCountToIpChart;
import static com.vaadin.starter.beveragebuddy.ui.views.charts.ChartTemplates.pieChart;
import static com.vaadin.starter.beveragebuddy.ui.views.charts.ChartTemplates.visualizationOfActivity;


@Route(value = "log", layout = MainLayout.class)
@PageTitle("")
public class LogVisualView extends VerticalLayout{

  private VerticalLayout details = new VerticalLayout();

  public LogVisualView() throws IOException {
    init();
  }

  public static void main(String[] args) throws IOException {
    File dbfile = new File("C:\\Users\\Amze\\Documents\\IPDB\\GeoLite2-Country_20180501\\GeoLite2-Country.mmdb");
    if(!dbfile.exists()) {
      System.out.println("blat");
      return;
    }
    LookupService cl = new LookupService(dbfile, LookupService.GEOIP_MEMORY_CACHE);

    Country country = cl.getCountry("87.240.129.71");
    System.out.println(country.getName());
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
    right.add(visualizationOfActivity());
    right.add(details);
    details.setSizeFull();

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
      details.add(getRow(getCountToIpChart(ReviewService.getInstance().getCountToIpDate(fileName))));
    } else {
      details.add(getRow(pieChart(ReviewService.getInstance().getPortCount(fileName)), getCountToIpChart(ReviewService.getInstance().getCountToIpDate(fileName))));
    }


  }

  private HorizontalLayout getRow(Component... components) {
    HorizontalLayout horizontalLayout = new HorizontalLayout(components);
    horizontalLayout.setSizeFull();
    return horizontalLayout;
  }



}
