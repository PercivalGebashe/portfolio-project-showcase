document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector(".experience-form");
    const addBtn = document.querySelector(".btn-add");
    const template = document.querySelector(".experience-template");
    const hiddenInput = document.getElementById("experiencesJson");

    // Add new experience card
    addBtn.addEventListener("click", () => {

        const newCard = document.createElement("div");
        newCard.classList.add("experience-card");

        newCard.innerHTML = `
            <div class="form-group">
                <label>Position</label>
                <input type="text"  class="position" placeholder="e.g., Backend Developer" required />
            </div>

            <div class="form-group">
                <label>Company</label>
                <input type="text" class="company" placeholder="e.g., Momentum Group" required />
            </div>

            <div class="form-group">
                <label>Description</label>
                <textarea class="description" rows="3" placeholder="Describe your responsibilities"></textarea>
            </div>

            <div class="form-group">
                <label>Technology</label>
                <textarea class="technologies" rows="3" placeholder="Enter Technology used eg. Java, Python"></textarea>
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

            <button type="button" class="btn btn-remove">Remove</button>
        `;
        console.log(newCard)

        form.insertBefore(newCard, document.querySelector(".form-actions"));
    });

    // Remove experience card
    form.addEventListener("click", e => {
        if (e.target.classList.contains("btn-remove")) {
            e.target.closest(".experience-card").remove();
        }
    });

    // On form submit, serialize all cards to JSON
    form.addEventListener("submit", e => {
        e.preventDefault();
        const userId = e.target.userId.value;

        const cards = Array.from(form.querySelectorAll(".experience-card:not(.experience-template)"));
        const experiences = cards.map(card => ({
            userId: userId,
            position: card.querySelector(".position").value,
            company: card.querySelector(".company").value,
            description: card.querySelector(".description").value,
            technologies: card.querySelector(".technologies").value
                                      .split(",")           // split by commas
                                      .map(t => t.trim()),
            startDate: card.querySelector(".startDate").value,
            endDate: card.querySelector(".endDate").value,
            current: card.querySelector(".isCurrent").checked
        }));
        console.log(JSON.stringify(experiences));
        hiddenInput.value = JSON.stringify(experiences);
        e.target.submit();
    });
});
