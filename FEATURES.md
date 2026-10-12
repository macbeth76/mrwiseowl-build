# MyConcierge - Features Specification

## Core Features

### 1. AI Chatbot Assistant
**Description**: Conversational AI that helps users discover and plan through natural language

**Functionality**:
- Natural language queries: "Find family-friendly hotels with pools"
- Context-aware conversations with memory
- Automatic map updates based on chat
- POI recommendations and comparisons
- Quick action buttons and suggestions

**User Flow**:
1. User clicks chat bubble (bottom-right)
2. Types or speaks question
3. AI responds with recommendations + action cards
4. Results automatically show on map
5. User can refine with follow-up questions

**AI Capabilities**:
- Parse intent and extract filters
- Multi-turn conversations
- Personalized recommendations
- Function calling to search/filter POIs
- Proactive suggestions

### 2. Town Search
**Description**: Allow users to search and select a town to explore

**Functionality**:
- Autocomplete search input
- Geolocation detection (optional)
- Recent searches history
- Popular destinations suggestions

**User Flow**:
1. User enters town name
2. Autocomplete shows matching results
3. User selects town
4. Map centers on town with default zoom level

### 2. Interactive Map
**Description**: Main map interface displaying the selected town

**Functionality**:
- Pan and zoom controls
- Current location marker (if permitted)
- Cluster markers for dense areas
- Smooth transitions between layers

**Map Controls**:
- Zoom in/out buttons
- Reset view button
- Fullscreen toggle
- Geolocation button

### 3. Layer System
**Description**: Toggle different categories of points of interest

#### Layer 1: Hotels
- Hotel markers on map
- Price range indicators
- Star ratings
- Availability status
- Amenities: Pool, hot tub, gym, spa, breakfast

#### Layer 2: Activities
- **Outdoor Activities**: Hiking, parks, tours, sports, beaches
- **Indoor Activities**: Museums, theaters, galleries, entertainment
- Activity type icons
- Duration estimates
- Age appropriateness
- Seasonal availability
- Weather-dependent indicators

#### Layer 3: Campgrounds
- **RV Sites**: RV icon, hookup types (full/partial/none)
- **Tent Sites**: Tent icon, primitive/developed
- Amenities icons (water, electric, wifi, showers)
- Site availability and pricing
- Reservation links

#### Layer 4: Historic Sites
- Historic landmark markers
- Era/period tags
- Guided tour availability
- Educational content links

#### Layer 5: Public Transit
- **Subway/Metro Stations**: Station markers with line colors
- **Bus Stops**: Bus icon with route numbers
- **Train Stations**: Rail icon (commuter/regional)
- Real-time arrival times
- Route maps overlay
- Transfer points highlighted
- Accessibility info (elevators, ramps)

#### Layer 6: Events
- **Seasonal Events**: New Year's, holidays, festivals
- **Fireworks Shows**: Locations and times
- **Concerts & Performances**: Venues and schedules
- **Local Festivals**: Food, music, cultural events
- Date range filtering
- Ticket availability

#### Layer 7: Food & Drink
- **Restaurants**: Cuisine types, price range
- **Breweries**: Craft beer, tours, tastings
- **Bars & Pubs**: Live music, happy hours
- **Cafes**: Coffee shops, bakeries
- **Dietary Badges**: 🌾 Gluten-free, 🌱 Vegan, 🥜 Nut-free, 🥛 Dairy-free, 🌿 Vegetarian, 🦐 Shellfish-free
- Dietary filters (show only compatible restaurants)
- **Romantic dining**: Date night spots, fine dining

**Layer Controls**:
- Toggle switches for each layer
- "Show All" / "Hide All" options
- Layer legend with icon explanations
- Active layer count indicator

### 4. Point of Interest (POI) Details
**Description**: Detailed information panel for selected locations

**Information Displayed**:
- Name and category
- Address and directions link
- Photos (carousel)
- Description
- Contact information
- Website link
- Operating hours
- Price range
- User ratings and reviews
- Distance from town center

**Actions**:
- Get directions (Google Maps/Apple Maps)
- Save to favorites
- Share location
- Call/Email directly

### 5. Search and Filter
**Description**: Refine visible POIs based on criteria

**Filter Options**:
- Price range slider
- Distance radius
- Rating threshold
- Open now toggle
- Amenities checkboxes
- Category-specific filters

**Search Within Results**:
- Text search across POI names
- Real-time filtering as user types

### 6. List View
**Description**: Alternative view showing POIs as a list

**Functionality**:
- Sortable columns (distance, rating, price)
- Quick preview cards
- Click to highlight on map
- Pagination or infinite scroll

### 7. Saved Locations
**Description**: User can save favorite POIs for later

**Functionality**:
- Heart icon to save/unsave
- "My Saved Places" section
- Organize into custom lists
- Export saved locations

### 8. Family Profiles & Preferences
**Description**: Create profiles for each family member with individual preferences

**Functionality**:
- **Family Group Setup**: Create family (e.g., "Smith Family")
- **Member Profiles**:
  - Husband profile with preferences
  - Wife profile with preferences
  - Kids profiles (with or without accounts)
  - Age ranges: Adult, Teen, Child (6-12), Child (0-5)
  
**Individual Preferences**:
- Interests: Breweries, museums, hiking, playgrounds, shopping
- Dietary restrictions: Vegan, gluten-free, allergies
- Activity level: High, moderate, low
- Mobility needs: Wheelchair accessible, stroller-friendly
- Must-haves vs nice-to-haves

**Trip Planning with Family**:
- AI asks each member their preferences
- "What does [Name] want to do?"
- Balance activities for all members
- Suggest family-friendly + adult activities
- Find kid-friendly restaurants near breweries
- Schedule downtime for different age groups

**Preference Weighting**:
- Must-have activities for each person
- Compromise suggestions when preferences conflict
- Fair rotation of activity choices

### 9. Directions and Navigation
**Description**: Get directions to selected POI

**Functionality**:
- Integration with Google Maps/Apple Maps
- **Transit Mode**: Subway/bus/train routes with transfers
- Walking/driving options
- Estimated travel time and cost
- Real-time transit schedules
- Step-by-step transit directions
- Open in native map app

### 9. Transit Trip Planner
**Description**: Plan multi-stop trips using public transit

**Functionality**:
- Select multiple POIs to visit
- Optimize route using transit
- Show nearest stations/stops to each POI
- Calculate total travel time
- Display fare estimates
- Alternative route options
- Save transit passes/cards info

### 10. Multi-Day Trip Planner
**Description**: Plan extended stays with date-based itineraries

**Functionality**:
- Set trip dates (e.g., Dec 28 - Jan 4)
- Specify starting location (e.g., Garner, NC)
- Define preferences:
  - Accommodation type (RV full hookup)
  - Proximity to downtown
  - Interests (breweries, historic sites, events)
- Auto-suggest cities matching criteria
- Show events during date range (New Year's fireworks)
- Calculate driving distance and time
- Day-by-day itinerary builder
- Weather forecasts for dates
- Budget estimator

**Search Filters**:
- Distance from home (radius)
- RV site availability for dates
- Must-have amenities
- Event types during stay
- Downtown walkability score
- **Trip purpose**: Anniversary, family, solo, business
- **Romantic amenities**: Couples activities, fine dining, spas, wineries

## Future Features (Post-MVP)

### City Recommendation Engine
- Input: Home location, dates, preferences
- Output: Ranked list of cities matching criteria
- Comparison view: Side-by-side city stats
- Driving distance and time from home
- Availability checks for dates

### User Accounts
- Sign up/login
- Profile management
- Saved preferences
- Trip planning

### Reviews and Ratings
- User-generated reviews
- Photo uploads
- Rating system (1-5 stars)
- Helpful votes

### Itinerary Builder
- Drag-and-drop trip planning
- Multi-day itineraries
- Time optimization
- Export to calendar

### Social Features
- Share trips with friends
- Collaborative planning
- Public trip templates
- Follow other users

### Offline Mode
- Download town data
- Cached map tiles
- Offline POI access
- Sync when online

### Notifications
- New POIs in saved towns
- Price drops
- Event reminders
- Seasonal recommendations
