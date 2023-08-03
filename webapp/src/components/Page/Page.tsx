// Components
import Header from "./Header";
import Footer from "./Footer";

// Styles
import styles from "./Page.module.css";

export default function Page({ children }: any) {
  return (
    <>
      <Header />
      <main className={styles.container}>{children}</main>
      <Footer />
    </>
  );
}
