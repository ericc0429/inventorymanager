import React, { useEffect, useState } from "react";

// Components
import StockList from "components/StockList/StockList";

// API
const api_url = process.env.API_URL;

export default function Stock({ data }: any) {
  console.log(data);
  return (
    <div>
      <h2>Stock Count</h2>
      <StockList stocks={data}></StockList>
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
