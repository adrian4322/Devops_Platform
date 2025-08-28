import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Cluster from './pages/ClusterPage.tsx';
import Pod from './pages/PodPage.tsx';
import Header from './components/Header';
import './App.css';
import Node from './pages/NodePage.tsx';
import HomePage from './pages/HomePage.tsx';

function App() {

  return (
    <Router>
      <Header/>
      <Routes>
        <Route path="/" element={<HomePage/>} />
        <Route path="/clusters" element={<Cluster/>} />
        <Route path='/pods' element={<Pod/>} />
        <Route path='/nodes' element={<Node/>} />
      </Routes>
    </Router>
  )}

export default App
