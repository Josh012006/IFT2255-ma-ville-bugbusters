import { useEffect, useState } from "react";
import { Pagination } from "@mui/material";

interface PaginationWrapperProps<T> {
  /**qui représente le tableau initial */
  data: T[];
  /**qui représente un setter dans la classe qui utilise le composant pour set un state qui est censé 
  * contenir les éléments de la page de la pagination actuellement affichée */
  setPaginatedData: (data: T[]) => void;
  /**qui précise le nombre maximal d'éléments par page. */
  itemsPerPage?: number;
}


/**
 * La classe qui s'occupe de la gestion de la pagination pour toutes les listes de l'application.
 * @return ReactNode
 */
export default function MyPagination<T>(props: PaginationWrapperProps<T>) {
    const [page, setPage] = useState(1);

    const {
        data,
        setPaginatedData,
        itemsPerPage = 5
    } = props;

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
