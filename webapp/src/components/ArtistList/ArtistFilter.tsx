import React, { useState } from "react";

import { IArtist, IArtistListProps } from "components/DataList";
import styles from "components/DataList/DataList.module.css";
import Link from "next/link";
import Card from "components/Card/Card";

export default function ArtistFilter({ artists }: IArtistListProps) {
  const [expandFilter, setExpandFilter] = useState(false);
  const [name, setName] = useState("");
  // const [type, setType] = useState();
  // const [filter, setFilter] = useState<IArtist>();

  const expandHandler = () => {
    setExpandFilter((currExpand) => !currExpand);
  };
  /* const filterHandler = (e: any) => {
    e.preventDefault();
    setFilter(e);
  }; */

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
                <Card data={artist} />
              </a>
            </Link>
          ))}
    </>
  );
}
