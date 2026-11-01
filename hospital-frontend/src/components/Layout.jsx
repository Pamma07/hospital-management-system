import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/auth";
import { useEffect, useState } from "react";
import { notificationsApi } from "../api/endpoints";

const menus = {
  ADMIN: [
    ["Dashboard","/admin/dashboard","▦"],["Patients","/admin/patients","♙"],["Doctors","/admin/doctors","✚"],["Departments","/admin/departments","⌘"],
    ["Resources","/admin/resources","▤"],["Allocations","/admin/allocations","▥"],["Audit Logs","/admin/audit","◷"],["Notifications","/admin/notifications","●"]
  ],
  DOCTOR: [
    ["Dashboard","/doctor/dashboard","▦"],["Appointments","/doctor/appointments","◫"],["Schedule","/doctor/schedule","◴"],
    ["Medical Records","/doctor/records","▤"],["Prescriptions","/doctor/prescriptions","▥"],["Notifications","/doctor/notifications","●"],["Profile","/doctor/profile","♙"]
  ],
  PATIENT: [
    ["Dashboard","/patient/dashboard","▦"],["Find Doctors","/patient/doctors","⌕"],["Book Appointment","/patient/book","+"],
    ["My Appointments","/patient/appointments","◫"],["Medical Records","/patient/records","▤"],["Prescriptions","/patient/prescriptions","▥"],["Notifications","/patient/notifications","●"],["Profile","/patient/profile","♙"]
  ]
};

export default function Layout({ children }) {
  const auth = useAuth(), navigate = useNavigate();
  const [unread, setUnread] = useState(0);
  useEffect(() => {
    notificationsApi.count().then(r => setUnread(Number(r.data || 0))).catch(() => {});
  }, [auth?.username]);
  if (!auth) return null;
  const home = auth.role === "ADMIN" ? "/admin/dashboard" : auth.role === "DOCTOR" ? "/doctor/dashboard" : "/patient/dashboard";
  return <div className="app-shell">
    <aside className="sidebar">
      <div className="brand" onClick={() => navigate(home)}><span className="brand-mark">✚</span><span>MediCare <b>Hub</b></span></div>
      <div className="profile-mini"><div className="avatar">{(auth.username || "U")[0].toUpperCase()}</div><div><strong>{auth.username}</strong><small>{auth.role}</small></div></div>
      <nav>{menus[auth.role].map(([label,path,icon]) => <NavLink key={path} to={path} className={({isActive})=>isActive?"nav-item active":"nav-item"}><span>{icon}</span>{label}{label==="Notifications"&&unread>0?<em>{unread}</em>:null}</NavLink>)}</nav>
      <button className="logout-btn" onClick={() => {auth.logout();navigate("/login")}}>↪ Sign out</button>
    </aside>
    <main className="main-area">
      <header className="topbar"><div><span className="crumb">MediCare Hub</span><span className="slash">/</span><span>{auth.role?.toLowerCase()} workspace</span></div><button className="bell" onClick={()=>navigate(`/${auth.role.toLowerCase()}/notifications`)}>◔{unread>0&&<i>{unread}</i>}</button></header>
      <div className="content">{children}</div>
    </main>
  </div>;
}