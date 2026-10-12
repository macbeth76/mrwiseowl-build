# MyConcierge - AI Chatbot Specification

## Purpose
AI-powered concierge assistant that helps users discover places, plan activities, and get personalized recommendations through natural conversation.

## Core Functionality

### Conversational Queries
Users can ask natural language questions:
- "Find me a hotel near downtown under $150"
- "What are the best hiking trails for families?"
- "Show me historic sites open on Sunday"
- "I want Italian restaurants with outdoor seating"
- "How do I get to the museum by subway?"
- "What's the nearest bus stop to my hotel?"
- "Show me all subway stations within walking distance"
- "I need a city with RV sites near breweries and historic sites"
- "Where can I see New Year's fireworks?"
- "Plan a 5-day trip from Garner NC with RV camping"

### AI Capabilities
- **Natural Language Understanding**: Parse user intent and extract filters
- **Context Awareness**: Remember conversation history
- **Personalized Recommendations**: Learn from user preferences
- **Multi-turn Conversations**: Handle follow-up questions
- **Map Integration**: Automatically show results on map

## Technical Implementation

### AI Provider Options

**Option 1: AWS Bedrock (Recommended)**
- Claude 3 Sonnet or Haiku
- Built-in AWS integration
- Pay-per-use pricing
- Low latency

**Option 2: OpenAI**
- GPT-4 Turbo or GPT-3.5
- Function calling for structured queries
- Streaming responses

**Option 3: Open Source**
- Llama 3 via AWS SageMaker
- Self-hosted option
- Lower cost at scale

### Architecture

```
User Message 
  → Frontend Chat UI
  → API: /api/chat
  → AI Service (Bedrock/OpenAI)
  → Function Calls (search POIs, filter, get details)
  → Database Query
  → Response + Map Update
```

### API Endpoints

```javascript
POST /api/chat
Body: {
  message: "Find hotels near beach",
  conversationId: "uuid",
  townId: 123
}
Response: {
  reply: "I found 5 hotels near the beach...",
  actions: {
    showPOIs: [poi_ids],
    updateFilters: { category: "hotels", amenities: ["beach"] },
    centerMap: { lat, lng, zoom }
  }
}
```

### Function Calling Schema

```json
{
  "functions": [
    {
      "name": "search_pois",
      "description": "Search for places",
      "parameters": {
        "category": "hotels|activity_outdoor|activity_indoor|campground_rv|campground_tent|historic",
        "priceRange": "1-4",
        "distance": "number in miles",
        "amenities": ["wifi", "parking", "pool", "full_hookup", "primitive", "trails", "kid_friendly"],
        "rating": "minimum rating 1-5"
      }
    },
    {
      "name": "get_poi_details",
      "description": "Get details about a specific place",
      "parameters": {
        "poiId": "number"
      }
    },
    {
      "name": "compare_pois",
      "description": "Compare multiple places",
      "parameters": {
        "poiIds": ["array of numbers"]
      }
    },
    {
      "name": "find_transit",
      "description": "Find nearby transit stops",
      "parameters": {
        "location": {"lat": "number", "lng": "number"},
        "types": ["subway", "bus", "train"],
        "radius": "miles"
      }
    },
    {
      "name": "get_transit_directions",
      "description": "Get transit directions between two points",
      "parameters": {
        "from": {"lat": "number", "lng": "number"},
        "to": {"lat": "number", "lng": "number"}
      }
    },
    {
      "name": "search_cities",
      "description": "Find cities matching trip criteria",
      "parameters": {
        "homeLocation": {"address": "string", "lat": "number", "lng": "number"},
        "dates": {"start": "date", "end": "date"},
        "maxDistance": "miles",
        "tripPurpose": "anniversary|family|solo|business|romantic_getaway",
        "requirements": {
          "rvFullHookup": "boolean",
          "nearDowntown": "boolean",
          "interests": ["array of strings"],
          "events": ["array of event types"],
          "romanticDining": "boolean",
          "couplesActivities": "boolean"
        }
      }
    },
    {
      "name": "get_events",
      "description": "Find events during date range",
      "parameters": {
        "townId": "number",
        "startDate": "date",
        "endDate": "date",
        "types": ["fireworks", "festival", "concert"]
      }
    },
    {
      "name": "create_trip_plan",
      "description": "Create multi-day itinerary",
      "parameters": {
        "townId": "number",
        "dates": {"start": "date", "end": "date"},
        "accommodation": "poi_id",
        "interests": ["array of categories"]
      }
    },
    {
      "name": "get_family_preferences",
      "description": "Get preferences for all family members",
      "parameters": {
        "familyGroupId": "number"
      }
    },
    {
      "name": "balance_family_activities",
      "description": "Create itinerary balancing all family member preferences",
      "parameters": {
        "familyGroupId": "number",
        "townId": "number",
        "dates": {"start": "date", "end": "date"}
      }
    }
  ]
}
```

## UI Components

### Chat Interface

**Desktop**: Floating chat bubble (bottom-right)
```
┌─────────────────────────┐
│ 💬 Ask me anything      │
├─────────────────────────┤
│                         │
│ [Chat messages]         │
│                         │
├─────────────────────────┤
│ Type your question...   │
└─────────────────────────┘
```

**Mobile**: Bottom sheet or full-screen modal

### Chat Bubble States
- **Collapsed**: Small circle with icon + notification badge
- **Expanded**: 400x600px chat window
- **Minimized**: Header bar only

### Message Types

**User Message**:
- Right-aligned
- Blue background
- User avatar

**AI Response**:
- Left-aligned
- White background
- Bot avatar
- Typing indicator while generating

**Action Cards**:
- POI preview cards (clickable)
- "Show on map" buttons
- Quick reply suggestions

### Example Conversation

```
User: "I need a family-friendly hotel with a pool"

AI: "I found 3 family-friendly hotels with pools:

    [Hotel Card: Seaside Resort]
    ⭐ 4.5 • $$ • 0.8 mi
    [View on Map]

    [Hotel Card: Family Inn]
    ⭐ 4.2 • $ • 1.2 mi
    [View on Map]

    Would you like to see more options or filter by price?"

User: "Show me the cheaper one"

AI: "Here's Family Inn:
    
    [Detailed Card]
    
    It's $89/night with free breakfast and a kids' pool.
    I've highlighted it on the map. Want directions?"

User: "How do I get there by subway?"

AI: "The nearest subway station is Union Station, 0.2 mi from Family Inn.
    
    🚇 Red Line - Downtown direction
    Next trains: 3 min, 8 min, 15 min
    
    From your location:
    1. Walk 0.3 mi to Central Station
    2. Take Red Line (Downtown) - 2 stops
    3. Exit at Union Station
    4. Walk 0.2 mi to hotel
    
    Total time: ~15 minutes
    Fare: $2.50
    
    [Show Route on Map] [Get Walking Directions]"
```

### Trip Planning Conversation Example

```
User: "I'm looking for a city with RV full hookup sites close to downtown that has breweries and historic sites. Planning 3-7 days with New Year's in the middle. Would love to see fireworks. Traveling from Garner NC. New Year's is my anniversary."

AI: "Congratulations on your anniversary! Let me find romantic cities perfect for your New Year's RV celebration from Garner, NC.

    I found 3 cities within 4 hours with romantic dining and anniversary activities:
    
    📍 Asheville, NC (3.5 hrs, 220 mi) ❤️ BEST FOR COUPLES
    ✓ 4 RV parks with full hookups near downtown
    ✓ 12 breweries + 8 wineries for tastings
    ✓ 8 historic sites (Biltmore Estate perfect for anniversary)
    ✓ New Year's Eve fireworks at Pack Square
    ✓ 15 romantic restaurants (fine dining, mountain views)
    ✓ Couples spa packages available
    Available: Dec 28 - Jan 4
    
    📍 Charleston, SC (4 hrs, 250 mi) ❤️ ROMANTIC WATERFRONT
    ✓ 3 RV parks with full hookups
    ✓ 8 breweries in historic district
    ✓ 15 historic sites + romantic carriage rides
    ✓ Waterfront fireworks at midnight
    ✓ 20+ upscale restaurants (seafood, rooftop dining)
    Available: Dec 29 - Jan 4
    
    📍 Savannah, GA (4.5 hrs, 280 mi) ❤️ HISTORIC CHARM
    ✓ 2 RV parks near downtown
    ✓ 6 breweries + ghost tours for couples
    ✓ 20+ historic sites (romantic squares)
    ✓ River Street fireworks show
    ✓ Intimate restaurants in historic homes
    Available: Dec 28 - Jan 3
    
    Which city interests you most?"

User: "Tell me more about Asheville"

AI: "Perfect choice for your anniversary! Here's your romantic Asheville trip plan:
    
    🚐 RV Camping (Dec 28 - Jan 4):
    [Card: Asheville Bear Creek RV Park]
    • 2.1 mi from downtown
    • Full hookups (50-amp available)
    • $65/night • ⭐ 4.6
    • Hot tub, WiFi, showers, quiet couples area
    [Check Availability] [Reserve]
    
    ❤️ Anniversary Highlights:
    • Biltmore Estate (Dec 29) - Romantic mansion tour + wine tasting
    • Couples spa day at Grove Park Inn
    • Sunset at Blue Ridge Parkway overlook
    
    🍺 Breweries & Wineries:
    • Wicked Weed Brewing (0.3 mi from downtown)
    • Biltmore Winery (on estate grounds)
    • Highland Brewing (1.8 mi)
    
    🍽️ Romantic Dining:
    • The Market Place (fine dining, anniversary specials)
    • Cúrate (Spanish tapas, intimate setting)
    • Posana (farm-to-table, wine pairings)
    
    🏛️ Historic Sites:
    • Biltmore Estate (8 mi) - America's largest home
    • Thomas Wolfe Memorial (downtown)
    • Grove Arcade (1920s shopping)
    
    🎆 New Year's Eve Anniversary:
    • Pack Square Park fireworks at midnight
    • Romantic dinner reservation at 7 PM
    • Champagne toast downtown
    • Live music 8 PM - 1 AM
    
    📅 Suggested Anniversary Itinerary:
    Day 1 (Dec 28): Arrive, settle in, romantic dinner downtown
    Day 2 (Dec 29): Biltmore Estate + wine tasting all day
    Day 3 (Dec 30): Couples spa morning, brewery tour afternoon
    Day 4 (Dec 31): 🎉 ANNIVERSARY - Fine dining + fireworks
    Day 5 (Jan 1): Relax at RV, scenic drive Blue Ridge Parkway
    Day 6-7: Historic downtown, more wineries, shopping
    
    💝 Anniversary Package Suggestions:
    • Biltmore + dinner package: $280/couple
    • Spa day for two: $350
    • Carriage ride downtown: $60
    
    Total estimated cost: $1,200 (RV + activities + romantic dining)
    
    Want me to make restaurant reservations or add more romantic activities?"
```

### Family Preferences Conversation Example

```
AI: "I see you're planning a family trip! Let me get everyone's preferences. 
    Who's traveling with you?"

User: "Me, my wife, and our two kids - 8 and 12 years old"

AI: "Great! Let me create profiles for your family.
    
    👨 What's your name?"

User: "John"

AI: "Perfect, John! What are you most interested in?
    - Breweries & craft beer
    - Historic sites
    - Outdoor activities
    - Museums
    - Sports
    - Other"

User: "Breweries and historic sites"

AI: "Got it! 
    
    👩 What's your wife's name and what does she enjoy?"

User: "Sarah. She loves museums, shopping, and good restaurants"

AI: "Excellent!
    
    👧 Tell me about your 8-year-old. What's their name and interests?"

User: "Emma. She loves playgrounds, animals, and ice cream"

AI: "Adorable! 
    
    👦 And your 12-year-old?"

User: "Jake. He's into video games, science museums, and outdoor adventures"

AI: "Perfect! I've created your family profile:
    
    👨 John (Adult)
    ✓ Breweries, historic sites
    
    👩 Sarah (Adult)  
    ✓ Museums, shopping, fine dining
    
    👧 Emma (8, Child)
    ✓ Playgrounds, animals, ice cream
    
    👦 Jake (12, Teen)
    ✓ Science, outdoor adventures, gaming
    
    Now let me find cities and create an itinerary that makes EVERYONE happy!
    
    For Asheville, here's a balanced plan:
    
    📅 Day 1 (Dec 28):
    Morning: Arrive & settle at RV park
    Afternoon: 👨👩 Downtown brewery tour WHILE 👧👦 Kids at RV park playground
    Evening: 🍕 Family pizza dinner (kid-friendly + craft beer)
    
    📅 Day 2 (Dec 29): BILTMORE DAY
    Morning: 👨👩 Biltmore mansion tour + 👧👦 Kids explore gardens & animals
    Afternoon: 👦 Jake: Antler Hill Village activities + 👧 Emma: Petting farm
    Evening: 👩 Sarah: Biltmore winery + 👨 John: Estate brewery
    
    📅 Day 3 (Dec 30): SPLIT ACTIVITIES
    Morning: 👩👧 Sarah & Emma: Downtown shopping + toy stores
    Morning: 👨👦 John & Jake: Historic sites + science museum
    Afternoon: 🏞️ ALL: Easy hiking trail (kid-friendly)
    Evening: 🍦 Emma's choice: Ice cream downtown
    
    📅 Day 4 (Dec 31): ANNIVERSARY + FAMILY
    Morning: 👧👦 Kids: Arcade & game center
    Afternoon: 👨👩 Parents: Couples spa (kids at RV with activities)
    Evening: 🎆 ALL: Family dinner + fireworks (kid-friendly viewing area)
    
    📅 Day 5 (Jan 1): RECOVERY DAY
    Morning: 😴 Sleep in, relax at RV
    Afternoon: 👦 Jake's choice: Outdoor adventure park
    Evening: 👩 Sarah's choice: Nice restaurant
    
    📅 Day 6 (Jan 2): SCIENCE & NATURE
    Morning: 👦 Jake: Asheville Museum of Science
    Afternoon: 👧 Emma: WNC Nature Center (animals!)
    Evening: 👨 John: Final brewery visit
    
    Each day balances:
    ✓ Adult activities (breweries, fine dining, spa)
    ✓ Kid activities (playgrounds, animals, ice cream)
    ✓ Teen activities (science, adventures)
    ✓ Family time together
    ✓ Downtime for everyone
    
    Everyone gets their must-haves! Sound good?"

User: "Perfect! But Emma needs gluten-free options"

AI: "Updated Emma's profile with gluten-free dietary needs!
    
    I've adjusted restaurant recommendations:
    ✓ All suggested restaurants have gluten-free menus
    ✓ Marked ice cream shops with GF options
    ✓ RV park has GF-friendly grocery nearby
    
    Emma will have plenty of safe, delicious options! 🍦"
```

## Features

### Smart Suggestions
- Quick reply buttons: "Show more", "Change filters", "Get directions"
- Suggested questions based on context
- Popular queries for the town

### Context Retention
- Remember user preferences in session
- Reference previous queries: "Show me more like that"
- Multi-step planning: "Add that to my itinerary"

### Proactive Assistance
- "You're viewing hotels. Want activity recommendations nearby?"
- "This campground is popular. Book soon!"
- "Historic site has guided tours at 2 PM today"

### Voice Input (Optional)
- Microphone button
- Speech-to-text via Web Speech API
- Voice responses (text-to-speech)

## Database Schema

```sql
CREATE TABLE conversations (
  id UUID PRIMARY KEY,
  user_id INTEGER REFERENCES users(id),
  town_id INTEGER REFERENCES towns(id),
  created_at TIMESTAMP DEFAULT NOW(),
  updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE messages (
  id SERIAL PRIMARY KEY,
  conversation_id UUID REFERENCES conversations(id),
  role VARCHAR(20), -- 'user' or 'assistant'
  content TEXT,
  metadata JSONB, -- function calls, POI references
  created_at TIMESTAMP DEFAULT NOW()
);
```

## Prompt Engineering

### System Prompt
```
You are a helpful local concierge assistant for [Town Name]. 
Help users discover hotels, activities, campgrounds, and historic sites.

Guidelines:
- Be friendly and conversational
- Ask clarifying questions when needed
- Provide 3-5 recommendations at a time
- Include relevant details (price, rating, distance)
- Use the available functions to search and retrieve data
- Always offer to show results on the map

Current context:
- Town: {townName}
- User location: {userLocation}
- Active layers: {activeLayers}
```

### Example Function Call
```json
{
  "role": "assistant",
  "content": null,
  "function_call": {
    "name": "search_pois",
    "arguments": {
      "category": "hotels",
      "priceRange": [1, 2],
      "amenities": ["pool"],
      "rating": 4.0
    }
  }
}
```

## Performance

### Response Time
- AI response: < 2 seconds
- Streaming: Show tokens as they arrive
- Function execution: < 500ms

### Caching
- Cache common queries per town
- Store conversation context in Redis
- Expire after 1 hour of inactivity

### Rate Limiting
- 20 messages per minute per user
- 100 messages per day for free users
- Unlimited for premium users

## Cost Optimization

### Token Management
- Limit conversation history to last 10 messages
- Summarize older context
- Use cheaper models for simple queries

### Smart Routing
- Simple queries → GPT-3.5 or Claude Haiku
- Complex queries → GPT-4 or Claude Sonnet
- Keyword detection before AI call

## Privacy & Safety

### Content Filtering
- Block inappropriate queries
- No personal data in prompts
- Sanitize user input

### Data Retention
- Store conversations for 30 days
- Allow users to delete history
- Anonymize data for training

## Integration with Existing Features

### Map Sync
- AI responses automatically update map
- Highlight mentioned POIs
- Adjust zoom/center based on results

### Filter Sync
- AI-applied filters update UI controls
- Users can manually adjust AI suggestions
- Bidirectional sync

### Saved Locations
- "Save this hotel" → Adds to saved locations
- "Show my saved places" → Lists saved POIs

## Mobile Optimizations

### Chat UI
- Full-screen on mobile
- Swipe down to minimize
- Voice input prominent

### Reduced Context
- Shorter AI responses on mobile
- More action buttons, less text
- Optimized for thumb navigation

## Future Enhancements

### Advanced Features
- Image recognition: "Find places like this photo"
- Itinerary generation: "Plan my weekend"
- Budget planning: "What can I do with $200?"
- Group planning: "Best for 4 adults and 2 kids"

### Integrations
- Calendar integration for bookings
- Weather-aware recommendations
- Real-time availability checking
- Direct booking through chat

### Personalization
- Learn user preferences over time
- Recommend based on past visits
- Seasonal suggestions
- Local events integration
