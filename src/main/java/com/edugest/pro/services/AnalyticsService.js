import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '../../services/authService';
import analyticsService from '../../services/analyticsService'; // 👈 On importe notre nouveau service

const PortailEleve = () => {
    const [user, setUser] = useState(null);
    const [periodeActive, setPeriodeActive] = useState('SEQ1'); 
    
    // 🧠 ÉTATS DYNAMIQUES VENANT DU BACKEND
    const [smartData, setSmartData] = useState({
        moyenneGenerale: 0.0,
        mentionMinesec: "Chargement...",
        decisionConseil: "Veuillez patienter...",
        alerteRisqueDecrochage: false,
        messageSmartInsight: "Chargement de l'analyse système..."
    });

    const navigate = useNavigate();

    // 1. Gestion de la session utilisateur
    useEffect(() => {
        const currentUser = authService.getCurrentUser();
        if (!currentUser || currentUser.role !== 'ELEVE') {
            navigate('/login');
        } else {
            setUser(currentUser);
        }
    }, [navigate]);

    // 2. 🧠 EFFET INTELLIGENT : Charge les données Spring Boot à chaque fois que la Séquence change !
    useEffect(() => {
        if (user && user.id) {
            analyticsService.getMappingPedagogique(user.id, periodeActive)
                .then(data => {
                    setSmartData(data); // On injecte les vrais calculs de Spring Boot dans l'interface
                })
                .catch(err => {
                    console.error("Impossible de joindre le moteur d'analyse", err);
                });
        }
    }, [periodeActive, user]);

    const handleLogout = () => {
        authService.logout();
        navigate('/');
    };

    return (
        <div className="min-vh-100 bg-light">
            <nav className="navbar navbar-expand-lg navbar-dark shadow-sm mb-4" style={{ backgroundColor: '#198754' }}>
                <div className="container">
                    <span className="navbar-brand fw-bold">🇨🇲 EduGest Pro — Espace Scolaire Camerounais</span>
                    <button className="btn btn-outline-light btn-sm fw-medium" onClick={handleLogout}>Déconnexion 🚪</button>
                </div>
            </nav>

            <div className="container py-2">
                <div className="bg-white p-4 rounded-4 shadow-sm border mb-4 d-flex justify-content-between align-items-center flex-wrap gap-3">
                    <div>
                        <h4 className="fw-bold text-dark mb-1">Bonjour, {user ? `${user.prenom} ${user.nom}` : 'Élève'} 👋</h4>
                        <span className="badge bg-success me-2">{user?.classeNom || 'Terminale'}</span>
                        <span className="text-muted small">Filière : <strong>Système ESG</strong></span>
                        
                        {/* Sélecteur de Séquences */}
                        <div className="d-flex gap-2 mt-3 flex-wrap">
                            {['SEQ1', 'SEQ2', 'SEQ3', 'SEQ4', 'SEQ5', 'SEQ6'].map((seq, idx) => (
                                <button 
                                    key={seq} 
                                    className={`btn btn-sm px-2 py-1 rounded-3 ${periodeActive === seq ? 'btn-success fw-bold' : 'btn-outline-secondary small'}`} 
                                    onClick={() => setPeriodeActive(seq)}
                                >
                                    Séquence {idx + 1}
                                </button>
                            ))}
                        </div>
                    </div>

                    {/* Bulletin dynamique connecté à Spring Boot */}
                    <div className="bg-success bg-opacity-10 text-success border border-success border-opacity-25 rounded-3 p-3 text-center" style={{ minWidth: '220px' }}>
                        <span className="text-secondary small d-block fw-medium">Moyenne Séquentielle</span>
                        <span className="fw-bold fs-3 d-block">{smartData.moyenneGenerale.toFixed(2)} / 20</span>
                        <span className="small d-block fw-bold text-dark">Mention : {smartData.mentionMinesec}</span>
                        <hr className="my-1 border-success border-opacity-25" />
                        <span className="text-muted fw-medium" style={{ fontSize: '11px' }}>{smartData.decisionConseil}</span>
                    </div>
                </div>

                {/* Encadré d'alerte adaptatif aux données Spring Boot */}
                <div className={`card shadow-sm border-0 rounded-4 p-3 mb-4 bg-white border-start border-4 ${smartData.alerteRisqueDecrochage ? 'border-danger bg-danger bg-opacity-10' : 'border-success'}`}>
                    <div className="small fw-medium text-dark">{smartData.messageSmartInsight}</div>
                </div>

                {/* ... Garde ton tableau d'emploi du temps fixe et tes sections existantes en dessous ... */}
            </div>
        </div>
    );
};

export default PortailEleve;