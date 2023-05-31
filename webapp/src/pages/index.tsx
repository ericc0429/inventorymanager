// Styles
import Layout from "components/Layout/Layout";
import styles from "styles/Home.module.css";

export default function Main()
{
  return (
    <Layout>
      <div className={styles.main}>
        <h1 className={styles.title}>HOMEPAGE</h1>
      </div>
    </Layout>
  );
}