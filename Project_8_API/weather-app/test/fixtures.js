/** Trimmed copy of a real OpenWeatherMap /data/2.5/weather response. */
export const londonResponse = {
  name: 'London',
  dt: 1791100800,
  sys: { country: 'GB' },
  main: { temp: 14.2, feels_like: 13.6, temp_min: 12.1, temp_max: 15.8, humidity: 82 },
  wind: { speed: 4.1 },
  weather: [{ description: 'light rain', icon: '10d' }],
};

/** A stand-in for fetch() that records the URL it was called with and returns a canned response. */
export function fakeFetch(status, body = {}) {
  const calls = [];
  const fn = async (url) => {
    calls.push(String(url));
    return new Response(JSON.stringify(body), { status, headers: { 'Content-Type': 'application/json' } });
  };
  fn.calls = calls;
  return fn;
}
