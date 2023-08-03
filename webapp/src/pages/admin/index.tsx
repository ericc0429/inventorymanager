import React, { useEffect, useState } from "react";

// Components
import AdminPanel from "components/Admin";
import Layout from "components/Page/Page";
import Link from "next/link";

// Styles
import styles from "styles/AdminPanel.module.css";

/* export default function Admin() {
  const [groups, setGroups] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);

    fetch("api/group")
      .then((response) => response.json())
      .then((data) => {
        setGroups(data);
        setLoading(false);
      });
  }, []);

  if (loading) {
    return <p>Loading...</p>;
  }

  return (
    <Layout>
      <AdminPanel />
      <h2>Groups</h2>
      {groups.map((group) => (
        <div key={group.id}>{group.name}</div>
      ))}
    </Layout>
  );
} */
export default function Admin() {
  return (
    <div className={styles.container}>
      <div className={styles.list}>
        <Link href="/admin/artists">
          <a className={styles.link}>Artists</a>
        </Link>
        <Link href="/admin/items">
          <a className={styles.link}>Items</a>
        </Link>
        <Link href="/admin/stock">
          <a className={styles.link}>Stock</a>
        </Link>
      </div>
      <div className={styles.body}>
        This is the homepage of the admin panel. Only visible when logged in.
      </div>
    </div>
  );
}
