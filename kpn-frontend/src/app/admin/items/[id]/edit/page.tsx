import { getItem } from "@/utils/getItem";

import EditItem from "components/ItemEdit";


export default async function Main({
  params: {id},
}: {
  params: {id: string}
}) {
  const data = await getItem(id)
  return <EditItem item={data} />;
}