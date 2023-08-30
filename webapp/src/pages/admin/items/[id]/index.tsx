import { GetServerSidePropsContext } from "next";

import ItemDetails from "components/ItemDetails";

const api_url = process.env.API_URL;
const api_dir = "/products/";

export default function Main({ data }: any) {
  return <ItemDetails item={data} />;
}

export async function getServerSideProps(context: GetServerSidePropsContext) {
  const { id } = context.params;

  const res = await fetch(api_url + api_dir + id);
  const data = await res.json();

  return {
    props: { data },
  };
}
