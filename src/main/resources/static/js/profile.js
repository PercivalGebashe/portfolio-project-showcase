// ===== MOBILE MENU TOGGLE =====
document.addEventListener("DOMContentLoaded", () => {
  const menuToggle = document.querySelector(".menu-toggle");
  const navLinks = document.querySelector(".nav-links");

  menuToggle.addEventListener("click", () => {
    navLinks.classList.toggle("show");
    menuToggle.textContent = navLinks.classList.contains("show") ? "✖" : "☰";
  });

  // Close menu when a link is clicked (for smoother UX)
  navLinks.querySelectorAll("a").forEach(link => {
    link.addEventListener("click", () => {
      navLinks.classList.remove("show");
      menuToggle.textContent = "☰";
    });
  });
});

document.addEventListener("DOMContentLoaded", () => {
    const createBtn = document.getElementById("create-btn");
    if(createBtn){
        const createMenu = document.getElementById("create-menu");

        createBtn.addEventListener("click", (e) => {
            e.stopPropagation();
            createMenu.classList.toggle("show");
        });

        // Close menu when clicking outside
        document.addEventListener("click", (e) => {
            if (!createMenu.contains(e.target) && !createBtn.contains(e.target)) {
                createMenu.classList.remove("show");
            }
        });
    }
});

// Animate skill bars when in view
document.addEventListener("DOMContentLoaded", () => {
    const skillBars = document.querySelectorAll(".skill-progress");

    const observer = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add("active");
                observer.unobserve(entry.target); // animate once
            }
        });
    }, { threshold: 0.3 }); // trigger when 30% visible

    skillBars.forEach(bar => observer.observe(bar));
});