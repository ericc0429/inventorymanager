import { IProduct } from "components/DataList";
import { useState } from "react";

import styles from "./ItemEdit.module.css";

interface IEditItemProps {
  item: IProduct;
}

export default function EditItem({ item }: IEditItemProps) {
  const [saving, setSaving] = useState(false);

  const [name, setName] = useState(item.name);
  const [gtin, setGTIN] = useState(item.gtin);
  const [desc, setDesc] = useState(item.description);
  const [price, setPrice] = useState(item.price);
  const [version, setVersion] = useState(item.version);
  const [released, setReleased] = useState(item.released);
  const [disc, setDisc] = useState(item.discography);
  const [format, setFormat] = useState(item.format);
  const [color, setColor] = useState(item.color);
  const [brand, setBrand] = useState(item.brand);

  return (
    <div>
      <h2>Edit Product</h2>
      <p>{item.id}</p>
      <p>
        Proof of Concept only, non-functional as of yet. Will update item in
        database upon submit.
      </p>
      <form>
        <div className={styles.flexRow}>
          <p>Name:</p>
          <input
            className={styles.input_text}
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </div>
        <div className={styles.flexRow}>
          <p>GTIN:</p>
          <input
            className={styles.input_text}
            type="text"
            value={gtin}
            onChange={(e) => setGTIN(e.target.value)}
          />
        </div>
        <div className={styles.flexRow}>
          <p>Description:</p>
          <input
            className={styles.input_text}
            type="text"
            value={desc}
            onChange={(e) => setDesc(e.target.value)}
          />
        </div>
        <div className={styles.flexRow}>
          <p>Price:</p>
          <input
            className={styles.input_text}
            type="number"
            value={price}
            onChange={(e) => setPrice(Number(e.target.value))}
          />
        </div>
        <div className={styles.flexRow}>
          <p>Version:</p>
          <input
            className={styles.input_text}
            type="text"
            value={version}
            onChange={(e) => setVersion(e.target.value)}
          />
        </div>
        <div className={styles.flexRow}>
          <p>Released:</p>
          <input
            className={styles.input_text}
            type="text"
            value={released}
            onChange={(e) => setReleased(e.target.value)}
          />
        </div>
        {item.type == "ALBUM" && (
          <>
            <div className={styles.flexRow}>
              <p>Discography:</p>
              <input
                className={styles.input_text}
                type="text"
                value={disc}
                onChange={(e) => setDisc(e.target.value)}
              />
            </div>
            <div className={styles.flexRow}>
              <p>Format:</p>
              <input
                className={styles.input_text}
                type="text"
                value={format}
                onChange={(e) => setFormat(e.target.value)}
              />
            </div>
            <div className={styles.flexRow}>
              <p>Color:</p>
              <input
                className={styles.input_text}
                type="text"
                value={color}
                onChange={(e) => setColor(e.target.value)}
              />
            </div>
          </>
        )}
        {item.type == "ASSET" && (
          <div className={styles.flexRow}>
            <p>Brand:</p>
            <input
              className={styles.input_text}
              type="text"
              value={brand}
              onChange={(e) => setBrand(e.target.value)}
            />
          </div>
        )}
        <input className={styles.submitButton} type="submit" />
      </form>
    </div>
  );
}
