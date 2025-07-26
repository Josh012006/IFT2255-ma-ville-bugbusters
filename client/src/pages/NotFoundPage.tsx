import MyLink from "../components/MyLink";


/**
 * Cette page s'affiche lorsque la route accédée n'est pas définie.
 * @returns ReactNode
 */
export default function NotFoundPage() {
    return (
        <div className="d-flex flex-column min-vh-100 min-vw-100 align-items-center justify-content-center">
            <div className="m-5 d-flex flex-column align-items-center justify-content-center">
                <h1 className="giant">404</h1>
                <p className="fs-3">Page inconnue!</p>
            </div>
            <MyLink to="/" className="rounded-4 text-white orange p-3">Go back home</MyLink>
        </div>
    );
}