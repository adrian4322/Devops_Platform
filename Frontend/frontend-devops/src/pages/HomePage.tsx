import { useState, useEffect } from 'react';
import './HomePage.css';

interface DashboardStats {
    clusters: number;
    nodes: number;
    pods: number;
}

function HomePage() {
    const [stats, setStats] = useState<DashboardStats>({ clusters: 0, nodes: 0, pods: 0 });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchStats = async () => {
            try {
                const [clustersRes, nodesRes, podsRes] = await Promise.all([
                    fetch('http://localhost:8080/api/clusters'),
                    fetch('http://localhost:8080/api/nodes'),
                    fetch('http://localhost:8080/api/pod')
                ]);

                const clusters = clustersRes.ok ? await clustersRes.json() : [];
                const nodes = nodesRes.ok ? await nodesRes.json() : [];
                const pods = podsRes.ok ? await podsRes.json() : [];

                setStats({
                    clusters: clusters.length,
                    nodes: nodes.length,
                    pods: pods.length
                });
            } catch (error) {
                console.error('Error fetching stats:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchStats();
    }, []);

    if (loading) {
        return (
            <div className="home-page">
                <div className="loading-container">
                    <div className="loading-spinner"></div>
                    <div className="loading-text">Se încarcă tabloul de bord...</div>
                </div>
            </div>
        );
    }

    return (
        <div className="home-page">
            <div className="home-header">
                <h1 className="home-title">Tabloul de bord Platformă DevOps</h1>
                <p className="home-subtitle">Monitorizează și gestionează infrastructura ta Kubernetes</p>
            </div>

            <div className="stats-grid">
                <div className="stat-card clusters">
                    <div className="stat-icon">🌐</div>
                    <div className="stat-content">
                        <div className="stat-number">{stats.clusters}</div>
                        <div className="stat-label">Clustere</div>
                    </div>
                </div>

                <div className="stat-card nodes">
                    <div className="stat-icon">🖥️</div>
                    <div className="stat-content">
                        <div className="stat-number">{stats.nodes}</div>
                        <div className="stat-label">Noduri</div>
                    </div>
                </div>

                <div className="stat-card pods">
                    <div className="stat-icon">📦</div>
                    <div className="stat-content">
                        <div className="stat-number">{stats.pods}</div>
                        <div className="stat-label">Poduri</div>
                    </div>
                </div>
            </div>

        </div>
    );
}

export default HomePage;
