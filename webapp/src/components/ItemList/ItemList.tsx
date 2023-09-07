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
  const [name, setName] = useState("le sserafim");
  const [maxPrice, setMaxPrice] = useState(3500);
  const [minPrice, setMinPrice] = useState(2200);

  const expandHandler = () => {
    setExpandFilter((currExpand) => !currExpand);
  };

  var headers = [
    {label: "DB ID", key: "id"},
    {label: "TYPE", key: "type"},
    {label: "NAME", key: "name"},
    {label: "GTIN", key: "gtin"},
    {label: "SKU", key: "sku"},
    {label: "PRICE", key: "price"},
    {label: "CHI STOCK", key: "stock[0].count"},
    {label: "ARTIST", key: "artist[0].name"},
    {label: "VERSION", key: "version"},
    {label: "EXTRAS", key: "extras.forEach((extra) => return extra.name)"},
    {label: "RELEASED", key: "released"},
    {label: "DISCOGRAPHY", key: "discography"},
    {label: "FORMAT", key: "format"},
    {label: "COLOR", key: "color"},
    {label: "BRAND", key: "brand"},
  ]

  var prodArr: IProduct[] =
  products.filter((product) =>
    (product.name.toLowerCase().includes(name.toLowerCase())) &&
    (product.price >= minPrice) && (product.price <= maxPrice)
  )
  console.log(prodArr[0].stock[0].count)

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
      <CSVLink data={prodArr} headers={headers}>Export</CSVLink>
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
