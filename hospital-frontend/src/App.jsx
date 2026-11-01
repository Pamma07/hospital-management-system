import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { AuthProvider, useAuth } from "./auth/auth";
import ProtectedRoute from "./auth/ProtectedRoute";
import Layout from "./components/Layout";
import Login from "./pages/auth/Login";
import Register from "./pages/auth/Register";
import Home from "./pages/Home";
import AdminDashboard from "./pages/admin/Dashboard";
import AdminNotifications from "./pages/admin/Notifications";
import AdminPatients from "./pages/admin/Patients";
import AdminDoctors from "./pages/admin/Doctors";
import AdminDepartments from "./pages/admin/Departments";
import Resources from "./pages/admin/Resources";
import Allocations from "./pages/admin/Allocations";
import Audit from "./pages/admin/Audit";
import DoctorDashboard from "./pages/doctor/Dashboard";
import DoctorAppointments from "./pages/doctor/Appointments";
import DoctorSchedule from "./pages/doctor/Schedule";
import DoctorRecords from "./pages/doctor/Records";
import DoctorPrescriptions from "./pages/doctor/Prescriptions";
import DoctorNotifications from "./pages/doctor/Notifications";
import DoctorProfile from "./pages/doctor/Profile";
import PatientDashboard from "./pages/patient/Dashboard";
import PatientDoctors from "./pages/patient/Doctors";
import BookAppointment from "./pages/patient/Book";
import PatientAppointments from "./pages/patient/Appointments";
import PatientRecords from "./pages/patient/Records";
import PatientPrescriptions from "./pages/patient/Prescriptions";
import PatientNotifications from "./pages/patient/Notifications";
import PatientProfile from "./pages/patient/Profile";

function RedirectHome(){const a=useAuth(); if(!a?.isAuthenticated)return <Navigate to="/login" replace/>; return <Navigate to={a.role==="ADMIN"?"/admin/dashboard":a.role==="DOCTOR"?"/doctor/dashboard":"/patient/dashboard"} replace/>}
function Guard({roles,children}){return <ProtectedRoute roles={roles}><Layout>{children}</Layout></ProtectedRoute>}

export default function App(){return <AuthProvider><BrowserRouter><Routes>
  <Route path="/" element={<Home/>}/><Route path="/login" element={<Login/>}/><Route path="/register" element={<Register/>}/><Route path="/app" element={<RedirectHome/>}/>
  <Route path="/admin/dashboard" element={<Guard roles={["ADMIN"]}><AdminDashboard/></Guard>}/><Route path="/admin/notifications" element={<Guard roles={["ADMIN"]}><AdminNotifications/></Guard>}/><Route path="/admin/patients" element={<Guard roles={["ADMIN"]}><AdminPatients/></Guard>}/><Route path="/admin/doctors" element={<Guard roles={["ADMIN"]}><AdminDoctors/></Guard>}/><Route path="/admin/departments" element={<Guard roles={["ADMIN"]}><AdminDepartments/></Guard>}/><Route path="/admin/resources" element={<Guard roles={["ADMIN"]}><Resources/></Guard>}/><Route path="/admin/allocations" element={<Guard roles={["ADMIN"]}><Allocations/></Guard>}/><Route path="/admin/audit" element={<Guard roles={["ADMIN"]}><Audit/></Guard>}/>
  <Route path="/doctor/dashboard" element={<Guard roles={["DOCTOR"]}><DoctorDashboard/></Guard>}/><Route path="/doctor/appointments" element={<Guard roles={["DOCTOR"]}><DoctorAppointments/></Guard>}/><Route path="/doctor/schedule" element={<Guard roles={["DOCTOR"]}><DoctorSchedule/></Guard>}/><Route path="/doctor/records" element={<Guard roles={["DOCTOR"]}><DoctorRecords/></Guard>}/><Route path="/doctor/prescriptions" element={<Guard roles={["DOCTOR"]}><DoctorPrescriptions/></Guard>}/><Route path="/doctor/notifications" element={<Guard roles={["DOCTOR"]}><DoctorNotifications/></Guard>}/><Route path="/doctor/profile" element={<Guard roles={["DOCTOR"]}><DoctorProfile/></Guard>}/>
  <Route path="/patient/dashboard" element={<Guard roles={["PATIENT"]}><PatientDashboard/></Guard>}/><Route path="/patient/doctors" element={<Guard roles={["PATIENT"]}><PatientDoctors/></Guard>}/><Route path="/patient/book" element={<Guard roles={["PATIENT"]}><BookAppointment/></Guard>}/><Route path="/patient/appointments" element={<Guard roles={["PATIENT"]}><PatientAppointments/></Guard>}/><Route path="/patient/records" element={<Guard roles={["PATIENT"]}><PatientRecords/></Guard>}/><Route path="/patient/prescriptions" element={<Guard roles={["PATIENT"]}><PatientPrescriptions/></Guard>}/><Route path="/patient/notifications" element={<Guard roles={["PATIENT"]}><PatientNotifications/></Guard>}/><Route path="/patient/profile" element={<Guard roles={["PATIENT"]}><PatientProfile/></Guard>}/>
  <Route path="*" element={<Navigate to="/app" replace/>}/>
</Routes></BrowserRouter></AuthProvider>}