import ClipLoader from "react-spinners/ClipLoader";


interface LoaderProps {
    /**La couleur du spinner. #FF5659 par défaut pour matcher le style de l'application. */
    color? : string, 
    /**La taille du spinner. Égale à 50 par défaut. */
    size?: number
}

/**
 * Cette classe représente un spinner utile pour le loading d'informations.
 * @returns ReactNode
 */
export default function Loader(props : LoaderProps) {

    const { color="#FF5659", size=50 } = props;

    return (
        <div className="d-flex align-items-center justify-content-center p-1">
            <ClipLoader size={size} color={color} cssOverride={{ margin: 'auto', marginTop: '4px', marginBottom: '4px' }} />
        </div>
    );
}