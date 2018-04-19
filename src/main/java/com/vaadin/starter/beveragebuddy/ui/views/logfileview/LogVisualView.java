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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.starter.beveragebuddy.ui.MainLayout;

import static com.vaadin.starter.beveragebuddy.ui.views.charts.ChartTemplates.getCountToIpChart;
import static com.vaadin.starter.beveragebuddy.ui.views.charts.ChartTemplates.pieChart;
import static com.vaadin.starter.beveragebuddy.ui.views.charts.ChartTemplates.visualizationOfActivity;


/**
 * Displays the list of available categories, with a search filter as well as
 * buttons to add a new category or edit existing ones.
 */
@Route(value = "log", layout = MainLayout.class)
@PageTitle("Categories List")
public class LogVisualView extends VerticalLayout implements HasUrlParameter<String> {

  private String logFileName = null;
  @Override
  public void setParameter(BeforeEvent event,
                           @OptionalParameter String fileName) {
    if(fileName == null){
      Div div = new Div();
      div.setText("file not selected");
      add(div);
    } else {
      this.logFileName = fileName;
      initView();
    }
  }

//  private final TextField searchField = new TextField("", "Search categories");
//  private final H2 header = new H2("Categories");
//  private final Grid<Category> grid = new Grid<>();

//  private final CategoryEditorDialog form = new CategoryEditorDialog(
//    this::saveCategory, this::deleteCategory);

  public LogVisualView() {

  }


  private void initContent(VerticalLayout wall) {
    VerticalLayout main = new VerticalLayout();
    main.setSizeFull();
    main.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);

    main.add(getRow(getCountToIpChart()));
    main.add(getRow(pieChart(), visualizationOfActivity()));
    wall.add(main);
  }

  private HorizontalLayout getRow(Component... components) {
    HorizontalLayout horizontalLayout = new HorizontalLayout(components);
    horizontalLayout.setSizeFull();
    return horizontalLayout;
  }


  private void initView() {
    addClassName("categories-list");
    setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
    setSizeFull();

    VerticalLayout container = new VerticalLayout();
    container.setSizeFull();
    container.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);

//    container.setClassName("view-container");
//    container.setAlignItems(Alignment.STRETCH);
    initContent(container);

    add(container);

  }

//  private void addSearchBar() {
//    Div viewToolbar = new Div();
//    viewToolbar.addClassName("view-toolbar");
//
//    searchField.setPrefixComponent(new Icon("lumo", "search"));
//    searchField.addClassName("view-toolbar__search-field");
//    searchField.addValueChangeListener(e -> updateView());
//    searchField.setValueChangeMode(ValueChangeMode.EAGER);
//
//    Button newButton = new Button("New category", new Icon("lumo", "plus"));
//    newButton.getElement().setAttribute("theme", "primary");
//    newButton.addClassName("view-toolbar__button");
//    newButton.addClickListener(e -> form.open(new Category(),
//      AbstractEditorDialog.Operation.ADD));
//
//    viewToolbar.add(searchField, newButton);
//    add(viewToolbar);
//
//  }

//  private void addContent() {
//    VerticalLayout container = new VerticalLayout();
//    container.setClassName("view-container");
//    container.setAlignItems(Alignment.STRETCH);
//
//    grid.addColumn(Category::getName).setHeader("Name").setWidth("8em").setResizable(true);
//    grid.addColumn(this::getReviewCount).setHeader("Beverages").setWidth("6em");
//    grid.addColumn(new ComponentRenderer<>(this::createEditButton))
//      .setFlexGrow(0);
//    grid.setSelectionMode(SelectionMode.NONE);
//
//    container.add(header, grid);
//    add(container);
//  }

//  private Button createEditButton(Category category) {
//    Button edit = new Button("Edit", event -> form.open(category,
//      AbstractEditorDialog.Operation.EDIT));
//    edit.setIcon(new Icon("lumo", "edit"));
//    edit.addClassName("review__edit");
//    edit.getElement().setAttribute("theme", "tertiary");
//    return edit;
//  }

//  private String getReviewCount(Category category) {
//    List<Review> reviewsInCategory = ReviewService.getInstance()
//      .findReviews(category.getName());
//    int sum = reviewsInCategory.stream().mapToInt(Review::getCount).sum();
//    return Integer.toString(sum);
//  }

//  private void updateView() {
//    List<Category> categories = CategoryService.getInstance()
//      .findCategories(searchField.getValue());
//    grid.setItems(categories);
//
//    if (searchField.getValue().length() > 0) {
//      header.setText("Search for “" + searchField.getValue() + "”");
//    } else {
//      header.setText("Categories");
//    }
//  }
//
//  private void saveCategory(Category category,
//                            AbstractEditorDialog.Operation operation) {
//    CategoryService.getInstance().saveCategory(category);
//
//    Notification.show(
//      "Category successfully " + operation.getNameInText() + "ed.", 3000, Position.BOTTOM_START);
//    updateView();
//  }
//
//  private void deleteCategory(Category category) {
//    List<Review> reviewsInCategory = ReviewService.getInstance()
//      .findReviews(category.getName());
//
//    reviewsInCategory.forEach(review -> {
//      review.setCategory(CategoryService.getInstance()
//        .findCategoryOrThrow("Undefined"));
//      ReviewService.getInstance().saveReview(review);
//    });
//    CategoryService.getInstance().deleteCategory(category);
//
//    Notification.show("Category successfully deleted.", 3000, Position.BOTTOM_START);
//    updateView();
//  }
}