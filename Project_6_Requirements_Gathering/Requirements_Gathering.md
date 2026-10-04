# Project Six: Requirements Gathering

## Project Plan

| # | Task | Estimated time | Depends on | Actual time |
|---|------|----------------|------------|-------------|
| 1 | Describe the requirements gathering process | 1 hr | — | |
| 2 | List common key assumptions | 0.5 hr | Task 1 | |
| 3 | Functional vs. technical requirements, with 4 examples of each | 1 hr | Task 1 | |
| 4 | Define gap analysis | 0.5 hr | — | |
| 5 | List key properties of the final deliverable | 0.5 hr | Task 1 | |
| 6 | Compile and review | 0.5 hr | Tasks 1–5 | |
| | **Total** | **4 hrs** | | |

---

## 1. The Requirements Gathering Process

1. **Identify stakeholders:** sponsors, business users, IT, and data owners.
2. **Define objectives and scope:** the problem being solved, what success looks like, and what is in or out of scope.
3. **Elicit requirements:** run interviews and workshops (JAD sessions), send surveys, observe users at work, review existing reports and documents, and build prototypes.
4. **Document:** write a Business Requirements Document (BRD) or functional specification, user stories with acceptance criteria, and use cases.
5. **Analyze and prioritize:** resolve conflicting requests, check feasibility, and rank requirements (e.g., MoSCoW: Must / Should / Could / Won't).
6. **Validate and sign off:** walk stakeholders through the documented requirements and get formal approval. The approved set becomes the baseline.
7. **Manage change:** trace each requirement through design and testing (traceability matrix), and route any new request through change control.

## 2. Common Key Assumptions

- Stakeholders and subject-matter experts will be available for interviews and reviews.
- Source data exists, can be accessed, and is of usable quality (or will be cleaned).
- Scope stays stable after sign-off, and any change goes through change control.
- Required infrastructure, licenses, and environments (dev/test/prod) will be ready on time.
- Budget and timeline are approved.
- Existing systems and interfaces will not change during the project.
- Users will receive training on the new system.

## 3. Functional vs. Technical Requirements

| | Functional | Technical |
|--|------------|-----------|
| Answers | **What** the system must do | **How** the system must be built and run |
| Focus | Features, business rules, user tasks | Platform, performance, security, integration |
| Written by / for | Business users and analysts | Architects, developers, IT operations |
| Verified by | User acceptance testing (UAT) | System, performance, and security testing |

### 4 Functional Requirements Common to Any Project

1. **User authentication and role-based access:** users log in, and each role sees only the features it is allowed to use.
2. **Data entry and validation:** users can create, update, and delete records, and the system enforces business rules on them.
3. **Reporting:** users can view reports and dashboards, filter them, and export them (PDF/Excel).
4. **Audit trail:** the system records who changed what and when.

### 4 Technical Requirements Common to Any Project

1. **Performance:** e.g., pages and reports load in under 3 seconds with 100 concurrent users.
2. **Security:** TLS encryption in transit, encryption at rest, hashed passwords, and SSO integration.
3. **Availability and recovery:** e.g., 99.9% uptime, daily backups, and defined RPO/RTO targets.
4. **Compatibility and integration:** supported browsers, OS, and database versions, plus APIs to existing systems (e.g., ERP).

## 4. Gap Analysis

**Gap analysis** compares the **current state** ("as-is") with the **desired future state** ("to-be") to find what is missing (capabilities, data, processes, or skills) and to plan how to close each gap.

In a software implementation, every requirement is mapped against what the product offers out of the box:
- **Fit:** met as-is.
- **Partial fit:** met through configuration.
- **Gap:** needs customization, a workaround, or a business process change.

## 5. Key Properties of the Final Deliverable to Document

For each report or dashboard in a BI project, document:

| Property | Example |
|----------|---------|
| Purpose / business question | "Which regions are missing sales targets?" |
| Audience and access | Sales managers; each manager sees only their region (row-level security) |
| KPIs and calculation rules | Target attainment = Actual sales ÷ Target × 100 |
| Dimensions, filters, drill-down | Time → Region → Store → Product |
| Data sources and owners | ERP sales tables, owned by Finance |
| Granularity and history | Daily data, 3 years of history |
| Refresh frequency | Daily by 7 AM |
| Format and delivery | Web dashboard + monthly PDF by email, mobile-friendly |
| Performance expectations | Loads in under 5 seconds |
| Acceptance criteria and sign-off | Totals match the ERP ledger; approved by the Sales VP |
