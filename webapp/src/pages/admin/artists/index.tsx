import React from "react";

// Components
import ArtistList from "components/ArtistList";

// API
const api_url = process.env.API_URL;

export default function Stock({ data }: any) {
  console.log(data);
  return (
    <div>
      <h2>Artists</h2>
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
