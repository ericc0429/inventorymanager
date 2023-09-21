import { IProduct, IStock } from "components/DataList";

import styles from "components/Card/Card.module.css";

interface IItemCardProps {
  data: IProduct;
}

export default function ItemCard({ data }: IItemCardProps) {
  return (
    <div className={styles.card}>
      <p className={styles.property}>{data.type}</p>
      <p className={styles.property}>{data.name}</p>
      <p className={styles.property}>{data.version}</p>
      <p className={styles.property}>{data.discography}</p>
      <p className={styles.property}>{data.format}</p>
      <p className={styles.property}>{data.color}</p>
      <p className={styles.property}>{data.brand}</p>
      <p className={styles.property}>{data.gtin}</p>
      <p className={styles.property}>{data.sku}</p>
      <p className={styles.property_small}>{data.price}</p>
      <p className={styles.property}>{printStock(data.stock)}</p>
    </div>
  );
}

function printStock(stocks: IStock[]) {
  var out = "";
  stocks.forEach((stock: IStock) => out += stock.location + ": " + stock.count + "\n")
  return out;
}