# MyConcierge - Design Patterns Guide

## Core Patterns for Spring Boot Application

### 1. Strategy Pattern - Search Algorithms

**Use Case**: Different search strategies for cities based on criteria

```java
// Strategy Interface
public interface CitySearchStrategy {
    List<CityMatch> search(SearchCriteria criteria);
}

// Concrete Strategies
@Component
public class RVFriendlySearchStrategy implements CitySearchStrategy {
    @Override
    public List<CityMatch> search(SearchCriteria criteria) {
        // Search logic for RV sites
        return poiRepository.findCitiesWithRVSites(criteria);
    }
}

@Component
public class EventBasedSearchStrategy implements CitySearchStrategy {
    @Override
    public List<CityMatch> search(SearchCriteria criteria) {
        // Search logic for events
        return eventRepository.findCitiesWithEvents(criteria);
    }
}

// Context
@Service
public class CitySearchService {
    private final Map<String, CitySearchStrategy> strategies;
    
    public CitySearchService(List<CitySearchStrategy> strategyList) {
        this.strategies = strategyList.stream()
            .collect(Collectors.toMap(
                s -> s.getClass().getSimpleName(),
                s -> s
            ));
    }
    
    public List<CityMatch> search(SearchCriteria criteria) {
        CitySearchStrategy strategy = selectStrategy(criteria);
        return strategy.search(criteria);
    }
}
```

---

### 2. Factory Pattern - POI Creation

**Use Case**: Create different POI types (Hotel, Restaurant, Campground)

```java
// Product Interface
public interface Poi {
    Long getId();
    String getCategory();
    Point getLocation();
}

// Concrete Products
@Entity
@DiscriminatorValue("HOTEL")
public class Hotel extends BasePoi {
    private Integer starRating;
    private List<String> amenities;
}

@Entity
@DiscriminatorValue("RESTAURANT")
public class Restaurant extends BasePoi {
    private String cuisineType;
    private List<String> dietaryOptions;
}

// Factory
@Component
public class PoiFactory {
    
    public Poi createPoi(String category, Map<String, Object> data) {
        return switch (category.toLowerCase()) {
            case "hotel" -> createHotel(data);
            case "restaurant" -> createRestaurant(data);
            case "campground_rv" -> createRVCampground(data);
            case "campground_tent" -> createTentCampground(data);
            default -> throw new IllegalArgumentException("Unknown POI type");
        };
    }
    
    private Hotel createHotel(Map<String, Object> data) {
        Hotel hotel = new Hotel();
        hotel.setName((String) data.get("name"));
        hotel.setStarRating((Integer) data.get("stars"));
        return hotel;
    }
}
```

---

### 3. Builder Pattern - Complex Search Criteria

**Use Case**: Build complex search queries with many optional parameters

```java
@Builder
@Getter
public class SearchCriteria {
    private String homeLocation;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer maxDistance;
    private Boolean rvFullHookup;
    private Boolean nearDowntown;
    private List<String> interests;
    private List<String> events;
    private TripPurpose tripPurpose;
    private List<DietaryRestriction> dietaryRestrictions;
    
    // Usage
    public static void main(String[] args) {
        SearchCriteria criteria = SearchCriteria.builder()
            .homeLocation("Garner, NC")
            .startDate(LocalDate.of(2025, 12, 28))
            .endDate(LocalDate.of(2026, 1, 4))
            .rvFullHookup(true)
            .nearDowntown(true)
            .interests(List.of("breweries", "historic"))
            .events(List.of("fireworks"))
            .tripPurpose(TripPurpose.ANNIVERSARY)
            .build();
    }
}
```

---

### 4. Repository Pattern - Data Access

**Use Case**: Abstract database operations with Spring Data JPA

```java
// Repository Interface
@Repository
public interface PoiRepository extends JpaRepository<Poi, Long> {
    
    // Custom geospatial query
    @Query("SELECT p FROM Poi p WHERE " +
           "distance(p.location, :point) < :radiusMeters " +
           "AND p.category IN :categories")
    List<Poi> findNearby(
        @Param("point") Point point,
        @Param("radiusMeters") double radiusMeters,
        @Param("categories") List<String> categories
    );
    
    // Dietary restrictions query
    @Query("SELECT p FROM Poi p WHERE " +
           "p.category = 'restaurant' AND " +
           "jsonb_contains(p.dietaryOptions, :dietary) = true")
    List<Poi> findRestaurantsWithDietary(@Param("dietary") String dietary);
}

// Service Layer
@Service
@Transactional
public class PoiService {
    private final PoiRepository poiRepository;
    
    public List<Poi> findNearbyPois(Town town, List<String> categories) {
        return poiRepository.findNearby(
            town.getLocation(),
            5000.0, // 5km radius
            categories
        );
    }
}
```

---

### 5. Observer Pattern - Real-Time Updates

**Use Case**: Notify UI when POI data changes or transit arrives

```java
// Event
@Getter
@AllArgsConstructor
public class TransitArrivalEvent {
    private Long stopId;
    private String routeName;
    private LocalDateTime arrivalTime;
}

// Publisher
@Service
public class TransitService {
    private final ApplicationEventPublisher eventPublisher;
    
    public void updateArrival(Long stopId, String route, LocalDateTime time) {
        // Update database
        transitRepository.updateArrival(stopId, route, time);
        
        // Publish event
        eventPublisher.publishEvent(
            new TransitArrivalEvent(stopId, route, time)
        );
    }
}

// Subscriber
@Component
public class TransitNotificationListener {
    
    @EventListener
    public void handleArrival(TransitArrivalEvent event) {
        // Send SSE to connected clients
        sseService.sendToClients(event);
    }
}
```

---

### 6. Decorator Pattern - POI Enrichment

**Use Case**: Add additional data to POIs (reviews, photos, availability)

```java
// Component Interface
public interface PoiData {
    Poi getPoi();
    Map<String, Object> getData();
}

// Concrete Component
public class BasicPoiData implements PoiData {
    private final Poi poi;
    
    @Override
    public Map<String, Object> getData() {
        return Map.of(
            "id", poi.getId(),
            "name", poi.getName(),
            "category", poi.getCategory()
        );
    }
}

// Decorators
public class ReviewDecorator implements PoiData {
    private final PoiData wrapped;
    private final ReviewService reviewService;
    
    @Override
    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>(wrapped.getData());
        data.put("reviews", reviewService.getReviews(wrapped.getPoi().getId()));
        data.put("rating", reviewService.getAverageRating(wrapped.getPoi().getId()));
        return data;
    }
}

public class PhotoDecorator implements PoiData {
    private final PoiData wrapped;
    private final PhotoService photoService;
    
    @Override
    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>(wrapped.getData());
        data.put("photos", photoService.getPhotos(wrapped.getPoi().getId()));
        return data;
    }
}

// Usage
@Service
public class PoiEnrichmentService {
    public PoiData enrichPoi(Poi poi, Set<String> enrichments) {
        PoiData data = new BasicPoiData(poi);
        
        if (enrichments.contains("reviews")) {
            data = new ReviewDecorator(data, reviewService);
        }
        if (enrichments.contains("photos")) {
            data = new PhotoDecorator(data, photoService);
        }
        
        return data;
    }
}
```

---

### 7. Chain of Responsibility - Family Preference Matching

**Use Case**: Process family preferences through multiple filters

```java
// Handler Interface
public abstract class PreferenceHandler {
    protected PreferenceHandler next;
    
    public void setNext(PreferenceHandler handler) {
        this.next = handler;
    }
    
    public abstract List<Poi> handle(List<Poi> pois, FamilyGroup family);
}

// Concrete Handlers
@Component
public class DietaryRestrictionHandler extends PreferenceHandler {
    @Override
    public List<Poi> handle(List<Poi> pois, FamilyGroup family) {
        List<String> dietary = family.getAllDietaryRestrictions();
        List<Poi> filtered = pois.stream()
            .filter(poi -> poi.supportsDietary(dietary))
            .toList();
        
        return next != null ? next.handle(filtered, family) : filtered;
    }
}

@Component
public class AgeAppropriateHandler extends PreferenceHandler {
    @Override
    public List<Poi> handle(List<Poi> pois, FamilyGroup family) {
        boolean hasKids = family.hasChildren();
        List<Poi> filtered = pois.stream()
            .filter(poi -> !hasKids || poi.isKidFriendly())
            .toList();
        
        return next != null ? next.handle(filtered, family) : filtered;
    }
}

// Chain Setup
@Configuration
public class PreferenceChainConfig {
    @Bean
    public PreferenceHandler preferenceChain(
            DietaryRestrictionHandler dietary,
            AgeAppropriateHandler ageHandler,
            AccessibilityHandler accessibility) {
        
        dietary.setNext(ageHandler);
        ageHandler.setNext(accessibility);
        
        return dietary;
    }
}
```

---

### 8. Template Method - Trip Itinerary Generation

**Use Case**: Common algorithm for trip planning with customizable steps

```java
// Abstract Class
public abstract class TripPlannerTemplate {
    
    // Template method
    public final TripPlan createPlan(SearchCriteria criteria) {
        Town town = selectTown(criteria);
        Accommodation accommodation = findAccommodation(town, criteria);
        List<Activity> activities = planActivities(town, criteria);
        List<Restaurant> dining = planDining(town, criteria);
        
        return assemblePlan(town, accommodation, activities, dining);
    }
    
    protected abstract Town selectTown(SearchCriteria criteria);
    protected abstract List<Activity> planActivities(Town town, SearchCriteria criteria);
    
    // Hook methods (optional override)
    protected List<Restaurant> planDining(Town town, SearchCriteria criteria) {
        return restaurantService.findPopular(town);
    }
    
    private TripPlan assemblePlan(Town town, Accommodation acc, 
                                  List<Activity> activities, 
                                  List<Restaurant> dining) {
        return TripPlan.builder()
            .town(town)
            .accommodation(acc)
            .activities(activities)
            .dining(dining)
            .build();
    }
}

// Concrete Implementations
@Component
public class FamilyTripPlanner extends TripPlannerTemplate {
    @Override
    protected Town selectTown(SearchCriteria criteria) {
        return townService.findFamilyFriendly(criteria);
    }
    
    @Override
    protected List<Activity> planActivities(Town town, SearchCriteria criteria) {
        return activityService.findKidFriendly(town);
    }
}

@Component
public class RomanticTripPlanner extends TripPlannerTemplate {
    @Override
    protected Town selectTown(SearchCriteria criteria) {
        return townService.findRomantic(criteria);
    }
    
    @Override
    protected List<Activity> planActivities(Town town, SearchCriteria criteria) {
        return activityService.findCouplesActivities(town);
    }
    
    @Override
    protected List<Restaurant> planDining(Town town, SearchCriteria criteria) {
        return restaurantService.findFineDining(town);
    }
}
```

---

### 9. Adapter Pattern - External API Integration

**Use Case**: Adapt different external APIs to common interface

```java
// Target Interface
public interface PoiDataProvider {
    List<PoiDto> fetchPois(Town town, String category);
}

// Adaptee 1: Google Places
public class GooglePlacesClient {
    public PlacesResponse searchPlaces(double lat, double lng, String type) {
        // Google API call
    }
}

// Adapter 1
@Component
public class GooglePlacesAdapter implements PoiDataProvider {
    private final GooglePlacesClient client;
    
    @Override
    public List<PoiDto> fetchPois(Town town, String category) {
        PlacesResponse response = client.searchPlaces(
            town.getLatitude(),
            town.getLongitude(),
            mapCategory(category)
        );
        
        return response.getResults().stream()
            .map(this::convertToPoiDto)
            .toList();
    }
    
    private PoiDto convertToPoiDto(Place place) {
        return PoiDto.builder()
            .name(place.getName())
            .location(new Point(place.getLat(), place.getLng()))
            .rating(place.getRating())
            .build();
    }
}

// Adaptee 2: Yelp
public class YelpClient {
    public YelpSearchResult search(YelpSearchRequest request) {
        // Yelp API call
    }
}

// Adapter 2
@Component
public class YelpAdapter implements PoiDataProvider {
    private final YelpClient client;
    
    @Override
    public List<PoiDto> fetchPois(Town town, String category) {
        YelpSearchResult result = client.search(
            YelpSearchRequest.builder()
                .latitude(town.getLatitude())
                .longitude(town.getLongitude())
                .categories(category)
                .build()
        );
        
        return result.getBusinesses().stream()
            .map(this::convertToPoiDto)
            .toList();
    }
}

// Service using adapters
@Service
public class PoiAggregationService {
    private final List<PoiDataProvider> providers;
    
    public List<PoiDto> aggregatePois(Town town, String category) {
        return providers.stream()
            .flatMap(provider -> provider.fetchPois(town, category).stream())
            .distinct()
            .toList();
    }
}
```

---

### 10. Facade Pattern - Complex Subsystem Simplification

**Use Case**: Simplify trip planning with multiple services

```java
// Facade
@Service
public class TripPlanningFacade {
    private final CitySearchService citySearchService;
    private final PoiService poiService;
    private final EventService eventService;
    private final TransitService transitService;
    private final ItineraryService itineraryService;
    
    public CompleteTripPlan planTrip(SearchCriteria criteria) {
        // Step 1: Find matching cities
        List<CityMatch> cities = citySearchService.search(criteria);
        Town selectedTown = cities.get(0).getTown();
        
        // Step 2: Find accommodation
        List<Poi> accommodations = poiService.findAccommodation(
            selectedTown, 
            criteria.getAccommodationType()
        );
        
        // Step 3: Find activities
        List<Poi> activities = poiService.findActivitiesByInterests(
            selectedTown,
            criteria.getInterests()
        );
        
        // Step 4: Find events
        List<Event> events = eventService.findByDateRange(
            selectedTown,
            criteria.getStartDate(),
            criteria.getEndDate()
        );
        
        // Step 5: Plan transit
        TransitPlan transit = transitService.planRoutes(
            selectedTown,
            accommodations.get(0),
            activities
        );
        
        // Step 6: Create itinerary
        Itinerary itinerary = itineraryService.generate(
            criteria,
            activities,
            events
        );
        
        return CompleteTripPlan.builder()
            .town(selectedTown)
            .accommodation(accommodations.get(0))
            .activities(activities)
            .events(events)
            .transit(transit)
            .itinerary(itinerary)
            .build();
    }
}

// Controller uses simple facade
@Controller
public class TripController {
    private final TripPlanningFacade facade;
    
    @PostMapping("/trip/plan")
    public String planTrip(@ModelAttribute SearchCriteria criteria, Model model) {
        CompleteTripPlan plan = facade.planTrip(criteria);
        model.addAttribute("plan", plan);
        return "trip-plan";
    }
}
```

---

## SOLID Principles in MyConcierge

### Single Responsibility Principle
```java
// ❌ Bad: Class doing too much
public class PoiService {
    public void savePoi(Poi poi) { }
    public void sendEmail(String to) { }
    public void generateReport() { }
}

// ✅ Good: Separate responsibilities
public class PoiService {
    public void savePoi(Poi poi) { }
}

public class EmailService {
    public void sendEmail(String to) { }
}

public class ReportService {
    public void generateReport() { }
}
```

### Open/Closed Principle
```java
// ✅ Open for extension, closed for modification
public interface SearchFilter {
    boolean matches(Poi poi, SearchCriteria criteria);
}

@Component
public class PriceFilter implements SearchFilter {
    public boolean matches(Poi poi, SearchCriteria criteria) {
        return poi.getPriceRange() <= criteria.getMaxPrice();
    }
}

// Add new filter without modifying existing code
@Component
public class DietaryFilter implements SearchFilter {
    public boolean matches(Poi poi, SearchCriteria criteria) {
        return poi.supportsDietary(criteria.getDietaryRestrictions());
    }
}
```

### Dependency Inversion Principle
```java
// ✅ Depend on abstractions, not concretions
@Service
public class TripService {
    private final PoiRepository repository; // Interface, not implementation
    private final NotificationService notifier; // Interface
    
    public TripService(PoiRepository repository, NotificationService notifier) {
        this.repository = repository;
        this.notifier = notifier;
    }
}
```

---

## Design Pattern Summary for MyConcierge

| Pattern | Use Case | Benefit |
|---------|----------|---------|
| **Strategy** | City search algorithms | Flexible search strategies |
| **Factory** | POI creation | Centralized object creation |
| **Builder** | Complex criteria | Readable object construction |
| **Repository** | Data access | Abstract database operations |
| **Observer** | Real-time updates | Decoupled event handling |
| **Decorator** | POI enrichment | Dynamic feature addition |
| **Chain of Responsibility** | Preference filtering | Flexible filter pipeline |
| **Template Method** | Trip planning | Reusable algorithms |
| **Adapter** | External APIs | Unified interface |
| **Facade** | Trip planning | Simplified complex operations |

**All patterns work beautifully with Spring Boot's dependency injection!** 🎯
