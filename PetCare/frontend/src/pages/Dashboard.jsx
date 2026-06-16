import { useState, useEffect } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import api from '../services/api'

function Dashboard() {
  const navigate = useNavigate()
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    api.get('/api/auth/me')
      .then((data) => {
        setUser(data)
        setLoading(false)
      })
      .catch(() => {
        navigate('/login')
      })
  }, [navigate])

  const handleLogout = async () => {
    await api.auth.logout()
    navigate('/login')
  }

  if (loading) return <div className="dashboard"><p>Cargando...</p></div>
  if (!user) return null

  return (
    <div>
      <nav className="navbar">
        <div className="container">
          <Link to="/dashboard" className="logo">
            <span className="logo-icon">🐾</span>
            <h1>PetCare</h1>
          </Link>
          <div className="nav-links">
            <span className="nav-user">👤 {user.nombre}</span>
            <span className="nav-role">{user.rol}</span>
            <button onClick={handleLogout} className="btn-secondary">Cerrar sesión</button>
          </div>
        </div>
      </nav>

      <div className="dashboard">
        <h1>{user.rol === 'ADMIN' ? 'Panel de Administración' : `¡Bienvenido, ${user.nombre}!`}</h1>
        <p>{user.rol === 'ADMIN' ? `Bienvenido, ${user.nombre}. Gestioná la plataforma PetCare.` : 'Gestioná tus mascotas, turnos y más desde tu panel.'}</p>

        {user.rol === 'DUENIO' && (
          <div className="card-grid">
            <DashboardCard icon="🐕" title="Mis Mascotas" desc="Registrá y administrá las mascotas a tu cargo." />
            <DashboardCard icon="👨‍⚕️" title="Profesionales" desc="Buscá profesionales y solicitá turnos." />
            <DashboardCard icon="🛒" title="Tienda" desc="Comprá productos para tu mascota." />
            <DashboardCard icon="🛍️" title="Carrito" desc="Revisá y confirmá tus compras." />
            <DashboardCard icon="💳" title="Métodos de pago" desc="Administrá tus tarjetas y medios de pago." />
            <DashboardCard icon="📅" title="Mis Turnos" desc="Revisá tus turnos y reservas activas." />
          </div>
        )}

        {user.rol === 'ADMIN' && (
          <div className="card-grid">
            <DashboardCard icon="👥" title="Usuarios" desc="Gestioná todos los usuarios registrados." />
            <DashboardCard icon="📋" title="Postulaciones" desc="Revisá y aprobá solicitudes de profesionales." badge={user.pendientes > 0 ? `${user.pendientes} pendientes` : null} />
            <DashboardCard icon="🐾" title="Mascotas" desc="Administrá las mascotas registradas." />
            <DashboardCard icon="📅" title="Turnos" desc="Visualizá todos los turnos." />
            <DashboardCard icon="🛒" title="Productos" desc="Administrá el catálogo de productos." />
            <DashboardCard icon="📊" title="Reportes" desc="Visualizá estadísticas y ganancias." />
          </div>
        )}

        {['VETERINARIO', 'PASEADOR', 'PELUQUERO', 'ADIESTRADOR', 'CUIDADOR'].includes(user.rol) && (
          <div className="card-grid">
            <DashboardCard icon="📅" title="Mis Turnos" desc="Gestioná tus turnos y horarios." />
            <DashboardCard icon="🐾" title="Mis Pacientes" desc="Revisá las mascotas a tu cargo." />
          </div>
        )}
      </div>
    </div>
  )
}

function DashboardCard({ icon, title, desc, badge }) {
  return (
    <div className="dash-card">
      <span className="dash-icon">{icon}</span>
      <h3>{title} {badge && <span className="nav-role">{badge}</span>}</h3>
      <p>{desc}</p>
      <span className="dash-link">Ver más →</span>
    </div>
  )
}

export default Dashboard
