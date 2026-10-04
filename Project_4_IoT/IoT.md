# Project Four: Internet of Things (IoT)

## Project Plan

| # | Task | Estimated time | Depends on | Actual time |
|---|------|----------------|------------|-------------|
| 1 | Define IoT | 0.5 hr | — | |
| 2 | List the top 5 IoT platform vendors and their platforms | 1 hr | — | |
| 3 | List common features/components of IoT platforms | 1 hr | — | |
| 4 | Pick 3 features and the platform best known for each | 0.5 hr | Tasks 2, 3 | |
| 5 | Compile and review | 0.5 hr | Tasks 1–4 | |
| | **Total** | **3.5 hrs** | | |

Tasks 1, 2, and 3 can run in parallel.

---

## 1. What is IoT?

The **Internet of Things (IoT)** is a network of physical objects, such as sensors, machines, vehicles, meters, and appliances, that have sensors, software, and connectivity built in. These objects collect data and exchange it with other devices and systems over the internet, usually without a person involved.

**Typical flow:** device/sensor → gateway → cloud **IoT platform** → analytics and apps → action (an alert, a dashboard, or a command back to the device).

An **IoT platform** is the software layer in the middle. It connects and manages the devices, collects their data, and makes that data usable.

## 2. Top 5 IoT Platform Vendors

| Vendor | Platform(s) |
|--------|-------------|
| Amazon Web Services (AWS) | AWS IoT Core, AWS IoT Greengrass (edge), AWS IoT SiteWise (industrial) |
| Microsoft | Azure IoT Hub, Azure IoT Operations (edge), Azure Digital Twins |
| Siemens | Insights Hub (formerly MindSphere) |
| PTC | ThingWorx |
| Cumulocity (formerly part of Software AG) | Cumulocity IoT |

*Google is not listed because it retired Google Cloud IoT Core in August 2023.*

## 3. Features / Components of IoT Platforms

| Feature / Component | What it does |
|---------------------|--------------|
| Device connectivity | Connects devices using IoT protocols (MQTT, HTTP, CoAP, LoRaWAN) |
| Device management | Provisioning, monitoring, configuration, and over-the-air (OTA) firmware updates |
| Data ingestion / message broker | Receives and routes large volumes of device messages |
| Rules engine | Triggers alerts or actions when data meets a condition (e.g., temp > 80°C) |
| Data storage | Stores time-series sensor data |
| Analytics & machine learning | Finds trends, anomalies, and predictions (e.g., predictive maintenance) |
| Edge computing | Runs processing on or near the device for low latency and offline operation |
| Digital twins | Virtual models of physical assets that are kept in sync with live data |
| Security | Device identity (X.509 certificates), encryption, and access control |
| Visualization / dashboards | Charts and real-time views of device data |
| APIs & integration | Connects IoT data to business systems (ERP, CRM, BI) |

## 4. Three Features and the Platform Best Known for Each

| Feature | Best-known platform | Why |
|---------|---------------------|-----|
| Edge computing | **AWS IoT Greengrass** | Runs AWS Lambda functions, containers, and ML inference directly on devices. Keeps working offline and syncs to the cloud when connected. |
| Digital twins | **Azure Digital Twins** | Models whole environments (buildings, factories, energy grids) as a live graph of connected twins, using the open DTDL modeling language. |
| Industrial application enablement | **PTC ThingWorx** | Low-code platform for building industrial apps. Connects to factory equipment through Kepware and adds augmented reality through Vuforia. |
