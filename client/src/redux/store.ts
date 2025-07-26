import { configureStore, combineReducers } from "@reduxjs/toolkit";
import { useSelector, type TypedUseSelectorHook } from "react-redux";
import storage from "redux-persist/lib/storage"; // stockage local (localStorage)
import { persistReducer, persistStore } from "redux-persist";
import authSlice from "./features/authSlice";

// 1. Configuration de redux-persist
const persistConfig = {
    key: "root",
    storage,
};

// 2. Combine les reducers si tu veux en ajouter d'autres plus tard
const rootReducer = combineReducers({
    auth: authSlice,
});

// 3. Création du reducer persistant
const persistedReducer = persistReducer(persistConfig, rootReducer);

// 4. Création du store avec le middleware adapté
export const store = configureStore({
    reducer: persistedReducer,
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware({
            serializableCheck: {
                // Ignore redux-persist actions
                ignoredActions: ["persist/PERSIST", "persist/REHYDRATE", "persist/FLUSH", "persist/PAUSE", "persist/PURGE", "persist/REGISTER"],
            },
        }),
});

// 5. Création du persistor
export const persistor = persistStore(store);

// Types
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;
