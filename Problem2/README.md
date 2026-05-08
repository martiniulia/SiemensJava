# Fraud Ring Detection System

## Overview
This project implements a high-performance engine for detecting fraud rings within financial transfer data. In Anti-Money Laundering (AML) contexts, a fraud ring is identified as a circular flow of funds where money is transferred through a series of accounts and eventually returns to the point of origin.

The system is built in pure Java, prioritizing algorithmic efficiency and clarity without the use of external frameworks.

## Core Concepts
The detection engine models bank accounts as nodes and transfers as directed edges in a graph. A fraud ring corresponds to a **Strongly Connected Component (SCC)** of size $\ge 2$.

### Detection Strategy
To minimize false positives and ensure relevance, the system applies several analytical layers:

1.  **Algorithmic Identification**: Uses **Tarjan's SCC Algorithm** to find all cycles in $O(V + E)$ time, ensuring the system remains performant even with large datasets.
2.  **Temporal Constraints**: Implements time-window analysis. A cycle is only flagged if the entire sequence of transfers occurs within a configurable duration (e.g., 24 hours).
3.  **Risk Scoring**: Assigns a risk level (`LOW`, `MEDIUM`, `HIGH`, `CRITICAL`) based on a combination of the number of accounts involved and the total volume of funds circulated.
4.  **Minimum Thresholds**: Filters out low-value transactions to focus on significant financial movements.

## Implementation Details
*   **Data Structures**: IBANs are mapped to integer indices for optimized graph operations.
*   **Efficiency**: The single-pass DFS approach ensures that all potential fraud rings are discovered simultaneously without redundant traversals.
*   **Flexibility**: The detection engine can be configured with custom financial thresholds and temporal windows.

## Testing and Validation
The project includes a comprehensive suite of unit tests and demonstration scenarios:
*   **Unit Tests**: Validate the mathematical correctness of the SCC discovery and the accuracy of the risk scoring matrix.
- **Scenario Testing**: The `Main` class includes seven distinct scenarios, including edge cases like nested rings, overlapping cycles, and rings that fail the temporal or financial filters.

## Prerequisites
*   Java 21 or higher.
*   Maven (optional, for running tests).

