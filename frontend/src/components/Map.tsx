import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

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

interface MapProps {
  center: [number, number];
  pois: Poi[];
  onPoiClick?: (poi: Poi) => void;
}

const categoryIcons: Record<string, string> = {
  hotel: '🏨',
  activity_outdoor: '🏔️',
  activity_indoor: '🎭',
  campground_rv: '🚐',
  campground_tent: '⛺',
  historic: '🏛️',
  brewery: '🍺',
  restaurant: '🍽️',
};

const categoryColors: Record<string, string> = {
  hotel: '#e74c3c',
  activity_outdoor: '#27ae60',
  activity_indoor: '#3498db',
  campground_rv: '#f39c12',
  campground_tent: '#16a085',
  historic: '#8e44ad',
  brewery: '#d35400',
  restaurant: '#c0392b',
};

const createCustomIcon = (category: string) => {
  const color = categoryColors[category] || '#3498db';
  const emoji = categoryIcons[category] || '📍';
  
  return L.divIcon({
    html: `<div style="
      background: ${color};
      width: 32px;
      height: 32px;
      border-radius: 50% 50% 50% 0;
      transform: rotate(-45deg);
      border: 3px solid white;
      box-shadow: 0 2px 5px rgba(0,0,0,0.3);
      display: flex;
      align-items: center;
      justify-content: center;
    ">
      <span style="transform: rotate(45deg); font-size: 16px;">${emoji}</span>
    </div>`,
    className: 'custom-marker',
    iconSize: [32, 32],
    iconAnchor: [16, 32],
    popupAnchor: [0, -32],
  });
};

const renderStars = (rating?: number) => {
  if (!rating) return null;
  const stars = '⭐'.repeat(Math.floor(rating));
  return <span>{stars} {rating.toFixed(1)}</span>;
};

const renderPrice = (priceRange?: number) => {
  if (!priceRange) return null;
  return <span>{'$'.repeat(priceRange)}</span>;
};

function Map({ center, pois, onPoiClick }: MapProps) {
  return (
    <MapContainer 
      center={center} 
      zoom={12} 
      style={{ height: '100%', width: '100%' }}
      key={`${center[0]}-${center[1]}`}
    >
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      
      {pois.map((poi) => (
        <Marker 
          key={poi.id} 
          position={[poi.latitude, poi.longitude]}
          icon={createCustomIcon(poi.category)}
        >
          <Popup>
            <div style={{ minWidth: '200px' }}>
              <h3 style={{ 
                margin: '0 0 0.5rem 0',
                fontSize: '1.1rem',
                color: categoryColors[poi.category]
              }}>
                {categoryIcons[poi.category]} {poi.name}
              </h3>
              
              <div style={{ 
                display: 'flex', 
                gap: '1rem', 
                marginBottom: '0.5rem',
                fontSize: '0.9rem'
              }}>
                {poi.rating && (
                  <div>{renderStars(poi.rating)}</div>
                )}
                {poi.priceRange && (
                  <div style={{ color: '#27ae60', fontWeight: 'bold' }}>
                    {renderPrice(poi.priceRange)}
                  </div>
                )}
              </div>
              
              {poi.description && (
                <p style={{ 
                  margin: '0.5rem 0',
                  fontSize: '0.9rem',
                  color: '#555'
                }}>
                  {poi.description}
                </p>
              )}
              
              {poi.amenities && poi.amenities.length > 0 && (
                <div style={{ 
                  marginTop: '0.5rem',
                  fontSize: '0.85rem',
                  color: '#666'
                }}>
                  <strong>Amenities:</strong> {poi.amenities.slice(0, 3).join(', ')}
                  {poi.amenities.length > 3 && '...'}
                </div>
              )}
              
              <a
                href={`https://www.google.com/maps/dir/?api=1&destination=${poi.latitude},${poi.longitude}`}
                target="_blank"
                rel="noopener noreferrer"
                style={{
                  display: 'inline-block',
                  marginTop: '0.75rem',
                  marginRight: '0.5rem',
                  padding: '0.5rem 1rem',
                  background: '#3498db',
                  color: 'white',
                  textDecoration: 'none',
                  borderRadius: '4px',
                  fontSize: '0.9rem',
                  fontWeight: 'bold'
                }}
              >
                🗺️ Directions
              </a>
              {onPoiClick && (
                <button
                  onClick={() => onPoiClick(poi)}
                  style={{
                    marginTop: '0.75rem',
                    padding: '0.5rem 1rem',
                    background: '#27ae60',
                    color: 'white',
                    border: 'none',
                    borderRadius: '4px',
                    fontSize: '0.9rem',
                    fontWeight: 'bold',
                    cursor: 'pointer'
                  }}
                >
                  ➕ Add to Trip
                </button>
              )}
            </div>
          </Popup>
        </Marker>
      ))}
    </MapContainer>
  );
}

export default Map;
