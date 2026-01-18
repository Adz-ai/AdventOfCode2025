# Day 10 Part 2: Solving Counter Puzzles with Linear Algebra

> **Interactive Demo**: Open [Day10Part2Visualisation.html](./Day10Part2Visualisation.html) in your browser for a hands-on experience!

---

## The Problem

We have machines with **buttons** and **counters**. Each button press increments specific counters by 1. Our goal is to reach exact target values for all counters using the **minimum total button presses**.

### Input Format

```
[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
```

- `{10,11,11,5,10,5}` - Target values for counters 0-5
- `(0,1,2,3,4)` - Button 0 increments counters 0,1,2,3,4
- `(0,3,4)` - Button 1 increments counters 0,3,4
- etc.

---

## Why This is a Linear Algebra Problem

Each button press adds to counters linearly. If we press button `b` a total of `x_b` times, we can express our constraints as linear equations.

### Example

Say we have 2 counters and 3 buttons:
- Button 0 affects counters {0, 1}
- Button 1 affects counter {0}
- Button 2 affects counter {1}

Target: Counter 0 = 5, Counter 1 = 3

This becomes:

```
Counter 0:  1*x₀ + 1*x₁ + 0*x₂ = 5
Counter 1:  1*x₀ + 0*x₁ + 1*x₂ = 3
```

Or in matrix form:

```
┌         ┐   ┌    ┐     ┌   ┐
│  1  1  0 │   │ x₀ │     │ 5 │
│  1  0  1 │ × │ x₁ │  =  │ 3 │
└         ┘   │ x₂ │     └   ┘
              └    ┘
     A     ×    x    =    b
```

**Objective**: Find non-negative integer values for `x` that minimize `x₀ + x₁ + x₂`.

---

## The Solution: Gaussian Elimination

### What is Gaussian Elimination?

It's a method to solve systems of linear equations by systematically eliminating variables until you can solve for them.

### Step 1: Build the Coefficient Matrix

From our input, we construct:
- **A** (coefficient matrix): Which buttons affect which counters
- **b** (targets): The values each counter must reach

### Step 2: Row Reduction (Eliminate Variables)

We transform the system to express some variables in terms of others.

**Starting System:**
```
x₀ + x₁       = 5
x₀      + x₂  = 3
```

**After elimination (subtract row 1 from row 2):**
```
x₀ + x₁       = 5
    -x₁  + x₂ = -2
```

Now we can express:
- `x₀ = 5 - x₁` (from row 1)
- `x₂ = x₁ - 2` (from row 2)

### Step 3: Identify Free Variables

Variables we can choose freely are called **free variables**. In this case, `x₁` is free.

### Step 4: Enumerate and Minimize

We try different values for free variables and compute the dependent ones:

| x₁ | x₀ = 5-x₁ | x₂ = x₁-2 | Valid? | Total |
|----|-----------|-----------|--------|-------|
| 0  | 5         | -2        | No (negative) | - |
| 1  | 4         | -1        | No (negative) | - |
| 2  | 3         | 0         | Yes | 5 |
| 3  | 2         | 1         | Yes | 6 |
| 4  | 1         | 2         | Yes | 7 |
| 5  | 0         | 3         | Yes | 8 |

**Minimum valid solution**: x₁=2, x₀=3, x₂=0 with **total = 5 presses**.

---

## The Code Structure

```
┌─────────────────────────────────────────────────────────────┐
│                      GaussianSolver                          │
├─────────────────────────────────────────────────────────────┤
│  1. Build coefficient matrix from button definitions        │
│  2. Perform Gaussian elimination                             │
│     - Find pivot rows                                        │
│     - Extract variable expressions                           │
│     - Substitute back into remaining equations               │
│  3. Identify free variables (not determined by equations)   │
│  4. Enumerate all valid combinations of free variables      │
│  5. For each: compute dependent variables, check validity   │
│  6. Return minimum sum across all valid solutions           │
└─────────────────────────────────────────────────────────────┘
```

---

## Key Optimizations

### 1. Upper Bounds

Before enumeration, we compute upper bounds for each variable:

```java
bounds[v] = Math.min(bounds[v], targets[c]);
```

If a button affects counter `c` with target 10, we never need to press it more than 10 times.

### 2. Early Termination in Evaluation

Invalid solutions (negative values, non-integers) are rejected immediately:

```java
if (x < -EPS) return Long.MAX_VALUE;  // Negative - invalid
if (Math.abs(x - rounded) > EPS) return Long.MAX_VALUE;  // Not integer - invalid
```

### 3. Floating Point Tolerance

We use `EPS = 1e-8` to handle floating-point precision issues during elimination.

---

## Visual Summary

```
INPUT                           MATH MODEL                    SOLUTION
┌─────────────┐               ┌─────────────┐              ┌───────────┐
│ Buttons:    │               │             │              │           │
│ (0,1,2,3,4) │──────────────▶│   A × x = b │─────────────▶│ x₀ = 2   │
│ (0,3,4)     │  Build        │             │  Gaussian    │ x₁ = 3   │
│ (0,1,2,4,5) │  Coefficient  │ Subject to: │  Elimination │ x₂ = 1   │
│ (1,2)       │  Matrix       │ xᵢ ≥ 0      │  + Enumerate │ x₃ = 5   │
│             │               │ xᵢ integer  │              │          │
│ Targets:    │               │             │              │ Total=11 │
│ {10,11,11,  │               │ Minimize:   │              │          │
│  5,10,5}    │               │ Σ xᵢ        │              │          │
└─────────────┘               └─────────────┘              └───────────┘
```

---

## Why Not Just Brute Force?

With `n` buttons and max target value `M`, brute force would be O(Mⁿ) - impossibly slow.

Gaussian elimination reduces the search space to only **free variables**. If we have `k` free variables with bound `B`, complexity becomes O(Bᵏ), which is manageable when `k` is small.

---

## Key Takeaways

1. **Many optimization problems are linear systems in disguise**
2. **Gaussian elimination transforms complex systems into manageable forms**
3. **Free variables represent the "degrees of freedom" in a solution**
4. **Integer constraints require enumeration over the solution space**
5. **Upper bounds dramatically reduce the search space**

---

## Complexity Analysis

| Phase | Complexity |
|-------|------------|
| Matrix construction | O(buttons × counters) |
| Gaussian elimination | O(n³) where n = max(buttons, counters) |
| Enumeration | O(Bᵏ) where B = max bound, k = free variables |
| Total | Dominated by enumeration for large k |
