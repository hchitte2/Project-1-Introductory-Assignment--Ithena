import { test, before, after } from 'node:test';
import assert from 'node:assert/strict';
import { createApp } from '../src/app.js';
import { fakeFetch, londonResponse } from './fixtures.js';

let server;
let baseUrl;

before(async () => {
  const app = createApp({ apiKey: 'test-key', fetchImpl: fakeFetch(200, londonResponse) });
  server = app.listen(0);
  await new Promise((resolve) => server.once('listening', resolve));
  baseUrl = `http://localhost:${server.address().port}`;
});

after(() => server.close());

test('GET /api/weather returns the weather summary as JSON', async () => {
  const res = await fetch(`${baseUrl}/api/weather?city=London`);
  assert.equal(res.status, 200);
  assert.match(res.headers.get('content-type'), /application\/json/);
  const body = await res.json();
  assert.equal(body.city, 'London');
  assert.equal(body.temperature, 14.2);
});

test('GET /api/weather rejects a missing or blank city with 400', async () => {
  for (const query of ['', '?city=', '?city=%20%20']) {
    const res = await fetch(`${baseUrl}/api/weather${query}`);
    assert.equal(res.status, 400, `query "${query}"`);
    assert.match((await res.json()).error, /Enter a city name/);
  }
});

test('GET /api/weather rejects an overly long city with 400', async () => {
  const res = await fetch(`${baseUrl}/api/weather?city=${'a'.repeat(101)}`);
  assert.equal(res.status, 400);
});

test('GET / serves the web page', async () => {
  const res = await fetch(baseUrl);
  assert.equal(res.status, 200);
  assert.match(await res.text(), /<title>Weather now<\/title>/);
});
