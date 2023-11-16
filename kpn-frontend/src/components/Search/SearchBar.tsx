// Libraries
import React, { useState } from "react";
import { useRouter } from "next/router";

// Styles
import styles from "./SearchBar.module.css";
import { IArtistListProps, IProductListProps, IStockListProps } from "components/DataList";

interface ISearchBarProps {
  placeholder: string;
  data: IArtistListProps | IProductListProps | IStockListProps;
}

function SearchBar({placeholder, data}: ISearchBarProps) {
  const [query, setQuery] = useState("");

/*   const router = useRouter();

  const handleSubmit = (e: any) => {
    e.preventDefault();
    router.push(`/search/${query}`);
  }; */

  const router = useRouter();

  const handleSubmit = (e: any) => {
    e.preventDefault();
    router.push(`/admin/${placeholder}`);
  }

  return (
    <form className={styles.search} onSubmit={handleSubmit}>
      <input
        type="text"
        className={styles.input}
        placeholder={"search " + placeholder}
        value={query}
        autoFocus
        onChange={(e) => setQuery(e.target.value)}
      />

      <button type="submit" className={styles.searchBtn}>
        Search
      </button>
    </form>
  );
}

export default SearchBar;
