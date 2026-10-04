const DEFAULT_BASE_URL = 'https://api.openweathermap.org/data/2.5';
const TIMEOUT_MS = 5000;

/** An error with a user-facing message and the HTTP status our API should return. */
export class WeatherError extends Error {
  constructor(message, status) {
    super(message);
    this.name = 'WeatherError';
    this.status = status;
  }
}

/**
 * Fetch the current weather for a city from OpenWeatherMap and return a small summary.
 * `fetchImpl` is injectable so tests can run without network access or an API key.
 */
export async function getCurrentWeather(city, { apiKey, baseUrl = DEFAULT_BASE_URL, fetchImpl = fetch } = {}) {
  if (!apiKey) {
    throw new WeatherError('The server has no OpenWeatherMap API key. Add OPENWEATHER_API_KEY to .env and restart.', 500);
  }

  const url = new URL(`${baseUrl}/weather`);
  url.search = new URLSearchParams({ q: city, appid: apiKey, units: 'metric' }).toString();

  let response;
  try {
    response = await fetchImpl(url, { signal: AbortSignal.timeout(TIMEOUT_MS) });
  } catch {
    throw new WeatherError('Could not reach OpenWeatherMap. Check your connection and try again.', 502);
  }

  if (response.status === 404) {
    throw new WeatherError(`No city named "${city}" was found. Check the spelling, or add a country code (e.g. "Paris, FR").`, 404);
  }
  if (response.status === 401) {
    throw new WeatherError('OpenWeatherMap rejected the API key. New keys can take up to 2 hours to activate.', 502);
  }
  if (response.status === 429) {
    throw new WeatherError('OpenWeatherMap rate limit reached. Wait a minute and try again.', 503);
  }
  if (!response.ok) {
    throw new WeatherError(`OpenWeatherMap returned an error (HTTP ${response.status}).`, 502);
  }

  const data = await response.json();
  return {
    city: data.name,
    country: data.sys?.country ?? '',
    temperature: data.main.temp,
    feelsLike: data.main.feels_like,
    tempMin: data.main.temp_min,
    tempMax: data.main.temp_max,
    humidity: data.main.humidity,
    windSpeed: data.wind?.speed ?? null,
    description: data.weather?.[0]?.description ?? '',
    observedAt: new Date(data.dt * 1000).toISOString(),
  };
}
