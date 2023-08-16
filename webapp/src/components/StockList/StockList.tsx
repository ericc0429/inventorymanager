// Libraries
import Link from "next/link";

// Components
import { IStockListProps } from "./Stock.types";
import Card from "components/Card";

import styles from "./StockList.module.css";

export default function StockList({ stocks }: IStockListProps) {
  return (
    <div className={styles.list}>
      {stocks &&
        stocks.map((stock) => (
          <Link href={"/stock/".concat(stock.id.toString())} key={stock.id}>
            <Card data={stock} />
          </Link>
        ))}
    </div>
  );
}
