import './ButonAdaugareCluster.css';
import { useState } from 'react';

interface ClusterFormData {
    name: string;
    endpoint: string;
    token: string;
}

function ButonAdaugareCluster() {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [formData, setFormData] = useState<ClusterFormData>({
        name: '',
        endpoint: '',
        token: ''
    })

    const [isLoading, setIsLoading] = useState(false);
    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const {name, value} = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        try {
            const response = await fetch("http://localhost:8080/api/clusters/create", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });

            if(response.ok) {
                setFormData({name: '', endpoint: '', token: ''});
                setIsModalOpen(false);
                window.location.reload();
            } else {
                throw new Error('Failed to add cluster')
            }
        }catch (error) {
            console.error('Error adding cluster:', error);
        } finally {
            setIsLoading(false);
        }
    }

    const openModal = () => setIsModalOpen(true);
    const closeModal = () => {
        if (!isLoading) {
            setIsModalOpen(false);
            setFormData({ name: '', endpoint: '', token: '' });
        }
    }


    return (
        <>
        <button className="add-cluster-btn" onClick={openModal}>
            <div className="btn-icon">+</div>
            <span className="btn-text">Adaugă cluster</span>
        </button>

        {isModalOpen && (
            <div className="modal-overlay" onClick={closeModal}>
                <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                    <div className="modal-header">
                        <h2 className="modal-title">Adaugă cluster nou</h2>
                        <button className="modal-close" onClick={closeModal} disabled={isLoading}>
                            ×
                        </button>
                    </div>

                    <form onSubmit={handleSubmit} className="cluster-form">
                        <div className="form-group">
                            <label htmlFor="name" className="form-label">
                                Nume cluster
                            </label>
                            <input
                                type="text"
                                id="name"
                                name="name"
                                value={formData.name}
                                onChange={handleInputChange}
                                className="form-input"
                                placeholder="Introdu numele clusterului"
                                required
                                disabled={isLoading}
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="endpoint" className="form-label">
                                Endpoint API
                            </label>
                            <input
                                type="url"
                                id="endpoint"
                                name="endpoint"
                                value={formData.endpoint}
                                onChange={handleInputChange}
                                className="form-input"
                                placeholder="https://api-cluster-exemplu.com"
                                required
                                disabled={isLoading}
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="token" className="form-label">
                                Bearer token
                            </label>
                            <textarea
                                id="token"
                                name="token"
                                value={formData.token}
                                onChange={handleInputChange}
                                className="form-textarea"
                                placeholder="Lipește aici tokenul de acces Kubernetes"
                                rows={4}
                                required
                                disabled={isLoading}
                            />
                        </div>

                        <div className="form-actions">
                            <button
                                type="button"
                                className="btn-secondary"
                                onClick={closeModal}
                                disabled={isLoading}
                            >
                                Anulează
                            </button>
                            <button
                                type="submit"
                                className="btn-primary"
                                disabled={isLoading}
                            >
                                {isLoading ? (
                                    <>
                                        <div className="btn-spinner"></div>
                                        Se adaugă...
                                    </>
                                ) : (
                                    'Adaugă cluster'
                                )}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        )}
    </>  
    );
}

export default ButonAdaugareCluster;