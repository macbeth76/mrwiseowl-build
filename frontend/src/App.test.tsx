import { describe, it, expect, vi } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import App from './App';
import axios from 'axios';
import type { Mock } from 'vitest';

vi.mock('axios');
const mockedAxios = axios as { get: Mock };

describe('App', () => {
  it('renders loading state initially', () => {
    render(<App />);
    expect(screen.getByText(/loading/i)).toBeInTheDocument();
  });

  it('renders error state when API fails', async () => {
    mockedAxios.get.mockRejectedValue(new Error('Network Error'));
    
    render(<App />);
    
    await waitFor(() => {
      expect(screen.getByText(/error/i)).toBeInTheDocument();
    });
  });

  it('displays API URL in error state', async () => {
    mockedAxios.get.mockRejectedValue(new Error('Network Error'));
    
    render(<App />);
    
    await waitFor(() => {
      expect(screen.getByText(/API URL:/i)).toBeInTheDocument();
    });
  });
});
