# Project Seven: Big Data

## Project Plan

| # | Task | Estimated time | Depends on | Actual time |
|---|------|----------------|------------|-------------|
| 1 | Define Big Data | 0.25 hr | — | 5 min |
| 2 | List 5 vendors and their Big Data products | 0.5 hr | — | 15 min |
| 3 | Compare 3 platforms (hardware, database, ETL, front end) | 1 hr | Task 2 | 30 min |
| 4 | Write two detailed use cases | 0.5 hr | Task 1 | 15 min |
| 5 | Analyze the situation: sizing, cost, dashboards, scaling | 1 hr | Task 3 | 30 min |
| 6 | Compile and review | 0.25 hr | Tasks 1–5 | 10 min |
| | **Total** | **3.5 hrs** | | **1 hr 45 min** |

---

## 1. What is Big Data?

**Big Data** is data that is too large, too fast, or too varied to store and process with a traditional single-server database. Handling it requires **distributed storage** and **parallel processing** across a cluster of machines. It is usually described by the **5 Vs**:

| V | Meaning |
|---|---------|
| Volume | Terabytes to petabytes of data |
| Velocity | Data arriving continuously (streams, sensors, clicks) |
| Variety | Structured (tables), semi-structured (JSON, logs), unstructured (text, images) |
| Veracity | Data of uncertain quality that must be cleaned |
| Value | The business insight the data can produce |

## 2. Five Vendors and Their Big Data Products

| Vendor | Big Data products |
|--------|-------------------|
| Cloudera | Cloudera Data Platform (CDP): Hadoop HDFS, Hive, Impala, Spark, Kafka, NiFi |
| Databricks | Databricks Data Intelligence Platform: Apache Spark, Delta Lake, Unity Catalog |
| Amazon Web Services | Amazon EMR, Amazon Redshift, Amazon Kinesis, AWS Glue, Amazon Athena |
| Microsoft | Microsoft Fabric, Azure Databricks, Azure Synapse Analytics, Azure HDInsight |
| Google Cloud | BigQuery, Dataproc (managed Hadoop/Spark), Dataflow, Pub/Sub |

## 3. Comparing 3 Big Data Platforms

The three platforms represent the three main deployment models: **on-premises Hadoop** (Cloudera), **managed Hadoop in the cloud** (Amazon EMR), and a **cloud lakehouse** (Databricks).

| | Cloudera CDP (Private Cloud Base) | Amazon EMR | Databricks |
|--|--|--|--|
| **Deployment** | Self-managed, on-premises (or on IaaS) | Managed clusters on AWS EC2 (also EMR Serverless) | SaaS on AWS, Azure, or GCP; compute runs in your cloud account or serverless |
| **1. Hardware requirements** | You buy commodity x86 servers. Master nodes run NameNode/ResourceManager (3 for high availability). Worker nodes have 8–16+ cores, 64–256 GB RAM, several local disks, and 10 GbE networking | No hardware to buy. You choose EC2 instance types: 1 primary node, plus core nodes (with HDFS) and optional task nodes. Autoscaling and Spot instances are supported | No hardware to buy. Clusters are cloud VMs (driver + workers) with autoscaling, or fully serverless compute |
| **2. Database / storage** | HDFS, Hive (warehouse), Impala (fast SQL), HBase/Kudu (real-time NoSQL) | Amazon S3 as the data lake (via EMRFS), HDFS on core nodes, Hive, HBase, Trino/Presto; AWS Glue Data Catalog for metadata | Delta Lake tables on object storage (S3/ADLS/GCS), Databricks SQL warehouses, Unity Catalog for governance |
| **3. ETL tools** | NiFi (ingestion), Kafka (streaming), Spark, Hive SQL, Oozie/Airflow (scheduling) | Spark, Hive, AWS Glue, Kinesis / Amazon MSK (streaming), Step Functions / Managed Airflow | Spark (PySpark/SQL), Auto Loader, Structured Streaming, Lakeflow Declarative Pipelines (formerly Delta Live Tables), Lakeflow Jobs |
| **4. Front-end tools** | Hue (SQL editor), Cloudera Data Visualization; Tableau/Power BI through ODBC/JDBC | EMR Studio (notebooks), Athena, Amazon QuickSight; Tableau/Power BI | Notebooks, Databricks SQL / AI/BI dashboards, Genie (natural-language queries); Power BI/Tableau connectors |
| **Strengths** | Full control, data stays on-site, lowest cost at large steady scale | Pay per use, scales in minutes, storage separate from compute | ETL, BI, and ML in one place; fast Spark (Photon engine); multi-cloud |
| **Trade-offs** | High upfront cost, needs Hadoop admins, slow to scale (must buy hardware) | Tied to AWS; idle clusters still cost money | Usage-based (DBU) pricing is harder to predict; not classic Hadoop (no HDFS/YARN) |

## 4. Two Big Data Use Cases

### Use Case 1: Predictive Maintenance in Manufacturing
**Problem:** Unplanned machine breakdowns stop production lines and are expensive. Each machine has sensors (vibration, temperature, pressure) that produce millions of readings a day. A traditional database cannot store that volume or analyze it fast enough to warn before a failure.

**Big Data solution:**
1. **Ingest:** sensors stream readings to **Kafka**.
2. **Real-time processing:** **Spark Structured Streaming** checks each reading against thresholds and an anomaly model, and sends an alert within seconds.
3. **Storage:** raw readings land in the data lake (HDFS/S3) as compressed Parquet, partitioned by date and machine.
4. **Machine learning:** a nightly **Spark MLlib** job trains a model on months of sensor history linked to past failures, and scores each machine's probability of failing in the next 7 days.
5. **Dashboards:** Power BI/Grafana shows machine health, risk scores, and alerts, and maintenance is scheduled before a breakdown.

**Outcome:** less unplanned downtime, lower repair costs, and longer equipment life.

### Use Case 2: Real-Time Credit Card Fraud Detection
**Problem:** A bank processes thousands of card transactions per second and must decide in milliseconds whether each one is fraudulent. Rules-only systems miss new fraud patterns and block too many genuine customers.

**Big Data solution:**
1. **Ingest:** every transaction is published to **Kafka**.
2. **Enrichment:** a stream processor (**Spark** or **Flink**) adds the customer's recent history and profile from a low-latency store (**HBase/Cassandra**).
3. **Scoring:** an ML model trained on years of labeled transactions in the data lake scores each transaction in real time. High-risk transactions are blocked or sent for two-factor verification.
4. **Feedback loop:** every transaction and its outcome (confirmed fraud or not) is stored in the data lake, and the model is retrained regularly so it learns new patterns.
5. **Dashboards:** fraud analysts monitor alerts, fraud rates, and model accuracy.

**Outcome:** fraud caught in real time, fewer false declines, and lower losses.

---

## 5. Situation Analysis: 10 Devices Writing CSV Logs

**Data volume.** 10 devices × 10 rows/second × 86,400 seconds = **8.64 million rows/day**. The assignment's "~1 million rows/day" figure is lower, so the sizing below uses 8.64 million as the worst case. The minimum cluster is the same either way.
- Assuming ~200 bytes per CSV row: **~1.7 GB/day**, **~52 GB/month**, **~630 GB/year** raw.
- Stored as compressed Parquet/ORC, this is roughly 5–10× smaller.
- On-premises HDFS keeps 3 copies of each block (~1.9 TB/year of raw CSV).

**Pipeline (the same design on all three platforms):**

```
10 devices ─► Kafka / Kinesis (real-time ingest) ─► raw zone in HDFS / S3 / Delta (partitioned by date, device)
                                                      ├─► daily Spark job   ─► daily_agg   (count, min/avg/max per device)
                                                      └─► monthly Spark job ─► monthly_agg (rolled up from daily_agg)
                                                                              └─► SQL engine ─► BI dashboards
```

### Sizing and Cost per Platform

| | Cloudera CDP (on-premises) | Amazon EMR | Databricks (on AWS) |
|--|--|--|--|
| **Minimum hardware** | 4 servers: 1 master (8 cores, 32 GB RAM) + 3 workers (8 cores, 32–64 GB RAM, 2 × 4 TB disks). 3 workers are needed because HDFS stores 3 copies | 3 × m5.xlarge (4 vCPU, 16 GB): 1 primary + 2 core | 1 driver + 1–2 workers (m5.xlarge), or serverless |
| **Recommended cluster** | 8 servers: 3 masters (high availability) + 4 workers (16 cores, 128 GB, 6 × 4 TB) + 1 edge node (Kafka, NiFi, Hue) | Kinesis Data Firehose → S3 for ingest (no cluster needed), plus an EMR cluster (1 primary + 2–3 core m5.xlarge) that starts for the daily/monthly jobs and then terminates | Auto Loader job on a small cluster (1 driver + 2 workers), scheduled aggregation jobs, and a 2X-Small SQL warehouse with auto-stop for dashboards |
| **Approx. cost\*** | Minimum ~$35–50k; recommended ~$80–120k in hardware up front (~$10–15k per server), plus a quote-based Cloudera subscription, power, and admin staff | Always-on 4 nodes: ~$700/month (4 × $0.24/hr × 730 hr). Transient design: **under $100/month** (cluster ~1 hr/day + Firehose + S3) | Always-on streaming: ~$650/month compute + ~$100–300/month SQL warehouse. Micro-batch every 15 min: ~$300–500/month total |

\*Rough US list prices (2026) for comparison only. EMR: m5.xlarge EC2 $0.192/hr + EMR $0.048/hr. Databricks: Jobs Compute ~$0.15 per DBU plus EC2. Use the vendor pricing calculators for real quotes.

### Dashboard Setup
The aggregated tables are queried through each platform's SQL engine: **Impala** (Cloudera), **Athena/Trino** (EMR), or a **Databricks SQL warehouse**. A BI tool connects to that engine through ODBC/JDBC.

| Dashboard | Source | Refresh | Tool |
|-----------|--------|---------|------|
| Live device status (rows/min, last reading, errors) | Raw stream / latest partition | Every 1 min | Grafana |
| Daily trends per device | `daily_agg` | After the daily job | Power BI / Tableau / QuickSight / Databricks AI/BI |
| Monthly summary and month-over-month comparison | `monthly_agg` | After the monthly job | Same as above |
| Alerts and anomalies (device silent, out-of-range values) | Stream + `daily_agg` | Real time | Grafana alerts / email |

### Adding More Data Streams
- **Design for it:** every row carries a `device_id`, and all devices publish to one topic keyed by device. A new device then needs no pipeline changes.
- **Cloudera:** add a NiFi flow (drag-and-drop) or a new Kafka topic / Kafka Connect source.
- **AWS:** add a Kinesis stream or Firehose delivery stream, an MSK topic, or an AWS IoT Core rule.
- **Databricks:** Auto Loader picks up new files and folders automatically, and Delta Lake schema evolution handles new columns.

### Scaling to 100 and 1,000 Devices

| Devices | Rows/day | Raw data/day | What changes |
|---------|----------|--------------|--------------|
| 10 | 8.6 M | ~1.7 GB | Minimum cluster |
| 100 | 86 M | ~17 GB (~6 TB/yr) | Add Kafka partitions and grow to ~6–8 workers. On EMR/Databricks, raise the autoscale limit |
| 1,000 | 864 M (~10,000 rows/s) | ~170 GB (~60 TB/yr) | 3-broker Kafka cluster, ~15–25 workers, compaction of small files, partitioning by date and device |

**How easy is it to scale?** All three platforms scale **horizontally**: you add nodes rather than buying bigger ones.
- **EMR and Databricks:** a configuration change that takes effect in minutes, with autoscaling.
- **Cloudera on-premises:** you must buy, rack, and add servers, which takes weeks. It is the cheapest option once volume is large and steady.
- **Ingest speed is not the problem:** 10,000 rows/s is light work for Kafka. At 1,000 devices, the real cost drivers are **storage and retention**, which compression, aggregation, and archiving old raw data keep under control.
