// Libraries
import React from "react";
import Link from "next/link";

// Components
// import { SearchButton } from "components/Searchbar";

// Styles
import styles from "./Page.module.css";

export default function Nav() {
  return (
    <div className={styles.navbar}>
      <Link href="/admin">
        <a className={styles.navcard}>Admin</a>
      </Link>
    </div>
  ); // End return
}
