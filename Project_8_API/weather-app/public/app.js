const form = document.querySelector('#search');
const input = document.querySelector('#city');
const button = form.querySelector('button');
const result = document.querySelector('#result');
const template = document.querySelector('#reading-template');

// Thermometer scale and colour stops (°C).
const SCALE_MIN = -20;
const SCALE_MAX = 45;
const COLOR_STOPS = [
  [-10, '#2f6bd0'], // cold
  [5, '#3a9cc2'],   // cool
  [16, '#3f9a6b'],  // mild
  [26, '#d9962b'],  // warm
  [35, '#c9412f'],  // hot
];

form.addEventListener('submit', async (event) => {
  event.preventDefault();
  const city = input.value.trim();
  if (!city) return;

  button.disabled = true;
  button.textContent = 'Checking…';
  try {
    const response = await fetch(`/api/weather?city=${encodeURIComponent(city)}`);
    const body = await response.json();
    if (!response.ok) throw new Error(body.error || `Request failed (HTTP ${response.status}).`);
    renderReading(body);
  } catch (err) {
    renderError(err.message || 'Could not load the weather. Try again.');
  } finally {
    button.disabled = false;
    button.textContent = 'Get temperature';
  }
});

function renderReading(w) {
  const node = template.content.cloneNode(true);
  const reading = node.querySelector('.reading');
  const t = Math.round(w.temperature);

  reading.style.setProperty('--temp-color', colorFor(w.temperature));
  node.querySelector('.place').textContent = w.country ? `${w.city}, ${w.country}` : w.city;
  node.querySelector('.value').textContent = t;
  node.querySelector('.summary').textContent =
    `${capitalize(w.description)}. Feels like ${Math.round(w.feelsLike)}°C.`;
  node.querySelector('.range').textContent = `${Math.round(w.tempMin)}° to ${Math.round(w.tempMax)}°`;
  node.querySelector('.humidity').textContent = `${w.humidity}%`;
  node.querySelector('.wind').textContent = w.windSpeed == null ? 'n/a' : `${w.windSpeed} m/s`;
  node.querySelector('.observed').textContent =
    `Observed at ${new Date(w.observedAt).toLocaleTimeString([], { hour: 'numeric', minute: '2-digit' })} your time.`;

  result.replaceChildren(node);

  // Fill the mercury on the next frame so the height transition plays.
  const fill = Math.min(100, Math.max(4, ((w.temperature - SCALE_MIN) / (SCALE_MAX - SCALE_MIN)) * 100));
  requestAnimationFrame(() => {
    requestAnimationFrame(() => reading.style.setProperty('--fill', `${fill}%`));
  });
}

function renderError(message) {
  const p = document.createElement('p');
  p.className = 'error';
  p.setAttribute('role', 'alert');
  p.textContent = message;
  result.replaceChildren(p);
}

/** Interpolate between colour stops so the reading's colour tracks the temperature. */
function colorFor(temp) {
  if (temp <= COLOR_STOPS[0][0]) return COLOR_STOPS[0][1];
  for (let i = 1; i < COLOR_STOPS.length; i++) {
    const [t1, c1] = COLOR_STOPS[i];
    if (temp <= t1) {
      const [t0, c0] = COLOR_STOPS[i - 1];
      return mix(c0, c1, (temp - t0) / (t1 - t0));
    }
  }
  return COLOR_STOPS[COLOR_STOPS.length - 1][1];
}

function mix(a, b, ratio) {
  const ca = a.match(/\w\w/g).map((h) => parseInt(h, 16));
  const cb = b.match(/\w\w/g).map((h) => parseInt(h, 16));
  return `rgb(${ca.map((v, i) => Math.round(v + (cb[i] - v) * ratio)).join(', ')})`;
}

function capitalize(text) {
  return text ? text[0].toUpperCase() + text.slice(1) : '';
}
