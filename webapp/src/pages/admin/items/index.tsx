import React from "react";

// Components
import ItemList from "components/ItemList";
import SearchBar from "components/Search/SearchBar";

// API
const api_url = process.env.API_URL;

export default function Items({ data }: any) {
  return (
    <div>
      <h2>Items</h2>
      <p>List of all items</p>
      <p>
        Stock at a glance not working yet, need to click on item to see stock
        information. Also ignore the alignment issues, working on implementing
        at-a-glance information for the list
      </p>
      <SearchBar placeholder={"items"} data={data} />
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
