import { Link, useLocation } from 'react-router-dom';
import "./Header.css"

function Header() {
  const location = useLocation();

  return (
    <header className="header">
      <nav className="nav">
        <div className="logo">Platforma DevOps</div>
        <ul className="nav-links">
          <li className={location.pathname === '/' ? 'active' : ''}>
            <Link to="/">Acasa</Link>
          </li>
          <li className={location.pathname === '/clusters' ? 'active' : ''}>
            <Link to="/clusters">Clustere</Link>
          </li>
          <li className={location.pathname === '/nodes' ? 'active' : ''}>
            <Link to="/nodes">Noduri</Link>
          </li>
          <li className={location.pathname === '/pods' ? 'active' : ''}>
            <Link to="/pods">Poduri</Link>
          </li>
        </ul>
      </nav>
    </header>
  );
}

export default Header;