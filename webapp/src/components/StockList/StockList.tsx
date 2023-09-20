// Libraries
import Link from "next/link";

// Components
import { IStock, IStockListProps } from "components/DataList";
import Card from "components/Card";

import styles from "components/DataList/DataList.module.css";

export default function StockList({ stocks }: IStockListProps) {
  console.log(stocks);

  return (
    <div className={styles.list}>
      <div className={styles.card}>
        <p className={styles.property}>Location</p>
        <p className={styles.property}>Product</p>
        <p className={styles.property_small}>Exclusivity</p>
        <p className={styles.property_small}>Count</p>
        <p className={styles.property_small}>Restock Threshold</p>
        <p className={styles.property}>Out of Stock Since</p>
        <p className={styles.property_small}>Ordered</p>
        <p className={styles.property}>Order Date</p>
        <p className={styles.property}>Tracking No.</p>
      </div>
      {stocks &&
        stocks.map((stock) => (
          <Link href={"/admin/items/".concat(stock.product_id)} key={stock.id}>
            <a>
              <Card data={stock} />
            </a>
          </Link>
        ))}
    </div>
  );
}
