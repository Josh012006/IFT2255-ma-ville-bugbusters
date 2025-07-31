import { useEffect, useState } from "react";
import { Pagination } from "@mui/material";

interface PaginationWrapperProps<T> {
  data: T[];
  setPaginatedData: (data: T[]) => void;
  itemsPerPage?: number;
}


/**
 * La classe qui s'occupe de la gestion de la pagination pour toutes les listes de l'application.
 * @param data qui représente le tableau initial
 * @param setPaginatedData qui représente un setter dans la classe qui utilise le composant pour set un state qui est censé 
 * contenir les éléments de la page de la pagination actuellement affichée
 * @param itemsPerPage qui précise le nombre maximal d'éléments par page.
 * @return ReactNode
 */
export default function MyPagination<T>({
  data,
  setPaginatedData,
  itemsPerPage = 5
}: PaginationWrapperProps<T>) {
  const [page, setPage] = useState(1);

  useEffect(() => {
    const start = (page - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    setPaginatedData(data.slice(start, end));
  }, [data, page, itemsPerPage, setPaginatedData]);

  const totalPages = Math.ceil(data.length / itemsPerPage);

  const handleChange = (_: React.ChangeEvent<unknown>, value: number) => {
    setPage(value);
  };

  if (data.length <= itemsPerPage) return null; // Pas de pagination nécessaire

  return (
    <div className="m-4 d-flex align-items-center justify-content-center">
      <Pagination
        count={totalPages}
        page={page}
        onChange={handleChange}
        color="primary"
      />
    </div>
  );
}
