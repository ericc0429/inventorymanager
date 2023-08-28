import { IProduct, IStock } from "components/DataList";

interface IItemPageProps {
  item: IProduct;
}
interface IItemStockProps {
  stocks: IStock[];
}

export default function ItemDetails({ item }: IItemPageProps) {
  var isAlbum = item.type == "ALBUM";
  console.log(item);
  return (
    <div>
      <h1>{item.name}</h1>
      <h2>{item.type}</h2>
      <p>{"GTIN: " + item.gtin + "\n"}</p>
      <p>{"Price: " + item.price + "\n"}</p>
      <p>{"Description: " + item.description + "\n"}</p>
      <p>{"Stock: \n" + item.stock.map((element) => element)}</p>
      <ItemStock stocks={item.stock} />
    </div>
  );
}

function ItemStock({ stocks }: IItemStockProps) {
  console.log(stocks);
  return (
    <div>
      <h2>Stock Data</h2>
      {stocks &&
        stocks.map((it) => (
          <div key={it.id}>
            <p>{"Location: " + it.location}</p>
            <p>{"Exclusivity: " + it.exclusive}</p>
            <p>{"Stock Count: " + it.count}</p>
            <p>{"Restock Limit: " + it.restock_threshold}</p>
            <p>{"Out of Stock Since: " + it.oos_date}</p>
            <p>{"New Stock Ordered: " + it.ordered}</p>
            <p>{"New Stock Ordered On: " + it.order_date}</p>
            <p>{"Order Tracking: " + it.tracking}</p>
          </div>
        ))}
    </div>
  );
}
