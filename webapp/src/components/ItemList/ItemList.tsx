// Libraries
import React, { useState } from "react";
import { CSVLink } from "react-csv";
import Link from "next/link";

// Components
import { IProduct, IProductDTO, IProductListProps, IProductListPropsDTO } from "components/DataList";
import Card from "components/Card";

import styles from "components/DataList/DataList.module.css";

export default function StockList({ products }: IProductListProps) {
  const [expandFilter, setExpandFilter] = useState(false);
  const [name, setName] = useState("");
  const [maxPrice, setMaxPrice] = useState(100000);
  const [minPrice, setMinPrice] = useState(0);

  const expandHandler = () => {
    setExpandFilter((currExpand) => !currExpand);
  };

  var prodArr: IProduct[] =
  products.filter((product) =>
    (product.name.toLowerCase().includes(name.toLowerCase())) &&
    (product.price >= minPrice) && (product.price <= maxPrice)
  )

  return (
    <>
      <div className={styles.container}>
        <div className={styles.shcontainer}>
          <p className={styles.nopad}>Filter</p>
          <button className={styles.showhide} onClick={expandHandler}>
            {expandFilter ? "Collapse" : "Expand"}
          </button>
        </div>
        {expandFilter && (
          <>
            Name:
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
            MinPrice:
            <input
              type="number"
              value={minPrice}
              onChange={(e) => setMinPrice(parseInt(e.target.value))}
            />
            MaxPrice:
            <input
              type="number"
              value={maxPrice}
              onChange={(e) => setMaxPrice(parseInt(e.target.value))}
            />
          </>
        )}
      </div>
      <CSVLink data={prodArr}>Export</CSVLink>
      <div className={styles.list}>
        <div className={styles.card}>
          <p className={styles.property}>Type</p>
          <p className={styles.property}>Name</p>
          <p className={styles.property}>Description</p>
          <p className={styles.property}>GTIN</p>
          <p className={styles.property}>Price</p>
          <p className={styles.property}>Stock</p>
        </div>
        {products &&
          products
            .filter((product) =>
              (product.name.toLowerCase().includes(name.toLowerCase())) &&
              (product.price >= minPrice) && (product.price <= maxPrice)
            )
            .map((product) => (
              <Link
                href={"/admin/items/".concat(product.id.toString())}
                key={product.id}
              >
                <a>
                  <Card data={product} />
                </a>
              </Link>
            ))}
      </div>
    </>
  );
}
