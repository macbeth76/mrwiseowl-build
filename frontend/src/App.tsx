import { useEffect, useState } from 'react';
import axios from 'axios';
import Map from './components/Map';
import Auth from './components/Auth';
import 'leaflet/dist/leaflet.css';
import './App.css';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

interface Town {
  id: number;
  name: string;
  state: string;
  latitude: number;
  longitude: number;
}

interface Poi {
  id: number;
  name: string;
  category: string;
  latitude: number;
  longitude: number;
  description?: string;
  rating?: number;
  priceRange?: number;
  amenities?: string[];
}

const CATEGORIES = [
  { id: 'hotel', label: '🏨 Hotels', color: '#e74c3c' },
  { id: 'activity_outdoor', label: '🏔️ Outdoor', color: '#27ae60' },
  { id: 'activity_indoor', label: '🎭 Indoor', color: '#3498db' },
  { id: 'campground_rv', label: '🚐 RV Parks', color: '#f39c12' },
  { id: 'campground_tent', label: '⛺ Camping', color: '#16a085' },
  { id: 'historic', label: '🏛️ Historic', color: '#8e44ad' },
  { id: 'brewery', label: '🍺 Breweries', color: '#d35400' },
  { id: 'restaurant', label: '🍽️ Dining', color: '#c0392b' },
];

function App() {
  const [token, setToken] = useState<string | null>(localStorage.getItem('token'));
  const [towns, setTowns] = useState<Town[]>([]);
  const [selectedTown, setSelectedTown] = useState<Town | null>(null);
  const [allPois, setAllPois] = useState<Poi[]>([]);
  const [filteredPois, setFilteredPois] = useState<Poi[]>([]);
  const [selectedCategories, setSelectedCategories] = useState<string[]>(
    CATEGORIES.map(c => c.id)
  );
  const [searchQuery, setSearchQuery] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [itinerary, setItinerary] = useState<Poi[]>([]);
  const [savedItineraries, setSavedItineraries] = useState<any[]>([]);
  const [showSaveDialog, setShowSaveDialog] = useState(false);
  const [itineraryName, setItineraryName] = useState('');
  const [chatMessages, setChatMessages] = useState<Array<{role: string, text: string}>>([
    { role: 'assistant', text: '👋 Hi! I\'m Mr Wise Owl. Ask me about places to visit!' }
  ]);
  const [chatInput, setChatInput] = useState('');

  useEffect(() => {
    loadTowns();
  }, []);

  useEffect(() => {
    if (selectedTown) {
      loadPois(selectedTown.id);
    }
  }, [selectedTown]);

  useEffect(() => {
    filterPois();
  }, [allPois, selectedCategories, searchQuery]);

  const loadTowns = async () => {
    try {
      const res = await axios.get(`${API_URL}/api/towns/search`);
      setTowns(res.data);
      if (res.data.length > 0) {
        setSelectedTown(res.data[0]);
      }
    } catch (error: any) {
      console.error('Error loading towns:', error);
      setError(error.message || 'Failed to load towns');
    } finally {
      setLoading(false);
    }
  };

  const loadPois = async (townId: number) => {
    try {
      const res = await axios.get(`${API_URL}/api/pois?townId=${townId}`);
      setAllPois(res.data);
    } catch (error: any) {
      console.error('Error loading POIs:', error);
    }
  };

  const filterPois = () => {
    let filtered = allPois.filter(poi => 
      selectedCategories.includes(poi.category)
    );

    if (searchQuery) {
      filtered = filtered.filter(poi =>
        poi.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        poi.description?.toLowerCase().includes(searchQuery.toLowerCase())
      );
    }

    setFilteredPois(filtered);
  };

  const toggleCategory = (categoryId: string) => {
    setSelectedCategories(prev =>
      prev.includes(categoryId)
        ? prev.filter(c => c !== categoryId)
        : [...prev, categoryId]
    );
  };

  const addToItinerary = (poi: Poi) => {
    if (!itinerary.find(p => p.id === poi.id)) {
      setItinerary([...itinerary, poi]);
    }
  };

  const removeFromItinerary = (poiId: number) => {
    setItinerary(itinerary.filter(p => p.id !== poiId));
  };

  const saveItinerary = async () => {
    if (!itineraryName.trim() || itinerary.length === 0) return;
    
    try {
      await axios.post(`${API_URL}/api/itineraries`, {
        name: itineraryName,
        townId: selectedTown?.id,
        poiIds: itinerary.map(p => p.id)
      }, {
        headers: { Authorization: `Bearer ${token}` }
      });
      
      setShowSaveDialog(false);
      setItineraryName('');
      loadSavedItineraries();
    } catch (error) {
      console.error('Error saving itinerary:', error);
    }
  };

  const loadSavedItineraries = async () => {
    try {
      const res = await axios.get(`${API_URL}/api/itineraries`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setSavedItineraries(res.data);
    } catch (error) {
      console.error('Error loading itineraries:', error);
    }
  };

  useEffect(() => {
    if (token) {
      loadSavedItineraries();
    }
  }, [token]);

  const sendChatMessage = async () => {
    if (!chatInput.trim()) return;
    
    const userMessage = chatInput;
    setChatInput('');
    setChatMessages([...chatMessages, { role: 'user', text: userMessage }]);
    
    try {
      const context = `Current town: ${selectedTown?.name}, ${selectedTown?.state}. ${filteredPois.length} places available. Categories: ${selectedCategories.join(', ')}`;
      
      const response = await axios.post(`${API_URL}/api/chat`, {
        message: userMessage,
        context: context
      });
      
      setChatMessages(prev => [...prev, 
        { role: 'assistant', text: response.data.response }
      ]);
    } catch (error) {
      console.error('Chat error:', error);
      setChatMessages(prev => [...prev, 
        { role: 'assistant', text: 'Sorry, I had trouble responding. Please try again!' }
      ]);
    }
  };

  const handleLogin = (newToken: string) => {
    setToken(newToken);
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    setToken(null);
  };

  if (!token) {
    return <Auth onLogin={handleLogin} />;
  }

  if (loading) {
    return (
      <div style={{ 
        display: 'flex', 
        justifyContent: 'center', 
        alignItems: 'center', 
        height: '100vh',
        width: '100vw',
        position: 'fixed',
        top: 0,
        left: 0,
        flexDirection: 'column',
        gap: '1rem',
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        color: 'white'
      }}>
        <div style={{
          width: '60px',
          height: '60px',
          border: '6px solid rgba(255,255,255,0.3)',
          borderTop: '6px solid white',
          borderRadius: '50%',
          animation: 'spin 1s linear infinite'
        }}></div>
        <h2 style={{ margin: 0 }}>🦉 Mr Wise Owl</h2>
        <p style={{ margin: 0 }}>Loading your adventure...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div style={{ 
        display: 'flex', 
        justifyContent: 'center', 
        alignItems: 'center', 
        height: '100vh',
        flexDirection: 'column'
      }}>
        <h2>🦉 Mr Wise Owl</h2>
        <p style={{ color: 'red' }}>Error: {error}</p>
        <button onClick={loadTowns} style={{
          padding: '0.5rem 1rem',
          cursor: 'pointer',
          background: '#667eea',
          color: 'white',
          border: 'none',
          borderRadius: '4px'
        }}>Retry</button>
      </div>
    );
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', height: '100vh', overflow: 'hidden' }}>
      {/* Top Bar */}
      <div style={{
        height: '60px',
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        color: 'white',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        padding: '0 1.5rem',
        boxShadow: '0 2px 10px rgba(0,0,0,0.1)',
        zIndex: 1000
      }}>
        <h1 style={{ margin: 0, fontSize: window.innerWidth < 768 ? '1.2rem' : '1.5rem', fontWeight: 'bold' }}>
          🦉 Mr Wise Owl
        </h1>
        <div style={{ display: 'flex', gap: window.innerWidth < 768 ? '0.5rem' : '1rem', alignItems: 'center', fontSize: window.innerWidth < 768 ? '0.85rem' : '1rem' }}>
          <span>{selectedTown?.name}, {selectedTown?.state}</span>
          <span style={{ 
            background: 'rgba(255,255,255,0.2)',
            padding: '0.5rem 1rem',
            borderRadius: '20px'
          }}>
            {filteredPois.length} places
          </span>
          <button
            onClick={handleLogout}
            style={{
              padding: '0.5rem 1rem',
              borderRadius: '8px',
              border: 'none',
              background: 'rgba(255,255,255,0.2)',
              color: 'white',
              cursor: 'pointer',
              fontWeight: 'bold'
            }}
          >
            Logout
          </button>
        </div>
      </div>

      {/* Main Content */}
      <div style={{ display: 'flex', flex: 1, overflow: 'hidden', flexDirection: window.innerWidth < 768 ? 'column' : 'row' }}>
      {/* Left Sidebar - Filters */}
      <div style={{
        width: window.innerWidth < 768 ? '100%' : '280px',
        height: window.innerWidth < 768 ? 'auto' : 'auto',
        maxHeight: window.innerWidth < 768 ? '40vh' : 'none',
        background: 'linear-gradient(180deg, #667eea 0%, #764ba2 100%)',
        color: 'white',
        padding: '1rem',
        overflowY: 'auto',
        boxShadow: '2px 0 10px rgba(0,0,0,0.1)'
      }}>
        <div style={{ marginBottom: '1rem' }}>
          <label style={{ fontSize: '0.9rem', marginBottom: '0.5rem', display: 'block' }}>
            Select Town
          </label>
          <select 
            value={selectedTown?.id || ''} 
            onChange={(e) => {
              const town = towns.find(t => t.id === Number(e.target.value));
              setSelectedTown(town || null);
            }}
            style={{
              width: '100%',
              padding: '0.5rem',
              borderRadius: '4px',
              border: 'none'
            }}
          >
            {towns.map(town => (
              <option key={town.id} value={town.id}>
                {town.name}, {town.state}
              </option>
            ))}
          </select>
        </div>

        <div style={{ marginBottom: '1rem' }}>
          <input
            type="text"
            placeholder="🔍 Search places..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            style={{
              width: '100%',
              padding: '0.5rem',
              borderRadius: '4px',
              border: 'none'
            }}
          />
        </div>

        <div style={{ marginBottom: '0.5rem', fontSize: '0.9rem' }}>
          <strong>Filters ({filteredPois.length} places)</strong>
        </div>

        {CATEGORIES.map(cat => (
          <button
            key={cat.id}
            onClick={() => toggleCategory(cat.id)}
            style={{
              width: '100%',
              padding: window.innerWidth < 768 ? '0.6rem' : '0.75rem',
              marginBottom: '0.5rem',
              borderRadius: '8px',
              border: 'none',
              cursor: 'pointer',
              background: selectedCategories.includes(cat.id) 
                ? 'white' 
                : 'rgba(255,255,255,0.2)',
              color: selectedCategories.includes(cat.id) ? cat.color : 'white',
              fontWeight: selectedCategories.includes(cat.id) ? 'bold' : 'normal',
              textAlign: 'left',
              transition: 'all 0.2s',
              fontSize: window.innerWidth < 768 ? '0.9rem' : '1rem',
              minHeight: '44px'
            }}
          >
            {cat.label}
          </button>
        ))}
      </div>

      {/* Main Content Area */}
      <div style={{ flex: 1, display: 'flex', flexDirection: 'column', minHeight: window.innerWidth < 768 ? '50vh' : 'auto' }}>
        {/* Map */}
        <div style={{ flex: 1, position: 'relative' }}>
          {selectedTown && (
            <Map 
              center={[selectedTown.latitude, selectedTown.longitude]} 
              pois={filteredPois}
              onPoiClick={addToItinerary}
            />
          )}
        </div>
      </div>

      {/* Right Panel - Itinerary and Chat */}
      <div style={{
        width: window.innerWidth < 768 ? '100%' : '320px',
        height: window.innerWidth < 768 ? 'auto' : 'auto',
        background: 'white',
        borderLeft: window.innerWidth < 768 ? 'none' : '2px solid #dee2e6',
        borderTop: window.innerWidth < 768 ? '2px solid #dee2e6' : 'none',
        display: 'flex',
        flexDirection: 'column'
      }}>
        {/* Itinerary Section */}
        <div style={{
          padding: '1rem',
          borderBottom: '2px solid #dee2e6',
          background: '#f8f9fa'
        }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.5rem' }}>
            <div>
              <h3 style={{ margin: 0 }}>📋 My Itinerary</h3>
              <p style={{ margin: '0.5rem 0 0 0', fontSize: '0.9rem', color: '#666' }}>
                {itinerary.length} places added
              </p>
            </div>
            {itinerary.length > 0 && (
              <button
                onClick={() => setShowSaveDialog(true)}
                style={{
                  padding: '0.5rem 1rem',
                  borderRadius: '8px',
                  border: 'none',
                  background: '#27ae60',
                  color: 'white',
                  cursor: 'pointer',
                  fontWeight: 'bold',
                  fontSize: '0.9rem'
                }}
              >
                💾 Save
              </button>
            )}
          </div>
          
          {savedItineraries.length > 0 && (
            <select
              onChange={(e) => {
                const itinerary = savedItineraries.find(i => i.id === Number(e.target.value));
                if (itinerary) {
                  const pois = allPois.filter(p => itinerary.poiIds.includes(p.id));
                  setItinerary(pois);
                }
              }}
              style={{
                width: '100%',
                padding: '0.5rem',
                borderRadius: '6px',
                border: '1px solid #dee2e6',
                fontSize: '0.9rem'
              }}
            >
              <option value="">Load saved itinerary...</option>
              {savedItineraries.map(it => (
                <option key={it.id} value={it.id}>{it.name}</option>
              ))}
            </select>
          )}
        </div>
        
        <div style={{
          flex: 1,
          overflowY: 'auto',
          padding: '1rem'
        }}>
          {itinerary.length === 0 ? (
            <p style={{ color: '#999', textAlign: 'center', marginTop: '2rem' }}>
              Click on map markers to add places to your itinerary
            </p>
          ) : (
            itinerary.map((poi, idx) => (
              <div key={poi.id} style={{
                padding: '0.75rem',
                marginBottom: '0.75rem',
                background: '#f8f9fa',
                borderRadius: '8px',
                border: '1px solid #dee2e6'
              }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start' }}>
                  <div style={{ flex: 1 }}>
                    <div style={{ fontWeight: 'bold', marginBottom: '0.25rem' }}>
                      {idx + 1}. {poi.name}
                    </div>
                    <div style={{ fontSize: '0.85rem', color: '#666' }}>
                      {CATEGORIES.find(c => c.id === poi.category)?.label}
                    </div>
                    {poi.rating && (
                      <div style={{ fontSize: '0.85rem', marginTop: '0.25rem' }}>
                        ⭐ {poi.rating.toFixed(1)}
                      </div>
                    )}
                  </div>
                  <button
                    onClick={() => removeFromItinerary(poi.id)}
                    style={{
                      background: '#dc3545',
                      color: 'white',
                      border: 'none',
                      borderRadius: '4px',
                      padding: '0.25rem 0.5rem',
                      cursor: 'pointer',
                      fontSize: '0.85rem'
                    }}
                  >
                    ✕
                  </button>
                </div>
              </div>
            ))
          )}
        </div>

        {/* Chat Section */}
        <div style={{
          height: window.innerWidth < 768 ? '250px' : '300px',
          borderTop: '2px solid #dee2e6',
          display: 'flex',
          flexDirection: 'column',
          background: '#f8f9fa'
        }}>
          <div style={{
            padding: '0.75rem 1rem',
            borderBottom: '1px solid #dee2e6',
            background: 'white'
          }}>
            <h4 style={{ margin: 0, fontSize: '1rem' }}>💬 Chat with Mr Wise Owl</h4>
          </div>
          <div style={{
            flex: 1,
            overflowY: 'auto',
            padding: '1rem'
          }}>
            {chatMessages.map((msg, idx) => (
              <div key={idx} style={{
                marginBottom: '0.75rem',
                textAlign: msg.role === 'user' ? 'right' : 'left'
              }}>
                <div style={{
                  display: 'inline-block',
                  padding: '0.5rem 0.75rem',
                  borderRadius: '12px',
                  background: msg.role === 'user' ? '#667eea' : 'white',
                  color: msg.role === 'user' ? 'white' : '#333',
                  maxWidth: '85%',
                  fontSize: '0.9rem',
                  border: msg.role === 'assistant' ? '1px solid #dee2e6' : 'none'
                }}>
                  {msg.text}
                </div>
              </div>
            ))}
          </div>
          <div style={{
            padding: '0.75rem',
            borderTop: '1px solid #dee2e6',
            display: 'flex',
            gap: '0.5rem',
            background: 'white'
          }}>
            <input
              type="text"
              placeholder="Ask me anything..."
              value={chatInput}
              onChange={(e) => setChatInput(e.target.value)}
              onKeyPress={(e) => e.key === 'Enter' && sendChatMessage()}
              style={{
                flex: 1,
                padding: '0.5rem',
                borderRadius: '6px',
                border: '1px solid #dee2e6',
                fontSize: '0.9rem'
              }}
            />
            <button
              onClick={sendChatMessage}
              style={{
                padding: '0.5rem 1rem',
                borderRadius: '6px',
                border: 'none',
                background: '#667eea',
                color: 'white',
                cursor: 'pointer',
                fontWeight: 'bold',
                fontSize: '0.9rem'
              }}
            >
              Send
            </button>
          </div>
        </div>
      </div>

      </div> {/* Close main content */}

      {/* Save Dialog */}
      {showSaveDialog && (
        <div style={{
          position: 'fixed',
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          background: 'rgba(0,0,0,0.5)',
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          zIndex: 2000
        }}>
          <div style={{
            background: 'white',
            padding: '2rem',
            borderRadius: '12px',
            width: '400px',
            boxShadow: '0 10px 40px rgba(0,0,0,0.3)'
          }}>
            <h3 style={{ margin: '0 0 1rem 0' }}>💾 Save Itinerary</h3>
            <input
              type="text"
              placeholder="Enter itinerary name..."
              value={itineraryName}
              onChange={(e) => setItineraryName(e.target.value)}
              style={{
                width: '100%',
                padding: '0.75rem',
                marginBottom: '1rem',
                border: '1px solid #dee2e6',
                borderRadius: '8px',
                fontSize: '1rem'
              }}
            />
            <div style={{ display: 'flex', gap: '0.5rem' }}>
              <button
                onClick={() => {
                  setShowSaveDialog(false);
                  setItineraryName('');
                }}
                style={{
                  flex: 1,
                  padding: '0.75rem',
                  borderRadius: '8px',
                  border: '1px solid #dee2e6',
                  background: 'white',
                  cursor: 'pointer',
                  fontWeight: 'bold'
                }}
              >
                Cancel
              </button>
              <button
                onClick={saveItinerary}
                disabled={!itineraryName.trim()}
                style={{
                  flex: 1,
                  padding: '0.75rem',
                  borderRadius: '8px',
                  border: 'none',
                  background: itineraryName.trim() ? '#27ae60' : '#ccc',
                  color: 'white',
                  cursor: itineraryName.trim() ? 'pointer' : 'not-allowed',
                  fontWeight: 'bold'
                }}
              >
                Save
              </button>
            </div>
          </div>
        </div>
      )}

      <style>{`
        @keyframes spin {
          0% { transform: rotate(0deg); }
          100% { transform: rotate(360deg); }
        }
        
        @media (max-width: 768px) {
          input, button, select {
            font-size: 16px !important;
          }
        }
      `}</style>
    </div>
  );
}

export default App;

