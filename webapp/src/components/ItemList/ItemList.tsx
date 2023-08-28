// Libraries
import Link from "next/link";

// Components
import { IProductListProps } from "components/DataList";
import Card from "components/Card";

import styles from "components/DataList/DataList.module.css";

export default function StockList({ products }: IProductListProps) {
  return (
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
        products.map((product) => (
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
  );
}
