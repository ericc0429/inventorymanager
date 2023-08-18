import React from "react";

import { IStock } from "components/DataList";

import styles from "components/DataList/DataList.module.css";

interface ICardProps {
  data: IStock;
}

export default function Card({ data }: ICardProps) {
  return (
    <div className={styles.card}>
      <p className={styles.property}>{data.location}</p>
      <p className={styles.property}>{data.product}</p>
      <p className={styles.property_small}>{data.count}</p>
      <p className={styles.property_small}>{data.restock_threshold}</p>
      <p className={styles.property}>{data.oos_date}</p>
      <p className={styles.property_small}>{data.ordered}</p>
      <p className={styles.property}>{data.order_date}</p>
      <p className={styles.property_small}>{data.exclusive}</p>
      <p className={styles.property}>{data.tracking}</p>
    </div>
  );
}
