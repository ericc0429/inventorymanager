// Components
import Header from "./Header";
import Footer from "./Footer";

// Styles
import styles from "./Layout.module.css";

export default function Layout({ children }: any) {
  return (
    <>
      <Header />
      <main className={styles.container}>{children}</main>
      <Footer />
    </>
  );
}
