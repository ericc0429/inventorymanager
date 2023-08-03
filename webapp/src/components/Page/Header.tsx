// Libraries
import React from "react";
import Head from "next/head";
import Link from "next/link";

// Components
import Nav from "./Nav";

// Styles
import styles from "./Page.module.css";
import Image from "next/image";

function Header() {
  return (
    <header>
      <Head>
        <title>K-POP NARA</title>
        <meta name="description" content="K-POP Store" />
        <link rel="icon" href="kpn_logo.svg" />
      </Head>

      <div className={styles.headerBar}>
        <div className={styles.header}>
          <Link href="/">
            <a className={styles.headerText}>K-POP NARA</a>
          </Link>
        </div>

        <Nav />
      </div>
    </header>
  ); // End return
}

export default Header;
