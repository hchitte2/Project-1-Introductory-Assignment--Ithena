# Project Three: DBMS Middleware

## Project Plan

| # | Task | Estimated time | Depends on | Actual time |
|---|------|----------------|------------|-------------|
| 1 | Identify the 5 main DBMS providers | 0.25 hr | — | |
| 2 | Research the middleware associated with each provider | 1 hr | Task 1 | |
| 3 | Document middleware by provider and review | 0.25 hr | Task 2 | |
| | **Total** | **1.5 hrs** | | |

**Middleware** is software that sits between applications and databases so they can talk to each other without knowing each database's internals. In a DBMS setting it usually does one of these jobs:

- **Connectivity:** drivers and network layers that carry SQL between an app and a database (e.g., ODBC, JDBC).
- **Application server:** hosts business logic and pools database connections, often behind a web server.
- **Messaging:** passes data between systems reliably and asynchronously, through queues.
- **Integration / replication:** moves or syncs data between different databases.
- **API layer:** exposes database data as REST/web services.

The 5 providers are the same DBMS vendors used in Project 1, chosen for their market share: Oracle, Microsoft, IBM, AWS, and Google Cloud.

---

## 1. Oracle (Oracle Database, MySQL)

| Middleware | Type | What it does |
|------------|------|--------------|
| Oracle Net Services (SQL*Net) | Connectivity | Network layer that connects client apps to Oracle Database |
| Oracle WebLogic Server | Application server | Java EE server at the core of Oracle Fusion Middleware |
| Oracle REST Data Services (ORDS) | API layer | Publishes tables and PL/SQL as REST APIs through a web server |
| Oracle Database Gateways | Integration | Lets Oracle query non-Oracle databases (SQL Server, Db2) as if they were local |
| Oracle GoldenGate | Replication | Real-time change data capture (CDC) between different databases |

## 2. Microsoft (SQL Server, Azure SQL)

| Middleware | Type | What it does |
|------------|------|--------------|
| ODBC / OLE DB drivers, ADO.NET | Connectivity | Standard drivers and libraries apps use to connect to SQL Server |
| Internet Information Services (IIS) + ASP.NET | Application/web server | Hosts web apps and APIs in front of SQL Server |
| Linked Servers / PolyBase | Integration | Run queries that join SQL Server with other databases and files |
| BizTalk Server / Azure Logic Apps | Integration & messaging | Connect and orchestrate data flows between enterprise systems |
| Data API builder | API layer | Exposes SQL Server/Azure SQL tables as REST and GraphQL APIs |

## 3. IBM (Db2, Informix)

| Middleware | Type | What it does |
|------------|------|--------------|
| Db2 Connect | Connectivity | Connects apps to Db2 on mainframe (z/OS) and IBM i |
| IBM WebSphere Application Server / Liberty | Application server | Java EE runtime for enterprise apps on Db2 |
| IBM MQ | Messaging | Reliable message queuing between apps and databases |
| IBM App Connect Enterprise | Integration | Integration broker/ESB (formerly IBM Integration Bus) |
| IBM Data Virtualization (Db2 federation) | Integration | Queries many data sources through a single SQL interface |

## 4. Amazon Web Services (Aurora, RDS, DynamoDB)

| Middleware | Type | What it does |
|------------|------|--------------|
| Amazon RDS Proxy | Connectivity | Pools and shares database connections for apps and serverless functions |
| AWS Database Migration Service (DMS) | Replication | Migrates and continuously replicates data between different databases |
| Amazon API Gateway (+ AWS Lambda) | API layer | REST/HTTP endpoints in front of database logic |
| Amazon MQ / Amazon SQS | Messaging | Managed message brokers and queues |
| AWS Glue | Integration | Serverless ETL and data catalog across data stores |

## 5. Google Cloud (Cloud SQL, Spanner, BigQuery)

| Middleware | Type | What it does |
|------------|------|--------------|
| Cloud SQL Auth Proxy | Connectivity | Secure, IAM-authorized connections to Cloud SQL |
| Apigee | API layer | API gateway and management in front of backend data services |
| Datastream | Replication | Serverless CDC from Oracle/MySQL/PostgreSQL into BigQuery and Cloud Storage |
| Pub/Sub | Messaging | Global asynchronous messaging between services |
| BigQuery federated queries | Integration | Query Cloud SQL and Spanner directly from BigQuery |

---

**Vendor-neutral standards** work with all five providers: **ODBC** and **JDBC** for connectivity, **Apache Kafka** for messaging, and **Apache Tomcat** for the web/application tier. Project 5 uses JDBC with Tomcat.
