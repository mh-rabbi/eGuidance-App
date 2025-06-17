package com.vrgc.eguidance.Model;

public class Booking {
    public String bookingId, userId, user_condition, counseling_date, time, status;
    public String doctorId, doctor_name;

    public Booking() {} // Required for Firebase

    public Booking(String bookingId, String userId, String user_condition, String counseling_date, String time, String status) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.user_condition = user_condition;
        this.counseling_date = counseling_date;
        this.time = time;
        this.status = status;
    }
}
