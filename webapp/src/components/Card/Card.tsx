import React from "react";

import { IArtist, IStock } from "components/DataList";

import styles from "./Card.module.css";

interface ICardProps {
  data: IStock | IArtist;
}
interface ICardParseProps {
  data: IStock | IArtist;
  key: string;
}

export default function Card({ data }: ICardProps) {
  var arr = [];
  Object.keys(data).forEach((key) => {
    if (key != "id") {
      arr.push(data[key]);
    }
  });
  console.log(arr);

  return (
    <div className={styles.card}>
      {arr.map((item) => (
        <CardParse key={data.id + item} value={item} />
      ))}
    </div>
  );
}

function CardParse({ value }: any) {
  if (typeof value == "number") {
    return <p className={styles.property_small}>{value}</p>;
  } else if (typeof value == "boolean") {
    return <p className={styles.property_small}>{value ? "YES" : "NO"}</p>;
  } else return <p className={styles.property}>{value}</p>;
}