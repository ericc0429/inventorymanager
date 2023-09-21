import { IStock } from "components/DataList";

import styles from "components/Card/Card.module.css";

interface IStockCardProps {
  data: IStock;
}

export default function StockCard({ data }: IStockCardProps) {
  return (
    <div className={styles.card}>
      <p className={styles.property}>{data.location}</p>
      <p className={styles.property}>{data.product_name}</p>
      <p className={styles.property}>{data.exclusive}</p>
      <p className={styles.property}>{data.count}</p>
      <p className={styles.property}>{data.restock_threshold}</p>
      <p className={styles.property}>{data.oos_date}</p>
      <p className={styles.property}>{data.ordered}</p>
      <p className={styles.property}>{data.order_date}</p>
      <p className={styles.property}>{data.tracking}</p>
    </div>
  );
}

function printArtists(artists: string[]) {
  var out = "";
  artists.forEach((artist: string) => out += artist + "\n")
  return out;
}