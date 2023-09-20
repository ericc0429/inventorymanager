import React from "react";

// Components
import LowStockList from "components/LowStockList/LowStockList";
import SearchBar from "components/Search/SearchBar";

// API
const api_url = process.env.API_URL;

export default function Stock({ data }: any) {
  return (
    <div>
      <h2>Low Stock</h2>
      <p>Items with stock count less than or equal to its restock threshold</p>
      {/* <SearchBar placeholder={"stock"} data={data} /> */}
      <LowStockList stocks={data}></LowStockList>
    </div>
  );
}

export async function getServerSideProps() {
  const res = await fetch(api_url + "/stock");
  const data = await res.json();
  return {
    props: { data },
  };
}
