import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { appointmentsApi } from "../../api/endpoints";
import { Badge, Empty, ErrorBox, Loader, PageHeader } from "../../components/UI";

export default function Appointments() {
  const [rows, setRows] = useState([]);
  const [err, setErr] = useState("");
  const [loading, setLoading] = useState(true);

  const load = async () => {
    setLoading(true);
    setErr("");
    try {
      const response = await appointmentsApi.my();
      setRows(Array.isArray(response.data) ? response.data : []);
    } catch (e) {
      setErr(e.response?.data?.message || "Unable to load your appointments.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  return (
    <>
      <PageHeader
        eyebrow="Appointments"
        title="My appointments"
        description="View and manage appointments booked with your account."
        action={<Link className="btn primary" to="/patient/book">+ Book appointment</Link>}
      />
      <ErrorBox error={err} />
      {loading ? <Loader /> : (
        <div className="panel table-wrap">
          <table className="table">
            <thead>
              <tr><th>Date</th><th>Time</th><th>Doctor</th><th>Reason</th><th>Status</th></tr>
            </thead>
            <tbody>
              {rows.map((x) => (
                <tr key={x.id}>
                  <td>{x.appointmentDate}</td>
                  <td>{x.appointmentTime?.slice(0, 5)}</td>
                  <td><strong>Dr. {x.doctorName || `#${x.doctorId}`}</strong></td>
                  <td>{x.reason || "—"}</td>
                  <td><Badge value={x.status} /></td>
                </tr>
              ))}
            </tbody>
          </table>
          {!rows.length && <Empty text="No appointments found. Book your first appointment." />}
        </div>
      )}
    </>
  );
}
