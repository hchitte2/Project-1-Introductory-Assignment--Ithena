"""Euclidean distance between two 2-D points, computed three equivalent ways."""

import math

plot1 = [1, 3]
plot2 = [2, 5]


def euclidean_distance(p, q):
    """Straight-line distance: sqrt((p0 - q0)^2 + (p1 - q1)^2)."""
    return math.sqrt((p[0] - q[0]) ** 2 + (p[1] - q[1]) ** 2)


if __name__ == "__main__":
    # 1. Formula from the hint
    print("Formula:   ", euclidean_distance(plot1, plot2))

    # 2. Standard library (Python 3.8+), works for any number of dimensions
    print("math.dist: ", math.dist(plot1, plot2))

    # 3. NumPy, the usual choice for arrays of points
    try:
        import numpy as np

        print("NumPy:     ", np.linalg.norm(np.array(plot1) - np.array(plot2)))
    except ImportError:
        print("NumPy:      not installed (pip install numpy)")
