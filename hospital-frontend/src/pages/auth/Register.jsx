import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { authApi } from "../../api/endpoints";
import { ErrorBox } from "../../components/UI";

export default function Register() {
  const [f, setF] = useState({
    username: "",
    password: "",
    role: "PATIENT",
    name: "",
    age: "",
    gender: "",
    phone: "",
    email: "",
    address: "",
  });

  const [error, setError] = useState("");
  const [ok, setOk] = useState("");
  const [loading, setLoading] = useState(false);

  const nav = useNavigate();

  const change = (e) => {
    setF({
      ...f,
      [e.target.name]: e.target.value,
    });
  };

  const submit = async (e) => {
    e.preventDefault();

    setError("");
    setOk("");
    setLoading(true);

    try {
      const payload = {
        username: f.username,
        password: f.password,
        role: f.role,
      };

      // Patient-specific information
      if (f.role === "PATIENT") {
        payload.name = f.name;
        payload.age = Number(f.age);
        payload.gender = f.gender;
        payload.phone = f.phone;
        payload.email = f.email;
        payload.address = f.address;
      }

      console.log("Registration payload:", payload);

      const r = await authApi.register(payload);

      setOk(
        r.data?.message || "Account created successfully."
      );

      setTimeout(() => {
        nav("/login");
      }, 1000);
    } catch (e) {
      console.error("Registration error:", e);

      setError(
        e.response?.data?.message ||
        e.response?.data?.error ||
        "Could not create account."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">

        {/* LEFT SIDE */}
        <div className="auth-side">
          <div className="brand">
            ✚ MediCare <b>Hub</b>
          </div>

          <h1>Start your workspace.</h1>

          <p>
            Create an account and access the appropriate
            hospital workspace.
          </p>

          <div className="feature">
            Patient — book and manage care
            <br />
            Doctor — manage clinical work
            <br />
            Admin — manage hospital operations
          </div>
        </div>

        {/* RIGHT SIDE */}
        <form className="auth-form" onSubmit={submit}>

          <h2>Create account</h2>

          <p>
            Register with the credentials used by your backend.
          </p>

          <ErrorBox error={error} />

          {ok && (
            <div className="alert success">
              {ok}
            </div>
          )}

          {/* BASIC ACCOUNT DETAILS */}
          <div className="form-grid">

            <label className="field full">
              <span>Username *</span>

              <input
                name="username"
                value={f.username}
                onChange={change}
                placeholder="Enter username"
                required
              />
            </label>

            <label className="field full">
              <span>Password *</span>

              <input
                type="password"
                name="password"
                value={f.password}
                onChange={change}
                placeholder="Enter password"
                minLength={4}
                required
              />
            </label>

            <label className="field full">
              <span>Role *</span>

              <select
                name="role"
                value={f.role}
                onChange={change}
                required
              >
                <option value="PATIENT">PATIENT</option>
                <option value="DOCTOR">DOCTOR</option>
                <option value="ADMIN">ADMIN</option>
              </select>
            </label>

          </div>

          {/* PATIENT DETAILS */}
          {f.role === "PATIENT" && (
            <>
              <h3 className="register-section-title">
                Patient Information
              </h3>

              <div className="form-grid">

                <label className="field full">
                  <span>Full Name *</span>

                  <input
                    name="name"
                    value={f.name}
                    onChange={change}
                    placeholder="Enter full name"
                    required
                  />
                </label>

                <label className="field">
                  <span>Age *</span>

                  <input
                    type="number"
                    name="age"
                    value={f.age}
                    onChange={change}
                    placeholder="Enter age"
                    min="1"
                    max="120"
                    required
                  />
                </label>

                <label className="field">
                  <span>Gender *</span>

                  <select
                    name="gender"
                    value={f.gender}
                    onChange={change}
                    required
                  >
                    <option value="">
                      Select gender
                    </option>

                    <option value="Male">
                      Male
                    </option>

                    <option value="Female">
                      Female
                    </option>

                    <option value="Other">
                      Other
                    </option>
                  </select>
                </label>

                <label className="field">
                  <span>Phone *</span>

                  <input
                    type="tel"
                    name="phone"
                    value={f.phone}
                    onChange={change}
                    placeholder="9876543210"
                    pattern="[0-9]{10}"
                    maxLength="10"
                    required
                  />
                </label>

                <label className="field">
                  <span>Email *</span>

                  <input
                    type="email"
                    name="email"
                    value={f.email}
                    onChange={change}
                    placeholder="example@email.com"
                    required
                  />
                </label>

                <label className="field full">
                  <span>Address *</span>

                  <textarea
                    name="address"
                    value={f.address}
                    onChange={change}
                    placeholder="Enter address"
                    rows="3"
                    required
                  />
                </label>

              </div>
            </>
          )}

          <div className="notice">
            Your account will be created according to the
            selected role. Patient information is required
            when registering as a patient.
          </div>

          <button
            className="btn primary"
            style={{
              width: "100%",
              marginTop: 20,
            }}
            disabled={loading}
          >
            {loading
              ? "Creating..."
              : "Create account"}
          </button>

          <div className="auth-switch">
            Already registered?{" "}
            <Link to="/login">
              Sign in
            </Link>
          </div>

        </form>
      </div>
    </div>
  );
}