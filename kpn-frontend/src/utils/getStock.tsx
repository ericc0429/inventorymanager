const api_url = process.env.API_URL;
const api_dir = "/stock";

export async function getStock() {
  const res = await fetch(api_url + api_dir);
  if (!res.ok) {
    throw new Error('Failed to fetch data')
  }
  return res.json()
}