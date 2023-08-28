import React from "react";

// Components
import ArtistList from "components/ArtistList";
import SearchBar from "components/Search/SearchBar";

// API
const api_url = process.env.API_URL;

export default function Artists({ data }: any) {
  return (
    <div>
      <h2>Artists</h2>
      <p>
        List of all artists, with groups listed first and then individual
        artists
      </p>
      <SearchBar placeholder={"artists"} data={data} />
      <ArtistList artists={data}></ArtistList>
    </div>
  );
}

export async function getServerSideProps() {
  const res = await fetch(api_url + "/artists");
  const data = await res.json();
  return {
    props: { data },
  };
}
