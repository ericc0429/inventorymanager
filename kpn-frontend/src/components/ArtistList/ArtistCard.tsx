import { IArtist } from "components/DataList";

import styles from "components/Card/Card.module.css";

interface IArtistCardProps {
  data: IArtist;
}

export default function ItemCard({ data }: IArtistCardProps) {
  return (
    <div className={styles.card}>
      <p className={styles.property}>{data.type}</p>
      <p className={styles.property}>{data.name}</p>
      <p className={styles.property}>{data.gender}</p>
      <p className={styles.property}>{data.debut}</p>
      <p className={styles.property}>{data.albums}</p>
      <p className={styles.property}>{data.assets}</p>
      <p className={styles.property}>{(data.type == "ARTIST") ? printArtists(data.group) : printArtists(data.members)}</p>
      <p className={styles.property}>{data.birthday}</p>
      <p className={styles.property}>{data.group}</p>
    </div>
  );
}

function printArtists(artists: string[]) {
  var out = "";
  artists.forEach((artist: string) => out += artist + "\n")
  return out;
}