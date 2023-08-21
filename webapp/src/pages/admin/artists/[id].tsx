import { GetServerSidePropsContext } from "next";

import ArtistDetails from "components/ArtistDetails";

const api_url = process.env.API_URL;
const api_dir = "/artists/";

export default function Main({ data }: any) {
  console.log(data);
  return <ArtistDetails artist={data} />;
}

export async function getServerSideProps(context: GetServerSidePropsContext) {
  const { id } = context.params;

  const res = await fetch(api_url + api_dir + id);
  const data = await res.json();

  return {
    props: { data },
  };
}
