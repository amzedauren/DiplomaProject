package com.vaadin.starter.beveragebuddy.backend;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.starter.beveragebuddy.ui.encoders.LocalDateToStringEncoder;

/**
 * Simple backend service to store and retrieve {@link Review} instances.
 */
public class ReviewService {

  private Map<Long, Review> reviews = new HashMap<>();
  private AtomicLong nextId = new AtomicLong(0);

  /**
   * Declared private to ensure uniqueness of this Singleton.
   */
  private ReviewService() {
  }

  /**
   * Gets the unique instance of this Singleton.
   *
   * @return the unique instance of this Singleton
   */
  public static ReviewService getInstance() {
    return SingletonHolder.INSTANCE;
  }

  public List<Review> getAll() {
    return new ArrayList<>(reviews.values());
  }


  /**
   * Helper class to initialize the singleton Service in a thread-safe way and
   * to keep the initialization ordering clear between the two services. See
   * also: https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
   */
  private static class SingletonHolder {
    static final ReviewService INSTANCE = createDemoReviewService();

    /**
     * This class is not meant to be instantiated.
     */
    private SingletonHolder() {
    }

    private static ReviewService createDemoReviewService() {
      //FIXME
      final ReviewService reviewService = new ReviewService();
      Review r1 = new Review("hahahahah", LocalDate.now(), "amun");
      Review r2 = new Review("kajsdncksnvkd", LocalDate.now(), "snort");
      reviewService.reviews.put(1L, r1);
      reviewService.reviews.put(2L, r2);
      return reviewService;
    }

    private static LocalDate getRandomDate() {
      long minDay = LocalDate.of(1930, 1, 1).toEpochDay();
      long maxDay = LocalDate.now().toEpochDay();
      long randomDay = ThreadLocalRandom.current().nextLong(minDay,
        maxDay);
      return LocalDate.ofEpochDay(randomDay);
    }
  }
}
