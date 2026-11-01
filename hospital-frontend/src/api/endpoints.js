import api from "./api";

export const authApi = {
  register: (data) => api.post("/auth/register", data),
  login: (data) => api.post("/auth/login", data)
};

export const dashboardApi = {
  admin: () => api.get("/dashboard"),
  doctor: () => api.get("/dashboard/doctor"),
  patient: () => api.get("/dashboard/patient")
};

export const patientsApi = {
  all: () => api.get("/patients"),
  get: (id) => api.get(`/patients/${id}`),
  profile: () => api.get("/patients/profile"),
  create: (data) => api.post("/patients", data),
  update: (id, data) => api.put(`/patients/${id}`, data),
  remove: (id) => api.delete(`/patients/${id}`)
};

export const doctorsApi = {
  all: () => api.get("/doctors"),
  me: () => api.get("/doctors/me"),
  get: (id) => api.get(`/doctors/${id}`),
  create: (data) => api.post("/doctors", data),
  update: (id, data) => api.put(`/doctors/${id}`, data),
  remove: (id) => api.delete(`/doctors/${id}`)
};

export const departmentsApi = {
  all: () => api.get("/departments"),
  get: (id) => api.get(`/departments/${id}`),
  create: (data) => api.post("/departments", data),
  update: (id, data) => api.put(`/departments/${id}`, data),
  remove: (id) => api.delete(`/departments/${id}`)
};

export const appointmentsApi = {
  all: () => api.get("/appointments"),
  my: () => api.get("/appointments/my"),
  doctorMy: () => api.get("/appointments/doctor/my"),
  get: (id) => api.get(`/appointments/${id}`),
  create: (data) => api.post("/appointments", data),
  update: (id, data) => api.put(`/appointments/${id}`, data),
  remove: (id) => api.delete(`/appointments/${id}`),
  status: (id, status) => api.put(`/appointments/${id}/status`, null, { params: { status } }),
  slots: (doctorId, date) => api.get("/appointments/available-slots", { params: { doctorId, date } })
};

export const schedulesApi = {
  all: () => api.get("/schedules"),
  byDoctor: (doctorId) => api.get(`/schedules/doctor/${doctorId}`),
  create: (doctorId, data) => api.post(`/schedules/doctor/${doctorId}`, data),
  update: (id, data) => api.put(`/schedules/${id}`, data),
  remove: (id) => api.delete(`/schedules/${id}`)
};

export const prescriptionsApi = {
  all: () => api.get("/prescriptions"),
  byId: (id) => api.get(`/prescriptions/${id}`),
  byPatient: (id) => api.get(`/prescriptions/patient/${id}`),
  byDoctor: (id) => api.get(`/prescriptions/doctor/${id}`),
  byAppointment: (id) => api.get(`/prescriptions/appointment/${id}`),
  create: (data) => api.post("/prescriptions", data),
  update: (id, data) => api.put(`/prescriptions/${id}`, data),
  remove: (id) => api.delete(`/prescriptions/${id}`)
};

export const recordsApi = {
  all: () => api.get("/medical-records"),
  byPatient: (id) => api.get(`/medical-records/patient/${id}`),
  byDoctor: (id) => api.get(`/medical-records/doctor/${id}`),
  byAppointment: (id) => api.get(`/medical-records/appointment/${id}`),
  create: (data) => api.post("/medical-records", data),
  update: (id, data) => api.put(`/medical-records/${id}`, data),
  remove: (id) => api.delete(`/medical-records/${id}`)
};

export const notificationsApi = {
  all: () => api.get("/notifications"),
  unread: () => api.get("/notifications/unread"),
  count: () => api.get("/notifications/unread/count"),
  read: (id) => api.put(`/notifications/${id}/read`),
  remove: (id) => api.delete(`/notifications/${id}`)
};

export const resourcesApi = {
  all: () => api.get("/resources"),
  available: () => api.get("/resources/available"),
  byType: (type) => api.get(`/resources/type/${type}`),
  create: (data) => api.post("/resources", data),
  update: (id, data) => api.put(`/resources/${id}`, data),
  remove: (id) => api.delete(`/resources/${id}`)
};

export const allocationsApi = {
  all: () => api.get("/resource-allocations"),
  byPatient: (id) => api.get(`/resource-allocations/patient/${id}`),
  create: (data) => api.post("/resource-allocations", data),
  release: (id) => api.put(`/resource-allocations/${id}/release`)
};

export const auditApi = {
  all: () => api.get("/audit-logs"),
  mine: () => api.get("/audit-logs/my"),
  byUser: (username) => api.get(`/audit-logs/user/${encodeURIComponent(username)}`),
  remove: (id) => api.delete(`/audit-logs/${id}`)
};