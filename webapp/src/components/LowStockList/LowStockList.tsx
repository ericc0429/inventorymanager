// Libraries
import Link from "next/link";

// Components
import { IStock, IStockListProps } from "components/DataList";
import StockCard from "components/StockList/StockCard";

import styles from "components/DataList/DataList.module.css";


export default function LowStockList({ stocks }: IStockListProps) {

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
        stocks
          .filter((stock) => stock.count <= stock.restock_threshold)
          .map((stock) => (
            <Link
              href={"/admin/items/".concat(stock.product_id)}
              key={stock.id}
            >
              <StockCard data={stock} />
            </Link>
          ))}
    </div>
  );
}
