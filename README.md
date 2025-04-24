# BTO Management System

A Java-based Build-To-Order (BTO) flat management system designed to simulate the application, registration, and administrative processes of HDB flats in Singapore. This system supports multiple user roles and emphasizes role-based logic, modular design, and real-world workflow simulation using object-oriented principles.

---

## 🚀 Features

- **Role-Based Access**
  - Applicants can apply for projects, view availability, and manage enquiries.
  - HDB Officers can register for projects, manage bookings, and respond to enquiries.
  - HDB Managers can control project visibility, generate reports, and reply to enquiries.

- **Modular Project Browsing**
  - Filter projects by flat type, location, price ceiling, and availability.

- **Report Generation**
  - Managers can generate filtered reports (by age, marital status, project, flat type).

- **Validation & Constraints**
  - Officers can’t apply for projects they manage.
  - Officers cannot manage overlapping projects.
  - Eligibility checks enforced for all applications.

- **Console UI (CLI)**
  - Role-based menus for each user type.
  - Simple navigation through user-friendly prompts.

---

## 🧱 System Architecture

Designed using the **Entity-Boundary-Control (EBC)** architecture:

- **Entity Layer**: `Applicant`, `HDBOfficer`, `BTOProject`, `BTOApplication`, `Enquiry`
- **Boundary Layer**: `ApplicantUIHandler`, `ManagerUIHandler`, `OfficerUIHandler`
- **Control Layer**: `UserLoader`, `ReportGenerator`, `ApplicantEnquiryHandler`

This structure supports separation of concerns and aligns with SOLID principles like SRP, OCP, and ISP.

---

## 📂 Project Structure

src/
├── entity/                # Core data models
├── boundary/              # Role-specific UI menus
├── control/               # Core logic and processing
├── data/                  # CSV data files
├── main/                  # Entry point and Main class

## 🛠 How to Run

1. Clone the repository to your local machine.
2. Open the project in **Eclipse** or another Java IDE.
3. Ensure all `.csv` files are placed in the `/data` folder.
4. Run `Main.java` from the `/main` directory.
5. Follow the menu to log in as Applicant, Officer, or Manager.

> Java 17 or later is recommended.
> ## 👨‍💻 Team Members

## 👨‍💻 Team Members

| Name             | Role & Contributions                                                                 |
|------------------|----------------------------------------------------------------------------------------|
| Akshaya          | Wrote SOLID Principles, System Architecture & Reflection; Set up GitHub repo; Coded `HDBOfficer` |
| Max (Group Leader) | Lead developer; Integrated all components; Contributed to report writing             |
| Serena           | Coded `HDBManager`, drew sequence diagrams, and contributed to that report section     |
| Jia Ying         | Coded `HDBOfficer`; Wrote Chapter 3; Created class diagram                             |
| Jing Ni          | Coded `HDBManager`; Added method comments; Generated Javadoc                           |


## 📌 Notes

- The system uses **CSV files** as a data source (read-only).
- Bookings and changes are **not saved** across sessions.
- Role-specific validation and filters are implemented.
- A GUI version may be considered in future versions.

