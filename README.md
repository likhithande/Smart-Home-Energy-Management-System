# Smart Home Energy Management System

A web-based application that helps users monitor and manage their home appliance energy consumption efficiently. The system calculates electricity usage, estimates monthly bills, and provides insights to reduce energy costs.

## Features

* OTP-Based Authentication
  Secure login and signup using OTP verification.

* Interactive Dashboard
  Displays appliance usage, energy consumption, and cost analysis.

* Energy Consumption Tracking
  Tracks electricity usage of different appliances in real-time.

* Monthly Bill Calculation
  Automatically calculates electricity bills based on usage.

* Energy Saving Insights
  Shows how much energy and cost can be saved.

* Appliance Management
  Add, update, and remove appliances easily.

### Frontend

* React.js
* HTML
* CSS
* JavaScript

### Backend

* Spring Boot (Java)

### Database

* MySQL


## Project Structure

Smart-Home-Energy-Management-System/
│
├── frontend/        # React frontend
├── backend/         # Spring Boot backend
├── database/        # SQL scripts (if any)
└── README.md

## Installation & Setup

### 1. Clone the Repository

git clone https://github.com/likhithande/Smart-Home-Energy-Management-System.git
cd Smart-Home-Energy-Management-System

### 2. Backend Setup (Spring Boot)

cd backend

Configure MySQL in application.properties

spring.datasource.url=jdbc:mysql://localhost:3306/your_db
spring.datasource.username=root
spring.datasource.password=your_password

Run the backend:

mvn spring-boot:run
### 3. Frontend Setup (React)
cd frontend
npm install
npm start
## How It Works
1. User signs up or logs in using OTP verification
2. Adds appliances with power ratings
3. System calculates:

   * Daily energy usage
   * Monthly consumption
   * Electricity bill
4. Dashboard displays usage statistics and savings
## Key Highlights

* Real-time energy tracking
* Cost-efficient electricity management
* User-friendly dashboard
* Helps reduce unnecessary power consumption

## Author

Ande Likhith 
B.Tech--Information Technology
Developed as part of a 40-day hands-on internship project


## License

This project is licensed under the MIT License.
