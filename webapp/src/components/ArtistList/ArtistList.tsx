// Libraries
import Link from "next/link";

// Components
import { IArtistListProps } from "components/DataList";
import Card from "components/Card";

import styles from "components/DataList/DataList.module.css";

export default function ArtistList({ artists }: IArtistListProps) {
  return (
    <div className={styles.list}>
      <div className={styles.card}>
        <p className={styles.property}>Name</p>
        <p className={styles.property}>Type</p>
        <p className={styles.property}>Gender</p>
        <p className={styles.property}>Debut Date</p>
        <p className={styles.property}>Albums</p>
        <p className={styles.property}>Assets</p>
        <p className={styles.property}>Members</p>
        <p className={styles.property}>Birthday</p>
        <p className={styles.property}>Group</p>
      </div>
      {artists &&
        artists.map((artist) => (
          <Link href={"/artist/".concat(artist.id.toString())} key={artist.id}>
            <Card data={artist} />
          </Link>
        ))}
    </div>
  );
}
