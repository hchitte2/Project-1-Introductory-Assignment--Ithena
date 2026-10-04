import { test } from 'node:test';
import assert from 'node:assert/strict';
import { getCurrentWeather, WeatherError } from '../src/weatherService.js';
import { fakeFetch, londonResponse } from './fixtures.js';

test('calls OpenWeatherMap with the city, key, and metric units', async () => {
  const fetchImpl = fakeFetch(200, londonResponse);
  await getCurrentWeather('London', { apiKey: 'test-key', fetchImpl });

  const url = new URL(fetchImpl.calls[0]);
  assert.equal(url.origin + url.pathname, 'https://api.openweathermap.org/data/2.5/weather');
  assert.equal(url.searchParams.get('q'), 'London');
  assert.equal(url.searchParams.get('appid'), 'test-key');
  assert.equal(url.searchParams.get('units'), 'metric');
});

test('maps the API response to a weather summary', async () => {
  const weather = await getCurrentWeather('London', { apiKey: 'k', fetchImpl: fakeFetch(200, londonResponse) });

  assert.deepEqual(weather, {
    city: 'London',
    country: 'GB',
    temperature: 14.2,
    feelsLike: 13.6,
    tempMin: 12.1,
    tempMax: 15.8,
    humidity: 82,
    windSpeed: 4.1,
    description: 'light rain',
    observedAt: new Date(londonResponse.dt * 1000).toISOString(),
  });
});

test('fails fast with a 500 when no API key is configured', async () => {
  const fetchImpl = fakeFetch(200, londonResponse);
  await assert.rejects(getCurrentWeather('London', { fetchImpl }), { name: 'WeatherError', status: 500 });
  assert.equal(fetchImpl.calls.length, 0);
});

test('turns upstream errors into clear WeatherErrors', async (t) => {
  const cases = [
    { upstream: 404, status: 404, message: /No city named "Atlantis"/ },
    { upstream: 401, status: 502, message: /rejected the API key/ },
    { upstream: 429, status: 503, message: /rate limit/ },
    { upstream: 500, status: 502, message: /HTTP 500/ },
  ];
  for (const c of cases) {
    await t.test(`upstream ${c.upstream} -> ${c.status}`, async () => {
      const promise = getCurrentWeather('Atlantis', { apiKey: 'k', fetchImpl: fakeFetch(c.upstream) });
      await assert.rejects(promise, (err) => err instanceof WeatherError && err.status === c.status && c.message.test(err.message));
    });
  }
});

test('reports a 502 when OpenWeatherMap cannot be reached', async () => {
  const fetchImpl = async () => {
    throw new TypeError('fetch failed');
  };
  await assert.rejects(getCurrentWeather('London', { apiKey: 'k', fetchImpl }), { status: 502 });
});
