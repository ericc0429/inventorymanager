import { IArtist } from "components/DataList";

interface IArtistPageProps {
  artist: IArtist;
}

export default function ArtistDetails({ artist }: IArtistPageProps) {
  var isGroup = artist.type == "GROUP" || artist.type == "SUBUNIT";

  return (
    <div>
      <h1>{artist.name}</h1>
      <p>{"Gender: " + artist.gender}</p>
      <p>{"Debut: " + artist.debut}</p>
      <p>{"Albums: \n" + artist.albums.map((album) => album + "\n")}</p>
      <p>{"Assets: \n" + artist.assets.map((asset) => asset + "\n")}</p>
      {isGroup && (
        <p>{"Members: \n" + artist.members.map((member) => member + "\n")}</p>
      )}
      {!isGroup && (
        <>
          <p>{"Group(s): \n" + artist.group.map((group) => group + "\n")}</p>
          <p>{"Birthday: " + artist.birthday}</p>
        </>
      )}
    </div>
  );
}
