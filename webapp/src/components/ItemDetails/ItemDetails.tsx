import { IProduct, IStock } from "components/DataList";
import styles from "components/DataList/DataList.module.css";
import Link from "next/link";

interface IItemPageProps {
  item: IProduct;
}
interface IItemStockProps {
  stocks: IStock[];
}

export default function ItemDetails({ item }: IItemPageProps) {
  var isAlbum = item.type == "ALBUM";

  return (
    <div>
      <div>
        <h1>{item.name}</h1>
        <h2>{item.type}</h2>
      </div>
      <p></p>
      <Link href={"/admin/items/".concat(item.id.toString()).concat("/edit")}>
        <a className={styles.button}>Edit</a>
      </Link>
      <p>{"GTIN: " + item.gtin + "\n"}</p>
      <p>{"Price: " + item.price + "\n"}</p>
      <p>{"Description: " + item.description + "\n"}</p>
      <ItemStock stocks={item.stock} />
    </div>
  );
}

function ItemStock({ stocks }: IItemStockProps) {
  return (
    <div>
      <h2>Stock Data</h2>
      <div className={styles.rowlist}>
        {stocks &&
          stocks.map((it) => (
            <div className={styles.vertical_card} key={it.id}>
              <h3 className={styles.nopad}>{"Location: " + it.location}</h3>
              <p className={styles.nopad}>{"Exclusivity: " + it.exclusive}</p>
              <p className={styles.nopad}>{"Stock Count: " + it.count}</p>
              <p className={styles.nopad}>
                {"Restock Limit: " + it.restock_threshold}
              </p>
              <p className={styles.nopad}>
                {"Out of Stock Since: " + it.oos_date}
              </p>
              <p className={styles.nopad}>
                {"New Stock Ordered: " + it.ordered}
              </p>
              <p className={styles.nopad}>
                {"New Stock Ordered On: " + it.order_date}
              </p>
              <p className={styles.nopad}>{"Order Tracking: " + it.tracking}</p>
            </div>
          ))}
      </div>
    </div>
  );
}
