import { createSlice } from "@reduxjs/toolkit";

import type { Prestataire } from "../../interfaces/users/Prestataire";
import type Resident from "../../interfaces/users/Resident";


interface InitialState {
    infos: null | Prestataire | Resident,
}

const initialState : InitialState = {
    infos: null
};

const authSlice = createSlice({
    name: "auth",
    initialState,
    reducers: {
        login: (state, action) => {
            // Update le state avec les informations du user
            return {
                infos: action.payload
            };
        },
    }
});

export const { login } = authSlice.actions;

export default authSlice.reducer;