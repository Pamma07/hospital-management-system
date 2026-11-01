import { useEffect, useState } from "react";
import { doctorsApi } from "../../api/endpoints";
import { ErrorBox, Loader, PageHeader } from "../../components/UI";

export default function Profile() {
  const [d, setD] = useState(null);
  const [err, setErr] = useState("");

  useEffect(() => {
    doctorsApi.me()
      .then((r) => setD(r.data))
      .catch((e) => setErr(e.response?.data?.message || "Unable to load profile."));
  }, []);

  return <>
    <PageHeader eyebrow="Account" title="My profile" description="Your doctor profile from the hospital system." />
    <ErrorBox error={err} />
    {!d && !err ? <Loader /> : d && <div className="panel profile-card">
      <div className="profile-big">{d.name?.[0] || "D"}</div>
      <div>
        <h2 style={{ margin: "0 0 5px" }}>Dr. {d.name}</h2>
        <p className="muted">{d.specialization} · {d.departmentName || "Department not assigned"}</p>
        <div className="details">
          <div className="detail"><span>Email</span><strong>{d.email || "—"}</strong></div>
          <div className="detail"><span>Phone</span><strong>{d.phone || "—"}</strong></div>
          <div className="detail"><span>Doctor ID</span><strong>#{d.id}</strong></div>
          <div className="detail"><span>Username</span><strong>{d.username || "—"}</strong></div>
        </div>
      </div>
    </div>}
  </>;
}
