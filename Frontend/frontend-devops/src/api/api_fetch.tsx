import {useState, useEffect} from 'react';
import '../pages/ClusterPage.css';
import '../pages/NodePage.css';
import '../pages/PodPage.css';

interface Cluster {
    id : number;
    name : string;
    endpoint : string;
    token : string;
    status : string;
    nodesList : Node[];
}

interface Node {
    id : number;
    name : string;
    status : string;
    podsList : Pod[];
}

interface Pod {
    id : number;
    name : string;
    namespace : string;
    nodeName : string;
    phase : string;
    podIP : string;
    hostIP : string;
    restartCount : number;
}

const API_URL_CLUSTERS = 'http://localhost:8080/api/clusters';
const API_URL_NODES = 'http://localhost:8080/api/nodes'
const API_URL_PODS = 'http://localhost:8080/api/pod'

const FetchPods = () => {
    const [pods, setPods] = useState<Pod[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch(API_URL_PODS)
        .then((res) => {
            if (!res.ok) throw new Error('Failed to fetch pods');
            return res.json();
        })
        .then((data) => {
            console.log(data);
            setPods(data);
            setLoading(false);
        })
        .catch((error) => {
            console.error('Error fetching pods:', error);
            setLoading(false);
        });
    }, []);

    const getPhaseClass = (phase: string) => {
        switch (phase?.toLowerCase()) {
            case 'running': return 'phase-running';
            case 'pending': return 'phase-pending';
            case 'succeeded': return 'phase-succeeded';
            case 'failed': return 'phase-failed';
            default: return 'phase-unknown';
        }
    };

    if (loading) {
        return (
            <div className="loading-container">
                <div className="loading-spinner"></div>
                <div className="loading-text">Se încarcă podurile...</div>
            </div>
        );
    }

    return (
        <div className="pod-page">
            <div className="pod-header">
                <h1 className="pod-title">Poduri</h1>
                <p className="pod-subtitle">Gestionează și monitorizează podurile Kubernetes</p>
            </div>
            
            {pods.length === 0 ? (
                <div className="empty-state">
                    <div className="empty-state-icon">📦</div>
                    <div className="empty-state-text">Nu au fost găsite poduri</div>
                    <div className="empty-state-subtext">Momentan nu rulează niciun pod în clusterele tale</div>
                </div>
            ) : (
                <div className="pod-grid">
                    {pods.map((pod) => (
                        <div key={pod.id} className="pod-card">
                            <div className="pod-name">
                                <div className="pod-icon">P</div>
                                {pod.name}
                            </div>
                            <div className="pod-details">
                                <div className="pod-detail">
                                    <span className="detail-label">Spațiu de nume</span>
                                    <span className="detail-value namespace-badge">{pod.namespace}</span>
                                </div>
                                <div className="pod-detail">
                                    <span className="detail-label">Stare</span>
                                    <span className={`pod-phase ${getPhaseClass(pod.phase)}`}>
                                        {pod.phase || 'Necunoscut'}
                                    </span>
                                </div>
                                <div className="pod-detail">
                                    <span className="detail-label">Nod</span>
                                    <span className="detail-value">{pod.nodeName}</span>
                                </div>
                                <div className="pod-detail">
                                    <span className="detail-label">IP Pod</span>
                                    <span className="detail-value pod-ip">{pod.podIP}</span>
                                </div>
                                <div className="pod-detail">
                                    <span className="detail-label">IP Gazdă</span>
                                    <span className="detail-value pod-ip">{pod.hostIP}</span>
                                </div>
                                <div className="pod-detail">
                                    <span className="detail-label">Reporniri</span>
                                    <span className="detail-value restart-count">{pod.restartCount}</span>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export {FetchPods};

const FetchNodes = () => {
    const [nodes, setNodes] = useState<Node[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch(API_URL_NODES)
        .then((res) => {
            if (!res.ok)
                 throw new Error('Failed to fetch nodes');
            return res.json();
        })
        .then((data) => {
            console.log(data);
            setNodes(data);
            setLoading(false);
        })
        .catch((error) => {
            console.error('Error fetching nodes:', error);
            setLoading(false);
        });
    },[]);

    const getStatusClass = (status: string) => {
        switch (status?.toLowerCase()) {
            case 'ready': return 'status-ready';
            case 'notready': return 'status-not-ready';
            default: return 'status-unknown';
        }
    };

    if (loading) {
        return (
            <div className="loading-container">
                <div className="loading-spinner"></div>
                <div className="loading-text">Se încarcă nodurile...</div>
            </div>
        );
    }

    return (
        <div className="node-page">
            <div className="node-header">
                <h1 className="node-title">Noduri</h1>
                <p className="node-subtitle">Monitorizează nodurile clusterului Kubernetes</p>
            </div>
            
            {nodes.length === 0 ? (
                <div className="empty-state">
                    <div className="empty-state-icon">🖥️</div>
                    <div className="empty-state-text">Nu au fost găsite noduri</div>
                    <div className="empty-state-subtext">Momentan nu există noduri în clusterele tale</div>
                </div>
            ) : (
                <div className="node-grid">
                    {nodes.map((node) => (
                        <div key={node.id} className="node-card">
                            <div className="node-name">
                                <div className="node-icon">N</div>
                                {node.name}
                            </div>
                            <div className="node-details">
                                <div className="node-detail">
                                    <span className="detail-label">Stare</span>
                                    <span className={`node-status ${getStatusClass(node.status)}`}>
                                        {node.status || 'Necunoscut'}
                                    </span>
                                </div>
                                <div className="node-detail">
                                    <span className="detail-label">Poduri</span>
                                    <span className="detail-value pod-count">
                                        {node.podsList?.length || 0} poduri
                                    </span>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export {FetchNodes};

const FetchClusters = () => {
    const [clusters, setClusters] = useState<Cluster[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch(API_URL_CLUSTERS)
        .then((res) => {
            if (!res.ok) throw new Error('Failed to fetch clusters');
            return res.json();
        })
        .then((data) => {
            console.log(data);
            setClusters(data);
            setLoading(false);
        })
        .catch((error) => {
            console.error('Error fetching clusters:', error);
            setLoading(false);
        });
    }, []);

    const getStatusClass = (status: string) => {
        switch (status?.toLowerCase()) {
            case 'active': return 'status-active';
            case 'inactive': return 'status-inactive';
            case 'pending': return 'status-pending';
            default: return 'status-unknown';
        }
    };

    if(loading) {
        return (
            <div className="loading-container">
                <div className="loading-spinner"></div>
                <div className="loading-text">Se încarcă clusterele...</div>
            </div>
        );
    }

    return (
        <div className="cluster-page">
            <div className="cluster-header">
                <h1 className="cluster-title">Clustere</h1>
                <p className="cluster-subtitle">Gestionează clusterele Kubernetes</p>
            </div>
            
            {clusters.length === 0 ? (
                <div className="empty-state">
                    <div className="empty-state-icon">🌐</div>
                    <div className="empty-state-text">Nu au fost găsite clustere</div>
                    <div className="empty-state-subtext">Momentan nu există clustere configurate</div>
                </div>
            ) : (
                <div className="cluster-grid">
                    {clusters.map((cluster) => (
                        <div key={cluster.id} className="cluster-card">
                            <div className="cluster-name">{cluster.name}</div>
                            <div className="cluster-details">
                                <div className="cluster-detail">
                                    <span className="detail-label">Stare</span>
                                    <span className={`cluster-status ${getStatusClass(cluster.status)}`}>
                                        {cluster.status || 'Necunoscut'}
                                    </span>
                                </div>
                                <div className="cluster-detail">
                                    <span className="detail-label">Endpoint</span>
                                    <span className="detail-value">{cluster.endpoint}</span>
                                </div>
                                <div className="cluster-detail">
                                    <span className="detail-label">Noduri</span>
                                    <span className="detail-value">
                                        {cluster.nodesList?.length || 0} noduri
                                    </span>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default FetchClusters;