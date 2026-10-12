# MyConcierge - UI/UX Specification

## Design Principles
- **Mobile-first**: Optimize for mobile devices, enhance for desktop
- **Clarity**: Clear visual hierarchy and intuitive controls
- **Speed**: Fast loading, smooth interactions
- **Accessibility**: WCAG 2.1 AA compliant

## Layout Structure

### Desktop Layout (1024px+)
```
┌─────────────────────────────────────────────┐
│  Header: Logo | Search Bar | User Menu      │
├──────────────┬──────────────────────────────┤
│              │                              │
│  Sidebar     │      Map Canvas              │
│  - Layers    │                              │
│  - Filters   │                              │
│  - List View │                              │
│              │                              │
│              │                              │
└──────────────┴──────────────────────────────┘
```

### Mobile Layout (< 768px)
```
┌─────────────────────┐
│  Header             │
│  Search Bar         │
├─────────────────────┤
│                     │
│   Map Canvas        │
│   (Full Screen)     │
│                     │
├─────────────────────┤
│  Bottom Sheet       │
│  - Layer Toggles    │
│  - Filters          │
└─────────────────────┘
```

## Components

### 1. AI Chat Interface

**Desktop**: Floating bubble (bottom-right, 400x600px expanded)
```
┌─────────────────────────┐
│ 💬 Concierge Chat   [×] │
├─────────────────────────┤
│ AI: How can I help?     │
│                         │
│     User: Find hotels   │
│                         │
│ AI: I found 5 hotels... │
│ [Hotel Card Preview]    │
│ [Show on Map]           │
│                         │
├─────────────────────────┤
│ Type message...      🎤 │
└─────────────────────────┘
```

**Mobile**: Bottom sheet or full-screen modal

**States**:
- Collapsed: Circle icon with notification badge
- Expanded: Full chat window
- Typing indicator: Animated dots

**Message Styling**:
- User: Right-aligned, blue bubble
- AI: Left-aligned, white bubble with shadow
- Action cards: Clickable POI previews
- Quick replies: Pill-shaped buttons

### 2. Header
**Desktop**:
- Logo (left, 40px height)
- Search bar (center, 600px max width)
- User menu (right, avatar + dropdown)

**Mobile**:
- Hamburger menu (left)
- Logo (center, 32px height)
- User icon (right)

**Search Bar**:
- Placeholder: "Search for a town..."
- Autocomplete dropdown
- Search icon (left)
- Clear button (right, when text entered)
- Loading spinner during search

### 2. Layer Control Panel

**Desktop Sidebar** (280px width):
```
┌─────────────────────┐
│  Layers             │
│  ☑ Hotels (45)      │
│  ☑ Activities (32)  │
│    ☑ Outdoor (20)   │
│    ☑ Indoor (12)    │
│  ☑ Campgrounds (8)  │
│    ☑ RV Sites (5)   │
│    ☑ Tent Sites (3) │
│  ☑ Historic (12)    │
│  ☑ Transit (156)    │
│    ☑ Subway (24)    │
│    ☑ Bus Stops (120)│
│    ☑ Train (12)     │
│                     │
│  [Show All] [Hide]  │
│                     │
│  Filters ▼          │
│  Price: $-$$$$      │
│  Distance: 5 mi     │
│  Rating: 4+ ★       │
│  [Apply Filters]    │
└─────────────────────┘
```

**Mobile Bottom Sheet**:
- Swipe up to expand
- Collapsed: Show active layer count
- Expanded: Full layer controls + filters

**Layer Toggle Design**:
- Checkbox with custom styling
- Layer icon (24x24px)
- Layer name + count
- Color-coded by category:
  - Hotels: Blue (#2196F3)
  - Activities: Green (#4CAF50)
  - Campgrounds: Orange (#FF9800)
  - Historic: Purple (#9C27B0)

### 3. Map Canvas

**Map Controls** (bottom-right):
- Zoom in button (+)
- Zoom out button (-)
- Reset view button (home icon)
- Geolocation button (crosshair icon)
- Fullscreen toggle (desktop only)

**Markers**:
- Custom icons per category
- Cluster markers when > 10 POIs in view
- Hover effect: Scale 1.2x + shadow
- Active marker: Pulsing animation

**Marker Clustering**:
- Circle with count number
- Color matches layer color
- Click to zoom into cluster

### 4. POI Detail Panel

**Desktop**: Slide-in panel from right (400px width)
**Mobile**: Bottom sheet (full width)

**Content Structure**:
```
┌─────────────────────────────┐
│  [X Close]                  │
│                             │
│  Photo Carousel             │
│  ← [Image] →                │
│                             │
│  Restaurant Name            │
│  ★★★★☆ 4.2 (128 reviews)   │
│  $$ • 0.5 mi away           │
│  Italian • Casual Dining    │
│                             │
│  Dietary Options:           │
│  🌾 Gluten-free menu        │
│  🌱 Vegan options           │
│  🌿 Vegetarian friendly     │
│  🥜 Nut-free available      │
│                             │
│  📍 123 Main St             │
│  📞 (555) 123-4567          │
│  🌐 website.com             │
│  🕐 Open until 10 PM        │
│                             │
│  Description text...        │
│                             │
│  Amenities:                 │
│  ✓ WiFi  ✓ Parking  ✓ Pool │
│                             │
│  [Get Directions] [Save]    │
└─────────────────────────────┘
```

### 4b. Transit Stop Detail Panel

**Content Structure**:
```
┌─────────────────────────────┐
│  [X Close]                  │
│                             │
│  🚇 Union Station           │
│  Subway • 0.3 mi away       │
│                             │
│  Lines:                     │
│  🔴 Red Line                │
│  🔵 Blue Line               │
│                             │
│  Next Arrivals:             │
│  🔴 Downtown - 3 min        │
│  🔴 Uptown - 7 min          │
│  🔵 East - 5 min            │
│                             │
│  Accessibility:             │
│  ✓ Elevator  ✓ Ramps        │
│                             │
│  [Get Directions] [Routes]  │
└─────────────────────────────┘
```

### 5. List View Toggle

**Button**: "List View" / "Map View" toggle (top of sidebar)

**List View Layout**:
- Card-based design
- Each card shows:
  - Thumbnail image (80x80px)
  - Name and category
  - Rating and price
  - Distance
  - Quick actions (save, directions)
- Sort dropdown: Distance, Rating, Price
- Pagination: 20 items per page

### 6. Family Profile Manager

**Desktop**: Modal or dedicated page
**Mobile**: Full-screen view

**Family Setup**:
```
┌─────────────────────────────┐
│  Family Profile Setup       │
├─────────────────────────────┤
│  Family Name: [Smith Family]│
│                             │
│  Members:                   │
│  👨 John (Husband)          │
│     • Breweries             │
│     • Historic sites        │
│     [Edit]                  │
│                             │
│  👩 Sarah (Wife)            │
│     • Museums               │
│     • Shopping              │
│     • Fine dining           │
│     🌱 Vegan                │
│     [Edit]                  │
│                             │
│  👧 Emma (8, Child)         │
│     • Playgrounds           │
│     • Animals               │
│     • Ice cream             │
│     🌾 Gluten-free          │
│     [Edit]                  │
│                             │
│  👦 Jake (12, Teen)         │
│     • Science               │
│     • Outdoor adventures    │
│     🥜 Nut allergy          │
│     [Edit]                  │
│                             │
│  [+ Add Family Member]      │
└─────────────────────────────┘
```

**Member Preferences Form**:
```
┌─────────────────────────────┐
│  Edit: Emma's Preferences   │
├─────────────────────────────┤
│  Age Range: [Child 6-12 ▼]  │
│                             │
│  Interests (select all):    │
│  ☑ Playgrounds              │
│  ☑ Animals/Zoos             │
│  ☐ Museums                  │
│  ☑ Ice cream shops          │
│  ☐ Water activities         │
│                             │
│  Dietary Needs:             │
│  ☑ 🌾 Gluten-free           │
│  ☐ 🌱 Vegan                 │
│  ☐ 🌿 Vegetarian            │
│  ☐ 🥛 Dairy-free            │
│  ☐ 🥜 Nut allergies         │
│  ☐ 🦐 Shellfish allergies   │
│  ☐ 🍖 Halal                 │
│  ☐ ✡️ Kosher                │
│                             │
│  Activity Level:            │
│  ○ High  ● Moderate  ○ Low  │
│                             │
│  Mobility:                  │
│  ☐ Wheelchair accessible    │
│  ☑ Stroller-friendly        │
│                             │
│  [Save] [Cancel]            │
└─────────────────────────────┘
```

### 7. Filter Panel

**Collapsible Section** in sidebar:

**Price Range**:
- Slider with 4 stops ($, $$, $$$, $$$$)
- Visual indicator of selected range

**Distance**:
- Slider: 1-50 miles
- Current value display

**Rating**:
- Star selector: Any, 3+, 4+, 4.5+
- Visual star icons

**Open Now**:
- Toggle switch

**Dietary Restrictions** (for restaurants):
- 🌾 Gluten-free menu
- 🌱 Vegan options
- 🌿 Vegetarian friendly
- 🥛 Dairy-free options
- 🥜 Nut-free available
- 🦐 Shellfish-free
- 🍖 Halal
- ✡️ Kosher

**Amenities** (category-specific):
- Hotels: WiFi, Parking, Pool, Hot tub, Breakfast, Gym, Spa
- Activities:
  - Outdoor: Trails, Picnic areas, Bike rentals, Water access
  - Indoor: Air conditioning, Wheelchair accessible, Parking, Gift shop
  - Shared: Kid-friendly, Guided tours, Restrooms, Food available
- Campgrounds:
  - RV: Full hookup, Partial hookup, Pull-through, 50-amp
  - Tent: Primitive, Developed, Backcountry
  - Shared: Showers, Restrooms, WiFi, Firewood, Dump station, Hot tub
- Historic: Guided tours, Museum, Free entry

**Buttons**:
- "Apply Filters" (primary button)
- "Clear All" (text button)

## Color Palette

### Primary Colors
- Primary: #1976D2 (Blue)
- Secondary: #424242 (Dark Gray)
- Accent: #FF5722 (Orange)

### Layer Colors
- Hotels: #2196F3
- Activities: #4CAF50
- Campgrounds: #FF9800
- Historic: #9C27B0
- Transit: #F44336 (Red)
  - Subway: #E91E63 (Pink)
  - Bus: #FF5722 (Deep Orange)
  - Train: #795548 (Brown)

### Semantic Colors
- Success: #4CAF50
- Warning: #FFC107
- Error: #F44336
- Info: #2196F3

### Dietary Badge Colors
- 🌾 Gluten-free: #FFA726 (Orange)
- 🌱 Vegan: #66BB6A (Green)
- 🌿 Vegetarian: #81C784 (Light Green)
- 🥛 Dairy-free: #42A5F5 (Blue)
- 🥜 Nut-free: #8D6E63 (Brown)
- 🦐 Shellfish-free: #26C6DA (Cyan)
- 🍖 Halal: #7E57C2 (Purple)
- ✡️ Kosher: #5C6BC0 (Indigo)

### Neutral Colors
- Background: #FAFAFA
- Surface: #FFFFFF
- Border: #E0E0E0
- Text Primary: #212121
- Text Secondary: #757575

## Typography

### Font Family
- Primary: Inter, system-ui, sans-serif
- Monospace: 'Roboto Mono', monospace

### Scale
- H1: 2.5rem (40px) - Page title
- H2: 2rem (32px) - Section headers
- H3: 1.5rem (24px) - POI names
- H4: 1.25rem (20px) - Subsections
- Body: 1rem (16px) - Main text
- Small: 0.875rem (14px) - Metadata
- Tiny: 0.75rem (12px) - Labels

### Font Weights
- Regular: 400
- Medium: 500
- Semibold: 600
- Bold: 700

## Spacing System
Base unit: 8px
- xs: 4px
- sm: 8px
- md: 16px
- lg: 24px
- xl: 32px
- 2xl: 48px

## Responsive Breakpoints
- Mobile: 320px - 767px
- Tablet: 768px - 1023px
- Desktop: 1024px+

## Interactive States

### Buttons
- **Default**: Solid background, white text
- **Hover**: Darken 10%, cursor pointer
- **Active**: Darken 20%, scale 0.98
- **Disabled**: Gray background, cursor not-allowed
- **Focus**: 2px blue outline

### Inputs
- **Default**: Gray border, white background
- **Focus**: Blue border, no outline
- **Error**: Red border, error message below
- **Disabled**: Gray background, cursor not-allowed

### Cards
- **Default**: White background, subtle shadow
- **Hover**: Elevated shadow, scale 1.02
- **Active**: Blue border

### Dietary Badges
- **Style**: Pill-shaped, 6px border-radius
- **Size**: Small (20px height), Medium (24px height)
- **Content**: Emoji + text label
- **Spacing**: 4px margin between badges
- **Display**: Inline-flex, wrap on overflow

**Badge Examples**:
```
🌾 Gluten-free    🌱 Vegan    🥜 Nut-free
```

**Usage**:
- Restaurant cards: Show 2-3 most relevant badges
- Detail panels: Show all applicable badges
- Family profiles: Show member dietary needs
- Filter panel: Checkboxes with badge styling

## Loading States

### Map Loading
- Skeleton screen with placeholder map
- Loading spinner in center
- "Loading map..." text

### POI Loading
- Skeleton cards in list view
- Pulsing marker placeholders on map

### Search Loading
- Spinner in search bar
- Disabled input during search

## Error States

### No Results
- Empty state illustration
- "No results found" message
- Suggestions: "Try adjusting filters"

### API Error
- Toast notification (top-right)
- Error icon + message
- "Retry" button

### Map Load Error
- Error message overlay on map
- "Reload Map" button

## Accessibility Requirements

### Keyboard Navigation
- Tab through all interactive elements
- Enter to activate buttons/links
- Escape to close modals/panels
- Arrow keys for map navigation

### Screen Reader Support
- All images have alt text
- ARIA labels on icons
- ARIA live regions for dynamic content
- Semantic HTML (nav, main, aside)

### Focus Indicators
- 2px solid blue outline
- Visible on all interactive elements
- Never remove outline without alternative

### Color Contrast
- Text: 4.5:1 minimum
- Large text: 3:1 minimum
- Interactive elements: 3:1 minimum

### Touch Targets
- Minimum 44x44px
- Adequate spacing between targets
- No overlapping interactive elements

## Animations

### Transitions
- Duration: 200-300ms
- Easing: ease-in-out
- Properties: transform, opacity

### Map Animations
- Smooth zoom: 500ms
- Pan: 300ms
- Marker appear: Fade in 200ms

### Panel Animations
- Slide in: 300ms
- Fade: 200ms

### Micro-interactions
- Button press: Scale 0.98
- Checkbox toggle: 150ms
- Hover effects: 200ms

## Performance Optimization

### Image Optimization
- Lazy load images
- WebP format with fallback
- Responsive images (srcset)
- Thumbnail sizes: 80x80, 400x300

### Map Performance
- Render max 1000 markers
- Use clustering for dense areas
- Debounce zoom/pan events (100ms)
- Cache map tiles

### Code Splitting
- Route-based splitting
- Lazy load heavy components
- Defer non-critical JS

## Mobile-Specific Considerations

### Touch Gestures
- Pinch to zoom on map
- Swipe to close panels
- Pull to refresh (optional)
- Long press for context menu

### Mobile Optimizations
- Reduce marker density
- Larger touch targets
- Simplified filters
- Bottom sheet UI pattern

### Offline Handling
- Show offline indicator
- Cache last viewed town
- Graceful degradation
