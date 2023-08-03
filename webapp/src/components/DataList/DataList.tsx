// Libraries
import Link from "next/link";

// Components
import { IDataListProps } from "./Data.types";
import Card from "components/Card";

export default function DataList({ stocks }: IDataListProps) {
  return (
    <div>
      {stocks &&
        stocks.map((stock) => (
          <Link href={"/stock/".concat(stock.id.toString())} key={stock.id}>
            <Card data={stock} />
          </Link>
        ))}
    </div>
  );
}
