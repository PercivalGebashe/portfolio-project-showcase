document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector(".experience-form");
    const addBtn = document.querySelector(".btn-add");
    const hiddenInput = document.getElementById("experiencesJson");

    // Add new experience card
    addBtn.addEventListener("click", () => {
        const newCard = document.createElement("div");
        newCard.classList.add("experience-card");
        newCard.innerHTML = `
            <div class="form-group">
                <label>Position</label>
                <input type="text" class="position" placeholder="Backend Developer" required />
            </div>
            <div class="form-group">
                <label>Company</label>
                <input type="text" class="company" placeholder="Company Name" required />
            </div>
            <div class="form-group">
                <label>Description</label>
                <textarea class="description" rows="3" placeholder="Describe your responsibilities"></textarea>
            </div>
            <div class="form-group">
                <label>Technologies Used</label>
                <input type="text" class="technologies" placeholder="Java, Python, Spring Boot" />
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label>Start Date</label>
                    <input type="date" class="startDate" required />
                </div>
                <div class="form-group">
                    <label>End Date</label>
                    <input type="date" class="endDate" />
                </div>
                <div class="form-group checkbox">
                    <label><input type="checkbox" class="isCurrent" /> Current</label>
                </div>
            </div>
            <button type="button" class="btn btn-remove"><ion-icon name="trash-outline"></ion-icon> Remove</button>
        `;
        form.insertBefore(newCard, document.querySelector(".form-actions"));
    });

    // Remove card
    form.addEventListener("click", e => {
        if (e.target.classList.contains("btn-remove") || e.target.closest(".btn-remove")) {
            e.target.closest(".experience-card").remove();
        }
    });

    // Serialize cards to JSON on submit
    form.addEventListener("submit", e => {
        const userId = e.target.userId.value;
        const cards = Array.from(form.querySelectorAll(".experience-card"));
        const experiences = cards.map(card => ({
            userId,
            position: card.querySelector(".position").value,
            company: card.querySelector(".company").value,
            description: card.querySelector(".description").value,
            technologies: card.querySelector(".technologies").value
                          .split(",")
                          .map(t => t.trim())
                          .filter(Boolean),
            startDate: card.querySelector(".startDate").value,
            endDate: card.querySelector(".endDate").value,
            current: card.querySelector(".isCurrent").checked
        }));
        hiddenInput.value = JSON.stringify(experiences);
    });
});
