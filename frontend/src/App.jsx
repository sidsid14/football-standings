import "./App.css";
import { createBrowserRouter, RouterProvider } from "react-router";
import NotFound from "./pages/NotFound";
import Layout from "./components/Layout";
import FootballStandings from "./pages/FootballStandings/FootballStandings";

const router = createBrowserRouter([
  {
    path: "/",
    element: (
      <Layout>
        <FootballStandings />
      </Layout>
    ),
  },
  {
    path: "*",
    element: (
      <Layout>
        <NotFound />
      </Layout>
    ),
  },
]);

function App() {
  return <RouterProvider router={router}></RouterProvider>;
}

export default App;
