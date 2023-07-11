import React, { useEffect, useState } from "react";

// Components
import AdminPanel from "components/Admin";
import Layout from "components/Layout/Layout";

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
  return <AdminPanel />;
}
