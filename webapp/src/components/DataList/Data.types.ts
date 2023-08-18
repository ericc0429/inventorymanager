export interface IStockListProps {
  stocks: IStock[];
}
export interface IArtistListProps {
  artists: IArtist[];
}

export interface IStock {
  id: string;
  location: string;
  product: string;
  exclusive: boolean;
  count: number;
  restock_threshold: number;
  oos_date: string;
  ordered: boolean;
  order_date: string;
  tracking: string;
}
export interface IArtist {
  id: string;
  name: string;
  type: string;
  gender: string;
  debut: string;
  albums: string[];
  assets: string[];
  members: string[];
  birthday: string;
  group: string[];
}
