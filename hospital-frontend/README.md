# MediCare Hub — React Frontend

Complete React frontend for the Spring Boot Hospital Appointment & Resource Management backend supplied with this project.

## Stack
- React 19
- Vite
- React Router
- Axios
- Bootstrap (base dependency)
- Custom responsive CSS

## Run
1. Start the Spring Boot backend on `http://localhost:8080`.
2. Open this frontend folder.
3. Run:
   ```bash
   npm install
   npm run dev
   ```
4. Open `http://localhost:5173`.

If your backend uses another URL, create `.env`:
```env
VITE_API_URL=http://localhost:8080/api
```

## Included workspaces

### Admin
- Dashboard
- Patient CRUD
- Doctor CRUD
- Department CRUD
- Resource CRUD
- Resource allocation/release
- Audit logs
- Notifications

### Doctor
- Dashboard
- Appointments + status updates
- Doctor schedule CRUD
- Medical records
- Prescriptions
- Notifications
- Profile

### Patient
- Dashboard
- Doctor search
- Available-slot lookup
- Appointment booking
- Appointment history (see backend note below)
- Medical records
- Prescriptions
- Notifications
- Profile

## Important backend compatibility notes

The supplied backend has two security rules that conflict with its controller annotations:

1. `SecurityConfig` currently has:
   `requestMatchers("/api/doctors/**").hasRole("DOCTOR")`

   This blocks PATIENT users from the doctor search endpoint even though `DoctorController` explicitly allows `PATIENT`.

2. `SecurityConfig` currently has:
   `requestMatchers("/api/patients/**").hasRole("PATIENT")`

   This blocks ADMIN/DOCTOR access to patient endpoints even though the controller allows ADMIN/DOCTOR for several operations.

The clean fix is to remove those broad path matchers and let the controller `@PreAuthorize` annotations decide access.

Also, there is no patient-specific GET appointment endpoint. `GET /api/appointments` is restricted to ADMIN/DOCTOR. Therefore a patient cannot retrieve their complete appointment list from the server. The patient appointment page displays bookings made in the current browser and clearly labels this limitation.

Finally, the login response contains only the JWT token. This frontend decodes the token's `role`, `userId`, and `sub` claims, so no backend login DTO change is required.


## Backend integration updates
- Patient appointments now use `GET /api/appointments/my`.
- Doctor appointments now use `GET /api/appointments/doctor/my`.
- The backend security configuration has been reordered so `anyRequest()` is last.
- Doctor responses expose the existing `username` field so doctor profile matching can work when the doctor record is linked to the login username.
