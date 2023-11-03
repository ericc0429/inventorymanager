const api_url = process.env.API_URL;
const api_dir = "/artists/";

export async function getArtist(id: string) {

  const res = await fetch(api_url + api_dir + id);

  if (!res.ok) {
    // This will activate the closest `error.js` Error Boundary
    throw new Error('Failed to fetch data')
  }

  return res.json()
}