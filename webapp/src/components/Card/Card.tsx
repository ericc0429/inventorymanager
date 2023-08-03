import React from "react";

import { IStock } from "components/DataList";

interface ICardProps {
  data: IStock;
}

export default function Card({ data }: ICardProps) {
  return (
    <div>
      <p>Location: {data.location}</p>
      <p>Product: {data.product}</p>
      <p>Count: {data.count}</p>
    </div>
  );
}
