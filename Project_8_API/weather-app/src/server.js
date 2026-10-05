import { createApp } from './app.js';

const port = Number(process.env.PORT) || 3000;
const apiKey = process.env.OPENWEATHER_API_KEY;

createApp({ apiKey }).listen(port, () => {
  console.log(`Weather app running at http://localhost:${port}`);
  if (!apiKey) {
    console.warn('OPENWEATHER_API_KEY is not set. Copy .env.example to .env and add your key.');
  }
});
