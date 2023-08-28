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
      <Link href="/admin">
        <a className={styles.snavcard}>Admin Home</a>
      </Link>
      <Link href="/admin/artists">
        <a className={styles.snavcard}>Artists</a>
      </Link>
      <Link href="/admin/items">
        <a className={styles.snavcard}>Items</a>
      </Link>
      <Link href="/admin/stock">
        <a className={styles.snavcard}>Stock Alerts</a>
      </Link>
    </div>
  ); // End return
}
