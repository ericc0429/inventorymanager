'use client'

import React, { useEffect, useState } from "react";

// Components
import AdminPanel from "components/Admin";
import Layout from "components/Page/Page";
import Link from "next/link";

// Styles
import styles from "styles/AdminPanel.module.css";

const api_url = process.env.API_URL;

export default function Admin() {
  const [loading, setLoading] = useState(false);

  const pullFromSquare = () => {
    console.log("Pulling inv data");
    setLoading(true);
    /* const res =  */
    fetch("http://localhost:8080" + "/square/inventory", {
      method: "POST",
    })
      .then((res) => res.json())
      .then((data) => console.log(data))
      .catch((err) => console.log(err));
    setLoading(false);
  };

  return (
    <div className={styles.container}>
      <div className={styles.body}>
        {!loading && <button onClick={pullFromSquare}>Import Data</button>}

        <p>Note: Filtering only works on name at the moment</p>
        <p>
          Things left to do: <br />
          Add multifaceted filtering -- filter works but only for name <br />
          Finish edit functionality <br />
          Expand functionality to all pages <br />
        </p>
        <p>
          This is the homepage of the admin panel. Only visible when logged in.
        </p>
      </div>
    </div>
  );
}
