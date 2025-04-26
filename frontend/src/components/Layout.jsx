import Header from "./Header";

function Layout({ children }) {
  return (
    <main>
      <Header />
      <div className="bg-gray-100 w-full h-dvh">{children}</div>
    </main>
  );
}

export default Layout;
