import { configureStore, combineReducers } from "@reduxjs/toolkit";
import { useSelector, type TypedUseSelectorHook } from "react-redux";
import storageSession from "redux-persist/lib/storage/session"; 
import { persistReducer, persistStore } from "redux-persist";
import authSlice from "./features/authSlice";

const persistConfig = {
    key: "root",
    storage: storageSession, 
};

const rootReducer = combineReducers({
    auth: authSlice,
});

const persistedReducer = persistReducer(persistConfig, rootReducer);

/**
 * Il s'agit du store qui stocke le sinformations sur l'utilisateur de l'application.
 * Il implémente aussi une persistance de ses données pendant la durée de la session 
 * de connexion.
 */
export const store = configureStore({
    reducer: persistedReducer,
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware({
            serializableCheck: {
                ignoredActions: [
                    "persist/PERSIST",
                    "persist/REHYDRATE",
                    "persist/FLUSH",
                    "persist/PAUSE",
                    "persist/PURGE",
                    "persist/REGISTER",
                ],
            },
        }),
});

export const persistor = persistStore(store);

// Types
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;
