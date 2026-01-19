/**
 * Part 1 Visualizations for Day 10 Presentation
 * - BFS Tree Visualization
 * - BFS Code Step-through
 * - Part 1 Answer Animation
 */

// ============ BFS CODE VISUALIZATION ============
let bfsCodeStep = 0;
let bfsAutoPlayInterval = null;

// BFS walk-through using Machine 1: [.##.] target=0110=6
// Buttons: (3)=8, (1,3)=10, (2)=4, (2,3)=12, (0,2)=5, (0,1)=3
const bfsCodeSteps = [
    {
        phase: "Define function",
        highlight: [0],
        current: null,
        presses: null,
        queue: [],
        visited: [],
        action: "Receive target=0110 and buttons list"
    },
    {
        phase: "Initialize start state",
        highlight: [1],
        current: null,
        presses: null,
        queue: [],
        visited: [],
        action: "start = 0000 (all lights off)"
    },
    {
        phase: "Initialize visited set",
        highlight: [2],
        current: null,
        presses: null,
        queue: [],
        visited: ['0000'],
        action: "Mark 0000 as visited"
    },
    {
        phase: "Initialize queue",
        highlight: [3],
        current: null,
        presses: null,
        queue: [{ state: '0000', presses: 0 }],
        visited: ['0000'],
        action: "Add (0000, 0) to queue"
    },
    {
        phase: "Enter while loop",
        highlight: [4],
        current: null,
        presses: null,
        queue: [{ state: '0000', presses: 0 }],
        visited: ['0000'],
        action: "Queue not empty → continue"
    },
    {
        phase: "Pop from queue",
        highlight: [5],
        current: '0000',
        presses: 0,
        queue: [],
        visited: ['0000'],
        action: "Pop (0000, 0) from front"
    },
    {
        phase: "Iterate buttons",
        highlight: [6],
        current: '0000',
        presses: 0,
        queue: [],
        visited: ['0000'],
        action: "Try each button..."
    },
    {
        phase: "XOR with button (0,2)=5",
        highlight: [7],
        current: '0000',
        presses: 0,
        queue: [],
        visited: ['0000'],
        action: "0000 ^ 0101 = 0101",
        trying: '0101'
    },
    {
        phase: "Check if target",
        highlight: [8],
        current: '0000',
        presses: 0,
        queue: [],
        visited: ['0000'],
        action: "0101 ≠ 0110 (target)",
        trying: '0101'
    },
    {
        phase: "Add to queue",
        highlight: [10, 11, 12],
        current: '0000',
        presses: 0,
        queue: [{ state: '0101', presses: 1 }],
        visited: ['0000', '0101'],
        action: "0101 is new → add to queue",
        newVisited: '0101'
    },
    {
        phase: "XOR with button (0,1)=3",
        highlight: [7],
        current: '0000',
        presses: 0,
        queue: [{ state: '0101', presses: 1 }],
        visited: ['0000', '0101'],
        action: "0000 ^ 0011 = 0011",
        trying: '0011'
    },
    {
        phase: "Add 0011 to queue",
        highlight: [10, 11, 12],
        current: '0000',
        presses: 0,
        queue: [{ state: '0101', presses: 1 }, { state: '0011', presses: 1 }],
        visited: ['0000', '0101', '0011'],
        action: "0011 is new → add to queue",
        newVisited: '0011'
    },
    {
        phase: "... (process more buttons)",
        highlight: [6],
        current: '0000',
        presses: 0,
        queue: [{ state: '0101', presses: 1 }, { state: '0011', presses: 1 }, { state: '1000', presses: 1 }, { state: '1010', presses: 1 }, { state: '0100', presses: 1 }, { state: '1100', presses: 1 }],
        visited: ['0000', '0101', '0011', '1000', '1010', '0100', '1100'],
        action: "All 6 new states added for level 1"
    },
    {
        phase: "Pop next state",
        highlight: [5],
        current: '0101',
        presses: 1,
        queue: [{ state: '0011', presses: 1 }, { state: '1000', presses: 1 }, { state: '1010', presses: 1 }, { state: '0100', presses: 1 }, { state: '1100', presses: 1 }],
        visited: ['0000', '0101', '0011', '1000', '1010', '0100', '1100'],
        action: "Pop (0101, 1) from front"
    },
    {
        phase: "XOR with button (0,1)=3",
        highlight: [7],
        current: '0101',
        presses: 1,
        queue: [{ state: '0011', presses: 1 }, { state: '1000', presses: 1 }, { state: '1010', presses: 1 }, { state: '0100', presses: 1 }, { state: '1100', presses: 1 }],
        visited: ['0000', '0101', '0011', '1000', '1010', '0100', '1100'],
        action: "0101 ^ 0011 = 0110",
        trying: '0110'
    },
    {
        phase: "Check if target",
        highlight: [8],
        current: '0101',
        presses: 1,
        queue: [{ state: '0011', presses: 1 }, { state: '1000', presses: 1 }, { state: '1010', presses: 1 }, { state: '0100', presses: 1 }, { state: '1100', presses: 1 }],
        visited: ['0000', '0101', '0011', '1000', '1010', '0100', '1100'],
        action: "0110 == 0110 ✓ TARGET FOUND!",
        trying: '0110',
        found: true
    },
    {
        phase: "Return result!",
        highlight: [9],
        current: '0110',
        presses: 2,
        queue: [],
        visited: ['0000', '0101', '0011', '1000', '1010', '0100', '1100', '0110'],
        action: "Return presses + 1 = 2",
        found: true,
        done: true
    }
];

function initBFSCodeViz() {
    bfsCodeStep = 0;
    updateBFSViz();
}

function updateBFSViz() {
    const step = bfsCodeSteps[bfsCodeStep];
    if (!step) return;

    // Update code highlighting
    let firstHighlightedLine = null;
    document.querySelectorAll('.code-line.bfs-code').forEach(line => {
        line.classList.remove('highlight', 'complete');
        const lineStep = parseInt(line.dataset.bfsStep);
        if (step.highlight.includes(lineStep)) {
            line.classList.add('highlight');
            if (!firstHighlightedLine) firstHighlightedLine = line;
        } else if (lineStep < Math.min(...step.highlight)) {
            line.classList.add('complete');
        }
    });

    // Auto-scroll code panel
    if (firstHighlightedLine) {
        const codeContent = document.getElementById('bfsCodeContent');
        if (codeContent) {
            const lineTop = firstHighlightedLine.offsetTop;
            const lineHeight = firstHighlightedLine.offsetHeight;
            const containerHeight = codeContent.clientHeight;
            const scrollTop = codeContent.scrollTop;
            if (lineTop < scrollTop || lineTop + lineHeight > scrollTop + containerHeight) {
                codeContent.scrollTop = lineTop - containerHeight / 3;
            }
        }
    }

    // Update current state display
    const currentDisplay = document.getElementById('bfsCurrentDisplay');
    if (currentDisplay) {
        let stateClass = 'bfs-state-box';
        if (step.found) stateClass += ' found';
        else if (step.current) stateClass += ' current';

        const stateVal = step.trying || step.current || '-';
        currentDisplay.innerHTML = `
            <span class="${stateClass}">${stateVal}</span>
            <span class="bfs-press-count">(${step.presses !== null ? step.presses : '-'} presses)</span>
        `;
    }

    // Update queue display
    const queueDisplay = document.getElementById('bfsQueueDisplay');
    if (queueDisplay) {
        if (step.queue.length === 0) {
            queueDisplay.innerHTML = '<span class="bfs-queue-item empty">empty</span>';
        } else {
            queueDisplay.innerHTML = step.queue.map((item, i) =>
                `<span class="bfs-queue-item${i === 0 ? ' processing' : ''}">${item.state}:${item.presses}</span>`
            ).join('');
        }
    }

    // Update visited display
    const visitedDisplay = document.getElementById('bfsVisitedDisplay');
    if (visitedDisplay) {
        if (step.visited.length === 0) {
            visitedDisplay.innerHTML = '<span class="bfs-visited-item">none</span>';
        } else {
            visitedDisplay.innerHTML = step.visited.map(v =>
                `<span class="bfs-visited-item${v === step.newVisited ? ' new' : ''}">${v}</span>`
            ).join('');
        }
    }

    // Update phase description
    const phaseEl = document.getElementById('bfsPhase');
    if (phaseEl) {
        let bgColor = 'rgba(0,217,255,0.1)';
        if (step.found) bgColor = 'rgba(255,215,0,0.2)';
        phaseEl.style.background = bgColor;
        phaseEl.innerHTML = `<strong>${step.phase}</strong><br><span style="font-size:0.8rem;color:#aaa;">${step.action}</span>`;
    }
}

function nextBFSStep() {
    if (bfsCodeStep < bfsCodeSteps.length - 1) {
        bfsCodeStep++;
        updateBFSViz();
    }
}

function prevBFSStep() {
    if (bfsCodeStep > 0) {
        bfsCodeStep--;
        updateBFSViz();
    }
}

function autoPlayBFS() {
    if (bfsAutoPlayInterval) {
        clearInterval(bfsAutoPlayInterval);
        bfsAutoPlayInterval = null;
        return;
    }
    bfsAutoPlayInterval = setInterval(() => {
        if (bfsCodeStep < bfsCodeSteps.length - 1) {
            bfsCodeStep++;
            updateBFSViz();
        } else {
            clearInterval(bfsAutoPlayInterval);
            bfsAutoPlayInterval = null;
        }
    }, 1200);
}

function resetBFSViz() {
    if (bfsAutoPlayInterval) {
        clearInterval(bfsAutoPlayInterval);
        bfsAutoPlayInterval = null;
    }
    bfsCodeStep = 0;
    updateBFSViz();
}

// ============ BFS TREE VISUALIZATION ============
// Machine 1: [.##.] with 6 buttons, Target=0110=6
// Buttons: (3)=8, (1,3)=10, (2)=4, (2,3)=12, (0,2)=5, (0,1)=3
const bfsButtons = [8, 10, 4, 12, 5, 3];
const bfsButtonNames = ['(3)', '(1,3)', '(2)', '(2,3)', '(0,2)', '(0,1)'];
const bfsTarget = 6;  // 0110

// Pre-computed BFS tree for visualization
const bfsTreeData = {
    level0: [{ state: 0, label: '0000', isStart: true }],
    level1: [
        { state: 8, label: '1000', parent: 0 },
        { state: 10, label: '1010', parent: 0 },
        { state: 4, label: '0100', parent: 0 },
        { state: 12, label: '1100', parent: 0 },
        { state: 5, label: '0101', parent: 0 },
        { state: 3, label: '0011', parent: 0 }
    ],
    level2: [
        { state: 2, label: '0010', parent: 8 },
        { state: 6, label: '0110', parent: 5, isTarget: true },  // TARGET! via (0,2) then (0,1)
        { state: 14, label: '1110', parent: 10 },
        { state: 1, label: '0001', parent: 4 },
        { state: 9, label: '1001', parent: 12 },
        { state: 7, label: '0111', parent: 3 }
    ],
    // Solution path: 0 -> 5 (press 0,2) -> 6 (press 0,1)
    solutionPath: [0, 5, 6],
    solutionButtons: ['(0,2)', '(0,1)']
};

let bfsCurrentLevel = 0;
let bfsShowingSolution = false;

function toBinary(n) {
    return n.toString(2).padStart(4, '0');
}

function initBFSCanvas() {
    bfsReset();
}

function bfsReset() {
    bfsCurrentLevel = 0;
    bfsShowingSolution = false;
    document.getElementById('bfsResult').style.display = 'none';
    renderBFSTree();
    updateBFSInfo();
}

function bfsExpandLevel() {
    if (bfsCurrentLevel < 2) {
        bfsCurrentLevel++;
        renderBFSTree();
        updateBFSInfo();

        // Check if target found at this level
        if (bfsCurrentLevel === 2) {
            setTimeout(() => {
                document.getElementById('bfsResult').style.display = 'block';
                document.getElementById('bfsResultPresses').textContent = '2';
                document.getElementById('bfsPathDisplay').textContent = '0000 → 0101 → 0110';
            }, 500);
        }
    }
}

function bfsShowPath() {
    bfsCurrentLevel = 2;
    bfsShowingSolution = true;
    renderBFSTree();
    updateBFSInfo();
    document.getElementById('bfsResult').style.display = 'block';
    document.getElementById('bfsResultPresses').textContent = '2';
    document.getElementById('bfsPathDisplay').textContent = '0000 → 0101 → 0110';
}

function renderBFSTree() {
    // Level 0
    const nodes0 = document.getElementById('bfsNodes0');
    nodes0.innerHTML = bfsTreeData.level0.map(n =>
        `<div class="bfs-node start ${bfsShowingSolution ? 'on-path' : ''}">${n.label}</div>`
    ).join('');
    document.getElementById('bfsLevel0').style.opacity = '1';

    // Level 1
    const nodes1 = document.getElementById('bfsNodes1');
    if (bfsCurrentLevel >= 1) {
        nodes1.innerHTML = bfsTreeData.level1.map(n => {
            const onPath = bfsShowingSolution && bfsTreeData.solutionPath.includes(n.state);
            return `<div class="bfs-node ${onPath ? 'on-path' : 'visited'}">${n.label}</div>`;
        }).join('');
        document.getElementById('bfsLevel1').style.opacity = '1';
    } else {
        nodes1.innerHTML = '<span style="color:#444">Press "Expand" to explore...</span>';
        document.getElementById('bfsLevel1').style.opacity = '0.3';
    }

    // Level 2
    const nodes2 = document.getElementById('bfsNodes2');
    if (bfsCurrentLevel >= 2) {
        nodes2.innerHTML = bfsTreeData.level2.map(n => {
            const onPath = bfsShowingSolution && bfsTreeData.solutionPath.includes(n.state);
            const isTarget = n.isTarget;
            let cls = 'bfs-node';
            if (isTarget) cls += ' target';
            else if (onPath) cls += ' on-path';
            else cls += ' visited';
            return `<div class="${cls}">${n.label}${isTarget ? ' ✓' : ''}</div>`;
        }).join('');
        document.getElementById('bfsLevel2').style.opacity = '1';
    } else {
        nodes2.innerHTML = '';
        document.getElementById('bfsLevel2').style.opacity = '0.3';
    }
}

function updateBFSInfo() {
    // Update queue
    const queueEl = document.getElementById('bfsQueue');
    if (queueEl) {
        let queueItems = [];
        if (bfsCurrentLevel === 0) {
            queueItems = bfsTreeData.level0.map(n => `<span class="bfs-queue-item">${n.label}</span>`);
        } else if (bfsCurrentLevel === 1) {
            queueItems = bfsTreeData.level1.map(n => `<span class="bfs-queue-item">${n.label}</span>`);
        } else {
            queueItems = ['<span style="color:#00ff88">Target found!</span>'];
        }
        queueEl.innerHTML = queueItems.join(' ');
    }

    // Update stats
    const exploredEl = document.getElementById('bfsExplored');
    const levelEl = document.getElementById('bfsCurrentLevel');
    if (exploredEl) {
        const counts = [1, 7, 13];
        exploredEl.textContent = counts[bfsCurrentLevel];
    }
    if (levelEl) {
        levelEl.textContent = bfsCurrentLevel;
    }
}

// ============ PART 1 ANSWER ANIMATION ============
function animateAnswerP1() {
    const rows = ['p1m1', 'p1m2', 'p1m3', 'p1mN', 'p1mTotal'];
    rows.forEach((id, i) => {
        setTimeout(() => {
            const el = document.getElementById(id);
            if (el) el.classList.add('visible');
        }, i * 350);
    });
}

function resetAnswerP1() {
    ['p1m1', 'p1m2', 'p1m3', 'p1mN', 'p1mTotal'].forEach(id => {
        const el = document.getElementById(id);
        if (el) el.classList.remove('visible');
    });
}
