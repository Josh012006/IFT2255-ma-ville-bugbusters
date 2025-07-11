import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./components/pages/Home";
import MainLayout from "./components/layout/MainLayout";

function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<MainLayout />}>
          <Route index element={<Home />} />
          {/* <Route path="about" element={<About />} />  Just an example for you guys to see how to add a new page!*/}
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
