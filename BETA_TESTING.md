# Mr Wise Owl - Beta Testing Guide

## 🎯 Beta Testing Goals
- Validate core user flows
- Identify bugs and usability issues
- Gather feedback on AI recommendations
- Test mobile responsiveness
- Measure performance

## 🔗 Access
- **Frontend**: https://frontend-k4g28iok6-wise-owl-techs-projects.vercel.app
- **Backend API**: https://explicit-tiffani-wiseowltech-07037a1a.koyeb.app

## 📋 Test Scenarios

### 1. User Registration & Login
- [ ] Register new account with email/password
- [ ] Login with existing credentials
- [ ] Verify JWT token persists across page refreshes
- [ ] Test logout functionality

### 2. Town & POI Browsing
- [ ] View default town (Asheville, NC)
- [ ] Switch between towns (Asheville, Gatlinburg, Pigeon Forge, Boone)
- [ ] See POI count update when switching towns
- [ ] Verify map centers on selected town

### 3. Category Filtering
- [ ] Toggle individual categories on/off
- [ ] Verify POI markers update on map
- [ ] Test "Select All" / "Deselect All" behavior
- [ ] Check category counts are accurate

### 4. Map Interaction
- [ ] Click POI markers to see details
- [ ] Verify popup shows name, rating, price range
- [ ] Test "Get Directions" button
- [ ] Zoom in/out and pan around map
- [ ] Verify custom colored markers by category

### 5. Search Functionality
- [ ] Search for specific POI by name
- [ ] Verify filtered results on map
- [ ] Clear search and see all POIs return

### 6. Itinerary Builder
- [ ] Click map markers to add POIs to itinerary
- [ ] Verify POIs appear in right panel
- [ ] Remove POIs from itinerary
- [ ] Reorder itinerary items (if implemented)

### 7. Save/Load Itineraries
- [ ] Save itinerary with custom name
- [ ] Load saved itinerary from dropdown
- [ ] Verify POIs load correctly on map
- [ ] Delete saved itinerary

### 8. AI Chat
- [ ] Ask for campground recommendations
- [ ] Request brewery suggestions
- [ ] Ask about family-friendly activities
- [ ] Verify context-aware responses (mentions current town)
- [ ] Test multiple conversation turns

### 9. Mobile Responsiveness
- [ ] Test on phone (iOS/Android)
- [ ] Verify vertical layout stacking
- [ ] Check touch targets are 44px minimum
- [ ] Test map gestures (pinch zoom, pan)
- [ ] Verify no horizontal scrolling

### 10. Performance
- [ ] Measure initial page load time
- [ ] Test POI filtering speed
- [ ] Check map rendering performance
- [ ] Verify caching (second load faster)

## 🐛 Bug Report Template

**Title**: Brief description

**Steps to Reproduce**:
1. Step one
2. Step two
3. Step three

**Expected Behavior**: What should happen

**Actual Behavior**: What actually happened

**Environment**:
- Device: (iPhone 14, Desktop Chrome, etc.)
- Browser: (Safari, Chrome, Firefox)
- Screen Size: (375x667, 1920x1080)

**Screenshots**: (if applicable)

## 💬 Feedback Questions

1. **First Impression**: What was your initial reaction to the app?

2. **Ease of Use**: How intuitive was the interface? (1-5 scale)

3. **AI Quality**: Were the AI recommendations helpful? (1-5 scale)

4. **Features**: What features would you like to see added?

5. **Performance**: Did the app feel fast and responsive? (1-5 scale)

6. **Mobile Experience**: How was the mobile experience? (1-5 scale)

7. **Would You Use It?**: Would you use this for trip planning? (Yes/No/Maybe)

8. **Other Comments**: Any other feedback?

## 📊 Success Metrics

- [ ] 80%+ of testers complete registration
- [ ] 70%+ successfully save an itinerary
- [ ] 60%+ interact with AI chat
- [ ] Average usability rating 4+/5
- [ ] Zero critical bugs
- [ ] Page load under 3 seconds

## 🚀 Next Steps After Beta

1. Fix critical bugs
2. Implement top feature requests
3. Optimize performance issues
4. Polish UI based on feedback
5. Prepare for public launch

## 📧 Submit Feedback

Email: feedback@wiseowltech.net
Or create GitHub issue: https://github.com/wiseowltechnet/mrwiseowl/issues
