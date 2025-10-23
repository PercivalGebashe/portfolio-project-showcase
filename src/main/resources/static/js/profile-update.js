// ===== Helper Functions =====
function addSkill(name = "", level = "") {
    const container = document.getElementById("skills-container");
    const div = document.createElement("div");
    div.classList.add("dynamic-item");

    div.innerHTML = `
        <input type="text" placeholder="Skill (e.g., Java, React)" class="skill-name" required value="${name}"/>
        <select class="skill-level" required>
            <option value="">Select Level</option>
            <option value="Beginner" ${level === "Beginner" ? "selected" : ""}>Beginner</option>
            <option value="Intermediate" ${level === "Intermediate" ? "selected" : ""}>Intermediate</option>
            <option value="Advanced" ${level === "Advanced" ? "selected" : ""}>Advanced</option>
            <option value="Expert" ${level === "Expert" ? "selected" : ""}>Expert</option>
        </select>
        <button type="button" class="remove-btn">×</button>
    `;
    container.appendChild(div);
}

function addContact(platform = "", link = "") {
    const container = document.getElementById("contacts-container");
    const div = document.createElement("div");
    div.classList.add("dynamic-item");

    div.innerHTML = `
        <select class="contact-name" required>
            <option value="">Select Platform</option>
            <option value="LinkedIn" ${platform === "LinkedIn" ? "selected" : ""}>LinkedIn</option>
            <option value="GitHub" ${platform === "GitHub" ? "selected" : ""}>GitHub</option>
            <option value="Twitter" ${platform === "Twitter" ? "selected" : ""}>Twitter</option>
            <option value="Instagram" ${platform === "Instagram" ? "selected" : ""}>Instagram</option>
            <option value="Portfolio" ${platform === "Portfolio" ? "selected" : ""}>Portfolio</option>
            <option value="Other" ${platform === "Other" ? "selected" : ""}>Other</option>
        </select>
        <input type="url" placeholder="Profile URL" class="contact-link" required value="${link}"/>
        <button type="button" class="remove-btn">×</button>
    `;
    container.appendChild(div);
}

// ===== Event Listeners for Adding Items =====
document.getElementById("add-skill").addEventListener("click", () => addSkill());
document.getElementById("add-contact").addEventListener("click", () => addContact());

// ===== Event Delegation for Removing Items =====
document.getElementById("skills-container").addEventListener("click", (e) => {
    if (e.target.classList.contains("remove-btn")) e.target.parentElement.remove();
});

document.getElementById("contacts-container").addEventListener("click", (e) => {
    if (e.target.classList.contains("remove-btn")) e.target.parentElement.remove();
});

// ===== Populate Existing Data on Page Load =====
window.addEventListener("DOMContentLoaded", () => {
    if (existingSkills) {
        Object.entries(existingSkills).forEach(([name, level]) => addSkill(name, level));
    }

    if (existingContacts) {
        Object.entries(existingContacts).forEach(([platform, link]) => addContact(platform, link));
    }
});

// ===== Before Submitting Form =====
document.getElementById("profileForm").addEventListener("submit", (event) => {
    event.preventDefault();

    const skills = {};
    document.querySelectorAll("#skills-container .dynamic-item").forEach(item => {
        const name = item.querySelector(".skill-name").value.trim();
        const level = item.querySelector(".skill-level").value.trim();
        if (name && level) skills[name] = level;
    });

    const contacts = {};
    document.querySelectorAll("#contacts-container .dynamic-item").forEach(item => {
        const platform = item.querySelector(".contact-name").value.trim();
        const link = item.querySelector(".contact-link").value.trim();
        if (platform && link) contacts[platform] = link;
    });

    document.getElementById("skills").value = JSON.stringify(skills);
    document.getElementById("contacts").value = JSON.stringify(contacts);

    event.target.submit();
});

document.getElementById('imageInput').addEventListener('change', function (event) {
    const file = event.target.files[0];
    const errorEl = document.getElementById('imageError');
    const thumbnail = document.getElementById('thumbnail');

    if (!file) return;

    if (file.size > (2 * 1024 * 1024)) { // limit: 2MB
        errorEl.style.display = 'block';
        thumbnail.src = '/images/avatar-outline.svg';
        return;
    }

    errorEl.style.display = 'none';
    const reader = new FileReader();
    reader.onload = e => thumbnail.src = e.target.result;
    reader.readAsDataURL(file);
});