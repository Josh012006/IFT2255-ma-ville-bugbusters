import MyLink from "../../components/MyLink";


/**
 * Cette page affiche juste deux cartes pour laisser au résident le choix de ce qu'il veut faire.
 * Soit il veut voir ses signalements et les modifier ou créer un nouveau signalement.
 * On fait la redirection vers les pages appropriées dès que le choix est fait.
 * @returns ReactNode
 */
export default function ChoiceOptionSignalementPage() {
    return (
        <div>
            <h1 className="mt-5 mb-3 text-center">Que voulez-vous faire ?</h1>
            <div className="d-flex flex-column flex-lg-row justify-content-around align-items-center my-5">
                <MyLink to="/signalement/new" className="mycard3 orange text-white rounded-4 p-3 m-5 border d-flex align-items-center justify-content-center">
                    <img width="50" src="/new.png" alt="nouveau signalement" />
                    <h6 className="mx-3">Faire un nouveau signalement</h6>
                </MyLink>
                <MyLink to="/signalement/list" className="mycard3 orange text-white rounded-4 p-3 m-5 d-flex border align-items-center justify-content-center">
                    <img width="50" src="/signalement1.png" alt="voir signalements" />
                    <h6 className="mx-3">Voir mes signalements</h6>
                </MyLink>
            </div>
        </div>
    );
}