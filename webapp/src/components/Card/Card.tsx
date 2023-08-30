import React from "react";

import { IArtist, IProduct, IStock } from "components/DataList";
import { IProductGeneral, IStockGeneral } from "components/DataList";

import styles from "./Card.module.css";
import Link from "next/link";

interface ICardProps {
  data: IStock | IStockGeneral | IArtist | IProduct;
}
interface ICardParseProps {
  data: IStock | IArtist | IProduct;
  key: string;
}

export default function Card({ data }: ICardProps) {
  var arr = Object.keys(data).map((key) => [key, data[key]]);

  return (
    <div className={styles.card}>
      {arr.map((item) => (
        <CardParse key={item + "_" + data.id} keyval={item} />
      ))}
    </div>
  );
}

function CardParse({keyval}: any) {
  if (keyval[0] == "id" || keyval[0] == "product_id") {
    return null;
  }
  else if (Array.isArray(keyval[1])) {
    var out = "";
    keyval[1].forEach((element) => {
      out += element + "\n";
    });
    return (
      <p className={styles.property}>{keyval[1].length != 0 ? out : "[None]"}</p>
    );
  } else if (typeof keyval[1] == "number") {
    return <p className={styles.property_small}>{keyval[1] != null ? keyval[1] : 0}</p>;
  } else if (typeof keyval[1] == "boolean") {
    return <p className={styles.property_small}>{keyval[1] ? "YES" : "NO"}</p>;
  } else return <p className={styles.property}>{keyval[1] ? keyval[1] : "N/A"}</p>;
}

/* function CardParse({ value }: any) {
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
} */