// Components
import Header from "./Header";
import Footer from "./Footer";

// Styles
import styles from "./Page.module.css";
import SideNav from "./SideNav";

export default function Page({ children }: any) {
  return (
    <>
      <Header />
      <div className={styles.row}>
        <SideNav />
        <main className={styles.container}>{children}</main>
      </div>
      <Footer />
    </>
  );
}
