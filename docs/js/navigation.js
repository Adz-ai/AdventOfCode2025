/**
 * Navigation and Core Slide Logic for Day 10 Presentation
 * Handles slide transitions, keyboard navigation, and initialization
 */

// ============ SLIDE NAVIGATION ============
let currentSlide = 0;
const totalSlides = 19;

function updateSlide() {
    const slides = document.querySelectorAll('.slide');

    slides.forEach((slide, index) => {
        slide.classList.remove('active', 'exit');
        if (index === currentSlide) {
            slide.classList.add('active');
        } else if (index < currentSlide) {
            slide.classList.add('exit');
        }
    });

    document.getElementById('progressBar').style.width =
        ((currentSlide + 1) / totalSlides * 100) + '%';
    document.getElementById('slideCounter').textContent =
        `${currentSlide + 1} / ${totalSlides}`;

    document.getElementById('prevBtn').disabled = currentSlide === 0;
    document.getElementById('nextBtn').disabled = currentSlide === totalSlides - 1;

    // Initialize slide-specific content
    if (currentSlide === 4) initBFSCanvas();
    if (currentSlide === 5) initBFSCodeViz();
    if (currentSlide === 6) resetAnswerP1();
    if (currentSlide === 13) initElimination();
    if (currentSlide === 14) initCodeViz();
    if (currentSlide === 15) initViz3D();
    if (currentSlide === 16) resetAnswerAnimation();
}

function nextSlide() {
    // On BFS code viz slide (5), step through BFS first
    if (currentSlide === 5 && bfsCodeStep < bfsCodeSteps.length - 1) {
        nextBFSStep();
        return;
    }
    // On elimination slide (13), step through elimination first
    if (currentSlide === 13 && elimStep < elimSteps.length - 1) {
        nextElimStep();
        return;
    }
    // On code viz slide (14), step through code first
    if (currentSlide === 14 && codeStep < codeSteps.length - 1) {
        nextCodeStep();
        return;
    }
    if (currentSlide < totalSlides - 1) {
        currentSlide++;
        updateSlide();
    }
}

function prevSlide() {
    // On BFS code viz slide (5), step back through BFS first
    if (currentSlide === 5 && bfsCodeStep > 0) {
        prevBFSStep();
        return;
    }
    // On elimination slide (13), step back through elimination first
    if (currentSlide === 13 && elimStep > 0) {
        prevElimStep();
        return;
    }
    // On code viz slide (14), step back through code first
    if (currentSlide === 14 && codeStep > 0) {
        prevCodeStep();
        return;
    }
    if (currentSlide > 0) {
        currentSlide--;
        updateSlide();
    }
}

// Keyboard navigation
document.addEventListener('keydown', (e) => {
    if (e.key === 'ArrowRight' || e.key === ' ') {
        e.preventDefault();
        nextSlide();
    } else if (e.key === 'ArrowLeft') {
        e.preventDefault();
        prevSlide();
    }
});

// Initialize on load
document.addEventListener('DOMContentLoaded', () => {
    updateSlide();
});
