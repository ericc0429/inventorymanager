// Libraries
import React from "react";
import Link from "next/link";

// Components
// import { SearchButton } from "components/Searchbar";

// Styles
import styles from "./Page.module.css";

export default function SideNav() {
  return (
    <div className={styles.snavbar}>
      <Link className={styles.snavcard} href="/admin">
        Admin Home
      </Link>
      <Link className={styles.snavcard} href="/admin/artists">
        Artists
      </Link>
      <Link className={styles.snavcard} href="/admin/items">
        Items
      </Link>
      <Link className={styles.snavcard} href="/admin/stock">
        Stock Dat
      </Link>
      <Link className={styles.snavcard} href="/admin/lowstock">
        Stock Alerts
      </Link>
    </div>
  ); // End return
}
