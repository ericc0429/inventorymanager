import React, { useEffect, useState } from "react";

// Components
import DataList from "components/DataList/DataList";

// API
const api_url = process.env.API_URL;

/* export default function Stock({ data }) {
  console.log(data);
  return (
    <div>
      Stock Data
      <DataList stocks={data}></DataList>
    </div>
  );
}

export async function getServerSideProps() {
  const res = await fetch("http://localhost:8080/stock");
  const data = await res.json();
  return {
    props: { data },
  };
}
 */

const Stock = () => {
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
        <div key={stock.id}>{stock.count}</div>
      ))}
    </div>
  );
};

export default Stock;
