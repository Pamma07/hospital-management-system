import { useState } from "react";

export function Loader({ text="Loading..." }) { return <div className="loader"><div className="spinner"></div><span>{text}</span></div>; }
export function ErrorBox({ error }) { if(!error) return null; return <div className="alert error">⚠ {error}</div>; }
export function Empty({ text="No records found." }) { return <div className="empty"><div>◌</div><p>{text}</p></div>; }
export function PageHeader({ eyebrow,title,description,action }) { return <div className="page-header"><div><div className="eyebrow">{eyebrow}</div><h1>{title}</h1>{description&&<p>{description}</p>}</div>{action}</div>; }
export function StatCard({label,value,icon,sub}) { return <div className="stat-card"><div className="stat-icon">{icon}</div><div><span>{label}</span><strong>{value ?? "—"}</strong>{sub&&<small>{sub}</small>}</div></div>; }
export function Modal({title,children,onClose,wide=false}) { return <div className="modal-backdrop" onMouseDown={onClose}><div className={wide?"modal-card wide":"modal-card"} onMouseDown={e=>e.stopPropagation()}><div className="modal-head"><h3>{title}</h3><button onClick={onClose}>×</button></div>{children}</div></div>; }
export function Badge({value}) { return <span className={`badge ${String(value).toLowerCase()}`}>{value}</span>; }
export function useForm(initial) { const [form,setForm]=useState(initial); const set=(e)=>setForm({...form,[e.target.name]:e.target.value}); return [form,setForm,set]; }