# Day 10 Presentation - Presenter Script

## Overview
- **Total Slides:** 19
- **Part 1:** Slides 1-7 (BFS Solution)
- **Transition:** Slide 8
- **Part 2:** Slides 9-19 (Gaussian Elimination Solution)
- **Estimated Time:** 15-20 minutes

---

## Slide 1: Title Slide
**"Day 10: Factory Machines"**

### Talking Points:
- "Today we're going to walk through Day 10 of Advent of Code"
- "This problem has two parts that use completely different algorithmic approaches"
- "Part 1 uses **Breadth-First Search** - [Colleague's name] will cover this with their Python solution"
- "Part 2 uses **Gaussian Elimination** - I'll cover this with my Java solution"
- "Both parts involve buttons that affect machine states, but the mechanics differ significantly"

### Transition:
"Let's start with Part 1..."

---

## Slide 2: Part 1 Section Header
**"Part 1: Configure the Indicator Lights"**

### Talking Points:
- "In Part 1, we need to configure indicator lights on machines"
- "Each machine has lights that are either ON or OFF"
- "We have buttons that toggle specific lights"
- "Our goal is to reach a target configuration with the minimum button presses"
- "This was solved with BFS in Python"

### Transition:
"Let's look at the problem statement..."

---

## Slide 3: Part 1 Problem Statement
**"The Problem"**

### Talking Points:
- "Each machine has indicator lights - all start OFF"
- "The target shows which lights need to be ON (shown as `#`) and OFF (shown as `.`)"
- "For example, `[.##.]` means lights 2 and 3 should be ON, lights 1 and 4 should be OFF"
- "Buttons **toggle** the lights they affect - if a light is ON, it turns OFF, and vice versa"
- "A button like `(1,3)` toggles the 2nd and 4th lights (0-indexed)"
- "We need to find the **minimum total presses** to reach the target"

### Key Point:
"The word 'toggle' is crucial here - it hints at XOR operations"

### Transition:
"This toggling behavior is actually perfect for a well-known operation..."

---

## Slide 4: Key Insight - Toggle = XOR
**"The Key Insight: Toggle = XOR"**

### Talking Points:
- "We can represent the light state as **bits** - 0 for OFF, 1 for ON"
- "So `[.##.]` becomes `0110` in binary"
- "Each button is also a **bitmask** - `(1,3)` becomes `1010` (bits 1 and 3 set)"
- "Here's the magic: **toggling is exactly the XOR operation!**"
- "When you XOR a bit with 1, it flips. When you XOR with 0, it stays the same"
- "So `state XOR button_mask = new_state`"

### Demo (if time):
"0110 XOR 1010 = 1100 - we flipped bits 1 and 3"

### Key Insight:
"This means our problem is a **graph problem**! Each state is a node, each button press is an edge"

### Transition:
"And for finding the shortest path in an unweighted graph, we use..."

---

## Slide 5: BFS Search Tree
**"BFS Search Tree"**

### Talking Points:
- "This visualization shows the BFS tree for Machine 1"
- "We start at state `0000` - all lights OFF"
- "Our target is `0110`"
- **[Click "Expand Next Level"]** "Level 1 shows all states reachable with 1 button press - that's 6 states, one per button"
- **[Click "Expand Next Level"]** "Level 2 shows states reachable with 2 presses"
- "Notice `0110` appears at level 2 - **TARGET FOUND!**"
- **[Click "Show Solution Path"]** "The solution path is `0000 → 0101 → 0110`"
- "That's pressing button `(0,2)` then `(0,1)` - just 2 presses!"

### Key Point:
"BFS guarantees we find the shortest path because we explore all k-press states before any (k+1)-press states"

### Transition:
"Let's look at the algorithm code..."

---

## Slide 6: BFS Algorithm
**"The BFS Algorithm"**

### Talking Points:
- "Here's the Python implementation - clean and elegant"
- **[Step through with arrow keys or click Next]**
- "We start with state 0 (all lights off) and add it to our queue"
- "The queue stores tuples of (state, number_of_presses)"
- "We pop from the front (FIFO - that's what makes it BFS!)"
- "For each button, we XOR to get the next state"
- "If it's our target - we're done! Return presses + 1"
- "If it's a new state we haven't seen, add it to the queue"
- "The visited set prevents us from revisiting states"

### Complexity:
- "Time: O(2^n × b) where n is number of lights, b is number of buttons"
- "Space: O(2^n) for the visited set"
- "This is manageable because n is small (4-6 lights per machine)"

### Transition:
"And that gives us our Part 1 answer..."

---

## Slide 7: Part 1 Answer
**"Part 1 Answer"**

### Talking Points:
- **[Click "Show Calculation"]**
- "We apply BFS to every machine in the input"
- "Machine 1: 2 presses, Machine 2: 3 presses, Machine 3: 2 presses..."
- "Sum all the results together"
- "**Total: 547 presses**"

### Key Point:
"BFS guarantees this is the minimum - we always find the shortest path first"

### Transition:
"Now let's move on to Part 2, which requires a completely different approach..."

---

## Slide 8: Transition to Part 2
**"Part 2: Configure the Joltage Counters"**

### Talking Points:
- "Part 2 changes the game completely"
- "Instead of toggling lights, buttons now **increment counters**"
- "Counters can be pressed **many times**, not just toggled"
- "This means the state space becomes **infinite!**"
- "We can't use BFS anymore - it would never terminate"
- "We need a smarter approach: **Gaussian Elimination**"

### Key Contrast:
"Part 1: finite states (2^n), toggle operation → BFS works"
"Part 2: infinite states, increment operation → need linear algebra"

### Transition:
"Let's understand the Part 2 problem..."

---

## Slide 9: Part 2 Problem Statement
**"Part 2: The Problem"**

### Talking Points:
- "Now we have **numeric counters** instead of binary lights"
- "All counters start at **zero**"
- "Target values like `{3,5,4,7}` mean we need counter 0 = 3, counter 1 = 5, etc."
- "Each button press **increases** the listed counters by 1 (not toggle!)"
- "Button `(1,3)` means each press adds 1 to counters 1 and 3"
- "We need the **minimum total presses** across all buttons"

### Example:
"If we press button `(1,3)` three times, counters 1 and 3 each increase by 3"

### Transition:
"There's a pattern here that hints at the solution..."

---

## Slide 10: Spotting the Pattern
**"Spotting the Pattern"**

### Talking Points:
- "Let's look at the clues in the problem..."
- "We have **buttons** - these will become our **variables** (how many times to press each)"
- "Each button can be pressed **multiple times** - this is **linear** (pressing 3x = 3× the effect)"
- "Each press **increases specific counters** by exactly 1 - these are **fixed coefficients**"
- "We want to **minimize total presses** - this is an **objective function**"

### Recognition:
"This is a **system of linear equations** with an optimization objective!"
"Classic linear algebra problem - we can use **Gaussian Elimination**"

### Transition:
"Let's translate this into math..."

---

## Slide 11: Linear Algebra Formulation
**"This is Linear Algebra!"**

### Talking Points:
- "Let x₀, x₁, x₂, ... be how many times we press each button"
- "For each counter, we can write an equation"
- "Look at Machine 1 with target `{3,5,4,7}` and 6 buttons"
- "Counter 0 is affected by buttons `(0,2)` and `(0,1)` - so x₄ + x₅ = 3"
- "Counter 1 is affected by buttons `(1,3)` and `(0,1)` - so x₁ + x₅ = 5"
- "And so on for each counter..."

### Matrix Form:
"We can write this as **Ax = b** where:"
- "A is the coefficient matrix (which buttons affect which counters)"
- "x is our solution vector (how many times to press each button)"
- "b is our target vector"

### Objective:
"Minimize **sum of all xᵢ** subject to **xᵢ ≥ 0** and **xᵢ is integer**"

### Transition:
"Let's see the actual coefficient matrix..."

---

## Slide 12: The Coefficient Matrix
**"The Coefficient Matrix"**

### Talking Points:
- "Here's the coefficient matrix for Machine 1"
- "Each row is a counter (constraint), each column is a button (variable)"
- "A 1 means that button affects that counter"
- "Notice: 4 equations but 6 variables"
- "This means we have **more variables than constraints**"
- "Some variables will be 'free' - we can choose their values"

### Reading the Matrix:
- "Row 0: buttons 4 and 5 affect counter 0 (x₄ + x₅ = 3)"
- "Row 1: buttons 1 and 5 affect counter 1 (x₁ + x₅ = 5)"
- "And so on..."

### Transition:
"Gaussian Elimination will help us solve this..."

---

## Slide 13: Gaussian Elimination Intro
**"Gaussian Elimination"**

### Talking Points:
- "Gaussian Elimination transforms a system into a simpler form"
- "We eliminate variables one by one"
- "Some variables become **determined** - expressed in terms of others"
- "Some variables become **free** - we can choose their values"
- "The key insight: we only need to search over the **free variables**!"

### Why This Matters:
"Instead of trying all combinations of 6 variables (potentially millions), we only enumerate over 2 free variables (maybe 20 combinations)"

### Transition:
"Let's walk through the elimination step by step..."

---

## Slide 14: Step by Step Elimination
**"Step by Step"**

### Talking Points:
- **[Use arrow keys to step through]**
- "**Step 1:** Start with our 4 equations, 6 unknowns"
- "**Step 2:** Solve first equation for x₄: x₄ = 3 - x₅"
- "**Step 3:** Solve second equation for x₁: x₁ = 5 - x₅"
- "**Step 4:** Continue until we've processed all equations"
- "**Step 5:** Variables with no pivot become **FREE**: x₃ and x₅"
- "**Step 6:** Now we only enumerate over x₃ ∈ {0..4} and x₅ ∈ {0..3}"

### Key Result:
"6 variables - 4 equations = 2 free variables"
"We reduced the search space from O(M⁶) to O(M²)!"

### Transition:
"Let's see this in code..."

---

## Slide 15: Algorithm in Action
**"The Algorithm in Action"**

### Talking Points:
- **[Step through with arrow keys or Auto Play]**
- "**Phase 1:** Build the coefficient matrix from button definitions"
- "**Phase 2:** Gaussian Elimination loop - for each variable:"
  - "Find a row with non-zero coefficient (pivot)"
  - "If found: extract expression, substitute into other equations"
  - "If not found: mark variable as FREE"
- "**Phase 3:** Collect free variables"
- "**Phase 4:** Enumerate all combinations of free variable values"
- "**Phase 5:** For each combo, compute dependent variables and validate"
- "**Phase 6:** Track minimum valid sum"

### Watch the Visualization:
"Notice how the matrix gets processed row by row, and variables get determined or marked free"

### Transition:
"We can also visualize this geometrically..."

---

## Slide 16: 3D Visualization
**"Visualizing the Solution Space"**

### Talking Points:
- "Each constraint is a **plane** in n-dimensional space"
- **[Click "Animate"]**
- "Watch as each plane appears..."
- "The solution is where **all planes intersect**"
- "The golden point is our optimal solution!"

### Geometric Intuition:
- "In 3D: 3 planes can intersect at a point (unique solution)"
- "Or a line (infinite solutions - need to optimize)"
- "Or not at all (no solution)"
- "Our case: the intersection gives us constraints, but we optimize within the valid region"

### Transition:
"Now let's see the final answer..."

---

## Slide 17: Building to the Answer
**"Building to the Answer"**

### Talking Points:
- **[Click "Show Calculation"]**
- "Just like Part 1, we apply our algorithm to every machine"
- "Machine 1: 10 presses (using our Gaussian Elimination solver)"
- "Machine 2: 12 presses"
- "Machine 3: 11 presses"
- "Continue for all machines in the input..."

### Transition:
"And the final total is..."

---

## Slide 18: Part 2 Answer
**"Part 2 Answer"**

### Talking Points:
- "**Total: 21,111 presses**"
- "This is achievable because Gaussian Elimination found the optimal solution for each machine"
- "The algorithm handles:"
  - "Underdetermined systems (more variables than equations)"
  - "Integer constraints"
  - "Non-negativity constraints"
  - "Minimization objective"

### Contrast with Brute Force:
"Brute force would have been O(M^n) per machine - impossibly slow"
"Gaussian Elimination reduced this to O(M^k) where k = number of free variables (typically 1-3)"

### Transition:
"Let's summarize what we learned..."

---

## Slide 19: Key Takeaways
**"Key Takeaways"**

### Talking Points:
1. "**Recognize problem patterns** - Part 1 was a graph problem (BFS), Part 2 was linear algebra"
2. "**Toggle operations = XOR** - this transforms problems into bit manipulation"
3. "**Increment operations = linear equations** - this leads to Gaussian Elimination"
4. "**Free variables reduce search space** - from O(M^n) to O(M^k)"
5. "**Same problem structure, different mechanics** - toggle vs increment completely changes the solution approach"

### Final Thought:
"The key skill is recognizing which mathematical framework applies to your problem. Once you identify it, you can apply well-known algorithms."

### Questions?
"Any questions about the BFS approach or the Gaussian Elimination solution?"

---

## Appendix: Quick Reference

### Part 1 Summary
- **Problem:** Toggle lights to match target pattern
- **Approach:** BFS on state graph
- **Key Insight:** Toggle = XOR, state space is finite (2^n)
- **Complexity:** O(2^n × b) per machine
- **Answer:** 547

### Part 2 Summary
- **Problem:** Increment counters to match target values
- **Approach:** Gaussian Elimination + enumeration of free variables
- **Key Insight:** Linear system Ax = b, minimize sum(x)
- **Complexity:** O(n³) for elimination + O(M^k) for enumeration
- **Answer:** 21,111

### Common Questions

**Q: Why can't we use BFS for Part 2?**
A: State space is infinite - counters can be any non-negative integer, not just 0/1.

**Q: What if there's no solution?**
A: The algorithm returns infinity/error. This happens if constraints are contradictory.

**Q: What are "free variables"?**
A: Variables that aren't uniquely determined by the equations. We can choose their values within bounds.

**Q: Why is Gaussian Elimination efficient?**
A: It reduces the search space from all variables to only free variables. With 6 vars and 4 equations, we only search over 2 free variables.
