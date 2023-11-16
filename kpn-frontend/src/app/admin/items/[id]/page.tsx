import ItemDetails from "components/ItemDetails";
import { getItem } from "@/utils/getItem";

const api_url = process.env.API_URL;
const api_dir = "/products/";

export default async function Main({
  params: {id},
}: {
  params: {id: string}
}) {
  const data = await getItem(id);
  return <ItemDetails item={data} />;
}
