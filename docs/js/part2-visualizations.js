/**
 * Part 2 Visualizations for Day 10 Presentation
 * - Gaussian Elimination Steps
 * - 3D Visualization
 * - Code Step-through
 * - Part 2 Answer Animation
 */

// ============ ELIMINATION STEPS ============
const elimSteps = [
    {
        title: "System of Equations",
        equations: ["x₄ + x₅ = 3", "x₁ + x₅ = 5", "x₂ + x₃ + x₄ = 4", "x₀ + x₁ + x₃ = 7"],
        explanation: "4 equations, 6 unknowns. More variables than equations means free variables will exist."
    },
    {
        title: "Process First Row (Pivot on x₄)",
        equations: ["<span style='color:#00ff88'>x₄ = 3 - x₅</span>", "x₁ + x₅ = 5", "x₂ + x₃ + (3 - x₅) = 4", "x₀ + x₁ + x₃ = 7"],
        explanation: "Solve equation 1 for x₄, then substitute into equation 3. This is the 'elimination' step."
    },
    {
        title: "Simplify & Process More Rows",
        equations: ["<span style='color:#00ff88'>x₄ = 3 - x₅</span>", "<span style='color:#00ff88'>x₁ = 5 - x₅</span>", "x₂ + x₃ - x₅ = 1", "x₀ + (5 - x₅) + x₃ = 7"],
        explanation: "Solve equation 2 for x₁. Substitute into remaining equations containing x₁."
    },
    {
        title: "Continue Elimination",
        equations: ["<span style='color:#00ff88'>x₄ = 3 - x₅</span>", "<span style='color:#00ff88'>x₁ = 5 - x₅</span>", "<span style='color:#00ff88'>x₂ = 1 - x₃ + x₅</span>", "x₀ + x₃ = 2 + x₅"],
        explanation: "Solve equation 3 for x₂. Variables x₀, x₃, x₅ remain. We have 1 equation left but 3 variables."
    },
    {
        title: "Identify Free Variables",
        equations: ["Free: <span style='color:#ffd700'>x₃</span>, <span style='color:#ffd700'>x₅</span>", "Determined: x₀ = 2 + x₅ - x₃", "Determined: x₁ = 5 - x₅", "Determined: x₂ = 1 - x₃ + x₅, x₄ = 3 - x₅"],
        explanation: "6 vars - 4 equations = 2 free variables (x₃ and x₅). We only enumerate over these 2!"
    },
    {
        title: "Enumerate & Find Minimum",
        equations: ["Try x₃ ∈ {0,1,...,4}", "Try x₅ ∈ {0,1,...,3}", "Check: all xᵢ ≥ 0?", "Find minimum Σxᵢ"],
        explanation: "Solution: x₀=1, x₁=3, x₂=0, x₃=3, x₄=1, x₅=2 → Total = <span style='color:#ffd700'>10 presses</span>"
    }
];

let elimStep = 0;

function initElimination() {
    elimStep = 0;
    updateElimDisplay();
}

function updateElimDisplay() {
    const container = document.getElementById('elimContainer');
    const indicator = document.getElementById('stepIndicator');
    if (!container) return;

    container.innerHTML = elimSteps.map((step, idx) => `
        <div class="elim-step ${idx === elimStep ? 'active' : ''} ${idx < elimStep ? 'complete' : ''}" id="elimStep${idx}">
            <div class="step-title">Step ${idx + 1}: ${step.title}</div>
            ${step.equations.map(eq => `<div class="equation-box">${eq}</div>`).join(' ')}
            <p class="step-explanation">${step.explanation}</p>
        </div>
    `).join('');

    if (indicator) {
        indicator.innerHTML = elimSteps.map((_, idx) => `
            <div class="step-dot ${idx === elimStep ? 'active' : ''} ${idx < elimStep ? 'complete' : ''}"></div>
        `).join('');
    }

    // Auto-scroll to active step
    const activeStep = document.getElementById(`elimStep${elimStep}`);
    if (activeStep) {
        activeStep.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }
}

function nextElimStep() {
    if (elimStep < elimSteps.length - 1) {
        elimStep++;
        updateElimDisplay();
    }
}

function prevElimStep() {
    if (elimStep > 0) {
        elimStep--;
        updateElimDisplay();
    }
}

// ============ 3D VISUALIZATION ============
let vizAnimationId = null;
let vizRotation = 0;
let vizPhase = 0;

function initViz3D() {
    resetViz();
    drawViz3D();
}

function drawViz3D() {
    const canvas = document.getElementById('canvas3d');
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    const w = canvas.width;
    const h = canvas.height;

    ctx.fillStyle = '#0a0a0f';
    ctx.fillRect(0, 0, w, h);

    const cx = w / 2;
    const cy = h / 2;
    const scale = 12;

    const cosR = Math.cos(vizRotation);
    const sinR = Math.sin(vizRotation);
    const tilt = 0.3;

    function project(x, y, z) {
        x -= 5; y -= 5; z -= 5;
        const rx = x * cosR - z * sinR;
        const rz = x * sinR + z * cosR;
        const ry = y * Math.cos(tilt) - rz * Math.sin(tilt);
        const rz2 = y * Math.sin(tilt) + rz * Math.cos(tilt);
        const perspective = 200 / (200 + rz2);
        return { x: cx + rx * scale * perspective, y: cy - ry * scale * perspective, z: rz2 };
    }

    // Grid
    ctx.strokeStyle = 'rgba(255,255,255,0.1)';
    ctx.lineWidth = 1;
    for (let i = 0; i <= 10; i += 2) {
        let p1 = project(i, 0, 0), p2 = project(i, 0, 10);
        ctx.beginPath(); ctx.moveTo(p1.x, p1.y); ctx.lineTo(p2.x, p2.y); ctx.stroke();
        p1 = project(0, 0, i); p2 = project(10, 0, i);
        ctx.beginPath(); ctx.moveTo(p1.x, p1.y); ctx.lineTo(p2.x, p2.y); ctx.stroke();
    }

    // Axes
    ctx.lineWidth = 2;
    const origin = project(0, 0, 0);

    ctx.strokeStyle = '#00d9ff';
    let end = project(12, 0, 0);
    ctx.beginPath(); ctx.moveTo(origin.x, origin.y); ctx.lineTo(end.x, end.y); ctx.stroke();
    ctx.fillStyle = '#00d9ff'; ctx.font = 'bold 14px sans-serif';
    ctx.fillText('x₀', end.x + 8, end.y + 4);

    ctx.strokeStyle = '#00ff88';
    end = project(0, 12, 0);
    ctx.beginPath(); ctx.moveTo(origin.x, origin.y); ctx.lineTo(end.x, end.y); ctx.stroke();
    ctx.fillStyle = '#00ff88';
    ctx.fillText('x₁', end.x - 5, end.y - 10);

    ctx.strokeStyle = '#ffd700';
    end = project(0, 0, 12);
    ctx.beginPath(); ctx.moveTo(origin.x, origin.y); ctx.lineTo(end.x, end.y); ctx.stroke();
    ctx.fillStyle = '#ffd700';
    ctx.fillText('x₂', end.x + 8, end.y + 4);

    // Constraint planes
    if (vizPhase >= 1) {
        ctx.fillStyle = 'rgba(0, 217, 255, 0.15)';
        ctx.strokeStyle = 'rgba(0, 217, 255, 0.6)';
        ctx.lineWidth = 2;
        const plane1 = [project(3, 0, 0), project(3, 10, 0), project(3, 10, 10), project(3, 0, 10)];
        ctx.beginPath(); ctx.moveTo(plane1[0].x, plane1[0].y);
        plane1.forEach(p => ctx.lineTo(p.x, p.y)); ctx.closePath(); ctx.fill(); ctx.stroke();
        document.getElementById('eq0').classList.add('solved');
    }

    if (vizPhase >= 2) {
        ctx.fillStyle = 'rgba(0, 255, 136, 0.15)';
        ctx.strokeStyle = 'rgba(0, 255, 136, 0.6)';
        const plane2 = [project(0, 5, 0), project(10, 5, 0), project(10, 5, 10), project(0, 5, 10)];
        ctx.beginPath(); ctx.moveTo(plane2[0].x, plane2[0].y);
        plane2.forEach(p => ctx.lineTo(p.x, p.y)); ctx.closePath(); ctx.fill(); ctx.stroke();
        document.getElementById('eq1').classList.add('solved');
    }

    if (vizPhase >= 3) {
        ctx.fillStyle = 'rgba(255, 215, 0, 0.15)';
        ctx.strokeStyle = 'rgba(255, 215, 0, 0.6)';
        const plane3 = [project(0, 0, 4), project(10, 0, 4), project(10, 10, 4), project(0, 10, 4)];
        ctx.beginPath(); ctx.moveTo(plane3[0].x, plane3[0].y);
        plane3.forEach(p => ctx.lineTo(p.x, p.y)); ctx.closePath(); ctx.fill(); ctx.stroke();
        document.getElementById('eq2').classList.add('solved');
        document.getElementById('eq3').classList.add('solved');
    }

    if (vizPhase >= 4) {
        const solPoint = project(3, 5, 4);
        const gradient = ctx.createRadialGradient(solPoint.x, solPoint.y, 0, solPoint.x, solPoint.y, 35);
        gradient.addColorStop(0, 'rgba(255, 215, 0, 0.9)');
        gradient.addColorStop(0.5, 'rgba(255, 215, 0, 0.3)');
        gradient.addColorStop(1, 'rgba(255, 215, 0, 0)');
        ctx.fillStyle = gradient;
        ctx.beginPath(); ctx.arc(solPoint.x, solPoint.y, 35, 0, Math.PI * 2); ctx.fill();

        const pulseSize = 8 + Math.sin(vizRotation * 10) * 2;
        ctx.fillStyle = '#ffd700';
        ctx.beginPath(); ctx.arc(solPoint.x, solPoint.y, pulseSize, 0, Math.PI * 2); ctx.fill();
        ctx.strokeStyle = '#fff'; ctx.lineWidth = 2; ctx.stroke();

        ctx.fillStyle = '#fff'; ctx.font = 'bold 13px sans-serif';
        ctx.fillText('SOLUTION', solPoint.x + 20, solPoint.y - 5);

        document.getElementById('vizSolution').style.display = 'block';
    }

    vizRotation += 0.008;
    vizAnimationId = requestAnimationFrame(drawViz3D);
}

function startVizAnimation() {
    if (vizPhase < 4) {
        vizPhase++;
        if (vizPhase < 4) setTimeout(startVizAnimation, 600);
    }
}

function resetViz() {
    vizPhase = 0;
    const vizSol = document.getElementById('vizSolution');
    if (vizSol) vizSol.style.display = 'none';
    ['eq0', 'eq1', 'eq2', 'eq3'].forEach(id => {
        const el = document.getElementById(id);
        if (el) el.classList.remove('solved');
    });
}

// ============ ANSWER ANIMATION ============
function resetAnswerAnimation() {
    ['m1', 'm2', 'm3', 'mN', 'mTotal'].forEach(id => {
        const el = document.getElementById(id);
        if (el) el.classList.remove('visible');
    });
}

function animateAnswer() {
    const rows = ['m1', 'm2', 'm3', 'mN', 'mTotal'];
    const values = ['127', '88', '...', '156', '21,111'];

    rows.forEach((id, i) => {
        setTimeout(() => {
            const el = document.getElementById(id);
            if (el) {
                el.classList.add('visible');
                const valSpan = el.querySelector('.value');
                if (valSpan && values[i]) valSpan.textContent = values[i];
            }
        }, i * 400);
    });
}

// ============ CODE VISUALIZATION ============
let codeStep = 0;
let autoPlayInterval = null;
const codeSteps = [
    {
        phase: "Phase 1: Building coefficient matrix A",
        highlight: [0],
        vars: ['?', '?', '?', '?', '?', '?'],
        varStates: ['unknown', 'unknown', 'unknown', 'unknown', 'unknown', 'unknown'],
        matrixHighlight: ['mrow0', 'mrow1', 'mrow2', 'mrow3']
    },
    {
        phase: "Phase 2: Start Gaussian Elimination loop",
        highlight: [1],
        vars: ['?', '?', '?', '?', '?', '?'],
        varStates: ['unknown', 'unknown', 'unknown', 'unknown', 'unknown', 'unknown'],
        matrixHighlight: []
    },
    {
        phase: "v=0: Searching for pivot row (non-zero A[row][0])",
        highlight: [2],
        vars: ['?', '?', '?', '?', '?', '?'],
        varStates: ['unknown', 'unknown', 'unknown', 'unknown', 'unknown', 'unknown'],
        matrixHighlight: ['mrow0', 'mrow1', 'mrow2', 'mrow3']
    },
    {
        phase: "v=0: Found pivot at row 4 (A[3][0]=1)",
        highlight: [3],
        vars: ['?', '?', '?', '?', '?', '?'],
        varStates: ['unknown', 'unknown', 'unknown', 'unknown', 'unknown', 'unknown'],
        matrixHighlight: ['mrow3']
    },
    {
        phase: "v=0: Extract expression for x₀ from pivot row",
        highlight: [4],
        vars: ['...', '?', '?', '?', '?', '?'],
        varStates: ['unknown', 'unknown', 'unknown', 'unknown', 'unknown', 'unknown'],
        matrixHighlight: ['mrow3']
    },
    {
        phase: "v=0: x₀ = 7 - x₁ - x₃ (determined!)",
        highlight: [5],
        vars: ['7-x₁-x₃', '?', '?', '?', '?', '?'],
        varStates: ['determined', 'unknown', 'unknown', 'unknown', 'unknown', 'unknown'],
        matrixHighlight: ['mrow3']
    },
    {
        phase: "v=0: Substitute x₀ into all other equations",
        highlight: [6],
        vars: ['7-x₁-x₃', '?', '?', '?', '?', '?'],
        varStates: ['determined', 'unknown', 'unknown', 'unknown', 'unknown', 'unknown'],
        matrixHighlight: ['mrow0', 'mrow1', 'mrow2']
    },
    {
        phase: "v=1: Find pivot → row 2. Extract x₁ = 5 - x₅",
        highlight: [2, 3, 4, 5],
        vars: ['7-x₁-x₃', '5-x₅', '?', '?', '?', '?'],
        varStates: ['determined', 'determined', 'unknown', 'unknown', 'unknown', 'unknown'],
        matrixHighlight: ['mrow1']
    },
    {
        phase: "v=2: Find pivot → row 3. Extract x₂ = 4-x₃-x₄",
        highlight: [2, 3, 4, 5],
        vars: ['7-x₁-x₃', '5-x₅', '4-x₃-x₄', '?', '?', '?'],
        varStates: ['determined', 'determined', 'determined', 'unknown', 'unknown', 'unknown'],
        matrixHighlight: ['mrow2']
    },
    {
        phase: "v=3: No pivot found! → x₃ is FREE",
        highlight: [3],
        vars: ['7-x₁-x₃', '5-x₅', '4-x₃-x₄', 'FREE', '?', '?'],
        varStates: ['determined', 'determined', 'determined', 'free', 'unknown', 'unknown'],
        matrixHighlight: []
    },
    {
        phase: "v=4: Find pivot → row 1. Extract x₄ = 3 - x₅",
        highlight: [2, 3, 4, 5],
        vars: ['7-x₁-x₃', '5-x₅', '4-x₃-x₄', 'FREE', '3-x₅', '?'],
        varStates: ['determined', 'determined', 'determined', 'free', 'determined', 'unknown'],
        matrixHighlight: ['mrow0']
    },
    {
        phase: "v=5: No pivot found! → x₅ is FREE",
        highlight: [3],
        vars: ['7-x₁-x₃', '5-x₅', '4-x₃-x₄', 'FREE', '3-x₅', 'FREE'],
        varStates: ['determined', 'determined', 'determined', 'free', 'determined', 'free'],
        matrixHighlight: []
    },
    {
        phase: "Phase 3: Collect free variables → {x₃, x₅}",
        highlight: [7],
        vars: ['7-x₁-x₃', '5-x₅', '4-x₃-x₄', 'FREE', '3-x₅', 'FREE'],
        varStates: ['determined', 'determined', 'determined', 'free', 'determined', 'free'],
        matrixHighlight: []
    },
    {
        phase: "Enumerate: x₃ ∈ {0..4}, x₅ ∈ {0..3}",
        highlight: [8],
        vars: ['...', '...', '...', '0..4', '...', '0..3'],
        varStates: ['determined', 'determined', 'determined', 'free', 'determined', 'free'],
        matrixHighlight: []
    },
    {
        phase: "For each combo: compute dependent variables",
        highlight: [9],
        vars: ['calc', 'calc', 'calc', 'try', 'calc', 'try'],
        varStates: ['determined', 'determined', 'determined', 'free', 'determined', 'free'],
        matrixHighlight: []
    },
    {
        phase: "Validate: x[i] ≥ 0 and integer?",
        highlight: [10],
        vars: ['≥0?', '≥0?', '≥0?', '✓', '≥0?', '✓'],
        varStates: ['determined', 'determined', 'determined', 'free', 'determined', 'free'],
        matrixHighlight: []
    },
    {
        phase: "Track minimum: best = min(best, sum(x))",
        highlight: [11],
        vars: ['1', '3', '0', '3', '1', '2'],
        varStates: ['determined', 'determined', 'determined', 'free', 'determined', 'free'],
        matrixHighlight: []
    },
    {
        phase: "Return best = 10 presses!",
        highlight: [12],
        vars: ['1', '3', '0', '3', '1', '2'],
        varStates: ['determined', 'determined', 'determined', 'free', 'determined', 'free'],
        matrixHighlight: []
    }
];

function initCodeViz() {
    codeStep = 0;
    if (autoPlayInterval) {
        clearInterval(autoPlayInterval);
        autoPlayInterval = null;
    }
    updateCodeViz();
}

function updateCodeViz() {
    const step = codeSteps[codeStep];
    if (!step) return;

    // Update code highlighting
    let firstHighlightedLine = null;
    document.querySelectorAll('.code-line').forEach(line => {
        line.classList.remove('highlight', 'complete');
        const lineStep = parseInt(line.dataset.step);
        if (step.highlight.includes(lineStep)) {
            line.classList.add('highlight');
            if (!firstHighlightedLine) firstHighlightedLine = line;
        } else if (lineStep < Math.min(...step.highlight)) {
            line.classList.add('complete');
        }
    });

    // Auto-scroll code panel to show highlighted line
    if (firstHighlightedLine) {
        const codeContent = document.getElementById('codeContent');
        if (codeContent) {
            const lineTop = firstHighlightedLine.offsetTop;
            const lineHeight = firstHighlightedLine.offsetHeight;
            const containerHeight = codeContent.clientHeight;
            const scrollTop = codeContent.scrollTop;

            // Scroll if line is not visible
            if (lineTop < scrollTop || lineTop + lineHeight > scrollTop + containerHeight) {
                codeContent.scrollTop = lineTop - containerHeight / 3;
            }
        }
    }

    // Update variable status
    const varDisplay = document.getElementById('varStatusDisplay');
    if (varDisplay) {
        varDisplay.innerHTML = step.vars.map((val, i) =>
            `<span class="viz-var-item ${step.varStates[i]}">x${subscript(i)} = ${val}</span>`
        ).join('');
    }

    // Update matrix highlighting
    ['mrow0', 'mrow1', 'mrow2', 'mrow3'].forEach(id => {
        const row = document.getElementById(id);
        if (row) {
            row.classList.remove('processing', 'eliminated');
            if (step.matrixHighlight.includes(id)) {
                row.classList.add('processing');
            }
        }
    });

    // Update phase
    const phaseEl = document.getElementById('algorithmPhase');
    if (phaseEl) {
        phaseEl.textContent = `Phase: ${step.phase}`;
    }
}

function subscript(n) {
    const subs = ['₀', '₁', '₂', '₃', '₄', '₅', '₆', '₇', '₈', '₉'];
    return String(n).split('').map(d => subs[parseInt(d)]).join('');
}

function nextCodeStep() {
    if (codeStep < codeSteps.length - 1) {
        codeStep++;
        updateCodeViz();
    }
}

function prevCodeStep() {
    if (codeStep > 0) {
        codeStep--;
        updateCodeViz();
    }
}

function autoPlayCode() {
    if (autoPlayInterval) {
        clearInterval(autoPlayInterval);
        autoPlayInterval = null;
        return;
    }
    autoPlayInterval = setInterval(() => {
        if (codeStep < codeSteps.length - 1) {
            codeStep++;
            updateCodeViz();
        } else {
            clearInterval(autoPlayInterval);
            autoPlayInterval = null;
        }
    }, 1200);
}

function resetCodeViz() {
    if (autoPlayInterval) {
        clearInterval(autoPlayInterval);
        autoPlayInterval = null;
    }
    codeStep = 0;
    updateCodeViz();
}
