// Type
import type { AppProps } from "next/app";

// Imports
import Page from "components/Page";
import { SessionProvider } from "next-auth/react";

// Styles
import "styles/globals.css";
import { useEffect, useState } from "react";
import { Router } from "next/router";

function App({ Component, pageProps }: any) {

  const [load, setLoad] = useState(false);
  useEffect(() => {
    const begin = () => setLoad(true);
    const end = () => setLoad(false);

    Router.events.on("routeChangeStart", begin);
    Router.events.on("routeChangeComplete", end);
    Router.events.on("routeChangeError", end);
    return () => {
      Router.events.off("routeChangeStart", begin);
      Router.events.off("routeChangeComplete", end);
      Router.events.off("routeChangeError", end);
    }
  }, []);

  return (
    <Page>
      {load ? (<h2>Loading...</h2>) : <Component {...pageProps} />}
    </Page>
  );
}

export default App;
