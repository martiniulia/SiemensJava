# Siemens Java Project

This repository contains two distinct Java applications developed for the Siemens recruitment process.

---

## Problem 1: Train Ticketing Application

### Overview
A console-based Java application for managing train routes, stations, and booking tickets. It features direct and changeover route searching, seat availability management, and email notifications.

### Configuration
Before running the application, you **must** update the configuration file in the `Problem1` directory to enable email notifications:
1. Open `Problem1/config.properties`.
2. Update the following fields with your SMTP email credentials:
   * `mail.username`: Your email address.
   * `mail.password`: Your email app password.
   * `mail.from`: The email address for the sender.

---

## Problem 2: Fraud Ring Detection System

### Overview
A high-performance engine for detecting fraud rings within financial transfer data. It identifies circular flows of funds using **Tarjan's SCC Algorithm** and applies temporal constraints and risk scoring to flag suspicious activities.

### Execution
The system is built in pure Java and can be executed via the `Main` class located in `Problem2/src/main/java/fraud/Main.java`. It includes several pre-configured scenarios to demonstrate the detection of:
* Simple and nested rings.
* Rings that exceed temporal windows (e.g., > 24h).
* Rings with high-risk financial volumes.

---

## General Information

### Prerequisites
* **JDK 23**: Ensure you have Java Development Kit 23 installed.
* **Maven**: Both projects use Maven for dependency management. For Problem 1, a local Maven installation is provided in `Problem1/apache-maven-3.9.6`.

### Data Storage
Problem 1 uses JSON files for persistent data storage, located in the `Problem1/data/` folder. Problem 2 uses in-memory data for its demonstration scenarios.
