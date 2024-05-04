package com.example.betterhealthapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<Appointment> appointmentList;
    private Context context;

    public AppointmentAdapter(List<Appointment> appointmentList, Context context) {
        this.appointmentList = appointmentList;
        this.context = context;
    }
    public void updateAppointments(List<Appointment> appointments) {
        appointmentList.clear();
        appointmentList.addAll(appointments);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_appointment_history, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);
        holder.tvDate.setText("Appointment Date: " + appointment.getDate());
        holder.tvTime.setText("Appointment Time: " + appointment.getTime());
        holder.tvReason.setText("Appointment Visit: " + appointment.getReason());
        holder.tvDoctor.setText("Doctor Attend by: " + appointment.getDoctor());
        holder.tvNotes.setText("Perscription: " + appointment.getNotes());
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTime, tvReason, tvDoctor, tvStatus, tvNotes, tvNextAppointment;
        CardView cardView;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvReason = itemView.findViewById(R.id.tv_reason);
            tvDoctor = itemView.findViewById(R.id.tv_doctor);
            tvNotes = itemView.findViewById(R.id.tv_notes);
            cardView = itemView.findViewById(R.id.appointment_card);
        }
    }
}