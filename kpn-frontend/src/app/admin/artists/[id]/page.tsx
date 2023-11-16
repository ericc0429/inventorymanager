import { getArtist } from "@/utils/getArtist";

import ArtistDetails from "components/ArtistDetails";

export default async function Main({
  params: {id},
}: {
  params: {id: string}
}) {
  const data = await getArtist(id);
  return <ArtistDetails artist={data} />;
}
