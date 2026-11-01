import { createContext, useContext, useMemo, useState } from "react";

const AuthContext = createContext(null);

function decodeJwt(token) {
  try {
    const part = token.split(".")[1];
    return JSON.parse(atob(part.replace(/-/g, "+").replace(/_/g, "/")));
  } catch { return {}; }
}

export function AuthProvider({ children }) {
  const [session, setSession] = useState(() => {
    const token = localStorage.getItem("token");
    if (!token) return null;
    const claims = decodeJwt(token);
    return { token, role: claims.role, userId: claims.userId, username: claims.sub, claims };
  });

  const login = (response) => {
    const token = response.data?.token ?? response.token;
    if (!token) throw new Error("Login response did not contain a token.");
    const claims = decodeJwt(token);
    const next = { token, role: claims.role, userId: claims.userId, username: claims.sub, claims };
    localStorage.setItem("token", token);
    localStorage.setItem("role", claims.role || "");
    localStorage.setItem("user", JSON.stringify(next));
    setSession(next);
    return next;
  };

  const logout = () => {
    localStorage.clear();
    setSession(null);
  };

  const value = useMemo(() => ({ ...session, isAuthenticated: !!session, login, logout }), [session]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export const useAuth = () => useContext(AuthContext);