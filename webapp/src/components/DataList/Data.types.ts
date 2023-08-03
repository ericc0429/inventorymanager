export interface IDataListProps {
  stocks: IStock[];
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
