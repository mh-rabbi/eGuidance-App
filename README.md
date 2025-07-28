# 💡 eGuidance: Mental Health Companion App 🧠📱

eGuidance is an Android-based mental wellness application designed to provide emotional support through **online doctor bookings**, **anonymous real-time chat**, and **self-assessment tests**. Built using **Java**, **Firebase**, and modern Android architecture, this project aims to make mental health support accessible, secure, and user-friendly.

🎥 **Live Demo:** [Watch on YouTube](https://youtu.be/q9ghzHS4LCw)

---

## 🚀 Features

🔐 **User Authentication**  
- Firebase Authentication for secure sign-up and login  
- SharedPreferences to keep users logged in  

👥 **Role-Based Access**  
- Three user roles with dedicated dashboards:  
  - 👤 **User**  
  - 🩺 **Doctor**  
  - 👨‍💼 **Admin**

📆 **Appointment Booking & Scheduling**  
- Book and manage sessions with doctors in real-time  
- Admin view for approving and managing appointments  

💬 **Real-time Chat Support**  
- One-to-one chat between users and doctors  
- Option for **anonymous chat**

🧠 **Emotional Self-Assessment Test**  
- Take guided quizzes to assess emotional well-being  

🎗️ **Free Support Section**  
- Access mental health guidance without sign-up  

🔔 **Reminders & Notifications**  
- Scheduled alerts using `BroadcastReceiver`  
- Session notifications to ensure timely mental care  

🎨 **Smooth & Responsive UI**  
- Modern Material Design  
- Lottie Animations for engaging interactions  
- Glide + Base64 for efficient image handling  

---

## 🛠️ Tech Stack

### 📱 App Development
- **Java** – Main programming language  
- **Android SDK** – Target SDK 35, Minimum SDK 26  
- **Architecture:** MVC (Model-View-Controller)  

### 🌐 Backend & Cloud
- **Firebase Authentication** – User login & session handling  
- **Firebase Realtime Database** – Store user data, chat messages, and bookings  
- **SharedPreferences** – Persistent login across app sessions  
- **Base64 Encoding** – Store and retrieve profile images as strings

### 🖼️ UI & Media
- **Material Components** – For responsive UI  
- **Glide** – Fast image loading  
- **Lottie** – Smooth micro-interactions  
- **RecyclerView + Custom Adapters** – Dynamic list rendering  

### 🧪 Testing
- **JUnit** – Unit tests  
- **Espresso** – UI testing (`androidx.test.espresso`)  

---



## 🧰 Setup & Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/mh-rabbi/eGuidance-App.git
2. **Open in Android Studio**

   -Import the project and allow Gradle to sync.

4. **Firebase Configuration**

    -Add google-services.json file under the app/ directory.

6. **Run the App**

    -Connect your Android device or emulator.

   -Hit Run in Android Studio.

---

## 🙌 Acknowledgements

Special thanks to the **Firebase team** and the creators of amazing open-source libraries like **Glide**, **Lottie**, and **Material Components** — your tools made this project possible! 💙

---

## 🤝 Connect with Me

- 🔗 [LinkedIn – MH Rabbi](https://www.linkedin.com/in/rabbi221)
- 📧 Email: **22103257@iubat.edu**
- 📂 GitHub: [mh-rabbi/eGuidance-App](https://github.com/mh-rabbi/eGuidance-App)

---

## 💬 Feedback & Contributions are Welcome!

If you found this project helpful, please consider giving it a ⭐ on GitHub — it motivates me to keep building! 😊

Feel free to **fork** this repo and submit a **pull request** to contribute or improve the project.  
Let's build something great together! 🚀
Thank you.❤️



