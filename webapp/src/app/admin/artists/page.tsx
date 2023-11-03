'use client'
import React from "react";

// Components
import ArtistList from "components/ArtistList";
import SearchBar from "components/Search/SearchBar";
import { IArtist } from "components/DataList";

// API
const api_url = process.env.API_URL;

export default async function Artists() {
    const data = await getArtists();

  return (
    <div>
      <h2>Artists</h2>
      <p>
        List of all artists, with groups listed first and then individual
        artists
      </p>
      {/* <SearchBar placeholder={"artists"} data={data} /> */}
      <ArtistList artists={data}></ArtistList>
    </div>
  );
}

export async function getArtists() {
  const res = await fetch(api_url + "/artists");
  if (!res.ok) {
    throw new Error('Failed to fetch data')
  }
  return res.json();
}
