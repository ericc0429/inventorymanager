// Libraries
import React, { useState } from "react";
import Link from "next/link";

// Components
import { IArtistListProps } from "components/DataList";
import Card from "components/Card";
import ArtistCard from "./ArtistCard"

import styles from "components/DataList/DataList.module.css";

export default function ArtistList({ artists }: IArtistListProps) {
  const [expandFilter, setExpandFilter] = useState(false);
  const [name, setName] = useState("");

  const expandHandler = () => {
    setExpandFilter((currExpand) => !currExpand);
  };

  return (
    <>
      <div className={styles.container}>
        <div className={styles.shcontainer}>
          <p className={styles.nopad}>Filter</p>
          <button className={styles.showhide} onClick={expandHandler}>
            {expandFilter ? "Collapse" : "Expand"}
          </button>
        </div>

        {/* <form onSubmit={(e) => setFilter}>
        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
        <select onChange={(e) => setType}>
          <option>--</option>
          <option value={"ARTIST"}>Artist</option>
          <option value={"GROUP"}>Group</option>
          <option value={"SUBUNIT"}>Subunit</option>
          <option value={"NONE"}>None</option>
        </select>
        <button type="submit">apply filter</button>
      </form> */}
        {expandFilter && (
          <>
            Name:
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
          </>
        )}
      </div>
      <div className={styles.list}>
        <div className={styles.card}>
          <p className={styles.property}>Type</p>
          <p className={styles.property}>Name</p>
          <p className={styles.property}>Gender</p>
          <p className={styles.property}>Debut Date</p>
          <p className={styles.property}>Albums</p>
          <p className={styles.property}>Assets</p>
          <p className={styles.property}>Group/Members</p>
          <p className={styles.property}>Birthday</p>
        </div>
        {artists &&
          artists
            .filter((artist) =>
              artist.name.toLowerCase().includes(name.toLowerCase())
            )
            .map((artist) => (
              <Link
                href={"/admin/artists/".concat(artist.id.toString())}
                key={artist.id}
              >
                <a>
                  <ArtistCard data={artist} />
                </a>
              </Link>
            ))}
      </div>
    </>
  );
}
