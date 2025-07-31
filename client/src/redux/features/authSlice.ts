import { createSlice } from "@reduxjs/toolkit";

import type { Prestataire } from "../../interfaces/users/Prestataire";
import type Resident from "../../interfaces/users/Resident";


interface InitialState {
    has: boolean,
    userType : "stpm" | "Prestataire" | "Resident" | null,
    infos: null | Prestataire | Resident,
}

const initialState : InitialState = {
    has: false,
    userType : null,
    infos: null
};

/**
 * Le slice pour la gestion de l'authentification. Permet de stocker et d'update les informations sur l'utilisateur
 * actuellement connecté. Très utile pour assurer la sécurité entre les pages et les rôles.
 */
const authSlice = createSlice({
    name: "auth",
    initialState,
    reducers: {
        loginInfos: (state, action) => {
            return {
                has : state.has,
                userType : state.userType,
                infos: action.payload
            };
        },
        loginType : (state, action) => {
            return {
                has : state.has,
                userType : action.payload,
                infos: state.infos
            };
        },
        updateHas : (state, action) => {
            return {
                has : action.payload,
                userType : state.userType,
                infos : state.infos
            }
        }
    }
});

export const { loginInfos, loginType, updateHas } = authSlice.actions;

export default authSlice.reducer;