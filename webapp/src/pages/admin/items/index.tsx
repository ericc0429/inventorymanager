import React from "react";

// Components
import ItemList from "components/ItemList";

// API
const api_url = process.env.API_URL;

export default function Items({ data }: any) {
  console.log(data);
  return (
    <div>
      <h2>Items</h2>
      <ItemList products={data}></ItemList>
    </div>
  );
}

export async function getServerSideProps() {
  const res = await fetch(api_url + "/products");
  const data = await res.json();
  return {
    props: { data },
  };
}
