# Project Nine: Data Science

## Project Plan

| # | Task | Estimated time | Depends on | Actual time |
|---|------|----------------|------------|-------------|
| 1 | Define core ML concepts (Q1–4, Q11) | 0.75 hr | — | 20 min |
| 2 | Explain methods (Q5–7, Q9, Q12) | 0.75 hr | — | 20 min |
| 3 | Scenario questions: feature selection, Euclidean distance code, imputation (Q8, Q10, Q13) | 0.5 hr | Task 1 | 10 min |
| 4 | Compile and review | 0.5 hr | Tasks 1–3 | 10 min |
| | **Total** | **2.5 hrs** | | **1 hr** |

---

### 1. What is ensemble learning?
Combining the predictions of several models to get a more accurate and stable result than any single model.
- **Bagging:** train models in parallel on random samples of the data, then vote or average (e.g., Random Forest).
- **Boosting:** train models one after another, each fixing the errors of the last (e.g., XGBoost, AdaBoost).
- **Stacking:** a final "meta" model learns how best to combine the base models.

### 2. What is a confusion matrix?
A table that compares a classifier's predictions with the actual classes.

| | Predicted Positive | Predicted Negative |
|--|--|--|
| **Actual Positive** | True Positive (TP) | False Negative (FN) |
| **Actual Negative** | False Positive (FP) | True Negative (TN) |

Metrics derived from it: **Accuracy** = (TP+TN)/All · **Precision** = TP/(TP+FP) · **Recall** = TP/(TP+FN) · **F1** = 2·P·R/(P+R)

### 3. What is a random forest?
An ensemble of many decision trees. Each tree trains on a random sample of rows and considers a random subset of features at each split. The forest predicts by **majority vote** (classification) or **average** (regression). This reduces the overfitting a single tree suffers from, and it also gives **feature importance** scores.

### 4. What is a normal distribution? How do you normalize data?
A **normal distribution** is a symmetric, bell-shaped distribution defined by its mean (μ) and standard deviation (σ). Mean = median = mode, and about 68% / 95% / 99.7% of values fall within 1 / 2 / 3 σ of the mean.

**Normalizing** puts features on a comparable scale:
- **Min-max scaling:** x′ = (x − min) / (max − min), which gives values in [0, 1].
- **Z-score standardization:** z = (x − μ) / σ, which gives mean 0 and standard deviation 1.

Fit the scaler on the training data only, then apply it to the test data. For skewed data, a log or Box-Cox transform makes it closer to normal.

### 5. High-level steps for data pre-processing and cleaning
1. Explore the data (shape, types, summary statistics, plots).
2. Handle missing values (drop or impute).
3. Remove duplicates.
4. Fix data types and inconsistent formats (dates, category spellings).
5. Detect and treat outliers.
6. Encode categorical variables (one-hot or label encoding).
7. Scale or normalize numeric features.
8. Engineer and select features.
9. Split into train, validation, and test sets, without letting test information leak into training.

### 6. How is k-NN different from k-means?

| | k-NN (k-Nearest Neighbors) | k-means |
|--|--|--|
| Type | **Supervised** (classification/regression) | **Unsupervised** (clustering) |
| Needs labels? | Yes | No |
| What *k* means | Number of neighbors consulted | Number of clusters to create |
| How it works | Finds the k closest labeled points and takes a vote or average | Repeatedly assigns points to the nearest centroid, then recomputes the centroids |
| Training | None ("lazy"); all the work happens at prediction time | Iterative training until the centroids stop moving |

### 7. How would you handle an imbalanced data set?
**Resample the training set.** Oversample the minority class with **SMOTE** (which creates synthetic minority examples) or undersample the majority class. Resample only the training data, never the test data. Then judge the model on **precision, recall, F1, or PR-AUC**, not accuracy: a model that always predicts "negative" can still score 99% accuracy.
(Another option: class weights, e.g., `class_weight="balanced"`.)

### 8. Finding useful columns quickly (100,000 rows × 100 columns), explained to a 5-year-old

**Technique 1: Correlation (a filter method)**
> You want to guess how tall a friend will be. You notice that when their **shoe size** goes up, their height goes up too, so shoe size is a good clue. Their **favorite color** doesn't change with height at all, so it's a useless clue. We keep the clues that move together with the answer.

*In practice:* compute the correlation (or mutual information / chi-square for categories) between each column and the target, then keep the top-scoring columns. It is fast even on 100 columns.

**Technique 2: Random forest feature importance (an embedded method)**
> You play "20 Questions" to guess an animal. "Does it fly?" helps a lot. "Is its name long?" doesn't help at all. A random forest plays this game thousands of times and counts which questions helped the most.

*In practice:* train a random forest, rank the columns by `feature_importances_`, and keep the top ones.

### 9. Supervised vs. unsupervised learning

| | Supervised | Unsupervised |
|--|--|--|
| Data | Labeled (the answers are known) | Unlabeled |
| Goal | Predict a known output | Find hidden structure or groups |
| Tasks | Classification, regression | Clustering, association, dimensionality reduction |
| Examples | Spam detection, house prices | Customer segmentation, market-basket analysis |
| Algorithms | Linear/logistic regression, decision trees, k-NN | k-means, hierarchical clustering, PCA |

### 10. Euclidean distance in Python: plot1 = [1, 3], plot2 = [2, 5]

```python
import math

plot1 = [1, 3]
plot2 = [2, 5]

euclidean_distance = math.sqrt((plot1[0] - plot2[0]) ** 2 + (plot1[1] - plot2[1]) ** 2)
print(euclidean_distance)          # 2.23606797749979

print(math.dist(plot1, plot2))     # built-in shortcut, same result
```

**Result:** √((1−2)² + (3−5)²) = √(1 + 4) = **√5 ≈ 2.236**. Runnable script: [euclidean_distance.py](euclidean_distance.py)

### 11. What is dimensionality reduction, and what are its benefits?
Reducing the number of input features while keeping most of the useful information. It is done either by **selecting** a subset of features or by **extracting** new combined features (PCA, LDA, t-SNE/UMAP for visualization).

**Benefits:** faster training and less storage, less overfitting (the "curse of dimensionality"), redundant and correlated features removed, less noise, and data that can be plotted in 2-D or 3-D.

### 12. How should you maintain a deployed model?
- **Monitor:** prediction quality against actual outcomes, **data drift** (inputs change), and **concept drift** (the relationship between inputs and outputs changes), plus latency and errors.
- **Log** inputs and predictions so problems can be investigated.
- **Retrain** on fresh data on a schedule, or when performance drops.
- **Version** models and data (e.g., MLflow), and test a new model against the current one (A/B or champion/challenger) before replacing it.
- **Automate** retraining and deployment with an MLOps (CI/CD) pipeline, and keep a rollback plan.

### 13. Which algorithm can impute missing values of both categorical and continuous variables?
**Answer: C. k-NN.** It fills a missing value from the *k* most similar rows: the **mean** of the neighbors for continuous variables and the **mode** (most common value) for categorical ones.
- *K-means* (A) is a clustering method, not an imputation method.
- *Linear regression* (B) predicts only continuous values.
- *Decision trees* (D) can also impute both types, but only by training a separate model per column, so k-NN is the standard answer.
