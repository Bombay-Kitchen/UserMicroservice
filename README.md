# CampusConnect - Connect, Chat, and Share on Campus

# UserModule
User Module for CampusConnect - Independent Microservice for User Sign-In Sign-Up Functionality and Many User Based Features

Version: Stable Runway : 1.0.0
Project for Campus Communication and Sharing Platform

Developed By Aditya Sunit Kanoi 👨‍💻

---

CampusConnect is a versatile platform designed to foster connections among students on campus. It enables students to engage in real-time chat conversations, share posts, and build a vibrant campus community.

![CampusConnect Logo](CampusConnectBanner.png)


# API Documentation

This document provides information about the API endpoints for our application.

## Sign-Up API

**Description**: This API endpoint allows users to sign up for the application by providing their registration information.

- **HTTP Method**: POST
- **Path**: `UserModuleApiPath.SIGN_UP`
- **Request Body**: 
  - `firstName` (String): User's first name.
  - `email` (String): User's email address.
  - (Other registration fields as needed)
- **Response**: A JSON response containing signup details.

## Sign-In API

**Description**: This API endpoint allows users to sign in to the application by providing their credentials.

- **HTTP Method**: POST
- **Path**: `UserModuleApiPath.SIGN_IN`
- **Request Body**:
  - `email` (String): User's email address.
  - `password` (String): User's password.
- **Response**: A JSON response containing sign-in details.

## Password Reset Link API

**Description**: This API endpoint allows users to request a password reset link by providing their email address.

- **HTTP Method**: POST
- **Path**: `UserModuleApiPath.RESET_PASSWORD_LINK`
- **Request Parameters**:
  - `email` (String): The email address for which the reset link is requested.
- **Response**: A JSON response containing a password reset link.

## Change of Password API

**Description**: This API endpoint allows users to change their password.

- **HTTP Method**: PUT
- **Path**: `UserModuleApiPath.UPDATE_PASSWORD`
- **Request Body**:
  - `email` (String): User's email address.
  - `oldPassword` (String): User's current password.
  - `newPassword` (String): User's new password.
- **Response**: A JSON response indicating the status of the password change request.

## Deletion of Account API

**Description**: This API endpoint allows users to delete their accounts.

- **HTTP Method**: DELETE
- **Path**: `UserModuleApiPath.DELETE_USER_ACCOUNT`
- **Request Parameter**:
  - `authenticationToken` (String): The user's authentication token for account verification.
- **Response**: A JSON response indicating the status of the account deletion request.

## Deactivation of Account API

**Description**: This API endpoint allows users to deactivate or reactivate their accounts.

- **HTTP Method**: PUT
- **Path**: `UserModuleApiPath.DEACTIVATE_USER_ACCOUNT`
- **Request Parameters**:
  - `authenticationToken` (String): The user's authentication token for account verification.
  - `action` (String): A string indicating whether to deactivate or reactivate the account.
- **Response**: A JSON response indicating the status of the account deactivation/reactivation request.

## Features

📢 **Real-time Chat**: Connect with fellow students through instant messaging within the campus.

📝 **Post Sharing**: Share posts, announcements, and updates with the campus community.

👥 **User Profiles**: Customize your user profile and connect with others based on interests and majors.

🏫 **Campus Directory**: Access a directory of students, faculty, and staff for easy communication.

🔎 **Search Functionality**: Find specific users and posts quickly with a robust search feature.

📅 **Event Announcements**: Stay informed about campus events, club meetings, and important dates.

## Technologies Used

- Spring Boot: Backend development for the application.
- ReactJS: Frontend user interface for an interactive user experience.
- WebSocket: Powering real-time chat functionality.
- MongoDB: Storing user data, posts, and chat messages.
- AI based Model for Personalized Feature.

## How to Use

1. Clone the repository to your local machine.
2. Run the application using your preferred IDE or by following the provided setup instructions.
3. Access the CampusConnect application via the web browser.

## Endpoints

Access the CampusConnect application through the provided web interface to explore its features and functionalities.

### 1. Chat with Campus Community

- **Description**: Start a conversation with fellow students, faculty, or staff members.
- **Usage**: Access the chat feature from the main dashboard.
- **Benefits**: Instant communication for academic discussions, project collaboration, or socializing.

### 2. Share Campus Updates

- **Description**: Post announcements, event details, or general updates for the campus community.
- **Usage**: Create and share posts through the "Post" section of the application.
- **Benefits**: Keep everyone informed about important news and events on campus.

### 3. Explore User Profiles

- **Description**: View and customize your user profile with interests, majors, and more.
- **Usage**: Access your profile settings and interact with others based on shared interests.
- **Benefits**: Build connections with like-minded individuals on campus.

### 4. Campus Directory

- **Description**: Browse through a directory of campus members for easy communication.
- **Usage**: Search for specific users or browse by department and year.
- **Benefits**: Connect with classmates, faculty, and staff effortlessly.

## Performance

[Performance data and metrics can be included here.]

## How to Contribute

Contributions to CampusConnect are highly encouraged! If you have ideas for improvements, features, or bug fixes, please don't hesitate to get involved.

🌟 Please star the repository if you find CampusConnect helpful!

Thank you for being a part of the CampusConnect community! 😊

---

# 💡 UPCOMING FEATURES!!

- [ ] 📚 **Study Groups Creation**
  Form and join study groups with students sharing your academic interests and courses.

- [ ] 📢 **Campus Newsfeed**
  Stay updated with a real-time newsfeed featuring campus-wide announcements.

- [ ] 🎉 **Event RSVPs and Reminders**
  RSVP to events and receive reminders for upcoming campus activities.

- [ ] 🎓 **Alumni Network**
  Connect with alumni to gain insights and career advice.

- [ ] 🎓 **Rèsume Digital Badges**
  Get Sharable and Digital Badges on the Basis of Resume also steps to Improve your Resume and QR Code for Quick Resume Share.

Stay tuned for these exciting updates! 🔥💯🚀

Feel free to reach out if you have any suggestions or questions in CampusConnect Project Chat. We're committed to enhancing your campus experience. Happy connecting on CampusConnect! 😄📚🎉
