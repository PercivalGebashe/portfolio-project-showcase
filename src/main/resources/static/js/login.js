document.addEventListener('DOMContentLoaded', () => {
    const intro = document.querySelector('.intro');
    const formContainer = document.querySelector('.form-container');

    [intro, formContainer].forEach((el, i) => {
        el.style.opacity = 0;
        el.style.transform = 'translateY(30px)';
        setTimeout(() => {
            el.style.transition = 'all 1s ease';
            el.style.opacity = 1;
            el.style.transform = 'translateY(0)';
        }, 400 + i * 200);
    });
});
