import React, { useEffect, useState } from "react";

// Components
import StockList from "components/StockList/StockList";

// API
const api_url = process.env.API_URL;

export default function Stock({ data }: any) {
  console.log(data);
  return (
    <div>
      Stock Data
      <StockList stocks={data}></StockList>
    </div>
  );
}

export async function getServerSideProps() {
  const res = await fetch(api_url + "/stock");
  const data = await res.json();
  //console.log(data);
  return {
    props: { data },
  };
}


/* const Stock = () => {
  const [stocks, setStocks] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);

    fetch("http://localhost:8080/stock")
      .then((res) => res.json())
      .then((data) => {
        setStocks(data);
        setLoading(false);
      })
      .catch((err) => console.log(err));
  }, []);

  console.log(stocks);
  if (loading) {
    return <p>Loading...</p>;
  }

  return (
    <div>
      {stocks.map((stock) => (
        <div key={stock.id}>{stock.title}</div>
      ))}
    </div>
  );
};

export default Stock;
*/