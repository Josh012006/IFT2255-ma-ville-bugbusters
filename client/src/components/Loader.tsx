import ClipLoader from "react-spinners/ClipLoader";

/**
 * Cette classe représente un spinner utile pour le loading d'informations.
 * @param color la couleur du spinner
 * @param size la taille du spinner
 * @returns ReactNode
 */
export default function Loader({color="#FF5659", size=50} : {color? : string, size?: number}) {
    return (
        <div className="d-flex align-items-center justify-content-center p-1">
            <ClipLoader size={size} color={color} cssOverride={{ margin: 'auto', marginTop: '4px', marginBottom: '4px' }} />
        </div>
    );
}