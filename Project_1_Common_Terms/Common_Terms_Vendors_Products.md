# Project One: Common Terms, Vendors, and Products

## Project Plan

| # | Task | Estimated time | Depends on | Actual time |
|---|------|----------------|------------|-------------|
| 1 | Define the 6 terms | 0.75 hr | — | 50 min |
| 2 | Identify top 5 vendors per term (30 total) | 1 hr | — | 1 hr 10 min |
| 3 | Identify top 2 products per vendor (60 total) | 0.75 hr | Task 2 | 55 min |
| 4 | Compile, format, and review the document | 0.5 hr | Tasks 1, 2, 3 | 35 min |
| | **Total** | **3 hrs** | | **3.5 hrs** |

**How "top" was decided:** vendors were ranked by market share and by their position in industry analyst reports (Gartner Magic Quadrants, IDC market trackers). Products are each vendor's flagship or most widely adopted offerings in that category.

---

## 1. ERP (Enterprise Resource Planning)

**Definition:** ERP is business software that combines a company's core processes, such as finance, accounting, procurement, supply chain, manufacturing, HR, and sales, in one system with a shared database. Because every department works from the same data, ERP removes duplicate entry, gives one view of the business, and serves as a main data source for BI and reporting.

| Vendor | Product 1 | Product 2 |
|--------|-----------|-----------|
| SAP | SAP S/4HANA | SAP Business One |
| Oracle | Oracle Fusion Cloud ERP | Oracle NetSuite |
| Microsoft | Dynamics 365 Finance & Operations | Dynamics 365 Business Central |
| Infor | Infor CloudSuite Industrial (SyteLine) | Infor M3 |
| Workday | Workday Financial Management | Workday Human Capital Management |

## 2. Server Hardware

**Definition:** Server hardware is the physical computing equipment built to run applications, databases, and services for many users or systems at once. Compared with desktop PCs, servers have more processors, memory, and storage, plus redundant power, hot-swappable parts, and remote management. They come as rack, tower, blade, and mainframe systems, and are housed in data centers or on-premises server rooms.

| Vendor | Product 1 | Product 2 |
|--------|-----------|-----------|
| Dell Technologies | PowerEdge rack servers (e.g., R760) | PowerEdge XE-series AI/GPU servers |
| Hewlett Packard Enterprise (HPE) | ProLiant DL380 | HPE Synergy (composable infrastructure) |
| Lenovo | ThinkSystem SR650 | ThinkSystem SR630 |
| Supermicro | Hyper SuperServer | GPU SuperServer |
| IBM | IBM Z mainframe | IBM Power servers |

## 3. Database / DBMS

**Definition:** A database is an organized collection of stored data. A Database Management System (DBMS) is the software used to create, store, query, update, and secure that data while handling concurrent access, backup, and recovery. The main types are relational (RDBMS, which use tables and SQL), NoSQL (document, key-value, graph), and cloud data warehouses built for analytics.

| Vendor | Product 1 | Product 2 |
|--------|-----------|-----------|
| Oracle | Oracle Database | MySQL |
| Microsoft | SQL Server | Azure SQL Database |
| IBM | Db2 | Informix |
| Amazon Web Services (AWS) | Amazon Aurora | Amazon DynamoDB |
| Google Cloud | BigQuery | Cloud Spanner |

## 4. ETL (Extract, Transform, Load)

**Definition:** ETL is the process of moving data from source systems into a target such as a data warehouse:
- **Extract:** pull data from sources (ERP, CRM, files, APIs, databases).
- **Transform:** clean, standardize, deduplicate, join, and aggregate the data according to business rules.
- **Load:** write the prepared data into the target system for reporting and analytics.

ETL tools automate, schedule, and monitor these pipelines. In a common modern variant, ELT, data is loaded first and then transformed inside the warehouse.

| Vendor | Product 1 | Product 2 |
|--------|-----------|-----------|
| Informatica | PowerCenter | Intelligent Data Management Cloud (IDMC) |
| Microsoft | SQL Server Integration Services (SSIS) | Azure Data Factory |
| IBM | IBM DataStage | IBM StreamSets |
| Oracle | Oracle Data Integrator (ODI) | Oracle GoldenGate |
| Qlik (Talend) | Talend Data Fabric | Qlik Replicate |

## 5. BI (Business Intelligence)

**Definition:** Business Intelligence is the set of practices and tools that turn raw business data into information for decision-making. It covers data warehousing, reporting, dashboards, ad-hoc querying, data visualization, and KPI tracking. BI tools usually sit on top of a data warehouse or data mart that ETL processes fill, and they let business users monitor performance and find trends.

| Vendor | Product 1 | Product 2 |
|--------|-----------|-----------|
| Microsoft | Power BI | Microsoft Fabric |
| Salesforce | Tableau | CRM Analytics |
| Qlik | Qlik Sense | QlikView |
| Google | Looker | Looker Studio |
| Oracle | Oracle Analytics Cloud | Oracle Analytics Server |

## 6. Mobility

**Definition:** Enterprise mobility is a company's ability to let employees work and reach business applications and data from mobile devices (smartphones, tablets, laptops) wherever they are. It covers the devices and mobile operating systems themselves, mobile apps (including mobile BI dashboards), and Enterprise Mobility Management (EMM/UEM). EMM/UEM means securing, configuring, and managing devices, apps, and corporate data, including under BYOD (Bring Your Own Device) policies.

| Vendor | Product 1 | Product 2 |
|--------|-----------|-----------|
| Apple | iPhone / iPad (iOS, iPadOS) | Apple Business Manager |
| Google | Android Enterprise | Pixel devices |
| Samsung | Samsung Knox Suite | Galaxy enterprise devices (e.g., XCover rugged series) |
| Microsoft | Microsoft Intune | Power Apps (mobile app building) |
| Omnissa (formerly VMware EUC) | Workspace ONE UEM | Horizon (virtual desktops and apps) |

---

## How the Terms Connect

In a typical BI setup, these pieces fit together as follows:
1. **ERP** systems generate transactional data.
2. **Server hardware** (on-premises or in the cloud) runs everything.
3. **ETL** tools extract that data and load it into a **database** or data warehouse.
4. **BI** tools analyze and visualize the data.
5. **Mobility** puts those dashboards and apps on users' devices.
