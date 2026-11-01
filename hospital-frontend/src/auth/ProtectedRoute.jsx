import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "./auth";

export default function ProtectedRoute({ roles, children }) {
  const auth = useAuth();
  const location = useLocation();
  if (!auth?.isAuthenticated) return <Navigate to="/login" replace state={{ from: location.pathname }} />;
  if (roles && !roles.includes(auth.role)) {
    const path = auth.role === "ADMIN" ? "/admin/dashboard" : auth.role === "DOCTOR" ? "/doctor/dashboard" : "/patient/dashboard";
    return <Navigate to={path} replace />;
  }
  return children;
}