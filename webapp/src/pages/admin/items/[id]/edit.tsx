import { GetServerSidePropsContext } from "next";

import EditItem from "components/ItemEdit";

const api_url = process.env.API_URL;
const api_dir = "/products/";

export default function Main({ data }: any) {
  return <EditItem item={data} />;
}

export async function getServerSideProps(context: GetServerSidePropsContext) {
  const { id } = context.params;

  const res = await fetch(api_url + api_dir + id);
  const data = await res.json();

  return {
    props: { data },
  };
}
