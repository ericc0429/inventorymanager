// Type
import type { AppProps } from "next/app";

// Imports
import Page from "components/Page";
import { SessionProvider } from "next-auth/react";

// Styles
import "styles/globals.css";

function App({ Component, pageProps: { session, pageProps } }: AppProps) {
  return (
    <Page>
      <Component {...pageProps} />
    </Page>
  );
}

export default App;
