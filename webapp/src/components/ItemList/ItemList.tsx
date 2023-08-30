// Libraries
import React, { useState } from "react";
import Link from "next/link";

// Components
import { IProductListProps } from "components/DataList";
import Card from "components/Card";

import styles from "components/DataList/DataList.module.css";

export default function StockList({ products }: IProductListProps) {
  const [expandFilter, setExpandFilter] = useState(false);
  const [name, setName] = useState("");

  const expandHandler = () => {
    setExpandFilter((currExpand) => !currExpand);
  };

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
          </>
        )}
      </div>
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
              product.name.toLowerCase().includes(name.toLowerCase())
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
