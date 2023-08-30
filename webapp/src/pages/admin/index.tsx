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
      <div className={styles.body}>
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
