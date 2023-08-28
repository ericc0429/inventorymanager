import React from "react";

import { IArtist, IProduct, IStock } from "components/DataList";

import styles from "./Card.module.css";
import Link from "next/link";

interface ICardProps {
  data: IStock | IArtist | IProduct;
}
interface ICardParseProps {
  data: IStock | IArtist | IProduct;
  key: string;
}

export default function Card({ data }: ICardProps) {
  var arr = [];
  Object.keys(data).forEach((key) => {
    if (key != "id") {
      arr.push(data[key]);
    }
  });

  return (
    <div className={styles.card}>
      {arr.map((item) => (
        <CardParse key={item + "_" + data.id} value={item} />
      ))}
    </div>
  );
}

function CardParse({ value }: any) {
  if (Array.isArray(value)) {
    var out = "";
    value.forEach((element) => {
      out += element + "\n";
    });
    return (
      <p className={styles.property}>{value.length != 0 ? out : "[None]"}</p>
    );
  } else if (typeof value == "number") {
    return <p className={styles.property_small}>{value != null ? value : 0}</p>;
  } else if (typeof value == "boolean") {
    return <p className={styles.property_small}>{value ? "YES" : "NO"}</p>;
  } else return <p className={styles.property}>{value ? value : "N/A"}</p>;
}