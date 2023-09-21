import { GetServerSidePropsContext } from "next";
import { ParsedUrlQuery } from "querystring";

import ArtistDetails from "components/ArtistDetails";

const api_url = process.env.API_URL;
const api_dir = "/artists/";

interface Params extends ParsedUrlQuery {
  id: string;
}

export default function Main({ data }: any) {
  return <ArtistDetails artist={data} />;
}

export async function getServerSideProps(context: GetServerSidePropsContext) {
  const { id } = context.params as Params;

  const res = await fetch(api_url + api_dir + id);
  const data = await res.json();

  return {
    props: { data },
  };
}
