function Header() {
  return (
    <nav className="flex items-center justify-between flex-wrap bg-white px-6 py-4  shadow-md">
      <div className="flex items-center flex-shrink-0 text-gray-600 ml-6">
        <img
          src="https://upload.wikimedia.org/wikipedia/commons/thumb/d/d3/Soccerball.svg/800px-Soccerball.svg.png"
          alt="Logo"
          className="h-8 w-8 mr-2"
        />
        <span className="font-semibold text-xl tracking-tight ml-2">
          Football Stats
        </span>
      </div>
      <div className="block lg:hidden">
        <button className="flex items-center px-3 py-2 border rounded text-teal-200 border-teal-400 hover:text-black hover:border-white">
          <svg
            className="fill-current h-3 w-3"
            viewBox="0 0 20 20"
            xmlns="http://www.w3.org/2000/svg"
          >
            <title>Menu</title>
            <path d="M0 3h20v2H0V3zm0 6h20v2H0V9zm0 6h20v2H0v-2z" />
          </svg>
        </button>
      </div>
    </nav>
  );
}

export default Header;
