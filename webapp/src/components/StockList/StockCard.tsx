import React from "react";

import { IStock } from "./Stock.types";

import styles from "./StockList.module.css";

interface ICardProps {
  data: IStock;
}

export default function Card({ data }: ICardProps) {
  return (
    <div className={styles.card}>
      <div className={styles.property}>Location: {data.location}</div>
      <div className={styles.property}>Product: {data.product}</div>
      <p className={styles.property}>Count: {data.count}</p>
    </div>
  );
}
