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
      <Link className={styles.navcard} href="/admin">
        Admin
      </Link>
    </div>
  ); // End return
}
