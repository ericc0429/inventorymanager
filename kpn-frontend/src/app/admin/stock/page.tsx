import React from "react";

// Components
import StockList from "components/StockList/StockList";
import SearchBar from "components/Search/SearchBar";
import { getStock } from "@/utils/getStock";

// API
const api_url = process.env.API_URL;

export default async function Stock() {
  const data = await getStock();
  return (
    <div>
      <h2>Stock</h2>
      <p>Items with stock count less than or equal to its restock threshold</p>
      {/* <SearchBar placeholder={"stock"} data={data} /> */}
      <StockList stocks={data}></StockList>
    </div>
  );
}
