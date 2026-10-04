import express from 'express';
import { fileURLToPath } from 'node:url';
import { getCurrentWeather, WeatherError } from './weatherService.js';

const publicDir = fileURLToPath(new URL('../public', import.meta.url));
const MAX_CITY_LENGTH = 100;

/**
 * Build the Express app. Options are passed through to the weather service
 * ({ apiKey, baseUrl, fetchImpl }), which keeps the app easy to test.
 */
export function createApp(weatherOptions = {}) {
  const app = express();
  app.disable('x-powered-by');
  app.use(express.static(publicDir));

  // GET /api/weather?city=London -> current weather summary as JSON
  app.get('/api/weather', async (req, res) => {
    const city = typeof req.query.city === 'string' ? req.query.city.trim() : '';
    if (!city || city.length > MAX_CITY_LENGTH) {
      return res.status(400).json({ error: `Enter a city name (up to ${MAX_CITY_LENGTH} characters).` });
    }

    try {
      res.json(await getCurrentWeather(city, weatherOptions));
    } catch (err) {
      if (err instanceof WeatherError) {
        return res.status(err.status).json({ error: err.message });
      }
      console.error(err);
      res.status(500).json({ error: 'Something went wrong on the server. Try again.' });
    }
  });

  return app;
}
