import { createSlice } from "@reduxjs/toolkit";

import type { Prestataire } from "../../interfaces/users/Prestataire";
import type Resident from "../../interfaces/users/Resident";


interface InitialState {
    userType : "STPM" | "Prestataire" | "Resident" | null,
    infos: null | Prestataire | Resident,
}

const initialState : InitialState = {
    userType : null,
    infos: null
};

const authSlice = createSlice({
    name: "auth",
    initialState,
    reducers: {
        loginInfos: (state, action) => {
            return {
                userType : state.userType,
                infos: action.payload
            };
        },
        loginType : (state, action) => {
            return {
                userType : action.payload,
                infos: state.infos
            };
        }
    }
});

export const { loginInfos, loginType } = authSlice.actions;

export default authSlice.reducer;