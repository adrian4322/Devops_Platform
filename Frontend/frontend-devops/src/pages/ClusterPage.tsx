import FetchClusters from '../api/api_fetch';
import ButonAdaugareCluster from '../components/ButonAdaugareCluster';
import './ClusterPage.css';

function Cluster() {
    return (
        <div>
            <div style={{ padding: '2rem', textAlign: 'center' }}>
                <ButonAdaugareCluster />
            </div>
            <FetchClusters/>
        </div>
    )
   
}

export default Cluster;

